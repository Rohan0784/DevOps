package com.napier.devops;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppIntegrationTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
        app.connect("localhost:33060", 30000);
    }

    @AfterAll
    static void shutdown()
    {
        app.disconnect();
    }

    @Test
    void getEmployeeTest()
    {
        Employee emp = app.getEmployee(255530);

        assertNotNull(emp);
        assertEquals(255530, emp.emp_no);
        assertEquals("Ronghao", emp.first_name);
        assertEquals("Garigliano", emp.last_name);
        assertEquals("Technique Leader", emp.title);
        assertEquals(57499, emp.salary);

        assertNotNull(emp.dept);
        assertEquals("Development", emp.dept.dept_name);

        assertNotNull(emp.manager);
        assertEquals("Leon", emp.manager.first_name);
        assertEquals("DasSarma", emp.manager.last_name);
    }

    @Test
    void getEmployeeTestNotFound()
    {
        Employee emp = app.getEmployee(-1);

        assertNull(emp);
    }

    @Test
    void getDepartmentTest()
    {
        Department dept = app.getDepartment("Development");

        assertNotNull(dept);
        assertEquals("d005", dept.dept_no);
        assertEquals("Development", dept.dept_name);
        assertNotNull(dept.manager);
    }

    @Test
    void getDepartmentTestNotFound()
    {
        Department dept = app.getDepartment("Unknown Department");

        assertNull(dept);
    }

    @Test
    void getSalariesByRoleTest()
    {
        List<Employee> employees = app.getSalariesByRole("Engineer");

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertTrue(employees.get(0).salary > 0);
    }

    @Test
    void getSalariesByDepartmentTest()
    {
        Department dept = app.getDepartment("Development");
        ArrayList<Employee> employees =
                app.getSalariesByDepartment(dept);

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertTrue(employees.get(0).salary > 0);
    }
    @Test
    void testAddEmployee()
    {
        Employee emp = new Employee();
        emp.emp_no = 500000;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";

        app.addEmployee(emp);

        Employee addedEmployee = app.getEmployee(500000);

        assertNotNull(addedEmployee);
        assertEquals(500000, addedEmployee.emp_no);
        assertEquals("Kevin", addedEmployee.first_name);
        assertEquals("Chalmers", addedEmployee.last_name);
    }
}