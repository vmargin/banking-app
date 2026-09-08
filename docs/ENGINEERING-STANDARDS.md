# BankingApp Engineering Standards

**Status:** active for learner-authored code. These standards support the JCash Swing/JDBC assessment application; they do not claim that banking features already exist.

## Enforced baseline

| Rule | Enforcement |
|---|---|
| Java 21 and Maven 3.9+ | Maven Enforcer |
| Conventional Maven layout | Repository structure |
| UTF-8, LF, four-space indentation | `.editorconfig` and `.gitattributes` |
| No wildcard imports, braces, conventional names, one top-level type | Checkstyle |
| Repeatable build | Maven Wrapper and GitHub Actions |

Use root package `com.vmargin.banking`. Package names are lowercase. Classes/enums use `PascalCase`; methods, fields, and parameters use `camelCase`; constants use `UPPER_SNAKE_CASE`. A public top-level type has the matching filename.

## Architecture and Java rules

1. Keep the assessment structure: `model`, `service`, `util`, plus `repository`, `ui`, and `Main` only where each has a clear job.
2. Keep mutable model state `private`. Constructors establish valid state; public methods preserve it.
3. Use `BigDecimal` for PHP money and `compareTo` for numeric comparison. Reject null, zero, negative, and more-than-two-decimal money before mutation.
4. A failed deposit, withdrawal, cash-in, or transfer must not leave partial state behind.
5. `service` coordinates use cases. `repository` executes SQL/maps results. `util` configures JDBC connections. `ui` renders/collects input. `Main` wires a small smoke scenario. Do not mix these responsibilities.
6. Use `PreparedStatement`, never SQL built by string-concatenating user inputs. Close JDBC resources with try-with-resources.
7. For transfer persistence, use one JDBC transaction. Commit only after both balances and needed log records succeed; otherwise roll back.
8. Expected business failures need readable, specific handling. Do not catch broad `Exception`, hide an error as `null`, or show a stack trace as normal UI feedback.
9. Demo PINs and local database values are not production authentication. Never commit real credentials, personal data, tokens, or database passwords.

## Testing and evidence

Write tests around public observable behaviour. Each use case needs a success case and a rejected case that proves state remained correct.

| Area | Minimum evidence when active |
|---|---|
| `BankAccount` | constructor, valid deposit/withdrawal, invalid amount, insufficient funds, unchanged state |
| Login | valid credentials, invalid attempt, third-attempt stop |
| JDBC repositories | save/read, missing record, user-scoped transaction query |
| Cash-in | persisted balance plus history; invalid amount changes neither |
| Transfer | receiver/amount/funds validation; atomic success; no partial update on failure |
| Swing UI | visible success/error feedback and a current balance/history refresh |

Run the smallest relevant command before each commit:

```powershell
.\scripts\dev.ps1 -Task style
.\scripts\dev.ps1 -Task compile
.\scripts\dev.ps1 -Task test
.\scripts\dev.ps1 -Task verify
```

`test` and `verify` are expected to fail until a real test exists. Record actual results in the related issue; a green build alone does not prove a banking flow.

## Post-submission upgrades

Spring Boot, role/admin screens, deployment, Docker, React, password hashing, and production-grade authentication are deliberately excluded from this assessment baseline. Add them only after the Swing/JDBC flow is safe and keep the tested domain/service boundaries reusable.

## Sources

- [Java Language Specification: names](https://docs.oracle.com/en/java/javase/21/docs/specs/jls/jls-6.html)
- [Maven standard directory layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout)
- [JDBC basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- [Maven Enforcer Plugin](https://maven.apache.org/enforcer/maven-enforcer-plugin/)
- [Checkstyle getting started](https://checkstyle.org/getting-started.html)
