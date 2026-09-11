# BankingApp: 100-Question Mastery Game

Date: September 9, 2026  
Scope: the current Java Swing + JDBC + PostgreSQL BankingApp  
Rule: answer source-closed first. You may inspect the source only after committing an answer.

## Goal

Mastery does not mean memorising the whole project or reproducing the entire UI from a blank file.
Mastery means you can trace a requirement through the affected layers, predict success and failure,
write or modify a bounded piece, test it, and explain the tradeoff to another developer.

## How to play

- Answer 10 questions per round. Use your own words.
- For trace questions, answer as a numbered execution path.
- For prediction questions, state the UI state, object state, and database state before checking.
- For test questions, include input, expected result, and the invariant that must remain true.
- For modification questions, name the smallest affected layer and the regression test needed.
- Do not paste method bodies. Short pseudocode or plain-language edits are allowed.
- Mark every answer `closed-book` or `source-open`.
- Record a confidence score from 1 (guess) to 5 (certain).

### Scoring each question

| Score | Meaning |
|---:|---|
| 0 | No answer, contradiction, or invented project behavior. |
| 1 | Recognizes a concept but cannot connect it to this project. |
| 2 | Correctly explains or traces the project behavior. |
| 3 | Adds the failure path, invariant, and a safe test or change. |
| 4 | Does all of that and defends a tradeoff or identifies a better alternative. |

After each round, total the 40 possible points:

- 24–40: continue.
- 18–23: continue, then retry the three weakest questions.
- 0–17: pause new material and retry the weakest concepts with smaller prompts.
- Any safety question below 2 must be retried regardless of the total.

## Round 1 — Project map and working habits

1. [R] What is the user-visible submission flow of this project from mobile number and PIN to transaction history?
2. [R] Which three meaningful vertical features are we using as the learning sequence, and why are they better than studying every class alphabetically?
3. [T] Trace startup from `Main.main()` to the first visible Swing window. Name each dependency constructed on the way.
4. [R] What does `.\scripts\dev.ps1 -Task verify` check, and what does a green result not prove about the banking behavior?
5. [E] Explain the difference between a model, service, repository, and UI using one class from this project for each.
6. [D] Why does the project keep the plain Java domain before adding Spring or another framework?
7. [R] Which files define the current domain, service, repository, UI, and test layers?
8. [P] If PostgreSQL credentials are missing, what should the user see at login, and which layer knows that configuration is missing?
9. [E] Explain the project boundary between synthetic demo banking data and real production authentication.
10. [D] What evidence would convince a recruiter that this project is working rather than merely compiling?

## Round 2 — Domain model and Java fundamentals

11. [T] What state does a `BankAccount` object own, and which method is allowed to change its balance?
12. [P] What happens when a `BankAccount` is constructed with a negative opening balance? Name the exception and the unchanged state.
13. [R] Why is `BigDecimal` used instead of `double` for PHP amounts?
14. [P] Why does the code use `compareTo(BigDecimal.ZERO)` instead of `==` for money validation?
15. [T] Trace `BankAccount.withdraw(amount)` for an amount greater than the balance. What happens before and after the exception?
16. [E] What does the `User` constructor guarantee before a `User` object can exist?
17. [R] Why does `User.addTransaction()` reject a transaction whose user ID does not match?
18. [P] What breaks if `BankAccount` exposes its mutable balance field publicly?
19. [M] Add a rule that details may not exceed 100 characters. Which model or service owns it, and what test proves it?
20. [D] Which validation belongs in a model, which belongs in a service, and which belongs only in the UI?

## Round 3 — Login vertical feature

