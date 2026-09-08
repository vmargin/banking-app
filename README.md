# Banking App

An educational banking simulator being built to demonstrate explainable Java programming.

**Status:** development environment and learning backlog prepared. Banking features have not been implemented.

Read [the full project guide](BANKING-APP-GUIDE.md) and [tomorrow's starting point](START-TOMORROW.md).
For setup explanations, see [Workflow and Tools](docs/WORKFLOW-AND-TOOLS.md).
For the classes, packages, and issue-by-issue coding flow, read the [Implementation Blueprint](docs/IMPLEMENTATION-BLUEPRINT.md).
For the coding, architecture, testing, and review rules used in this repository, read the [Engineering Standards](docs/ENGINEERING-STANDARDS.md).

## Development

Use Java 21 and the included Maven Wrapper. On Windows:

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task test
```

The test/verify phases intentionally fail when no tests exist. `style` is safe to run from the beginning; it checks the committed Java style rules. A successful setup or style check is not evidence that banking rules work.

On another OS, install JDK 21 and run `./mvnw test`.

## Intended scope

Account creation, deposits, withdrawals, account rules, transaction history, transfers, tests, then a Spring web interface. PostgreSQL, authentication, Docker and deployment are tracked as further milestones. All balances are simulated; no real financial services are provided.

## Authorship

AI assistance prepares tooling, requirements and reviews. The learner reasons through and implements the banking features. Feature claims require source and test evidence.
