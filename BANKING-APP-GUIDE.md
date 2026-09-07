# BankingApp — build and learning guide

Planning baseline: September 7, 2026. This guide describes intended work; it is not evidence that the features exist. Completed work must be supported by a commit, a runnable result, and the learner's explanation. Repository and environment verification results belong in the setup handoff.

Read `docs/IMPLEMENTATION-BLUEPRINT.md` with this guide. It supplies the concrete proposed package structure, class responsibilities, default baseline rules, and issue-by-issue coding flow. The learner is not expected to invent the complete architecture alone.

## 1. What we are building

Build an educational banking simulator with clear Java business rules, a usable interface, and a history of small, explainable changes. The first milestone is a functioning TESDA Java Programming NCIII project. The longer goal is a portfolio project whose author can explain its design, demonstrate its behaviour, and change it during an interview.

The September 12, 2026 submission is screenshots, which may be collected into a PDF. No exact submission time or additional operation requirements were supplied. A terminal application is permitted by the stated requirement, but the intended result is a web interface. Persistence and deployment are desirable improvements; neither was stated as an instructor requirement.

A project can support a job application; it cannot guarantee a job or demonstrate skills that its author cannot reproduce. The learning measure is whether a new case can be solved and explained without receiving the implementation first.

## 2. Scope and success criteria

The proposed first useful version supports creating or selecting demo accounts, displaying balances, depositing, withdrawing, transferring between accounts, and viewing transaction history. It rejects invalid operations without silently changing balances. Savings and Premium account differences are included only after their rules are written down.

Use fictional names and money throughout. There are no real bank connections, payments, deposits, identity checks, or financial products.

| Priority | Outcome | Evidence |
|---|---|---|
| Required foundation | Java account operations and validation run correctly | Executed examples and automated tests |
| Intended submission | Simple web interface presents working operations | Local walkthrough and screenshots |
| Required handoff | Screenshot documentation and run instructions | Readable PDF and README |
| Planned improvement | Data survives application restarts | Restart and retrieval check |
| Planned improvement | Hosted demo | Verified URL and deployment notes |
| Optional direction | React frontend | Separate decision based on learning time and desired interface |

If time becomes tight, reduce scope deliberately: one account type before two, basic styling before visual polish, and a local demonstration before hosted access. A terminal smoke-test runner is a useful development aid and emergency submission option, but it does not silently replace the intended web interface.

## 3. Stack decisions and their reasons

Installed software does not establish either suitability or familiarity. These choices are based on the learning sequence and the target application. Exact dependency versions are recorded in the build files after verification; this guide does not claim a release is the latest.

| Tool or technology | Role | Timing and decision |
|---|---|---|
| Java 21 | Language and runtime baseline | Start here; use one project JDK consistently |
| IntelliJ IDEA | Java editor, debugger, and Maven integration | Recommended working IDE; learn only the needed controls first |
| Git and GitHub | Version history, review, and task tracking | From the first planning commit |
| Maven Wrapper | Reproducible build entry point | From setup; project wrapper avoids reliance on global Maven |
| JUnit | Repeatable checks of rules | Introduce after predicting and running initial examples |
| Spring Boot | Application configuration and web integration | After a small working Java core, not after months of console work |
| Thymeleaf | HTML rendered by the Java application | Provisional default for a single application |
| Bootstrap | Consistent basic form and page styling | When building the interface |
| PostgreSQL | Persistent relational data | Planned after account and transaction structures are understood |
| Docker | Reproducible process packaging | Optional, independently scheduled after a runnable application exists |
| React | Browser application consuming a backend API | Optional alternative; decide before investing in a large Thymeleaf interface |

Thymeleaf does not have to be a temporary stepping stone to React. It can remain the final interface. If React becomes the chosen learning goal, select it intentionally and budget for API endpoints, frontend state, validation display, and another build. Maintaining two full interfaces provides little benefit for this deadline.

H2 is an optional development database, not an obligation. Introducing H2 and later PostgreSQL creates migration and compatibility work. If persistence becomes feasible early, using PostgreSQL directly can be preferable to learning two database configurations. The initial in-memory implementation still has value for learning and tests.

## 4. Plain Java first: what that actually means

Start with the behaviour: what a deposit means, what a withdrawal may do, which inputs fail, and what remains unchanged after failure. Write and run those rules without requiring a browser, a database server, or Spring configuration.

Then let Spring call the same business operations. This is incremental integration, not a promise that no class will change. Persistence, transaction boundaries, and authentication will require deliberate changes and additional tests.

The target responsibilities are:

| Part | Responsibility | Example question |
|---|---|---|
| Domain | Account state and banking rules | May this withdrawal happen? |
| Application service | Coordinate a use case | Which accounts participate in a transfer? |
| Repository boundary | Find and save records | Where do these accounts come from? |
| Web layer | Read requests and return pages or responses | How is an invalid amount shown to the user? |
| Infrastructure | Database and runtime integration | How are records persisted and the app started? |

