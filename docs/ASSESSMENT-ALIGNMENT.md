# Assessment Alignment — JCash Banking App

**Status:** active delivery contract for the September 12 assessment. This document records what the supplied assessment brief requires; it does not claim that those features already exist.

## Source order

1. The candidate instruction is the binding assessment requirement.
2. The supplied final-output PDF is an example of a presentable solution, not a list of mandatory features.
3. The original portfolio ambition guides work only after the assessment MVP is demonstrably safe.

The supplied candidate instruction names a **JCash Banking App**, JDBC/database programming, a mobile-number/PIN login with a three-attempt limit, balance viewing, cash-in, transfers, and per-user transaction logs. It also specifies a `model`, `service`, and `util` project structure plus an application entry point.

## Delivery decision

Build one local **Java Swing + JDBC + PostgreSQL** application for the assessment. This is deliberately a desktop GUI, not a terminal-only demo and not a Spring web app before the deadline. It keeps Java as the centre of the project, meets the JDBC requirement directly, and produces clear screenshots.

`BankAccount`, deposit validation, and withdrawal validation remain useful learner-authored core work. They are not discarded. The next coding issues will connect that core to the assessment model and persistent data without duplicating balance rules.

## Requirement traceability

| Assessment requirement | Planned implementation evidence | Issue range |
|---|---|---|
| Java advanced/OOP design | private model state, constructors, focused services, explicit validation | BA-02 to BA-06 |
| `model`, `service`, `util`, main structure | package migration and a small `Main` composition root | BA-06, BA-15 |
| JDBC database application | PostgreSQL schema, connection utility, JDBC repositories | BA-07 to BA-09 |
| User: mobile number, PIN, name, balance, transactions | `User` model plus persistent user and transaction records | BA-06 to BA-09 |
| Transaction: type, amount, details, time | `Transaction`/`TransactionType` and persisted history | BA-06, BA-09, BA-14 |
| Mobile/PIN login, maximum three attempts | `LoginService` plus Swing login feedback | BA-10, BA-17 |
| Balance after successful login | dashboard reads the logged-in user's stored balance | BA-11, BA-18 |
| Positive cash-in, saved balance and log | `CashInService`, repository updates, GUI form | BA-12, BA-19 |
| Transfer validation and persistent records | transactional transfer service and GUI form | BA-13, BA-20 |
| Per-user history | history query and GUI table/list | BA-14, BA-21 |
| Performance monitoring/tuning | small, honest timing/configuration evidence; no fake enterprise claims | BA-23 |
| Screenshot/PDF submission | walkthrough, captions, final PDF | BA-24 to BA-27 |

## What is not required now

The sample output demonstrates Swing, MySQL/XAMPP, FlatLaf, registration, cash-out, settings, and an admin dashboard. Only the first three are examples of possible implementation choices. Registration, cash-out, changing PINs, a role/admin portal, Docker, React, deployment, Spring Boot, and real authentication/security are **not** required by the candidate brief.

We retain Spring, deployment, Docker, role-based admin work, and React as portfolio-upgrade work after submission. The assessment PIN is a simple local demonstration credential; it must never be described as production authentication.

## Scope cut line

Before screenshots, protect this order:

1. Persistent users and transactions through JDBC.
2. Actual three-attempt login, balance, cash-in, transfer, and history.
3. A usable Swing interface with readable success/error feedback.
4. Tests, local walkthrough, README, and screenshots.

Do not begin a Spring conversion, Docker image, deployment, React frontend, or admin portal while an item above is unproven. If time becomes tight, use seeded users and omit registration/cash-out/admin styling rather than weakening a required flow.

## Database boundary

PostgreSQL is the selected local database because it supports a credible portfolio upgrade and JDBC has no framework lock-in. The application will use one configured local database, fictional records only, and no credentials committed to Git. The connection and schema are not counted as complete until BA-07 proves a real JDBC connection and BA-08 proves a save/read round trip.
