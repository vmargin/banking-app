# BankingApp Workflow and Tools

This note explains the setup already prepared in this repository. It is a reference for the learner, not a replacement for understanding the code.

## The short version

- **IntelliJ IDEA:** the primary editor, debugger, Maven interface, and Java learning environment.
- **Java JDK 21:** compiles and runs this project. The installed distribution is Eclipse Temurin, which is an OpenJDK distribution; it is not the Eclipse IDE.
- **Maven:** reads `pom.xml`, downloads declared libraries and plugins, compiles the project, runs tests, and packages it.
- **Maven Wrapper:** the tracked `mvnw.cmd` script downloads/uses the project’s pinned Maven version, so the project does not depend on a globally installed Maven.
- **Git:** records small, explainable changes.
- **GitHub:** stores the repository and the learning issues/milestones.
- **Docker/PostgreSQL/React:** available or planned for later milestones, not prerequisites for the first plain-Java domain work.

Use one primary IDE and one source of truth: this repository. Do not install several IDEs just to work on the same task.

## Why Maven is here

Without Maven, you would manually download JUnit and later Spring JAR files, put them on the classpath, compile in the right order, and repeat that setup on every machine or CI run. Maven makes those instructions executable and repeatable.

`pom.xml` is the project’s build recipe. It currently declares:

1. Java release 21.
2. JUnit Jupiter 5.13.4 as a test dependency.
3. Compiler, Surefire (test), and Exec (run a `Main` class) plugins.
4. A deliberate `failIfNoTests` guard, so a green test command cannot hide an empty test suite.
5. Maven Enforcer, which rejects a build run with the wrong Java/Maven baseline.
6. Checkstyle, which checks the repository’s small Java naming and formatting baseline.

Maven does **not** write the banking logic for you. It only manages the repeatable build/test process.

## What the Maven Wrapper does

The repository contains `mvnw` and `mvnw.cmd`, plus `.mvn/wrapper/maven-wrapper.properties`. On Windows, use `mvnw.cmd` or the provided PowerShell script. The wrapper uses Maven 3.9.11 for this project and can download that version into your local Maven cache when needed.

This is why a global Maven installation is unnecessary. The wrapper is part of the repository and makes CI and your computer use the same Maven version.

## JDK 26 versus Temurin JDK 21

You do not need to uninstall OpenJDK 26. Multiple JDKs can be installed.

This project targets Java 21 because it is the declared project release and an LTS baseline suitable for the Spring upgrade later. Temurin is the vendor/distribution of OpenJDK 21. IntelliJ may initially open the project using your system OpenJDK 26; that is not the intended project setting.

### Set IntelliJ to the project JDK

1. Open the project using `pom.xml` as described below.
2. Open **File → Project Structure** (`Ctrl+Alt+Shift+S`).
3. Choose **Project**.
4. Set **Project SDK** to Temurin 21. If it is absent, choose **Add SDK → JDK** and select:
   `C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot`
5. Set **Language level** to 21.
6. Open **Settings → Build, Execution, Deployment → Build Tools → Maven**.
7. Under **Importing** and **Runner**, choose the Temurin 21/project SDK where IntelliJ asks for a JDK.
8. Reload the Maven project if IntelliJ offers a reload icon in the Maven tool window.

The exact Temurin folder can change when a newer JDK 21 patch is installed. Use the folder beginning with `jdk-21` under `C:\Program Files\Eclipse Adoptium`.

### Why `dev.ps1` still sets the JDK

The script temporarily sets `JAVA_HOME` and the command `PATH` to the newest installed Temurin JDK 21, runs the selected Maven task, and restores your previous environment afterward. This makes a terminal check deterministic even if your system default remains OpenJDK 26. It does not permanently change Windows or uninstall anything.

## How to open the project in IntelliJ

From the IntelliJ welcome screen:

1. Choose **Open**.
2. Browse to `C:\Users\margi\Desktop\lockedIn\banking-app`.
3. Select `pom.xml` and choose **Open as Project** (wording can vary slightly by IntelliJ version).
4. Trust the project if IntelliJ asks.
5. Select Temurin 21 when IntelliJ asks for the project SDK.
6. Wait for Maven import to finish. The Maven tool window should show the project and its lifecycle tasks.

You can also open the folder, then use the Maven tool window to reload `pom.xml`. The important part is that IntelliJ imports the Maven project from the repository’s `pom.xml`; do not create a second unrelated project beside it.

## What each development command means

Run these from `C:\Users\margi\Desktop\lockedIn\banking-app` in PowerShell:

