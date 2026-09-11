# BankingApp Submission Documentation Plan

## Purpose

Produce an editable BankingApp submission document and a rendered PDF that use the sample portfolio only as a presentation reference while treating the supplied Programming Java NC III candidate instruction as the authority. Every material claim must be tied to source code, a live application state, database evidence, or fresh verification output.

## Source boundary

- Authoritative requirement source: `portfolio-SPECIFIC-INSTRUCTION-FOR-THE-CANDIDATE-programming-JAVA-NC-III.pdf`.
- Presentation reference only: `StudentName_BankAppFinalOutput-StudentName_Redacted.pdf`.
- Current implementation source: this repository and its live PostgreSQL-backed Swing application.
- Important limitation: the supplied candidate-instruction file physically contains only pages 1-4 even though its footer says page 4 of 20. It ends at Transfer criterion G1. Criteria on pages 5-20 remain unknown and must not be invented.

## Planned deliverables

1. An editable DOCX draft with identity placeholders, concise technical explanations, numbered figures, and a provisional compliance matrix.
2. A PDF rendered from the DOCX and visually inspected page by page.
3. A screenshot evidence folder containing the selected source images used in the document.
4. A final verification report separating verified, unverified, blocked, and deferred items.

## Document structure

1. Cover page with project title, qualification, student/batch/instructor placeholders, and submission date placeholder.
2. Project description and scope.
3. Tools and technology stack: Java 21, Swing, Maven, JDBC, PostgreSQL, pgAdmin, JUnit, and Checkstyle, but only when confirmed locally.
4. System architecture and organized project structure.
5. User and Transaction data models, including encapsulation and required attributes.
6. Database design and JDBC persistence.
7. Login and three-attempt lockout evidence.
8. Balance display evidence.
9. Cash-in validation, balance update, transaction-log, and database evidence.
10. Transfer review, validation, atomic update, and database evidence.
11. Per-user transaction-history evidence.
12. Optional portfolio extensions: registration and administrator views, clearly distinguished from the visible mandatory criteria.
13. Test and build verification.
14. Performance monitoring and tuning evidence, with only measured and reproducible claims.
15. Rubric-to-evidence cross-reference and known limitations.

## Capture sequence

### Required application evidence

1. Initial login screen.
2. Incorrect PIN feedback with remaining attempts.
3. Third failed attempt and session lockout state.
4. Successful customer login and dashboard balance.
5. Cash-in input screen.
6. Rejected invalid cash-in amount.
7. Successful cash-in banner and updated balance.
8. Transfer input screen.
9. Transfer review screen before mutation.
10. Rejected invalid recipient, invalid amount, or insufficient-balance state.
11. Successful transfer banner and updated balance.
12. Transaction history showing cash-in, transfer sent, and transfer received records.

### Database evidence

1. pgAdmin object tree showing the `banking_app` database and `users` and `transactions` tables.
2. Safe users-table result showing IDs, synthetic mobile numbers, roles, and balances while excluding or obscuring PIN values.
3. Transactions-table result showing user IDs, types, amounts, details, and timestamps after the walkthrough.
4. Schema or query evidence showing primary key, foreign key, checks, and the transaction lookup index.
5. Before/after values matched to the cash-in and transfer screenshots.

### Code and verification evidence

1. Readable project/package tree proving model, service, util, repository, and main placement.
2. Focused excerpts from `User.java`, `Transaction.java`, `LoginService.java`, `JdbcCashInRepository.java`, `JdbcTransferRepository.java`, `DatabaseConnection.java`, and `schema.sql`.
3. Fresh `doctor`, `style`, `compile`, `test`, and `verify` output. Stale reports must not be cited.
4. Reproducible performance evidence such as JVM/process resource usage plus honest application/data-access tuning notes. No enterprise or container claim will be made unless directly demonstrated.

## Evidence rules

- Use synthetic data only and do not expose database passwords or real PINs.
- Prefer one purpose per screenshot and crop out unrelated windows or personal information.
- Caption every figure with what it proves, not merely what it shows.
- Use before/after pairs for state-changing operations.
- Do not use an IDE designer view as proof that the running feature works.
- Do not copy the sample's MySQL, XAMPP, phpMyAdmin, MLBB branding, cash-out, or change-PIN claims.
- Registration and administrator functions may appear as portfolio extensions because they exist in this app, but they must not be presented as requirements found in the visible rubric pages.
- Plaintext local demo PIN persistence must be identified as a limitation, not described as production-grade security.

## Execution phases and acceptance criteria

### Phase 1 Baseline verification

- Record repository state without overwriting existing user changes.
- Confirm PostgreSQL service availability and obtain a usable local database connection without exposing credentials.
- Run fresh build and test commands.
- Acceptance: every cited test/build count comes from this run; database-backed checks are clearly identified as executed or skipped.

### Phase 2 Controlled demo data

- Use only synthetic administrator, sender, and recipient accounts.
- Establish known starting balances and preserve enough transaction history to demonstrate all mandatory flows.
- Acceptance: credentials remain private; before/after balances reconcile arithmetically.

### Phase 3 Application and database capture

- Capture the required Swing states, then matching pgAdmin results.
- Acceptance: screenshots are readable, consistently sized, and collectively prove the visible criteria A1-G1 plus page-2 transfer/history requirements.

### Phase 4 Draft authoring

- Create a clean report-style DOCX with inline figures, captions, concise explanations, and a provisional compliance matrix.
- Render it to PDF.
- Acceptance: no invented identity details, no unsupported claims, no clipped/overlapping content, and all figures remain legible.

### Phase 5 Cross-reference verification

- Compare the draft against all 10 pages of the reference and all 4 supplied rubric pages.
- Mark each criterion `Verified`, `Partially verified`, `Unverified`, or `Not visible in supplied rubric`.
- Acceptance: the audit explicitly reports that pages 5-20 are missing and does not claim full rubric completion.

## Known risks

- Full compliance is impossible to certify without the missing rubric pages 5-20.
- PostgreSQL credentials are not present in the current process environment; pgAdmin saved access or user assistance may be required.
- Prior automated capture attempts were blocked by Windows integrity-level differences; the current Computer Use path must be tested before relying on it.
- The working tree already contains user changes, so no cleanup, reset, or unrelated commit is authorized.
- Current documentation reports conflicting historical test counts; only a fresh run will be used.