21. [T] Trace a successful login from pressing **Log in** through `LoginService` to `showDashboard()`.
22. [T] Which object remembers failed attempts, and when is that counter reset?
23. [P] Predict the result of three wrong PIN attempts: visible message, control state, and service state.
24. [F] Why does the current demo require closing and reopening the app after lockout? What would need to change for timed unlock?
25. [P] What should happen when the mobile number is blank but the PIN is filled in?
26. [F] Distinguish “user not found,” “incorrect PIN,” database failure, and missing database configuration in the current flow.
27. [T] Why is login work inside `SwingWorker.doInBackground()` and screen changes inside `done()`?
28. [M] Change the lockout threshold from three attempts to five. Which class, tests, and UI copy must change?
29. [X] Design tests for successful login, wrong PIN, blank input, third failure, and database failure.
30. [E] Explain the login flow to a nontechnical reviewer without using the words “repository,” “thread,” or “exception.”

## Round 4 — JDBC and database boundaries

31. [T] What is the responsibility of `DatabaseConnection.open()`, and why should the UI not construct JDBC connections directly?
32. [R] What is the purpose of a prepared statement in this project?
33. [F] What risk appears if user input is concatenated into an SQL string?
34. [T] In `JdbcCashInRepository`, identify the order of balance update, transaction insert, commit, and close.
35. [P] What must happen to the balance if the cash-in transaction insert fails after the balance update?
36. [T] In the transfer repository, identify the sender debit, recipient lookup, recipient credit, transaction inserts, commit, and rollback boundaries.
37. [P] What database state is acceptable if the recipient mobile number does not exist?
38. [F] Why is returning an updated balance different from returning a transaction receipt ID?
39. [X] Design a repository test proving that a failed transfer does not leave a debit without a credit.
40. [D] Why should transaction ownership stay in the repository boundary instead of being split across services?

## Round 5 — Swing UI and asynchronous work

41. [R] What is the Swing event-dispatch thread responsible for?
42. [F] What would a slow PostgreSQL query do to the old synchronous UI implementation?
43. [T] In a `SwingWorker`, which code runs in the background and which code is allowed to update Swing components?
44. [P] What should the user see while cash-in is being saved?
45. [F] Why are the submit controls disabled while a cash-in or transfer worker is running?
46. [T] What happens in the `done()` method when the worker returns an `ExecutionException`?
47. [M] Add a cancel action to history loading. What worker lifecycle and UI-state problem must you solve first?
48. [P] If the user clicks **Review transfer** twice quickly, should the service be called twice? Why or why not?
49. [X] Design a test using a blocking fake repository to prove the UI remains responsive during login.
50. [E] Explain why changing Swing controls from `doInBackground()` would be unsafe.

## Round 6 — Cash-in and transaction history

51. [T] Trace cash-in from typed amount and details to the repository, updated `User` balance, success banner, and history row.
52. [R] What cash-in values are rejected before database work begins?
53. [P] What happens when an amount has more than two decimal places?
54. [F] Why must a failed cash-in leave both the visible balance and database balance unchanged?
55. [T] Why does `TransactionHistoryService` depend on `TransactionRepository` instead of querying JDBC itself?
56. [P] How should an empty history screen differ from a history-load failure?
57. [R] Which transaction types are incoming and which are outgoing in the UI formatter?
58. [M] Add a date-range filter to history. Which layer should own filtering, and what performance tradeoff exists?
59. [X] Write three tests for history: ordered rows, empty result, and repository failure.
60. [D] Why should the sign on a transaction amount supplement, rather than replace, the transaction type text?

## Round 7 — Transfer and final proof

61. [T] Trace the current transfer flow from form input to the review screen. Where has no money moved yet?
62. [T] Trace the final confirmation from the review screen to `TransferService.transfer()`.
63. [P] What happens if the sender enters their own mobile number?
64. [P] What happens if the transfer amount is zero, negative, or has three decimal places?
65. [F] Why must recipient existence and sufficient balance be checked before claiming success?
66. [D] Why does the review screen show the recipient mobile number instead of inventing a recipient name?
67. [P] If the transfer repository throws after debiting the sender but before completing the operation, what should the database and UI show?
68. [M] Add a real transfer receipt ID. Which repository, service, model, UI, and tests would be affected?
69. [X] Design tests proving that Back and edit does not call the transfer service and Confirm transfer calls it once.
70. [E] Explain the transfer feature twice: once to a Java developer and once to a banking customer.

