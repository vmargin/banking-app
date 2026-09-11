# JCash UI/UX Implementation: Read Before the Learning Checkpoint

Read this after `UI-UX-AUDIT-2026-09-09.md`. It explains what changed, why it changed, and where to
trace it. It is not a replacement for the checkpoint: you should be able to explain the flow and make
one small modification yourself before moving on.

## The vertical feature map

| Feature | User-visible result | Main code path | Concept to understand |
|---|---|---|---|
| Login | Readable Log in button, busy state, clear lockout recovery | `attemptLogin()` -> `SwingWorker` -> `LoginService.login()` -> `done()` | Swing event-dispatch thread versus background work |
| Dashboard | Grouped balance, customer-facing copy, success banner | `showDashboard()` -> `createDashboardContent()` -> `createBalanceCard()` | A screen should communicate the result of the previous action |
| Cash-in | Add funds button, inline validation, busy state, success notice | `showCashInScreen()` -> `submitCashIn()` -> `CashInService.cashIn()` | Validate input before the database operation; update the screen only after success |
| Transaction history | Loading state, empty state, signed and coloured money movement | `showHistoryScreen()` -> `loadHistory()` -> `addHistoryRows()` | A transaction table must make money in and money out scannable |
| Transfer | Review first, then final confirmation and success notice | `showTransferScreen()` -> `reviewTransfer()` -> `showTransferReviewScreen()` -> `submitTransfer()` | Separate review from the action that mutates money |

## The important concepts

### 1. Swing has one UI thread

Button listeners start on Swing's event-dispatch thread. If that listener performs database work itself,
the window cannot repaint, accept clicks, or show a loading message while PostgreSQL is slow.

`SwingWorker` divides the work into two places:

- `doInBackground()` performs the service/database call away from the UI thread.
- `done()` runs back on the UI thread. It reads the result with `get()` and changes Swing components.

The UI disables the active controls first, shows a plain busy message, then either restores the form on
failure or changes screen on success. That prevents accidental double submission.

Trace: `LoginFrame.java` methods `attemptLogin`, `submitCashIn`, `submitTransfer`, and `loadHistory`.

### 2. A review action must not transfer money

The Transfer form button is now **Review transfer**, not **Confirm transfer**. It only reads the typed
recipient mobile number and amount, validates basic input, and opens the review screen. It does not call
`TransferService`.

Only **Confirm transfer** on the next screen calls `TransferService.transfer`. The review screen also
offers **Back and edit**, which preserves the entered recipient and amount.

The current model can verify the recipient mobile number at transfer time, but it cannot look up and
display a recipient name before transfer. The UI deliberately shows the mobile number instead of
inventing a name.

Trace: `reviewTransfer`, `showTransferReviewScreen`, and `submitTransfer`.

### 3. UI success is not the same as database success

The service performs the operation and returns the updated balance. Only after `get()` succeeds does the
UI create a dashboard notice such as “Cash-in successful” or “Transfer successful.” If the service throws
an exception, the UI stays on the current screen and restores the button and fields.

This preserves the existing service rule: a failed database operation must not be presented as successful.

### 4. Money needs two formats

The balance is a neutral amount: `PHP 125,000.50`.

The history needs direction as well as magnitude:

- `+ PHP 5,000.00` for Cash in and Transfer received
- `- PHP 1,250.50` for Transfer sent

The sign carries the meaning; green and red are only a visual reinforcement. Formatting helpers are
package-visible and tested in `LoginFrameFormattingTest`.

### 5. Accessibility is part of a functional form

Each label now calls `setLabelFor(field)`. Fields, buttons, feedback labels, and the transaction table
also receive names or descriptions for assistive technology and reliable component identification. The
window is resizable and uses a minimum size rather than a permanently fixed frame.

The Windows accessibility bridge itself still needs a local screen-reader check; code-level metadata is
present, but the earlier runtime inspector did not expose Java form controls.

## What did not change

- Banking balance rules, service validation, repository transactions, and database schema were not
  changed by this UI/UX work.
- There is no fake transfer reference number. A real receipt ID would need a deliberate persistence
  contract change.
- Recipient-name lookup was not added because the existing transfer API does not provide it.

## Verification you can trust

`.\scripts\dev.ps1 -Task verify` passed with 27 tests, 0 failures, and 5 skipped database-dependent
tests. The added formatter tests prove the PHP grouping and sign rules. Windows preview inspection proved
the primary buttons, layout, transfer review, and signed history rendering with synthetic data.

The remaining proof is a live local PostgreSQL walkthrough using disposable demo data. It must show busy
and success states for at least one cash-in and one transfer before screenshots are submitted.

## Learning checkpoint: answer without opening the source first

1. Why is `TransferService.transfer` inside `submitTransfer`, not `reviewTransfer`?
2. What would happen to the window if `cashInService.cashIn` were called directly inside the button
   listener instead of `doInBackground()`?
3. Why must `showDashboard()` run in `done()` rather than inside `doInBackground()`?
4. What does the `+` or `-` sign add that colour alone cannot?
5. If the transfer database insert fails, which screen should remain visible and why?
6. Change one customer-facing label yourself, run `.\scripts\dev.ps1 -Task verify`, and explain why the
   change belongs in the UI rather than the service.

## Next session boundary

Read this brief first. The next step is the learning checkpoint: explain one vertical feature back, trace
its methods in order, make the small label change yourself, then run and interpret verification. Do not
start another feature until that is complete.
