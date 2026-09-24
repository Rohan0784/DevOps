package com.napier.devops;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class App {
    /** Connection to the MySQL database. */
    private Connection con = null;

    public static void main(String[] args) {
        App a = new App();
        a.connect();
        try {
            String role = args.length > 0 ? args[0] : "Engineer";
            List<Employee> employees = a.getSalariesByRole(role);
            System.out.println("Current salaries for role: " + role);
            a.displaySalaries(employees);
        } finally {
            a.disconnect();
        }
    }

    /** Connect to the MySQL database. */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load SQL driver", e);
        }

        int retries = 10;
        for (int attempt = 1; attempt <= retries; ++attempt) {
            System.out.println("Connecting to database...");
            try {
                // Allow time for MySQL to initialize and import the employee data.
                Thread.sleep(30000);
                // Public key retrieval supports MySQL 8.4 authentication in this local lab.
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true",
                        "root", "example");
                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.println("Failed to connect to database attempt " + attempt);
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Connection attempt interrupted", e);
            }
        }

        throw new IllegalStateException("Could not connect to database after " + retries + " attempts");
    }

    /** Return current employee details, or null if no complete current record is found or the lookup fails. */
    public Employee getEmployee(int ID) {
        // Only rows with the sample database's current-entry end date are used.
        String strSelect = "SELECT e.emp_no, e.first_name, e.last_name, "
                + "t.title, s.salary, d.dept_name, "
                + "CONCAT(m.first_name, ' ', m.last_name) AS manager "
                + "FROM employees e "
                + "JOIN titles t ON t.emp_no = e.emp_no AND t.to_date = '9999-01-01' "
                + "JOIN salaries s ON s.emp_no = e.emp_no AND s.to_date = '9999-01-01' "
                + "JOIN dept_emp de ON de.emp_no = e.emp_no AND de.to_date = '9999-01-01' "
                + "JOIN departments d ON d.dept_no = de.dept_no "
                + "JOIN dept_manager dm ON dm.dept_no = de.dept_no "
                + "AND dm.to_date = '9999-01-01' "
                + "JOIN employees m ON m.emp_no = dm.emp_no "
                + "WHERE e.emp_no = " + ID;
        try (Statement stmt = con.createStatement();
             ResultSet rset = stmt.executeQuery(strSelect)) {
            if (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");
                emp.dept_name = rset.getString("dept_name");
                emp.manager = rset.getString("manager");
                return emp;
            }
            return null;
        } catch (Exception e) {
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
        String sql = "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                + "FROM employees, salaries, titles "
                + "WHERE employees.emp_no = salaries.emp_no "
                + "AND employees.emp_no = titles.emp_no "
                + "AND salaries.to_date = '9999-01-01' "
                + "AND titles.to_date = '9999-01-01' "
                + "AND titles.title = ? "
                + "ORDER BY employees.emp_no ASC";
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rset = stmt.executeQuery()) {
                while (rset.next()) {
                    Employee emp = new Employee();
                    emp.emp_no = rset.getInt("emp_no");
                    emp.first_name = rset.getString("first_name");
                    emp.last_name = rset.getString("last_name");
                    emp.salary = rset.getInt("salary");
                    employees.add(emp);
                }
            }
            return employees;
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to get salaries for role: " + role, e);
        }
    }

    /** Display employee numbers, names, and current salaries as a console report. */
    public void displaySalaries(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No current employees found for this role.");
            return;
        }
        System.out.printf("%-10s %-16s %-18s %s%n", "Employee", "First name", "Last name", "Salary");
        for (Employee emp : employees) {
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
                    + emp.dept_name + "\n"
                    + "Manager: " + emp.manager + "\n");
        }
    }

    /** Disconnect from the MySQL database. */
    public void disconnect() {
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
