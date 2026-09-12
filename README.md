# JCash Banking App

An educational **Java Swing + JDBC + PostgreSQL** banking simulator for the TESDA Java Programming NC III assessment. It uses synthetic data only and is being built through small learner-authored issues with review and verification.

## Status

The completed MVP includes Swing login, registration, balance, cash-in, atomic
transfer, per-user transaction history, and role-aware administration backed by
PostgreSQL. The verified suite runs 32 tests with 0 failures and 6 database
tests skipped when the local database is unavailable.

## Submission target

```text
mobile number + PIN -> logged-in balance -> cash-in / transfer -> per-user transaction history
```

The assessment notes and internal delivery guides remain local-only.

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

The v1 scope is Java, Swing, JDBC, PostgreSQL, login, registration, balance, cash-in,
transfer, history, administration, tests, and local development scripts. The WebView
redesign is intentionally developed in the separate banking-app-v2 repository.

## Authorship

AI assistance prepares requirements, tooling, issue structure, and review. The learner writes the banking models, services, SQL, UI, and tests, then explains the resulting behaviour.
