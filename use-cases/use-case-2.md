# USE CASE: 2 Produce a Salary Report for a Department

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to produce a report on the salary of employees in a department* so that *I can support financial reporting of the organisation.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The advisor is authorised to access salary information and knows the requested department.

### Success End Condition

A report of current employees and salaries for the selected department is available.

### Failed End Condition

No complete result is produced; the reason is reported and employee records remain unchanged.

### Primary Actor

HR advisor.

### Trigger

Finance requests salary information for a department.

## MAIN SUCCESS SCENARIO

1. The advisor selects the departmental salary report.
2. The advisor supplies the department name or identifier.
3. The system validates the department and retrieves its current employees and salaries.
4. The system displays employee numbers, names, and salaries ordered by employee number.
5. The advisor provides the report to finance.

## EXTENSIONS

2. **Missing or invalid input**: Request corrected department details.

3. **Department does not exist**: Inform the advisor and allow another selection.

3. **No current employees match**: Display a no-results message.

3. **Database access fails**: Report that the report could not be produced.

## SUB-VARIATIONS

2. The department may be selected from a list instead of entered as text.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
