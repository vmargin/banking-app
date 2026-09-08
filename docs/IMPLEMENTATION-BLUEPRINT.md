# BankingApp Implementation Blueprint

This is the coding map for the learner-authored assessment application. Read [Assessment Alignment](ASSESSMENT-ALIGNMENT.md) first: the candidate instruction now sets the submission scope.

## Build target

For September 12, build a local **Java Swing + JDBC + PostgreSQL** JCash simulator. It uses synthetic users and fictional PHP amounts. The proof flow is:

```text
mobile number + PIN -> successful login -> stored balance -> cash-in or transfer -> stored transaction history
```

The original portfolio direction is still valid, but it happens after this proof flow: Spring Boot/Thymeleaf, deployment, Docker, and a role-based admin experience are upgrades, not blockers.

## Architecture map

```text
Swing UI -> service -> model
                  |
                  v
             repository -> util (JDBC connection)
```

`model` owns valid data and small business rules. `service` coordinates one use case (login, cash-in, transfer, history). A `repository` owns SQL and mapping. `util` owns database connection configuration. The Swing UI reads input, calls a service, and displays the result; it does not contain SQL or duplicate money arithmetic.

## Package plan

Create files only when their issue becomes active. `BankAccount` stays valuable, but will be moved under `model` once its existing behaviour is covered by tests.

```text
src/main/java/com/vmargin/banking/
├── Main.java
├── model/
│   ├── BankAccount.java
│   ├── User.java
│   ├── Transaction.java
│   └── TransactionType.java
├── repository/
│   ├── UserRepository.java
│   ├── JdbcUserRepository.java
│   ├── TransactionRepository.java
│   └── JdbcTransactionRepository.java
├── service/
│   ├── LoginService.java
│   ├── CashInService.java
│   ├── TransferService.java
│   └── TransactionHistoryService.java
├── ui/
│   ├── LoginFrame.java
│   └── DashboardFrame.java
└── util/
    └── DatabaseConnection.java
```

The exact public API is designed at the relevant issue. Do not pre-create empty classes just to match this tree.

## Current work and immediate sequence

Your commits already cover valid initial account state, deposit, and withdrawal. Those are supporting domain rules, not wasted work. Do not rewrite the current in-progress `BankAccount.java` during this rebase.

| Issue | Learner outcome | Main concept |
|---|---|---|
| BA-01 to BA-04 | Assessment decisions, `BankAccount` construction/deposit/withdrawal | requirements, encapsulation, `BigDecimal`, validation |
| BA-05 | Add focused tests around the existing `BankAccount` behaviour | Arrange–Act–Assert; failed operation leaves state unchanged |
| BA-06 | Add the assessment `User`/`Transaction` model and move the tested account class into `model` | composition, enum, package structure |
| BA-07 to BA-09 | Configure JDBC, create the schema, and prove user/transaction persistence | JDBC, SQL mapping, separation of concerns |
| BA-10 to BA-14 | Implement login, balance, cash-in, transfer, and history as services | abstraction, exceptions, atomicity, use cases |
| BA-15 | Compose a small non-GUI smoke flow in `Main` | dependency wiring and predictable output |
| BA-16 to BA-22 | Build the Swing screens around the already-tested services | UI boundaries and user feedback |
| BA-23 to BA-27 | Performance evidence, regression, explanation, screenshots, PDF | evidence and submission quality |
| BA-28 to BA-29 | Spring and portfolio hardening | post-submission upgrade work |

## The learning loop for every issue

1. Read the issue’s outcome, acceptance criteria, and reasoning checkpoint.
2. In chat, state your prediction and a first implementation idea.
3. Write the smallest Java/database/UI change yourself.
4. Run the required command or manual walkthrough.
5. We review the result and edge cases.
6. Commit and attach concise real evidence to the GitHub issue.

The coach may explain a concept, ask critical questions, improve tooling, and review your diff. You remain the author of banking features, models, SQL, and tests so you can defend them.

## Rules that must hold

- Use `BigDecimal` for PHP money; accept only positive cash-in/transfer amounts with no more than two decimals.
- Preserve state on rejected operations.
- A transfer validates recipient, amount, and sender funds before persistent updates; the two balance updates and transaction records belong in one JDBC transaction.
- Store only synthetic names, mobile numbers, and simple demo PINs in local development data. Never commit a real credential or use the demo PIN design as a security claim.
- A failed login records an attempt for the running login interaction. On the third failed attempt, show the required failure message and stop that attempt flow.
- Every user sees only that user's history in the assessment UI.

## Submission cut line

The first submission does **not** need registration, cash-out, user settings, an admin panel, Spring Security, Docker, React, deployment, or a real bank integration. Adding one is allowed only after the required flow has evidence. A polished, correct Swing/JDBC app is stronger than a half-finished multi-stack project.
