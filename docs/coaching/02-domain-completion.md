# 02 - Domain Completion

## BA-07 - Savings and Premium differences

- Do: add account-type behavior only where the documented rules differ.
- Checkpoint: what rule changes, and why is a cast unnecessary?
- Proof: success/failure tests through a parent reference.

## BA-08 - Domain exceptions

- Do: replace unclear failure signals with meaningful domain exceptions and caller handling.
- Checkpoint: how does throwing differ from printing an error?
- Proof: callers distinguish failures; no swallowed errors or partial mutation.

## BA-09 - Account repository

- Do: store/find multiple accounts behind a small repository boundary.
- Checkpoint: why depend on a storage contract instead of a collection?
- Proof: unique IDs, lookup success, and missing-account tests.

## BA-10 - Transaction history

- Do: record successful operations with amount, type, time, and account identity.
- Checkpoint: can callers change old records, and which failures are recorded?
- Proof: order, decimal amounts, and rejected-operation policy verified.

## BA-11 - Transfers

- Do: coordinate two-account transfers without duplicating account rules.
- Checkpoint: what happens if destination lookup or validation fails after debit?
- Proof: successful conservation, self/missing destination rejection, no partial mutation, shared reference.

## BA-12 - Domain gate

- Do: run the complete core review before Spring.
- Checkpoint: trace one successful and one failed transfer unaided.
- Proof: Maven tests pass, boundaries are covered, and in-memory concurrency limits are documented.
