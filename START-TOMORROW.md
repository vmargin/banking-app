# Start here

1. Read `docs/ASSESSMENT-ALIGNMENT.md`, then `docs/IMPLEMENTATION-BLUEPRINT.md`.
2. Open `pom.xml` in IntelliJ IDEA and select Temurin JDK 21 as the Project SDK and Maven runner JRE.
3. In PowerShell from this project folder, run:

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
```

4. Do **not** rewrite the current `BankAccount.java` simply because the project plan changed. Its learner-authored construction/deposit/withdrawal work becomes your regression target.
5. Start **BA-05: Write regression tests for the current account rules**. Before coding, explain in chat:

```text
Which constructor, deposit, and withdrawal cases should pass?
Which failed call must leave the balance unchanged, and how will the test prove it?
```

6. Write the first JUnit attempt yourself. Then run `.\scripts\dev.ps1 -Task test` and bring the actual result for review.

## Working loop

```text
Issue -> your prediction -> your code -> test/run -> review -> commit -> issue evidence
```

The next stages are assessment models/package structure, JDBC/schema, persistent services, then Swing. Spring, Docker, deployment, React, and an admin portal come only after the submission core is safe.

## First-session prompt

> Read Assessment Alignment and BA-05. Coach me through the JUnit tests for my current BankAccount code. Explain the concept, ask for my prediction, let me write the attempt, then review actual test output. Do not write the banking implementation for me.
