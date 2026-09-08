# BankingApp Implementation Blueprint

**Purpose:** give you a concrete architecture and coding sequence before you write the banking features. This is the senior-reviewer part of the workflow: you get a clear target, the relevant concept, and a bounded issue; you write the implementation and explain it.

This document does **not** claim that the classes or features already exist. Do not create every file now. Create each class only when its issue becomes active.

## 1. The build target

For the September 12 submission, the target is a **single Java/Spring banking simulator**, not a real bank and not a collection of unrelated technologies.

The minimum impressive end-to-end flow is:

```text
Synthetic account -> dashboard -> deposit/withdraw/transfer -> validation result -> transaction history
```

The core must work without the browser first. Spring and the web pages call the same tested domain code later.

## 2. Proposed default rules for BA-01

You do not need to invent the application from zero. Treat these as the proposed baseline. In `docs/domain-decisions.md`, state whether you accept each one or change it and explain why.

| Rule | Proposed default | Why it keeps the first version manageable |
|---|---|---|
| Currency | PHP only, stored as `BigDecimal` | Avoids floating-point money errors and multi-currency scope |
| Precision | Exactly 0–2 decimal places; reject more than two, do not round silently | Clear behaviour for tests and forms |
| Account identity | Stable synthetic ID such as `ACC-001` plus holder display name | Simple to read in output and screenshots |
| Opening balance | Zero or a positive amount is allowed; negative is rejected | Lets a new account exist without inventing credit rules |
| Deposit | Positive amount only | Invalid input must not change balance |
| Withdrawal | Positive amount only; balance may reach zero; overdraft is rejected | One simple, visible balance rule |
| Savings | Maximum PHP 10,000.00 per withdrawal | Gives inheritance a real behaviour difference |
| Premium | No per-withdrawal cap, but never more than available balance | Different rule without adding loans or credit |
| Transfer | Positive amount, distinct existing sender/recipient; self-transfer rejected | Prevents ambiguous history and partial mutation |
| History | Record successful operations only in the first version, newest first | Keeps errors separate from an accounting/audit system |
| Identity baseline | Two seeded demo roles: `ADMIN` and `CUSTOMER`; customers own selected synthetic accounts, while an admin can view/manage all demo accounts | Demonstrates ownership and authorization without pretending that a demo selector is real login |
| Deferred | Interest, account closure, daily limits, real login, password handling, real payments | Protects the September submission scope |

Changing a default is fine. The standard is: can you explain the rule, predict one success and one failure case, and update the relevant tests?

## 3. Architecture map

```text
Main / later Spring controller
            |
            v
Application services (only when a use case spans objects)
            |
            v
Domain objects: User, Role, BankAccount, SavingsAccount, PremiumAccount, Transaction
            |
            v
Repository boundary: in-memory first, PostgreSQL later

Web/demo access layer (after the domain gate): seeded demo users, an explicit
"acting user" selector, and authorization checks before account operations.
```

The arrows are dependency direction. A `BankAccount` must not know about HTML, Spring, SQL, passwords, or Docker. This makes the same banking rules usable from `Main`, JUnit tests, and later web forms.

## 4. Package plan — create only when the issue calls for it

```text
src/main/java/com/vmargin/banking/
├── Main.java                              # BA-05: console smoke runner
├── domain/
│   ├── BankAccount.java                   # BA-02
│   ├── User.java                           # BA-15 to BA-18: demo ownership model
│   ├── Role.java                           # BA-15 to BA-18: ADMIN or CUSTOMER
│   ├── SavingsAccount.java                # BA-07, only after its rule exists
│   ├── PremiumAccount.java                # BA-07, only after its rule exists
│   ├── Transaction.java                   # BA-10
│   ├── TransactionType.java               # BA-10
│   └── exception/                         # BA-08
│       ├── InvalidAmountException.java
│       └── InsufficientFundsException.java
├── repository/
│   ├── AccountRepository.java             # BA-09
│   └── InMemoryAccountRepository.java     # BA-09
├── service/
│   ├── TransferService.java               # BA-11
│   └── AuthorizationService.java          # BA-15 to BA-18: ownership checks
└── web/                                   # BA-14 onward, Spring only
    ├── BankingApplication.java
    ├── DemoUserStore.java                  # BA-15: seeded identities, not authentication
    ├── DashboardController.java
    └── form/
        ├── AccountForm.java
        └── AmountForm.java

src/test/java/com/vmargin/banking/
├── domain/                                # BA-06 onward
└── service/                               # BA-11 onward
```

Later Spring persistence will add `infrastructure/persistence/`; it should not replace the domain packages.

## 5. Class responsibilities before code

| Class or layer | Owns | Must not own | First issue |
|---|---|---|---|
| `BankAccount` | ID, holder, private balance, deposit and withdrawal rules | Console input, HTML, SQL, password checks | BA-02 to BA-04 |
| `SavingsAccount` | Savings-specific withdrawal cap | Copying the whole base-account logic | BA-07 |
| `PremiumAccount` | Premium-specific rule difference | A fake feature with no changed behaviour | BA-07 |
| `Transaction` | A successful operation’s type, amount, time, and account reference | Calculating balances | BA-10 |
| `AccountRepository` | Find/save/list accounts | Banking validation rules | BA-09 |
| `TransferService` | Coordinate two accounts as one use case | Direct UI printing or HTTP details | BA-11 |
| `User` / `Role` | Represent a demo identity, role, and owned account IDs | Passwords, sessions, or security claims | BA-15 to BA-18 |
| `AuthorizationService` | Decide whether the acting demo user may access an account | Rendering pages or authenticating passwords | BA-15 to BA-18 |
| `DemoUserStore` | Seed one admin and customer identities for the showcase | Pretending a user selector is secure login | BA-15 |
| `Main` | Small predicted console demonstration | The actual banking rules | BA-05 |
| Spring controller | Read form input, call a service/domain object, display result | Reimplementing deposits or withdrawals | BA-14 onward |

