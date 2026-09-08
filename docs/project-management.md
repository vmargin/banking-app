# Project management

- Repository: https://github.com/vmargin/banking-app (private while work is in progress)
- Board: https://github.com/users/vmargin/projects/4
- Issues: https://github.com/vmargin/banking-app/issues
- Milestones: https://github.com/vmargin/banking-app/milestones

The 29-item assessment backlog in `docs/backlog.json` remains the traceability source. The active execution sequence is [VERTICAL-DELIVERY-PLAN.md](VERTICAL-DELIVERY-PLAN.md); GitHub issues retain detailed acceptance criteria.

## Issue rhythm

1. Select only the next issue whose dependencies are satisfied.
2. Move it to **In Progress** on the board.
3. Write your prediction or question in chat, then make your own small attempt.
4. Run the relevant command or walkthrough.
5. Commit the coherent change with `Refs #<issue-number>`.
6. Add one evidence comment to the issue:

```md
## Evidence

- Prediction:
- What I changed:
- Actual result:
- Verification: command, test, or visual check
- Remaining question:
```

7. Move it to Review. Close only when its acceptance criteria, evidence, and your explanation are real.

Never comment merely that an issue was started, and never close an issue just because source files exist.

## Current handoff

The model, schema, user JDBC persistence, and current regression checks are already committed. The next implementation checkpoint is **Feature 1: Login + dashboard**. BA-03 and BA-06 need issue-state/evidence reconciliation; do not restart completed code.

## GitHub synchronization

`scripts/rebaseline-assessment-issues.ps1` updates the existing BA issue titles, bodies, labels, and milestones from `docs/backlog.json`. It does not change issue states, post comments, close issues, or move board items. Run it with `-DryRun` before live synchronization.

The GitHub Projects access token does not currently provide Projects write scope, so board status remains a manual UI action. That is a tooling limitation, not an excuse to lose evidence in the issue itself.
