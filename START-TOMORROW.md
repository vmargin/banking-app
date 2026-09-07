# Start tomorrow

1. Read `BANKING-APP-GUIDE.md` tonight. The guide is a proposal with explicit cut lines, not a promise to finish every technology in five days.
2. Open this directory in IntelliJ IDEA using `pom.xml`. Select the installed Temurin JDK 21 as the Project SDK and Maven runner JRE if prompted. Use the Maven Wrapper.
3. Open PowerShell in this directory and run `.\scripts\dev.ps1 -Task doctor`.
4. Start backlog item `BA-01` in `docs/backlog.json`: write your banking rules and predicted examples in `docs/domain-decisions.md`. No code yet.
5. Explain why a negative deposit must leave the balance unchanged, what “Premium has no limit” means, and whether a savings limit is per withdrawal or per day.
6. Review that attempt with the assistant. Then start `BA-02`: your first Java account class.

## First session prompt

> Read this project's guide, setup report and BA-01. Coach me through one issue at a time. I write the banking code. Ask for my reasoning and attempt before showing a solution. Verify actual output and tests; don't close an issue because code merely exists. Start with the banking rules checkpoint.

## Commands

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test
.\scripts\dev.ps1 -Task run
```

`run` expects `com.vmargin.banking.Main` after you create it. Initially there is no application to run and no domain test to pass. See `SETUP-REPORT.md` for verified setup state and any GitHub action still needed.