## 6. The exact coding sequence

### Foundation: issues BA-01 to BA-06

| Issue | What you receive before coding | What you write | Concept being learned |
|---|---|---|---|
| BA-01 | The proposed rules in section 2 | Your acceptance/changes and six predicted examples in `docs/domain-decisions.md` | Requirements and edge cases |
| BA-02 | `BankAccount` responsibility and required fields | The class, constructor, private fields, and safe read-only access | Encapsulation and constructors |
| BA-03 | Deposit rule and invalid-input cases | `deposit` behaviour and a predicted console check | Validation and `BigDecimal` |
| BA-04 | Withdrawal rule and no-mutation-on-failure rule | `withdraw` behaviour and failure handling | Conditions and state protection |
| BA-05 | Expected demonstration scenarios | `Main` that calls existing behaviour only | `main`, objects, output prediction |
| BA-06 | Test cases derived from your manual examples | First JUnit tests | Automated verification |

### Domain completion: issues BA-07 to BA-12

| Issue | Architecture target | Concept being learned |
|---|---|---|
| BA-07 | Add `SavingsAccount` and `PremiumAccount` only because their rules differ | Inheritance and overriding |
| BA-08 | Replace vague failures with named domain exceptions where that improves callers | Exceptions and error meaning |
| BA-09 | Add `AccountRepository` plus `InMemoryAccountRepository` | Interfaces, collections, separation of storage |
| BA-10 | Add `Transaction` and `TransactionType` for successful activity | Composition, enums/records only if understood |
| BA-11 | Add `TransferService`; validate all conditions before a successful two-account mutation | Abstraction and coordination |
| BA-12 | Test the rules and explain the complete domain flow | JUnit, regression thinking, design explanation |

### Web vertical slice: issues BA-13 to BA-20

Default choice: **Spring Boot + Thymeleaf + Bootstrap**. This is one Java application and is the fastest credible route to a visual submission. React remains an optional portfolio upgrade, not a second UI to build before September 12.

The vertical slice includes a deliberately small access model:

- Seed one `ADMIN` demo user and at least two `CUSTOMER` demo users with synthetic account ownership.
- Show an explicit “acting demo user” selector or banner. It is a showcase control, not authentication.
- A customer dashboard lists and operates only on owned accounts.
- An admin dashboard may list and operate on all synthetic accounts.
- Customer-created accounts belong to that customer; only the admin may choose another demo owner.
- Put the ownership decision in an `AuthorizationService` (or equally small service), then call it before deposit, withdrawal, transfer, and history operations.
- Test both allowed and denied access. Do not add passwords, sessions, signup, or Spring Security to this slice.

| Issue range | Outcome |
|---|---|
| BA-13 | Confirm the three screens plus the acting-user/admin/customer states |
| BA-14 | Add Spring Boot around the existing domain; do not rewrite account arithmetic into controllers |
| BA-15 to BA-18 | Seed demo identities; show admin/customer dashboards; enforce ownership in account creation, deposit/withdraw, transfer, and history views |
| BA-19 | Basic accessible styling and clear error messages |
| BA-20 | Full browser walkthrough and written limitations |

### Submission and future work: BA-21 to BA-29

BA-21 and BA-22 are submission work. BA-23 to BA-29 (PostgreSQL, database atomicity, real authentication, Docker, deployment, React, interview demo) are intentionally future scope unless the core web application is already safe. The seeded demo role model is part of the web submission slice; it must not be described as secure authentication.

## 7. How the GitHub workflow fits this blueprint

Each issue already contains its outcome, checkpoint, prerequisites, and acceptance criteria. The blueprint supplies the missing answer to: **where does this issue fit in the application?**

For every active issue, use one concise comment when you have something real to record:

```md
## Progress

- Plan/prediction:
- Attempt:
- Actual result:
- Evidence: commit or command output
- Remaining question:
```

Do not post a comment just to say “started.” Move the board item to **In Progress** instead. Close an issue only when its acceptance criteria, evidence, and your explanation are all complete.

## 8. Daily flow when you return

1. Read this blueprint once, then open BA-01.
2. Copy/confirm the proposed rules in your own `docs/domain-decisions.md`; include the two roles, ownership examples, and the explicit “demo selector is not login” boundary. Change only rules you can explain. This is a short checkpoint, not a separate architecture project.
3. Open BA-02. I explain encapsulation, constructor state, and the exact class responsibility.
4. You write the first `BankAccount` attempt in IntelliJ.
5. Run `compile`; show the result or error.
6. We review the smallest next change, then move to BA-03.
7. When the web phase begins, implement the seeded role/access slice before optional styling or deployment.
8. After each coherent issue: commit, push, and add evidence to the issue.

The coding loop is therefore:

```text
Blueprint -> concept explanation -> your implementation -> compile/run/test -> review -> commit -> issue evidence
```

You are not expected to design the full system alone. You are expected to understand and write each small implementation step.

## 9. Scope cut line

If time is tight, protect these in order:

1. One working account type, deposit, withdrawal, transfer, validation, and history.
2. Tests for valid and rejected operations.
3. A basic Spring/Thymeleaf interface with synthetic data and the minimal admin/customer ownership demonstration.
4. Screenshots, README, and an honest limitation list.

Drop or defer account variants, PostgreSQL, real authentication, Docker, deployment, and React before sacrificing a working, explainable core. If time is tight, keep the role model to one admin, one customer, and two seeded accounts.
