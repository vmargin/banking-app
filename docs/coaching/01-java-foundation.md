# 01 - Java Foundation

## BA-01 - Define banking rules

- Do: document account creation, PHP/`BigDecimal` precision, opening balance, account types, transfer, roles, and deferred features.
- Checkpoint: explain private balance, per-operation versus daily limits, failure without mutation, and why a demo selector is not login.
- Proof: at least six concrete success/failure examples and explicit unknown/deferred rules.

## BA-02 - Create account state and constructor

- Do: create `BankAccount` with stable ID, holder, private `BigDecimal` balance, constructor validation, and read-only access.
- Checkpoint: which fields must be private, and what makes an account valid at creation?
- Proof: invalid constructor inputs are rejected; no public balance setter; style and compile pass.

## BA-03 - Implement deposits

- Do: add positive, valid decimal deposits with validation before mutation.
- Checkpoint: what happens for null, zero, negative, and excessive precision?
- Proof: predict and verify success plus every rejection; balance stays unchanged after rejection.

## BA-04 - Implement withdrawals

- Do: add withdrawal validation and insufficient-funds handling.
- Checkpoint: what validation order is safest, and can balance reach zero?
- Proof: exact balance, overdraw, zero, negative, and decimal cases; rejected state unchanged.

## BA-05 - Console smoke demonstration

- Do: write `Main` only to demonstrate existing operations.
- Checkpoint: what output do you predict before running it?
- Proof: compile/run `com.vmargin.banking.Main`; compare predicted and actual output.

## BA-06 - First meaningful JUnit tests

- Do: convert manual examples into focused tests.
- Checkpoint: what before/after state proves the behavior?
- Proof: success and rejection tests; temporarily break behavior, observe a failing test, then restore it.
