# JCash Banking App

An educational **Java Swing + JDBC + PostgreSQL** banking simulator for the TESDA Java Programming NC III assessment. It uses synthetic data only and is being built through small learner-authored issues with review and verification.

## Status

The local MVP is working: Swing login, balance dashboard, cash-in, atomic transfer,
and per-user transaction history are backed by PostgreSQL. The automated suite has
24 tests. Screenshot/PDF evidence is the remaining assessment-delivery work.

## Submission target

```text
mobile number + PIN -> logged-in balance -> cash-in / transfer -> per-user transaction history
```

Read [Assessment Alignment](docs/ASSESSMENT-ALIGNMENT.md), [Active Delivery Plan](docs/VERTICAL-DELIVERY-PLAN.md), and [Project Management](docs/project-management.md).

## Development

Use Temurin JDK 21 and the included Maven Wrapper on Windows:

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test
```

`test` and `verify` run the regression and JDBC checks. A green build verifies
the covered domain, persistence, and transfer behavior; it does not replace a
manual walkthrough of the Swing screens before screenshots are submitted.

## Scope boundary

The September assessment core is Java, Swing, JDBC, a local PostgreSQL database, login attempts, balance, cash-in, transfer, history, tests, and screenshot evidence. Spring Boot, deployment, Docker, React, admin roles, and production authentication are portfolio upgrades after the assessment flow is safe.

## Authorship

AI assistance prepares requirements, tooling, issue structure, and review. The learner writes the banking models, services, SQL, UI, and tests, then explains the resulting behaviour.