## Round 8 — Testing and debugging

71. [R] What is the difference between a unit test, a repository integration test, and a manual UI walkthrough in this project?
72. [T] Pick one existing test and explain what behavior it proves rather than merely what method it calls.
73. [P] If a test passes while no database-dependent tests run, what remains unknown?
74. [F] A valid cash-in test fails because the balance changed but no transaction row exists. Which layer would you inspect first and why?
75. [F] The UI shows “success” but the database contains no transfer. Which boundary is lying, and what evidence would you collect?
76. [X] Design a regression test for the invisible-primary-button bug without relying on pixel-perfect screenshots.
77. [X] Design a test for label-to-field accessibility association.
78. [M] A history query freezes the UI again. What exact code path and thread evidence would you inspect first?
79. [D] When is a fake repository better than a real PostgreSQL integration test?
80. [E] Give a debugging report with observed symptom, hypothesis, experiment, result, and next action for one BankingApp bug.

## Round 9 — Safe modification and developer practice

81. [M] Add a minimum transfer amount of PHP 10. Which service rule changes, and which existing behaviors must remain unchanged?
82. [M] Add a confirmation message for logout. Which UI method changes, and why should the service layer remain untouched?
83. [M] Add a `TRANSFER_FAILED` user-facing message without exposing SQL internals. Which exception boundary should translate it?
84. [D] The UI class is now large. Which responsibility would you extract first, and what seam would make it testable?
85. [M] Add a repository method for recent transactions only. What interface, JDBC query, service, UI, and tests change?
86. [X] For a new “daily transfer limit” requirement, list the success test, boundary test, failure test, and unchanged-state invariant.
87. [F] A reviewer says “the feature works” but cannot explain the rollback path. What evidence is missing?
88. [D] Compare a quick UI-only validation with service validation. Which one is authoritative for money rules, and why?
89. [E] Describe a clean Git commit for one bounded change in this project: title, files, test evidence, and explanation.
90. [P] If a future Spring controller calls the same services, which domain and repository rules should stay unchanged?

## Round 10 — Junior developer capstone

91. [E] Explain the complete BankingApp architecture in five minutes using one concrete cash-in example.
92. [D] Which three project decisions most protect correctness, and what failure would each prevent?
93. [X] Design an end-to-end acceptance test for login → cash-in → history.
94. [X] Design an end-to-end acceptance test for login → transfer review → confirm → updated history.
95. [F] List every place a failed operation could produce misleading success, and how the current UI prevents it.
96. [M] A recruiter asks for one feature you personally improved. Choose a bounded change you could implement unaided and define its acceptance criteria.
97. [D] Which part of this project is still beyond your independent coding ability, and what smaller exercise would bridge that gap?
98. [E] Defend why this project is educationally useful even though it is not production banking software.
99. [P] Given a new requirement—scheduled transfers—identify the affected UI, service, repository, database transaction, failure paths, tests, and user feedback before writing code.
100. [E] Final proof: explain how this project moves from user action to domain rule to database state and back to user feedback, then name one part you can now code without copying.

## Daily game schedule

- Block 1: Questions 1–20, project map and domain.
- Block 2: Questions 21–40, login and JDBC.
- Block 3: Questions 41–60, Swing, cash-in, and history.
- Block 4: Questions 61–80, transfer, tests, and debugging.
- Block 5: Questions 81–100, safe modification and capstone.

Take a short break between blocks and a longer break after Question 50. If two blocks in a row score
below 24/40, stop adding new material and retry corrections instead.

## Answer record template

```text
Question:
Answer:
Mode: closed-book / source-open
Confidence: 1–5
Score: 0–4
Evidence class/method/test:
Correction:
```

## Rules for the trainer

The trainer should ask one question at a time, require a prediction before source inspection, mark the
answer against the current repository, and ask a structurally similar retry after a correction. The
trainer should not reveal a complete method before the learner has attempted the answer. The target is
independent tracing and bounded implementation, not recognition of generated code.
