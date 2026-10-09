# USE CASE: 7 Update an Employee's Details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to update an employee's details* so that *I can keep employee details up-to-date.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The advisor is authorised to update records and knows the employee number, changes, and effective dates.

### Success End Condition

Confirmed changes are saved consistently, retaining employment history where applicable.

### Failed End Condition

No partial changes are saved; the reason for failure is reported.

### Primary Actor

HR advisor.

### Trigger

HR receives an authorised correction or employment-change request.

## MAIN SUCCESS SCENARIO

1. The advisor selects Update Employee and supplies the employee number.
2. The system retrieves and displays the existing details.
3. The advisor enters changes and effective dates.
4. The system validates the changes and displays a summary.
5. The advisor confirms the update.
6. The system saves the changes in one transaction and preserves applicable dated history.
7. The system displays the updated record and confirms completion.

## EXTENSIONS

2. **Employee not found**: Request another number or end the request.

4. **Invalid changes or dates**: Request corrections.

5. **Advisor cancels**: Leave the record unchanged.

6. **Record has changed since retrieval**: Request a fresh review before saving.

6. **Storage fails**: Roll back changes and report the failure.

## SUB-VARIATIONS

3. Personal-detail corrections update fields; salary, title, or department changes close the previous dated entry and create a new one with the agreed effective date.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
