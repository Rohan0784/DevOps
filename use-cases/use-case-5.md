# USE CASE: 5 Add a New Employee's Details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to add a new employee's details* so that *I can ensure the new employee is paid.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The advisor is authorised to add records. Required personal and employment details, department, title, salary, and effective dates are available.

### Success End Condition

The employee and initial employment records are saved consistently and available for salary reporting.

### Failed End Condition

No partial changes are saved; the reason for failure is reported.

### Primary Actor

HR advisor.

### Trigger

HR receives an authorised request to register a new employee.

## MAIN SUCCESS SCENARIO

1. The advisor selects Add Employee.
2. The advisor enters the required personal and employment details.
3. The system validates required fields, dates, employee-number uniqueness, and department.
4. The system displays the proposed record for review.
5. The advisor confirms the addition.
6. The system saves the employee and related records in one transaction and confirms the employee number.

## EXTENSIONS

3. **Invalid or missing information**: Identify fields requiring correction.

3. **Employee number already exists**: Reject the duplicate and request a unique number.

5. **Advisor cancels**: Save nothing.

6. **Storage fails**: Roll back all changes and report the failure.

## SUB-VARIATIONS

2. The system may allocate the employee number if the project adopts that policy.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
