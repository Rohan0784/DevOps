package com.napier.devops;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    void displaySalariesTestNull()
    {
        app.displaySalaries(null);
    }

    @Test
    void displaySalariesTestEmpty()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        app.displaySalaries(employees);
    }

    @Test
    void displaySalariesTestContainsNull()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        employees.add(null);
        app.displaySalaries(employees);
    }

    @Test
    void displaySalariesTestNormal()
    {
        ArrayList<Employee> employees = new ArrayList<>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        employees.add(emp);
        app.displaySalaries(employees);
    }

    @Test
    void displayEmployeeTestNull()
    {
        app.displayEmployee(null);
    }

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

        app.displayEmployee(emp);
    }

    @Test
    void displayEmployeeTestMissingDepartmentAndManager()
    {
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;

        app.displayEmployee(emp);
    }
}
