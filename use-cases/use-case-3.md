# USE CASE: 3 Produce a Salary Report for My Department

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *department manager* I want *to produce a report on the salary of employees in my department* so that *I can support financial reporting for my department.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The manager's identity and authority for their department are established. Current salary and department records are available.

### Success End Condition

The manager receives current salary details only for employees in their authorised department.

### Failed End Condition

No complete result is produced; the reason is reported and employee records remain unchanged.

### Primary Actor

Department manager.

### Trigger

The manager needs a departmental salary report.

## MAIN SUCCESS SCENARIO

1. The manager requests a report for their department.
2. The system identifies and verifies the manager's authorised department.
3. The system retrieves current employee and salary records for that department.
4. The system displays employee numbers, names, and salaries ordered by employee number.
5. The manager uses the report for departmental financial reporting.

## EXTENSIONS

2. **No authorised department is found**: Refuse access and explain the problem.

3. **No current records match**: Display a no-results message.

3. **Database access fails**: Report that the report is unavailable.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
