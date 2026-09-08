# Active delivery plan

The remaining work is delivered as three vertical features. The BA numbers remain traceability labels, not separate stop points after every class.

## Feature 1 — Login and dashboard

Build mobile/PIN login, the three-attempt limit, authenticated user state, and a simple Swing dashboard showing the persisted balance.

Related items: BA-10, BA-11, BA-16, BA-17, BA-18.

## Feature 2 — Cash-in and history

Build transaction persistence, cash-in with balance-and-log consistency, and a per-user history view.

Related items: BA-09, BA-12, BA-14, BA-19, BA-21.

## Feature 3 — Transfer and final proof

Build validated transfers with one database transaction, readable errors, regression walkthroughs, performance notes, and screenshot/PDF evidence.

Related items: BA-13, BA-20, BA-22 through BA-27.

## Ownership rule

For each feature, the learner explains the flow, writes the first service/UI attempt, and records the predicted result. The assistant may handle repetitive plumbing, diagnostics, verification, and documentation cleanup; the learner owns business rules and the final explanation.

Spring, Docker, deployment, React, and admin roles remain post-assessment upgrades.

## Current status override

The older `docs/backlog.json` priority fields are historical and may lag the repository. Treat the committed code and verification output as the source of truth until the GitHub issue states and evidence comments are reconciled.
