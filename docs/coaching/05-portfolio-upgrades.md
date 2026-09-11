# 05 - Portfolio Upgrades

These are stretch issues. Do not start them before the domain and submission gates justify the scope.

## BA-23 - PostgreSQL persistence

- Do: add schema, repository implementation, configuration, and restart-survival tests.
- Checkpoint: what changes when objects become rows?
- Proof: data survives restart; constraints/mappings tested; secrets external.

## BA-24 - Database atomicity and concurrency

- Do: verify rollback and concurrent-withdrawal behavior.
- Checkpoint: what happens when two withdrawals race?
- Proof: integration tests show no negative balance or partial transfer.

## BA-25 - Authentication and authorization

- Do: add real identity and ownership checks only after the required foundations exist.
- Checkpoint: how are authentication and authorization different?
- Proof: hashed passwords, session/CSRF design, denied cross-owner access, no credentials committed.

## BA-26 - Docker packaging

- Do: package only if the selected host needs it.
- Checkpoint: what persists outside the container?
- Proof: image builds/runs; configuration and health check documented; no secrets in layers.

## BA-27 - Bounded synthetic-data deployment

- Do: choose and document the demo isolation/reset model before deploying.
- Checkpoint: is the demo shared, resettable, or authenticated?
- Proof: URL and complete flows verified; persistence and cost claims match reality.

## BA-28 - React evaluation

- Do: evaluate learning value and replacement/API cost; do not rewrite automatically.
- Checkpoint: which existing UI work would be replaced?
- Proof: written decision with time cost and required follow-up issues.

## BA-29 - Interview demonstration

- Do: prepare a short demo from actual code, tests, and limitations.
- Checkpoint: can you change a rule and repair its tests live?
- Proof: every resume/demo claim maps to repository evidence.
