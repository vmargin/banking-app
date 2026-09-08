# BankingApp — assessment and learning guide

## What you are building

Build an explainable **JCash banking simulator** for TESDA Java Programming NC III. The September 12 delivery is a local Java Swing application backed by PostgreSQL through JDBC, demonstrated with screenshots in a PDF.

The required flow is real within the local demo:

```text
mobile number + PIN -> at most three login attempts -> stored balance -> cash-in / transfer -> per-user history
```

All people, mobile numbers, balances, and PINs are synthetic. This is not a real financial service and must never be presented as one.

## Why the plan changed

The original plan—plain Java rules first, then Spring—was sensible for a general portfolio. The official assessment instruction is more specific: it requires JDBC/database work, a mobile/PIN login limit, persistent cash-in/transfer data, and per-user logs. That requirement overrides the earlier deferral of persistence and the Spring-first UI idea.

This does not waste your existing Java work. `BankAccount` construction/deposit/withdrawal are the foundational rules you will test, move into the assessment package structure, and reuse from services. The difference is that the next layers must be JDBC and Swing before Spring.

## What is required versus deferred

| Build before screenshots | Defer until after the assessment core is safe |
|---|---|
| Java 21, Maven, Swing, JDBC, PostgreSQL | Spring Boot / Thymeleaf |
| User and transaction models | Docker and deployment |
| three-attempt mobile/PIN demo login | React |
| balance, cash-in, transfer, per-user logs | admin/roles, registration, cash-out, change PIN |
| tests, local walkthrough, README, screenshot PDF | real password security and production authentication |

The supplied sample output is useful visual inspiration. Its XAMPP/MySQL, FlatLaf, registration, cash-out, settings, and admin dashboard are not binding unless you finish the required flow first.

## How you work with the assistant

The project is intentionally senior-coach style:

1. You open the next GitHub issue and explain your prediction.
2. I teach the one concept that issue needs and challenge unclear assumptions.
3. You write the first implementation attempt.
4. We run it, inspect evidence, and review the smallest useful improvement.
5. You commit it and attach actual evidence to the issue.

This is not “AI writes a banking app while you watch.” The goal is that you can open any class, explain why it exists, change one business rule, and defend the result during an interview.

## The immediate next issue

Your current commits already establish account state, deposit, and withdrawal. The next task is **BA-05: regression tests**. Before writing code, answer:

- What valid constructor, deposit, and withdrawal cases must pass?
- Which rejected call must leave the balance unchanged?
- How will an assertion prove that unchanged state?

After those tests protect the current work, BA-06 introduces `User`, `Transaction`, `TransactionType`, and the required `model`/`service`/`util` structure. Then JDBC comes before the Swing screens.

## Commands

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test
.\scripts\dev.ps1 -Task verify
```

Use `doctor` to check Java/Maven, `style` and `compile` after source changes, `test` after test/behaviour changes, and `verify` for milestone checks. `test`/`verify` intentionally fail until you create real tests; a green compile is not feature evidence.

Read [Assessment Alignment](docs/ASSESSMENT-ALIGNMENT.md), [Implementation Blueprint](docs/IMPLEMENTATION-BLUEPRINT.md), and [Engineering Standards](docs/ENGINEERING-STANDARDS.md) before BA-05.
