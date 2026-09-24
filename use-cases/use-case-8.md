# USE CASE: 8 Delete an Employee's Details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to delete an employee's details* so that *I can ensure the company is compliant with data retention legislation.*

### Scope

HR System.

### Level

Primary task.

### Preconditions

The advisor is authorised to delete records. The employee is identified and the organisation's approved retention policy is available.

### Success End Condition

Eligible employee data and related records within the deletion scope are removed consistently and completion is confirmed.

### Failed End Condition

No unauthorised or partial deletion occurs; records remain unchanged and the reason is reported.

### Primary Actor

HR advisor.

### Trigger

An employee record is identified for deletion under the approved retention policy.

## MAIN SUCCESS SCENARIO

1. The advisor requests deletion and supplies the employee number.
2. The system retrieves the record and checks eligibility under the approved retention policy and any holds.
3. The system displays the employee identity, deletion scope, and consequences.
4. The advisor explicitly confirms deletion.
5. The system deletes the eligible records as one transaction.
6. The system confirms completion and retains only audit information permitted by policy.

## EXTENSIONS

2. **Employee not found**: Report that no matching record exists.

2. **Retention requirements or a hold prevent deletion**: Block deletion and explain the reason.

4. **Advisor cancels**: Delete nothing.

5. **Deletion fails**: Roll back the transaction and report failure.

## SUB-VARIATIONS

None. Retention periods and audit rules must be agreed with the responsible policy owner; no legal retention period is assumed here.

## SCHEDULE

**DUE DATE**: To be agreed with the project team.
