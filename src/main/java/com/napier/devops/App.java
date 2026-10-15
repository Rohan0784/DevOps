package com.napier.devops;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Runs the employee reporting application and exposes its REST endpoints. */
@SpringBootApplication
@RestController
public class App {
    /** Number of arguments supplied when the application runs in Docker. */
    private static final int REQUIRED_DOCKER_ARGUMENTS = 2;
    /** Employee number column name used in database results. */
    private static final String EMPLOYEE_NUMBER_COLUMN = "emp_no";
    /** First name column name used in database results. */
    private static final String FIRST_NAME_COLUMN = "first_name";
    /** Last name column name used in database results. */
    private static final String LAST_NAME_COLUMN = "last_name";
    /** Job title column name used in database results. */
    private static final String TITLE_COLUMN = "title";
    /** Salary column name used in database results. */
    private static final String SALARY_COLUMN = "salary";
    /** Department number column name used in database results. */
    private static final String DEPARTMENT_NUMBER_COLUMN = "dept_no";
    /** Department name column name used in database results. */
    private static final String DEPARTMENT_NAME_COLUMN = "dept_name";
    /** Manager first name alias used in database results. */
    private static final String MANAGER_FIRST_NAME_COLUMN = "manager_first_name";
    /** Manager last name alias used in database results. */
    private static final String MANAGER_LAST_NAME_COLUMN = "manager_last_name";
    /** Text displayed when report information is unavailable. */
    private static final String NOT_AVAILABLE = "N/A";
    /** Separator used between Markdown report columns. */
    private static final String TABLE_SEPARATOR = " | ";

    /** Connection to the MySQL database. */
    private static Connection con;

    /** Start the application and create the default manager salary report. */
    public static void main(String[] args) {
        if (args.length < REQUIRED_DOCKER_ARGUMENTS) {
            // Running directly from IntelliJ
            connect("localhost:33060", 10000);
        } else {
            // Running through Docker
            connect(args[0], Integer.parseInt(args[1]));
        }

        App app = new App();
        List<Employee> employees = app.getSalariesByRole("Manager");
        app.outputEmployees(employees, "ManagerSalaries.md");

        Runtime.getRuntime().addShutdownHook(new Thread(App::disconnect));
        SpringApplication.run(App.class, args);
    }

