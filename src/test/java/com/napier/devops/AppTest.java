package com.napier.devops;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/** Unit tests for employee report display methods. */
class AppTest
{
    /** Application instance used by the unit tests. */
    private static App app;

    /** Create the application used by all unit tests. */
    @BeforeAll
    static void init()
    {
        app = new App();
    }

    /** A null salary list should be handled safely. */
    @Test
    void displaySalariesTestNull()
    {
        assertDoesNotThrow(
                () -> app.displaySalaries(null),
                "Displaying a null salary list should not fail");
    }

    /** An empty salary list should be handled safely. */
    @Test
    void displaySalariesTestEmpty()
    {
        List<Employee> employees = new ArrayList<>();
        assertDoesNotThrow(
                () -> app.displaySalaries(employees),
                "Displaying an empty salary list should not fail");
    }

    /** Null entries in a salary list should be skipped safely. */
    @Test
    void displaySalariesTestContainsNull()
    {
        List<Employee> employees = new ArrayList<>();
        employees.add(null);
        assertDoesNotThrow(
                () -> app.displaySalaries(employees),
                "Displaying a salary list containing null should not fail");
    }

    /** A populated salary list should be displayed safely. */
    @Test
    void displaySalariesTestNormal()
    {
        List<Employee> employees = new ArrayList<>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        assertDoesNotThrow(
                () -> app.displaySalaries(employees),
                "Displaying a normal salary list should not fail");
    }

    /** A null employee should be handled safely. */
    @Test
    void displayEmployeeTestNull()
    {
        assertDoesNotThrow(
                () -> app.displayEmployee(null),
                "Displaying a null employee should not fail");
    }

    /** A complete employee should be displayed safely. */
    @Test
    void displayEmployeeTestNormal()
    {
        Department dept = new Department();
        dept.dept_no = "d005";
        dept.dept_name = "Development";

        Employee manager = new Employee();
        manager.emp_no = 110511;
        manager.first_name = "Leon";
        manager.last_name = "DasSarma";

        Employee emp = new Employee();
        emp.emp_no = 255530;
        emp.first_name = "Ronghao";
        emp.last_name = "Garigliano";
        emp.title = "Technique Leader";
        emp.salary = 57499;
        emp.dept = dept;
        emp.manager = manager;

        assertDoesNotThrow(
                () -> app.displayEmployee(emp),
                "Displaying a complete employee should not fail");
    }

    /** Missing department and manager details should be handled safely. */
    @Test
    void displayEmployeeTestMissingDepartmentAndManager()
    {
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;

        assertDoesNotThrow(
                () -> app.displayEmployee(emp),
                "Displaying an employee without department and manager should not fail");
    }
}
