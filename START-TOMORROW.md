# Start tomorrow

1. Read `BANKING-APP-GUIDE.md`, then `docs/IMPLEMENTATION-BLUEPRINT.md`. The blueprint gives the proposed packages, class responsibilities, default rules, and coding order; you do not need to invent the whole architecture alone.
2. Open this directory in IntelliJ IDEA using `pom.xml`. Select the installed Temurin JDK 21 as the Project SDK and Maven runner JRE if prompted. Use the Maven Wrapper.
3. Open PowerShell in this directory and run `.\scripts\dev.ps1 -Task doctor`.
4. Start backlog item `BA-01`: confirm or deliberately change the proposed rules in `docs/IMPLEMENTATION-BLUEPRINT.md` and write six predicted examples in your own `docs/domain-decisions.md`.
5. Treat BA-01 as a short requirements checkpoint, not as a request to design the entire system. The next task already specifies `BankAccount` and its responsibility.
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
