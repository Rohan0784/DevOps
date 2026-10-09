# USE CASE: 6 View an Employee's Details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to view an employee's details* so that *I can support the employee's promotion request.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The advisor is authorised to view the record and knows the employee number. The database is available.

### Success End Condition

The requested employee's number, name, current title, salary, department, and manager are displayed.

### Failed End Condition

No complete result is produced; the reason is reported and employee records remain unchanged.

### Primary Actor

HR advisor.

### Trigger

HR receives a request to review an employee for promotion.

## MAIN SUCCESS SCENARIO

1. The advisor requests employee details and supplies the employee number.
2. The system validates the number.
3. The system retrieves the employee and current title, salary, department, and department manager.
4. The system displays the details.
5. The advisor reviews the information to support the promotion request.

## EXTENSIONS

2. **Invalid or missing employee number**: Request a valid integer.

3. **Employee does not exist**: Display an employee-not-found message.

3. **Current employment records are incomplete or the employee has left**: Explain that complete current information is unavailable rather than presenting historical information as current.

3. **Database retrieval fails**: Report that details could not be retrieved.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