```powershell
.\scripts\dev.ps1 -Task doctor
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test
.\scripts\dev.ps1 -Task verify
.\scripts\dev.ps1 -Task run
.\scripts\dev.ps1 -Task package
```

| Command | Purpose | Current expected result |
|---|---|---|
| `doctor` | Shows the Maven and temporarily selected Temurin JDK versions | Passes now |
| `style` | Runs the project’s Checkstyle rules against production and test Java code | Passes now; there is no banking source yet |
| `compile` | Checks that current Java source compiles | Passes now; there is no banking source yet |
| `test` | Compiles and runs project tests | Intentionally fails until you create a real test |
| `verify` | Runs the full Maven lifecycle: tests, style gate, and packaging checks | Intentionally fails until BA-06 creates a real test |
| `run` | Compiles and runs `com.vmargin.banking.Main` | Intentionally fails until you create `Main` |
| `package` | Builds the distributable JAR after the project has runnable source/tests | Wait until the app exists |

You do not need to run every command after every keystroke. Use `doctor` when checking the environment, `style` before committing Java code, `compile` after source changes, `test` after a test or behaviour change, and `verify`/`package`/`run` at the relevant milestone. The actual rules are in `docs/ENGINEERING-STANDARDS.md`.

## How to answer the guide’s questions

There are two kinds of answers:

1. **Reasoning answers:** write your own decision before coding. For the first issue, create `docs/domain-decisions.md` and answer the banking-rule prompts there. You can also discuss the reasoning here for coaching.
2. **Evidence answers:** after implementation, record the command, predicted output, actual output, edge cases checked, and explanation in the relevant GitHub issue comment and/or a project document.

The guide is not a form that must be completed inside the chat. Chat is where we review your reasoning. The repository is where durable decisions, code, tests, and evidence live.

## How GitHub issues are used

The repository already has 29 small learning issues across five milestones. Each issue is a bounded checkpoint, not permission to skip the prerequisite reasoning.

Use this cycle:

1. Open the next issue whose prerequisites are satisfied.
2. Move it to **In Progress** on the Project board.
3. Read its acceptance criteria and write your prediction/plan before code.
4. Make the smallest learner-authored change that addresses the issue.
5. Run the relevant command and record actual evidence.
6. Add a concise progress/evidence comment to the issue. Comments are the right place for attempts, output, questions, and review notes; do not replace the issue’s original acceptance criteria.
7. Move the issue to **Review** when the evidence and explanation are ready.
8. Close it only after the acceptance criteria pass and you can explain the design. Reference the issue in the commit or pull request, for example `Refs #1` or `Closes #1` when closure is genuinely justified.

For BA-01 specifically, do **not** start with Java code. Write the rules and predicted examples in `docs/domain-decisions.md`, then review that attempt before moving to BA-02.

## What the issue setup contains

- **Issues:** the learning tasks, acceptance criteria, and discussion history.
- **Milestones:** larger checkpoints such as Java foundation, domain completion, web interface, and submission.
- **Labels:** filtering and grouping metadata.
- **Project board:** the visual workflow for Backlog/Ready/In Progress/Review/Done when the signed-in Projects UI is available.
- **`docs/backlog.json`:** the original planning seed. It is not a substitute for live issue comments or status.

The issue synchronization script creates missing exact-title issues and is safe to rerun. It does not replace your progress or close work automatically.

## Tools intentionally not used yet

- **Spring Boot:** added after the plain Java domain is understandable and working.
- **PostgreSQL:** added when persistence is justified by the selected milestone; the installed server is currently stopped.
- **Docker:** optional packaging/deployment support; the Docker CLI is installed, but the engine is currently not running.
- **React:** an optional frontend direction. Decide before investing in a second UI; do not build both React and Thymeleaf just to collect technologies.
- **Authentication and deployment:** important portfolio upgrades, but they come after the core behaviour and evidence unless the schedule changes.

These are not forgotten. They are deliberately separated so a technology setup task does not become a substitute for a working, explainable banking app.

## First session checklist

- [ ] Open the repository through `pom.xml` in IntelliJ.
- [ ] Select Temurin JDK 21 as the project SDK and Maven JDK.
- [ ] Run `.\scripts\dev.ps1 -Task doctor` from PowerShell.
- [ ] Read GitHub issue BA-01.
- [ ] Create `docs/domain-decisions.md` with your own rules and predicted examples.
- [ ] Discuss the attempt before writing the first Java class.

The learner writes the banking features. The assistant can explain concepts, challenge decisions, review diffs, and help verify output after an attempt exists.
