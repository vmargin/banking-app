# JCash Banking App

An educational **Java Swing + JDBC + PostgreSQL** banking simulator for the TESDA Java Programming NC III assessment. It uses synthetic data only and is being built through small learner-authored issues with review and verification.

## Status

The environment and assessment-aligned delivery plan are prepared. `BankAccount` construction, deposit validation, and withdrawal validation have learner-authored commits; the required persistent login, cash-in, transfer, history, and Swing interface still need implementation and evidence.

## Submission target

```text
mobile number + PIN -> logged-in balance -> cash-in / transfer -> per-user transaction history
```

Read [Assessment Alignment](docs/ASSESSMENT-ALIGNMENT.md) for the requirement traceability and scope cut line. [Implementation Blueprint](docs/IMPLEMENTATION-BLUEPRINT.md) gives the package plan and coding sequence. [Project Management](docs/project-management.md) explains the issue workflow.

## Development

Use Temurin JDK 21 and the included Maven Wrapper on Windows:

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test
```

`test` and `verify` intentionally fail until real learner-authored tests exist. A green compilation/style command is not proof that a banking flow works.

## Scope boundary

The September assessment core is Java, Swing, JDBC, a local PostgreSQL database, login attempts, balance, cash-in, transfer, history, tests, and screenshot evidence. Spring Boot, deployment, Docker, React, admin roles, and production authentication are portfolio upgrades after the assessment flow is safe.

## Authorship

AI assistance prepares requirements, tooling, issue structure, and review. The learner writes the banking models, services, SQL, UI, and tests, then explains the resulting behaviour.
