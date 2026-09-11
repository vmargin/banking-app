# JCash UI/UX Audit

Date: September 9, 2026  
Scope: Java Swing interface only  
Reviewed commit: `8019618`

## Verdict

The interface has a clear structure and restrained visual direction, but it is not screenshot-ready
or portfolio-polished. The invisible primary actions are a release blocker. Money movement also needs
a review step, explicit outcomes, responsive database handling, stronger currency presentation, and
basic accessibility support.

## Findings

| Priority | Finding | Impact |
|---|---|---|
| P0 | Primary-action text is nearly or completely invisible under the Windows look and feel. | Log in, Confirm cash-in, and Confirm transfer can appear as empty buttons. |
| P1 | Transfer has no genuine review step. | One click executes the transfer without a final recipient-and-amount check. |
| P1 | Database operations run on the Swing event thread. | A slow connection can freeze the window with no progress indication. |
| P1 | Successful cash-in and transfer actions have weak feedback. | Returning to the dashboard forces the user to infer whether the operation succeeded. |
| P2 | Money formatting is not banking-grade. | Values have no grouping separators, and history does not visually distinguish money in from money out. |
| P2 | Accessibility and scaling are weak. | Field labels are not programmatically associated, the runtime accessibility tree exposes little content, and the fixed window cannot adapt to larger text. |
| P2 | Login lockout has no recovery guidance. | The controls become disabled without explaining the next action. |
| P3 | Some copy exposes implementation details. | PostgreSQL language makes the dashboard read like a developer tool instead of a customer interface. |
| P3 | Transaction forms use more empty space than their content needs. | The forms feel unfinished and make the actions less visually focused. |

## What already works

- Clear dashboard hierarchy and restrained colour palette.
- Cash in, Transfer, and History are immediately discoverable.
- Back-to-dashboard and logout controls are consistently positioned.
- The transaction table is readable at the current desktop size.
- Empty-login validation is visible and understandable.

## Required remediation

1. Make primary buttons render consistently under the selected Swing look and feel.
2. Add a transfer review showing the recipient and formatted amount before execution.
3. Run login, cash-in, transfer, and history queries away from the event-dispatch thread.
4. Show an explicit busy state during database work and an explicit success result afterward.
5. Format Philippine pesos with grouping separators and signed transaction-history amounts.
6. Associate labels with fields, provide accessible names and descriptions, preserve keyboard focus,
   and allow the window to resize from a safe minimum size.
7. Explain that a locked login can be retried by restarting the local demo.
8. Replace database-facing copy with account-facing language.
9. Reduce unused form-card height and add a useful empty-history state.

## Verification boundary

The login, dashboard, cash-in, transfer, and history states were visually inspected. History was
rendered with synthetic preview data. No live database-backed financial transaction was submitted
during the audit.

## Implementation status

Implemented September 9, 2026:

- Primary buttons use a deterministic Swing UI delegate so their white label and green background are
  visible under the Windows look and feel.
- Login, cash-in, transfer, and history data access now run through `SwingWorker`; each operation
  shows a busy message and prevents duplicate submission while it is running.
- Transfer is now a two-step flow: enter details, review the recipient mobile number and PHP amount,
  then make the final confirmation. The transfer service is not called from the review action.
- Successful login, cash-in, and transfer operations now return the user to a dashboard with an
  explicit success banner. Cash-in and transfer notices include the formatted amount and balance.
- Amounts now use grouped PHP formatting. Transaction history uses `+` for cash-in/received money and
  `-` for sent money, with supplemental green/red text.
- Form labels are associated with their fields; fields, actions, feedback, and the history table have
  accessible names or descriptions. The window is resizable with a safe minimum size.
- Lockout copy now accurately tells the user to close and reopen the local demo to begin a new login
  session. Database-facing dashboard copy was replaced with customer-facing balance language.
- Forms and action cards have explicit maximum heights, and empty history has a useful explanation.

Verification completed:

- `.\scripts\dev.ps1 -Task verify` passed: 27 tests, 0 failures, 5 database-dependent tests skipped.
- Three new formatter tests cover grouped PHP amounts and incoming/outgoing transaction signs.
- Windows visual inspection confirmed readable primary buttons, resizable window controls, compact
  forms, transfer review, grouped balance display, and signed history rows using synthetic preview data.

Still required before submission: one live PostgreSQL walkthrough that submits a cash-in and a transfer
with disposable demo data, then captures the busy and success states for the assessment evidence.
