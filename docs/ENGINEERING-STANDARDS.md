# BankingApp Engineering Standards

**Status:** required for learner-authored code from BA-02 onward. These are project standards, not evidence that the banking features already exist.

This project uses a small-team Java standard: conventional names and Maven layout, protected domain invariants, explicit validation, focused tests, thin Spring web code, and repeatable quality checks. It is intentionally not a claim that this simulator is an enterprise banking system.

## 1. What is enforced now

| Rule | How it is enforced | Why it belongs in this project |
|---|---|---|
| Java 21 and Maven 3.9+ | Maven Enforcer runs before the build | Everyone and CI use the intended language/runtime baseline |
| Maven directory layout | Repository structure | Source, tests, and resources stay in predictable locations |
| UTF-8, LF, four-space indentation | `.editorconfig` and `.gitattributes` | Formatting stays consistent across IntelliJ, Git, and CI |
| No wildcard imports, braces around control flow, conventional names, one top-level type per file | Checkstyle through Maven | Feedback is repeatable instead of depending on a reviewer noticing every mistake |
| Build quality gate | `style` locally; `verify` after real tests exist; GitHub Actions | A pushed change gets the same checks as a local change |

`style` may be run before tests exist. `verify` is intentionally meaningful only after BA-06 creates real tests, because this project rejects an empty green test suite.

## 2. Java naming and file rules

Use the project root package `com.vmargin.banking`; never use Java's default package.

| Thing | Rule | Banking examples |
|---|---|---|
| Package | Lowercase, reversed-domain root, then a clear concern | `com.vmargin.banking.domain` |
| Class / enum / record | `PascalCase`, descriptive noun or noun phrase | `BankAccount`, `TransactionType` |
| Interface | `PascalCase`, noun or capability adjective | `AccountRepository`, `Transferable` if it ever has a real use |
| Method | `camelCase`, verb or verb phrase | `deposit`, `withdraw`, `findById` |
| Field / local / parameter | `camelCase`, specific name | `openingBalance`, `recipientAccountId` |
| Constant | `UPPER_SNAKE_CASE` | `MAX_SAVINGS_WITHDRAWAL` |
| Boolean method | Start with `is`, `has`, or `can` where it reads naturally | `hasSufficientFunds()` |
| File | One public top-level type; filename equals that type | `BankAccount.java` |

Prefer a meaningful full word over an unexplained abbreviation. `amount`, `accountId`, and `transferReference` explain more than `amt`, `accNo`, and `ref`.

## 3. Domain and architecture rules

1. `BankAccount` owns account state and its own banking rules. Its mutable balance stays `private`; there is no public balance setter.
2. Constructors establish valid initial state. Public methods preserve the class invariant on both success and failure.
3. Validate before mutation. A rejected deposit, withdrawal, transfer, or authorization check must leave balances and history unchanged.
4. Use `BigDecimal` for PHP amounts. Compare money with `compareTo`, not `equals`, when numeric equality is intended. Keep scale/precision rules in one small validation path.
5. Keep a class cohesive. A domain class does not print to the terminal, return HTML, execute SQL, read form parameters, or know a password.
6. Introduce an interface only at a real boundary with more than one useful implementation or a testable dependency—such as `AccountRepository`. Do not create interfaces only to look enterprise.
7. `TransferService` coordinates work that crosses accounts. It validates all preconditions before a successful two-account mutation and preserves total money.
8. Domain exceptions express meaningful failed operations. Do not catch `Exception` broadly, swallow an error, or turn a failure into a silent `null`/`false` without an explained contract.
9. `User`, `Role`, and `AuthorizationService` are a seeded demo access model. The role selector is not login, password storage, or real security.

The active package direction is:

```text
web -> service -> domain <- repository implementation
```

The web layer may call services; it must not duplicate account arithmetic. Repository implementations may store/find objects; they must not own withdrawal or transfer validation.

## 4. Validation and error handling

- Validate on the server/domain path even if a future HTML form also validates in the browser.
- Prefer allowlists and explicit ranges: positive money, scale of 0–2, known account IDs, known demo roles, and bounded holder-name length.
- Give the caller a readable failure message at the UI boundary, but keep the original domain reason available for tests and logging.
- Expected business failures become specific domain exceptions or a deliberately designed result. Unexpected programming failures are not hidden as normal user errors.
- Never put credentials, tokens, database passwords, or real personal data in source, commits, screenshots, or issue comments.

## 5. Tests and review evidence

Each test names observable behaviour, uses Arrange–Act–Assert, and checks public behaviour rather than private fields or private methods. Every banking operation needs a success case and a rejection case that proves state did not change.

Minimum examples as they become relevant:

| Area | Evidence to add |
|---|---|
| Deposit / withdrawal | Valid decimal amount; null/zero/negative/excess scale; insufficient funds; unchanged balance after rejection |
| Transfer | Successful conservation of total money; self/missing destination rejection; no partial mutation |
| Repository | Unique ID behaviour and missing-account handling |
| Demo roles | Customer allowed on owned account, denied on another customer's account, admin allowed on seeded accounts |
| Spring forms | Server-side invalid input is rejected and displayed clearly; controller does not calculate balances |

Use one test method per behaviour. Do not test private helpers directly; test the public operation that uses them. Parameterized tests are optional after ordinary focused tests are clear.

## 6. Spring rules — apply only from BA-14

- Put `BankingApplication` in `com.vmargin.banking`, above the project packages, so component scanning is bounded.
- Use constructor injection for required collaborators; no field injection.
- Controllers translate HTTP/form input to service calls and views. Services coordinate use cases. Domain objects retain business rules.
- Use form/request objects at the web boundary instead of binding web input directly into mutable domain objects.
- Use POST-Redirect-GET for successful form mutations so browser refresh does not repeat a deposit, withdrawal, or transfer.
- Keep demo-user authorization in a service before an operation. Real password login, sessions/CSRF, Spring Security, and persistent users remain BA-25 work.

## 7. Daily quality gate

Before committing an implementation issue:

```powershell
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test       # after BA-06 has a real test
.\scripts\dev.ps1 -Task verify     # after BA-06; style + tests + package lifecycle
```

Record the actual command result and boundary cases in the issue. A successful build proves compilation/checks; it does not prove a feature unless the relevant tests and walkthrough do.

## 8. Deliberately not adopted yet

Do not add microservices, CQRS, event sourcing, a generic `BaseService`, a factory just to use a factory, Lombok, a second frontend, or Spring Security before the current issue needs it. Those are not an enterprise badge; they are additional designs that must earn their complexity.

## Sources

- [Java Language Specification, names and naming conventions](https://docs.oracle.com/en/java/javase/21/docs/specs/jls/jls-6.html)
- [Oracle Java Code Conventions](https://www.oracle.com/docs/tech/java/codeconventions.pdf) — older formatting guidance; naming is cross-checked against the current JLS above.
- [Maven standard directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout)
- [Maven Enforcer Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/)
- [Checkstyle Maven setup](https://checkstyle.org/getting-started.html)
- [Spring Boot: structuring code](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html)
- [Spring Framework: dependency injection](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html)
- [OWASP input validation guidance](https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html)
