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
5. Start **Feature 1: Login + dashboard**. Before coding, explain in chat:

```text
How should the three-attempt login flow behave on success and failure?
What persisted balance should the dashboard display after a successful login?
```

6. Write the first service/UI attempt yourself. Then run the relevant tests and bring the actual result for review.

## Working loop

```text
Issue -> your prediction -> your code -> test/run -> review -> commit -> issue evidence
```

The next stages are login/dashboard, cash-in/history, then transfer/final proof. Spring, Docker, deployment, React, and an admin portal come only after the submission core is safe.

## First-session prompt

> Read Assessment Alignment and BA-05. Coach me through the JUnit tests for my current BankAccount code. Explain the concept, ask for my prediction, let me write the attempt, then review actual test output. Do not write the banking implementation for me.