These are responsibilities, not a requirement to create dozens of interfaces or packages immediately. Start with a few clear classes. Introduce a small repository interface when multiple-account storage is needed and the learner can explain why it exists. Keep direct SQL, browser request handling, and passwords out of account arithmetic.

Avoid placing all rules inside a controller or `main`. That would make later interfaces harder to add because every caller would need to duplicate the same logic.

## 5. What must be anticipated now

**Money:** choose `BigDecimal`, a currency convention, allowed decimal places, and an explicit rounding policy where needed. For the initial simulator, use PHP only. Test decimal amounts rather than assuming whole numbers cover the behaviour.

**Identity:** a bank account and a login user are different concepts. An account has an identifier and balance. A user may eventually own one or more accounts. Do not make an account number double as a password or assume knowing an identifier grants permission.

**Transfers:** a transfer should not leave money debited from one account without the corresponding credit. In-memory coordination can be tested first. Database integration must add a transaction boundary and address concurrent updates; an in-memory test does not prove database atomicity or concurrency safety.

**History:** describe the fields a transaction record needs before coding: identifier, kind, amount, time, account references, and outcome as appropriate. Decide whether failed operations appear in history or only as errors. Do not invent a complete accounting ledger merely to display a history table.

**Storage:** a repository boundary can reduce changes to calling code, but replacing a collection with PostgreSQL still requires a schema, mappings, constraints, configuration, and integration tests. The goal is manageable change, not zero change.

**Authentication:** plan ownership and access rules early. Implement login when the application needs user isolation, and verify authorization before exposing personal accounts or real-user data. A public simulator without login must have intentionally disposable synthetic data and clearly limited shared actions. Adding security last by habit is not an architectural rule.

**Deployment:** choose a host only after verifying support, current terms, and the application's needs. Keep configuration outside source code from the start, document the start command, and avoid hard-coded machine paths. This reduces later deployment friction without requiring a hosting account on day one.

**Docker:** Docker does not depend on React or authentication. It can run PostgreSQL locally, package the Java application, or be omitted if the deployment platform handles the runtime. A container still needs correct application configuration and persistence. Docker is not evidence that the application is secure or correct.

## 6. Rules to decide in the first issue

The following are open design questions, not completed decisions:

1. What information is required to create an account? Is zero opening balance permitted?
2. What distinguishes Savings from Premium? A per-withdrawal limit is different from a daily cumulative limit.
3. Are negative balances ever allowed? The proposed first version disallows them.
4. What happens when an amount has more than two decimal places: reject or round?
5. Can an account transfer to itself? The proposed behaviour is rejection.
6. Can a missing or closed account receive money? Account closure can remain out of scope.
7. Is interest part of this version? If included, define rate, period, rounding, and repeat application explicitly; otherwise defer it.
8. What does transaction history show, and in which order?

Write chosen rules with concrete examples. Do not use inheritance merely to display two names in a dropdown. If account types have no meaningful different behaviour yet, start with one and add the distinction when its rule exists.

## 7. GitHub working method

Use one repository and one Project board. Milestones group outcomes; issues define the next piece of work. An issue should fit a focused session where possible and contain one coherent, observable result. A whole module is too large for the normal learning loop, while a ticket for each getter creates administration without learning value.

Use `Backlog`, `Ready`, `In Progress`, `Review`, and `Done`. Keep one implementation issue in progress. Dependencies determine order; labels help distinguish learning, feature, test, documentation, and future work.

Every implementation issue should contain a purpose, relevant concept, prerequisites, a reasoning checkpoint, acceptance criteria, boundary cases, and evidence required for closure. Issue numbers and actual Project links are generated during setup; the sequence below is a planning outline, not a claim that those exact numbers exist.

| Phase | Suggested small issues |
|---|---|
| Foundation | Write rules and examples; verify build workflow; design account state; protect balance; implement deposit; implement withdrawal |
| Domain expansion | Define account-type behaviour; implement justified overrides; introduce domain exceptions; store and find accounts; record transactions; implement transfer |
| Verification | Learn one JUnit test; cover rejected amounts; cover insufficient funds; cover transfer failure and conservation; review the domain explanation |
| Web | Integrate Spring; display dashboard; add account creation; add deposit and withdrawal forms; show errors; add transfer form; display history; style and check usability |
| Submission | Verify complete walkthrough; document limitations; write run instructions; capture screenshots; assemble and inspect PDF |
| Portfolio | Persist with PostgreSQL; verify database transactions; plan and implement access control when needed; package if useful; deploy and verify; consider React separately |

A task is done when its stated behaviour works, the relevant checks pass, the author can explain the change, and the change is committed. Use a short branch for a coherent change and a small pull request where review helps. Do not create ceremony that consumes the coding session.

## 8. The coaching agreement

The learner writes the banking implementation. The assistant can prepare tooling, documentation, issues, review criteria, and debugging support. It asks for reasoning before supplying a complete feature implementation.

For each issue:

1. Read the goal and explain the intended behaviour in ordinary words.
2. Predict at least one successful case and one failure case.
3. Make an implementation attempt.
4. Compile and run it; record what actually happened.
5. Review the smallest relevant failure or design weakness.
6. Add a meaningful test and explain what it proves.
7. Commit the completed result and update the issue.

