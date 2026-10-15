package com.napier.devops;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Integration tests for application database operations. */
class AppIntegrationTest
{
    /** Department used by the integration tests. */
    private static final String DEVELOPMENT = "Development";
    /** Application instance connected to the test database. */
    private static App app;

    /** Connect to the test database before running the tests. */
    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);
    }

    /** Disconnect from the test database after all tests finish. */
    @AfterAll
    static void shutdown()
    {
        app.disconnect();
    }

    /** Retrieve a known employee and verify the complete record. */
    @Test
    void getEmployeeTest()
    {
        Employee emp = app.getEmployee(255530);

        assertNotNull(emp, "Employee 255530 should exist");
        assertEquals(255530, emp.emp_no, "Employee number should match");
        assertEquals("Ronghao", emp.first_name, "First name should match");
        assertEquals("Garigliano", emp.last_name, "Last name should match");
        assertEquals("Technique Leader", emp.title, "Current title should match");
        assertEquals(57499, emp.salary, "Current salary should match");

        assertNotNull(emp.dept, "Employee should have a department");
        assertEquals(DEVELOPMENT, emp.dept.dept_name, "Department should match");

        assertNotNull(emp.manager, "Employee should have a manager");
        assertEquals("Leon", emp.manager.first_name, "Manager first name should match");
        assertEquals("DasSarma", emp.manager.last_name, "Manager last name should match");
    }

    /** An unknown employee number should return no record. */
    @Test
    void getEmployeeTestNotFound()
    {
        Employee emp = app.getEmployee(-1);

        assertNull(emp, "An unknown employee should not be returned");
    }

    /** Retrieve a known department and its current manager. */
    @Test
    void getDepartmentTest()
    {
        Department dept = app.getDepartment(DEVELOPMENT);

        assertNotNull(dept, "Development department should exist");
        assertEquals("d005", dept.dept_no, "Department number should match");
        assertEquals(DEVELOPMENT, dept.dept_name, "Department name should match");
        assertNotNull(dept.manager, "Department should have a current manager");
    }

    /** An unknown department name should return no record. */
    @Test
    void getDepartmentTestNotFound()
    {
        Department dept = app.getDepartment("Unknown Department");

        assertNull(dept, "An unknown department should not be returned");
    }

    /** Retrieve current salary records for a known role. */
    @Test
    void getSalariesByRoleTest()
    {
        List<Employee> employees = app.getSalariesByRole("Engineer");

        assertNotNull(employees, "Role report should return a list");
        assertFalse(employees.isEmpty(), "Engineer report should contain employees");
        assertTrue(employees.get(0).salary > 0, "Returned salary should be positive");
    }

    /** Retrieve current salary records for a known department. */
    @Test
    void getSalariesByDepartmentTest()
    {
        Department dept = app.getDepartment(DEVELOPMENT);
        List<Employee> employees =
                app.getSalariesByDepartment(dept);

        assertNotNull(employees, "Department report should return a list");
        assertFalse(employees.isEmpty(), "Development report should contain employees");
        assertTrue(employees.get(0).salary > 0, "Returned salary should be positive");
    }
    /** Add an employee and verify that it can be retrieved. */
    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 500000;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";

        app.addEmployee(emp);

        Employee addedEmployee = app.getEmployee(500000);

        assertNotNull(addedEmployee, "Added employee should be retrievable");
        assertEquals(500000, addedEmployee.emp_no, "Employee number should match");
        assertEquals("Kevin", addedEmployee.first_name, "First name should match");
        assertEquals("Chalmers", addedEmployee.last_name, "Last name should match");
    }
}
