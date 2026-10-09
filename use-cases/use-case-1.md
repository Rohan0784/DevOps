# USE CASE: 1 Produce a Report on the Salary of All Employees

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to produce a report on the salary of all employees* so that *I can support financial reporting of the organisation.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The advisor is authorised to access salaries and the database contains employee salary records.

### Success End Condition

A report of all employees with current salaries is available for finance.

### Failed End Condition

No complete result is produced; the reason is reported and employee records remain unchanged.

### Primary Actor

HR advisor.

### Trigger

Finance requests organisation-wide salary information.

## MAIN SUCCESS SCENARIO

1. The advisor selects the all-employees salary report.
2. The system retrieves employee numbers, names, and current salaries.
3. The system displays the report ordered by employee number.
4. The advisor provides the report to finance.

## EXTENSIONS

2. **No current records exist**: Display a no-results message.

2. **Database retrieval fails**: Report the failure without presenting a partial report as complete.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
