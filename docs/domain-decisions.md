# Domain Decisions

## Currency and precision

- Decision: Use PHP only and store money with `BigDecimal`.
- Reason: One currency keeps the first version manageable, and `BigDecimal` avoids floating-point money errors.
- Success example: `PHP 1,000.00` is accepted and displayed with the PHP marker.
- Failure example: `PHP 10.001` is rejected because it has more than two decimals.

## Account identity

- Decision: Use a stable synthetic ID such as `ACC-001` and a holder display name.
- Reason: These values are simple to read in console output, screenshots, and tests.

## Opening balance

- Decision: Allow zero or positive opening balances; reject negative balances.
- Reason: An account can exist without inventing credit or overdraft rules.

## Deposit

- Decision: Accept positive amounts with at most two decimal places.
- Reason: Invalid input must not change the account balance.

## Withdrawal

- Decision: Accept positive amounts with at most two decimal places when the amount does not exceed the available balance.
- Reason: The first version allows the balance to reach zero but rejects overdrafts.

## Transfer

- Decision: Require a positive amount, two distinct existing accounts, and enough funds in the sender account.
- Reason: All conditions must pass before either account changes, preventing self-transfers and partial mutations.

## Transaction history

- Decision: Record successful operations only, newest first.
- Reason: Failed attempts remain validation results rather than successful accounting activity in the first version.

## Account types

- Decision: Start with only one `BankAccount` implementation.
- Reason: The first version should prove deposit, withdrawal, transfer, validation, and history before adding variants.
- Deferred: Add `SavingsAccount` and `PremiumAccount` only after the core domain tests pass and their rules create meaningful behavior differences.

## Demo roles and ownership

- Decision: Define synthetic `ADMIN` and `CUSTOMER` roles.
- ADMIN can view and operate on all synthetic demo accounts.
- CUSTOMER can view and operate only on accounts they own.
- Reason: This demonstrates ownership and authorization without pretending that a demo selector is secure login.

## Demo selector boundary

- The selected user and role are synthetic showcase controls.
- This is not authentication or real security.
- No passwords, sessions, signup, or Spring Security are included in this slice.

## Deferred features

- Savings and Premium account variants
- Interest
- Daily limits
- Real login and password handling
- Real payments
- Database persistence

## Assessment rebase — binding constraints

The original decisions above were made before the official JCash candidate instruction was available. The following assessment constraints now override the previous deferral of persistence and the previous demo-selector boundary:

- Use a local JDBC database with synthetic users and transaction records.
- Login uses a user's mobile number and local demo PIN, with a maximum of three failed attempts per login interaction.
- A successful cash-in and transfer must update stored balance and transaction history.
- History is retrieved per logged-in user.
- The assessment UI will be Java Swing. Spring/admin roles/real authentication remain post-submission upgrades.

The user must confirm any business-rule change beyond these binding constraints in the relevant issue before implementation.

## Six predicted scenarios

1. Create `ACC-001` for Valk with `PHP 1,000.00` → success; balance is `PHP 1,000.00`.
2. Create an account with a negative opening balance → reject; no account is created.
3. Deposit `PHP 500.00` into an account with `PHP 1,000.00` → success; balance becomes `PHP 1,500.00`.
4. Deposit `PHP 0.00` or `PHP -10.00` → reject; balance remains unchanged.
5. Withdraw `PHP 400.00` from an account with `PHP 1,000.00` → success; balance becomes `PHP 600.00`.
6. Withdraw `PHP 1,100.00` from an account with `PHP 1,000.00` → reject; balance remains `PHP 1,000.00`.

## Transfer examples

- Transfer `PHP 200.00` from `ACC-001` (`PHP 1,000.00`) to `ACC-002` (`PHP 300.00`) → success; balances become `PHP 800.00` and `PHP 500.00`.
- Transfer to the same account, an unknown account, or with insufficient funds → reject; both balances remain unchanged.