    /** Connect to the MySQL database. */
    public static void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load SQL driver", e);
        }

        int retries = 10;
        boolean shouldWait = false;

        for (int attempt = 1; attempt <= retries; ++attempt) {
            System.out.println("Connecting to database...");

            try {
                if (shouldWait) {
                    Thread.sleep(delay);
                }

                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location
                                + "/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example");

                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + attempt);
                System.out.println(e.getMessage());
                shouldWait = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Connection attempt interrupted", e);
            }
        }

        throw new IllegalStateException(
                "Could not connect to database after " + retries + " attempts");
    }

    /** REST endpoint for a single employee. */
    @GetMapping("/employee")
    public Employee employee(@RequestParam("id") String id) {
        try {
            return getEmployee(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Employee id must be a number", e);
        }
    }

    /** REST endpoint for all current salaries. */
    @GetMapping("/salaries")
    public List<Employee> salaries() {
        return getAllSalaries();
    }

    /** REST endpoint for current salaries matching a title. */
    @GetMapping("/salaries_title")
    public List<Employee> salariesByTitle(@RequestParam("title") String title) {
        return getSalariesByRole(title);
    }

    /** REST endpoint for one department and its current manager. */
    @GetMapping("/department")
    public Department department(@RequestParam("dept") String departmentName) {
        return getDepartment(departmentName);
    }

    /** REST endpoint for current salaries in a named department. */
    @GetMapping("/salaries_department")
    public List<Employee> salariesByDepartment(
            @RequestParam("dept") String departmentName) {
        Department department = getDepartment(departmentName);
        if (department == null) {
            return List.of();
        }
        return getSalariesByDepartment(department);
    }

    /**
     * Return current employee details.
     * Returns null if no complete current record is found or the lookup fails.
     */
    public Employee getEmployee(int employeeId) {
        // Only rows with the sample database's current-entry end date are used.
        String strSelect = "SELECT e.emp_no, e.first_name, e.last_name, "
                + "t.title, s.salary, d.dept_no, d.dept_name, "
                + "m.emp_no AS manager_emp_no, "
                + "m.first_name AS manager_first_name, "
                + "m.last_name AS manager_last_name "
                + "FROM employees e "
                + "LEFT JOIN titles t ON t.emp_no = e.emp_no AND t.to_date = '9999-01-01' "
                + "LEFT JOIN salaries s ON s.emp_no = e.emp_no AND s.to_date = '9999-01-01' "
                + "LEFT JOIN dept_emp de ON de.emp_no = e.emp_no AND de.to_date = '9999-01-01' "
                + "LEFT JOIN departments d ON d.dept_no = de.dept_no "
                + "LEFT JOIN dept_manager dm ON dm.dept_no = de.dept_no "
                + "AND dm.to_date = '9999-01-01' "
                + "LEFT JOIN employees m ON m.emp_no = dm.emp_no "
                + "WHERE e.emp_no = ?";
        try (PreparedStatement stmt = con.prepareStatement(strSelect)) {
            stmt.setInt(1, employeeId);
            try (ResultSet rset = stmt.executeQuery()) {
                if (rset.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rset.getInt(EMPLOYEE_NUMBER_COLUMN);
                    emp.first_name = rset.getString(FIRST_NAME_COLUMN);
                    emp.last_name = rset.getString(LAST_NAME_COLUMN);
                    emp.title = rset.getString(TITLE_COLUMN);
                    emp.salary = rset.getInt(SALARY_COLUMN);

                    String departmentNumber = rset.getString(DEPARTMENT_NUMBER_COLUMN);
                    if (departmentNumber != null) {
                        emp.dept = new Department();
                        emp.dept.dept_no = departmentNumber;
                        emp.dept.dept_name = rset.getString(DEPARTMENT_NAME_COLUMN);
                    }

                    int managerNumber = rset.getInt("manager_emp_no");
                    if (!rset.wasNull()) {
                        emp.manager = new Employee();
                        emp.manager.emp_no = managerNumber;
                        emp.manager.first_name = rset.getString(MANAGER_FIRST_NAME_COLUMN);
                        emp.manager.last_name = rset.getString(MANAGER_LAST_NAME_COLUMN);
                        if (emp.dept != null) {
                            emp.dept.manager = emp.manager;
                        }
                    }
                    return emp;
                }
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    /** Return employees with a current title and salary matching the given role. */
    public List<Employee> getSalariesByRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("A role must be provided");
        }
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, "
                + "t.title, s.salary, d.dept_no, d.dept_name, "
                + "m.emp_no AS manager_emp_no, "
                + "m.first_name AS manager_first_name, "
                + "m.last_name AS manager_last_name "
                + "FROM employees e "
                + "JOIN salaries s ON s.emp_no = e.emp_no "
                + "AND s.to_date = '9999-01-01' "
                + "JOIN titles t ON t.emp_no = e.emp_no "
                + "AND t.to_date = '9999-01-01' "
                + "JOIN dept_emp de ON de.emp_no = e.emp_no "
                + "AND de.to_date = '9999-01-01' "
                + "JOIN departments d ON d.dept_no = de.dept_no "
                + "LEFT JOIN dept_manager dm ON dm.dept_no = de.dept_no "
                + "AND dm.to_date = '9999-01-01' "
                + "LEFT JOIN employees m ON m.emp_no = dm.emp_no "
                + "WHERE t.title = ? "
                + "ORDER BY e.emp_no ASC";
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rset.getInt(EMPLOYEE_NUMBER_COLUMN);
                    emp.first_name = rset.getString(FIRST_NAME_COLUMN);
                    emp.last_name = rset.getString(LAST_NAME_COLUMN);
                    emp.title = rset.getString(TITLE_COLUMN);
                    emp.salary = rset.getInt(SALARY_COLUMN);

                    emp.dept = new Department();
                    emp.dept.dept_no = rset.getString(DEPARTMENT_NUMBER_COLUMN);
                    emp.dept.dept_name = rset.getString(DEPARTMENT_NAME_COLUMN);

                    int managerNumber = rset.getInt("manager_emp_no");
                    if (!rset.wasNull()) {
                        emp.manager = new Employee();
                        emp.manager.emp_no = managerNumber;
                        emp.manager.first_name = rset.getString(MANAGER_FIRST_NAME_COLUMN);
                        emp.manager.last_name = rset.getString(MANAGER_LAST_NAME_COLUMN);
                        emp.dept.manager = emp.manager;
                    }
                    employees.add(emp);
                }
            }
            return employees;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to get salaries for role: " + role, e);
        }
    }

    /** Return every employee with a current salary, ordered by employee number. */
    public List<Employee> getAllSalaries() {
        String sql = "SELECT e.emp_no, e.first_name, e.last_name, s.salary "
                + "FROM employees e "
                + "JOIN salaries s ON s.emp_no = e.emp_no "
                + "AND s.to_date = '9999-01-01' "
                + "ORDER BY e.emp_no ASC";
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rset = stmt.executeQuery()) {
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt(EMPLOYEE_NUMBER_COLUMN);
                emp.first_name = rset.getString(FIRST_NAME_COLUMN);
                emp.last_name = rset.getString(LAST_NAME_COLUMN);
                emp.salary = rset.getInt(SALARY_COLUMN);
                employees.add(emp);
            }
            return employees;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to get current salaries", e);
        }
    }

    /** Display employee numbers, names, and current salaries as a console report. */
    public void displaySalaries(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to display.");
            return;
        }
        System.out.printf("%-10s %-16s %-18s %s%n", "Employee", "First name", "Last name", "Salary");
        for (Employee emp : employees) {
            if (emp == null) {
                continue;
            }
            System.out.printf("%-10d %-16s %-18s %d%n",
                    emp.emp_no, emp.first_name, emp.last_name, emp.salary);
        }
    }

    /** Display an employee's information on the console. */
    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " "
                    + emp.first_name + " "
                    + emp.last_name + "\n"
                    + emp.title + "\n"
                    + "Salary:" + emp.salary + "\n"
                    + (emp.dept == null ? NOT_AVAILABLE : emp.dept.dept_name) + "\n"
                    + "Manager: "
                    + (emp.manager == null
                        ? NOT_AVAILABLE
                        : emp.manager.first_name + " " + emp.manager.last_name)
                    + "\n");
        }
    }

    /**
     * Find a department by name, including its current manager.
     * @return the department, or null when the name is not found
     */
    public Department getDepartment(String departmentName) {
        if (departmentName == null || departmentName.isBlank()) {
            return null;
        }
        String sql = "SELECT d.dept_no, d.dept_name, m.emp_no AS manager_emp_no, "
                + "m.first_name AS manager_first_name, m.last_name AS manager_last_name "
                + "FROM departments d "
                + "LEFT JOIN dept_manager dm ON dm.dept_no = d.dept_no "
                + "AND dm.to_date = '9999-01-01' "
                + "LEFT JOIN employees m ON m.emp_no = dm.emp_no "
                + "WHERE d.dept_name = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, departmentName);
            try (ResultSet rset = stmt.executeQuery()) {
                if (!rset.next()) {
                    return null;
                }
                Department dept = new Department();
                dept.dept_no = rset.getString(DEPARTMENT_NUMBER_COLUMN);
                dept.dept_name = rset.getString(DEPARTMENT_NAME_COLUMN);
                int managerId = rset.getInt("manager_emp_no");
                if (!rset.wasNull()) {
                    dept.manager = new Employee();
                    dept.manager.emp_no = managerId;
                    dept.manager.first_name = rset.getString(MANAGER_FIRST_NAME_COLUMN);
                    dept.manager.last_name = rset.getString(MANAGER_LAST_NAME_COLUMN);
                }
                return dept;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to get department details", e);
        }
    }

    /**
     * Return current salaries for employees linked to the department, ordered by ID.
     * Matches the Lab 5 query, including historical department memberships.
     */
    public List<Employee> getSalariesByDepartment(Department dept) {
        if (dept == null || dept.dept_no == null || dept.dept_no.isBlank()) {
            throw new IllegalArgumentException("A department number is required");
        }
        String sql = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                + "FROM employees, salaries, dept_emp, departments "
                + "WHERE employees.emp_no = salaries.emp_no "
                + "AND employees.emp_no = dept_emp.emp_no "
                + "AND dept_emp.dept_no = departments.dept_no "
                + "AND salaries.to_date = '9999-01-01' "
                + "AND departments.dept_no = ? "
                + "ORDER BY employees.emp_no ASC";
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, dept.dept_no);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rset.getInt(EMPLOYEE_NUMBER_COLUMN);
                    emp.first_name = rset.getString(FIRST_NAME_COLUMN);
                    emp.last_name = rset.getString(LAST_NAME_COLUMN);
                    emp.salary = rset.getInt(SALARY_COLUMN);
                    employees.add(emp);
                }
            }
            return employees;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to get department salaries", e);
        }
    }

    /**
     * Find a current employee by first and last name.
     * Names are not unique: when several match, return the lowest employee number.
     * @return the employee with their department and manager, or null if not found
     */
    public Employee getEmployee(String firstName, String lastName) {
        if (firstName == null || lastName == null
                || firstName.isBlank() || lastName.isBlank()) {
            return null;
        }
        String sql = "SELECT e.emp_no FROM employees e "
                + "JOIN dept_emp de ON de.emp_no = e.emp_no AND de.to_date = '9999-01-01' "
                + "JOIN titles t ON t.emp_no = e.emp_no AND t.to_date = '9999-01-01' "
                + "JOIN salaries s ON s.emp_no = e.emp_no AND s.to_date = '9999-01-01' "
                + "WHERE e.first_name = ? AND e.last_name = ? ORDER BY e.emp_no ASC LIMIT 1";
        int employeeId;
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            try (ResultSet rset = stmt.executeQuery()) {
                if (!rset.next()) {
                    return null;
                }
                employeeId = rset.getInt(EMPLOYEE_NUMBER_COLUMN);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to find employee by name", e);
        }
        return getEmployee(employeeId);
    }
    /** Add a new employee to the database. */
    public void addEmployee(Employee emp)
    {
        if (emp == null || emp.first_name == null || emp.last_name == null) {
            throw new IllegalArgumentException("Employee number and name are required");
        }

        String sql = "INSERT INTO employees "
                + "(emp_no, first_name, last_name, birth_date, gender, hire_date) "
                + "VALUES (?, ?, ?, '9999-01-01', 'M', '9999-01-01') "
                + "ON DUPLICATE KEY UPDATE "
                + "first_name = VALUES(first_name), last_name = VALUES(last_name)";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, emp.emp_no);
            stmt.setString(2, emp.first_name);
            stmt.setString(3, emp.last_name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to add employee", e);
        }
    }

    /** Write employee information to a Markdown report. */
    public void outputEmployees(List<Employee> employees, String filename) {
        if (employees == null || employees.isEmpty()) {
            System.out.println("No employees to output.");
            return;
        }

        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("A report filename is required");
        }

        StringBuilder report = new StringBuilder(512);
        report.append("| Emp No | First Name | Last Name | Title | Salary | Department | Manager |\r\n"
                + "| --- | --- | --- | --- | --- | --- | --- |\r\n");

        for (Employee emp : employees) {
            if (emp == null) {
                continue;
            }

            String department = emp.dept == null
                    ? NOT_AVAILABLE
                    : emp.dept.dept_name;

            String manager = emp.manager == null
                    ? NOT_AVAILABLE
                    : emp.manager.first_name + " " + emp.manager.last_name;

            report.append("| ").append(emp.emp_no)
                    .append(TABLE_SEPARATOR).append(emp.first_name)
                    .append(TABLE_SEPARATOR).append(emp.last_name)
                    .append(TABLE_SEPARATOR).append(emp.title)
                    .append(TABLE_SEPARATOR).append(emp.salary)
                    .append(TABLE_SEPARATOR).append(department)
                    .append(TABLE_SEPARATOR).append(manager)
                    .append(" |\r\n");
        }

        File reportDirectory = new File("./reports");
        if (!reportDirectory.exists() && !reportDirectory.mkdirs()) {
            throw new IllegalStateException("Could not create reports directory");
        }

        File reportFile = new File(reportDirectory, filename);
        try (BufferedWriter writer =
                     Files.newBufferedWriter(reportFile.toPath(), StandardCharsets.UTF_8)) {
            writer.write(report.toString());
            System.out.println("Report created: " + reportFile.getPath());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write employee report", e);
        }
    }

    /** Disconnect from the MySQL database. */
    public static void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                throw new IllegalStateException("Error closing connection to database", e);
            }
        }
    }
}

