# BankingApp Setup Report

**Prepared:** September 8, 2026 (Asia/Manila)
**Purpose:** record only the starter-workspace state verified before feature development begins.

## Verified

- The project uses the Maven Wrapper with **Apache Maven 3.9.11** and **Eclipse Temurin JDK 21**.
- `./scripts/dev.ps1 -Task doctor` confirmed the Java 21 toolchain.
- `./scripts/dev.ps1 -Task compile` completed successfully.
- An ignored, isolated toolchain probe ran one JUnit test successfully on Java 21. It proves the toolchain, **not** banking behaviour.
- The GitHub repository exists at <https://github.com/vmargin/banking-app> and is intentionally private until the project is ready for public review.
- GitHub has the planned labels, five milestones, and 29 learning issues. The planning board is at <https://github.com/users/vmargin/projects/4>.
- The repository contains an issue template, pull-request template, and a GitHub Actions compile check.
- `BANKING-APP-GUIDE.md`, `START-TOMORROW.md`, and `docs/project-management.md` are the working handoff documents.

## Expected at this stage

- `./mvnw test` fails while there are no project tests because the Maven configuration intentionally rejects an empty test suite. That is a guardrail, not a broken build.
- There is deliberately no banking domain implementation yet. BA-01 is the first learner-authored task.
- Spring Boot, PostgreSQL, authentication, Docker, a deploy target, and a possible React UI are planned decisions or later milestones; they are not claimed as implemented.

## First work session

1. Read `BANKING-APP-GUIDE.md` fully.
2. Open this folder in IntelliJ IDEA.
3. Run `./scripts/dev.ps1 -Task doctor`.
4. Start GitHub issue [BA-01](https://github.com/vmargin/banking-app/issues/1): define the banking rules, edge cases, and sample scenarios before writing Java.

## Recheck commands

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task compile
.\mvnw.cmd test  # expected to fail until the first real test exists
```
