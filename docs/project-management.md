# Project management

- Repository: https://github.com/vmargin/banking-app (private during development)
- Board: https://github.com/users/vmargin/projects/4
- First task: https://github.com/vmargin/banking-app/issues/1
- Milestones: https://github.com/vmargin/banking-app/milestones

There are 29 learning tasks. `docs/backlog.json` is the original planning seed; GitHub issue discussions and statuses become the live execution record. Do not overwrite progress from the seed. `scripts/sync-issues.ps1` only creates missing exact-title issues and is safe to rerun without duplicating existing titles.

## Daily rhythm

Select the next issue whose prerequisites are satisfied. Keep one implementation issue In Progress. Write your prediction, attempt the code, run it, review it, and commit. Move to Review when evidence is ready and Done only after the acceptance criteria and explanation pass.

Milestones 01-04 lead to screenshot submission. Milestone 05 is a future backlog, not a promise to deliver every technology by September 12. Cut scope explicitly if the core slips.

## Branch and commit example

```powershell
git switch -c task/ba-01-domain-rules
git add docs/domain-decisions.md
git commit -m "docs: define banking rules and examples"
git push -u origin task/ba-01-domain-rules
```

Run these after you write the file; it is intentionally not filled with an assistant's solution. Use a pull request to review a coherent change; reference its issue. Do not commit credentials, generated target output or local IDE settings.

Use the browser for Projects management with the existing signed-in session. The installed CLI's default authentication requires additional scopes; the issue synchronization script uses the already authorized Git credential only in process memory and never saves or prints it.