New concepts are learned when needed. Encapsulation arises when preventing arbitrary balance changes. Exceptions arise when an operation cannot succeed. Collections arise when managing several accounts. Abstraction arises when a shared contract has a real purpose. JUnit makes previously manual examples repeatable. Spring connects that verified behaviour to web requests.

Reading an explanation is not sufficient to close a learning checkpoint. A useful follow-up is a changed case: a different amount, another failure path, or a new caller using the same operation.

## 9. Five-day working plan

This is a forecast, not a guarantee of five-day mastery. Use morning and afternoon focus blocks with breaks, an evening review, and a clear stopping point. Long availability does not mean every hour is productive. When work slips, cut optional scope instead of relying on an all-night recovery.

| Date | Intended outcome | Gate or adjustment |
|---|---|---|
| September 8 | Rules, build familiarity, account state, deposit and withdrawal | Each operation compiles and matches predicted cases |
| September 9 | Collections, history, transfers, meaningful tests; account variants if justified | Basic domain behaviour works before web complexity increases |
| September 10 | Spring integration and one complete browser operation | A small vertical slice proves the connection works |
| September 11 | Remaining forms, errors, history, simple styling | Full local walkthrough; drop optional features if needed |
| September 12 | Final checks, screenshots, PDF, run instructions | Submission artifacts ready before optional upgrades |

If the domain gate slips, narrow the feature set immediately and reassess the interface effort. Persistence and deployment can enter earlier only when the submission remains safe. Their timing is a tradeoff, not a blanket prohibition.

## 10. First morning: start here

Open this project in IntelliJ and check the setup handoff for the verified JDK, build command, repository link, and first ready issue. Run the supplied environment/build check. An empty project with no tests is only a tooling result, not a working banking application.

Start the rules issue by confirming or deliberately changing the proposed defaults in `docs/IMPLEMENTATION-BLUEPRINT.md`, then write answers to these three checkpoints:

1. An account contains PHP 1,000.00. Which parts of its state may outside code change directly, and why?
2. A withdrawal of PHP 1,200.00 is attempted. What should the caller receive, and what must remain unchanged?
3. A transfer credits its recipient unsuccessfully. What final balances would be acceptable, and which outcome would be a bug?

Then choose the smallest account operation issue marked Ready. The first coding goal is a small runnable behaviour whose output you predicted. Do not begin by installing every future technology or generating the complete application.

## 11. Example checks without implementation solutions

| Scenario | Expected rule to establish |
|---|---|
| Deposit PHP 250.50 into PHP 1,000.00 | New balance PHP 1,250.50; one successful operation recorded if history is active |
| Deposit zero or a negative amount | Reject; balance unchanged |
| Withdraw the exact available balance | Decide and document whether reaching zero is permitted |
| Withdraw more than available balance | Reject; balance unchanged |
| Transfer PHP 200.00 between two different accounts | Combined balance unchanged; individual balances change consistently |
| Transfer to an unknown account | Reject; sender balance unchanged |
| Transfer to the same account | Apply the explicit chosen rule |
| Submit a non-numeric amount through a form | Readable error; no operation performed |
| Restart an in-memory demo | Reset is expected and documented |
| Restart a persistent version | Previously saved data is retrieved correctly |

Later tests must address repeated form submissions, database rollback, concurrent withdrawals, and account authorization when those capabilities exist. They are not proven by the early happy-path demonstration.

## 12. Screenshots and explanation checklist

Capture the dashboard with synthetic accounts, a successful deposit, a successful withdrawal, a successful transfer, resulting history, and at least one rejected operation. Keep amounts and account identifiers consistent across the sequence. Show a test result separately if available. Use readable crops with a short caption stating what each screenshot proves.

The PDF should include the project name, brief purpose, stack actually used, screenshots and captions, implemented features, known limitations, and run instructions or repository link. Do not claim deployment, persistence, authentication, or passing tests unless verified. Inspect the final PDF visually before submission.

Practise explaining why balance is private, where validation belongs, what inheritance contributes, what happens during a failed transfer, why `BigDecimal` is used, which tests matter, what Spring adds, and what remains incomplete. Be able to point to the relevant code and change a small rule without regenerating the project.

## 13. Official reference starting points

Use these when the corresponding issue is active; reading every documentation site before coding is unnecessary.

- [Java 21 documentation](https://docs.oracle.com/en/java/javase/21/)
- [Maven Wrapper](https://maven.apache.org/wrapper/)
- [JUnit documentation](https://docs.junit.org/)
- [Spring guide: serving web content](https://spring.io/guides/gs/serving-web-content)
- [Spring guide: managing transactions](https://spring.io/guides/gs/managing-transactions)
- [Spring Security reference](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap documentation](https://getbootstrap.com/docs/)
- [PostgreSQL documentation](https://www.postgresql.org/docs/)
- [Docker getting started](https://docs.docker.com/get-started/)
- [React learning documentation](https://react.dev/learn)
- [GitHub Projects documentation](https://docs.github.com/en/issues/planning-and-tracking-with-projects)
