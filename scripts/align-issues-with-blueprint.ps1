param(
    [string]$Repository = 'vmargin/banking-app',
    [switch]$DryRun
)

$ErrorActionPreference = 'Stop'
$blueprintUrl = "https://github.com/$Repository/blob/main/docs/IMPLEMENTATION-BLUEPRINT.md"
$gh = (Get-Command gh -ErrorAction SilentlyContinue).Source
if (-not $gh) { $gh = Join-Path $env:ProgramFiles 'GitHub CLI\gh.exe' }
if (-not (Test-Path -LiteralPath $gh)) { throw 'GitHub CLI is required.' }

# This supplements each original issue body. It never replaces the outcome,
# prerequisites, acceptance criteria, labels, milestone, assignee, or state.
$guidance = @{
    'BA-01' = @{ target = 'Confirm the proposed simulator rules, demo identities, account ownership, and six predicted examples; no Java feature is written in this issue.'; concept = 'Requirements, edge cases, ownership boundaries, and state that must remain unchanged on failure.'; learner = 'Write your own decision record in docs/domain-decisions.md, accepting or changing each proposed default and marking the acting-user selector as demo-only, not login.' }
    'BA-02' = @{ target = 'Create only the first domain class: BankAccount with valid construction and private state.'; concept = 'Encapsulation, constructors, fields, and safe read-only access.'; learner = 'Implement BankAccount yourself; do not create the later service, repository, or web packages yet.' }
    'BA-03' = @{ target = 'Add the deposit behaviour to the existing BankAccount class.'; concept = 'BigDecimal validation and no mutation after invalid input.'; learner = 'Implement the smallest deposit method and predict one successful and one rejected case.' }
    'BA-04' = @{ target = 'Add withdrawal behaviour to BankAccount without allowing an overdraft.'; concept = 'Conditional validation, order of checks, and protected state.'; learner = 'Implement withdrawal behaviour; keep console/UI code outside BankAccount.' }
    'BA-05' = @{ target = 'Create Main as a small smoke runner for the already implemented account behaviour.'; concept = 'Object creation, method calls, and predicted console output.'; learner = 'Write a demonstration only; do not move banking rules into Main.' }
    'BA-06' = @{ target = 'Translate manual deposit/withdraw examples into the first focused JUnit tests.'; concept = 'Arrange, act, assert and a test that proves a rejected operation preserved balance.'; learner = 'Write tests derived from your own BA-01 examples.' }
    'BA-07' = @{ target = 'Add SavingsAccount and PremiumAccount only for the documented rule difference.'; concept = 'Inheritance and overriding for behaviour, not labels.'; learner = 'Implement only the justified override; do not duplicate the base account implementation.' }
    'BA-08' = @{ target = 'Make important banking failures explicit with domain exceptions where that improves callers.'; concept = 'Exceptions communicate why a valid request could not complete.'; learner = 'Refactor the existing result/failure handling deliberately and update callers/tests.' }
    'BA-09' = @{ target = 'Introduce AccountRepository and InMemoryAccountRepository for multiple account lookup/storage.'; concept = 'Interfaces, collections, and separating storage from banking rules.'; learner = 'Keep validation in the domain/service layers, not in repository lookup code.' }
    'BA-10' = @{ target = 'Represent successful activity with Transaction and TransactionType.'; concept = 'Composition and a small data model for history.'; learner = 'Record only the fields needed by the documented first-version history.' }
    'BA-11' = @{ target = 'Add TransferService to coordinate two accounts through the repository boundary.'; concept = 'A service coordinates a use case that crosses domain objects.'; learner = 'Validate conditions before a successful two-account mutation; preserve total money.' }
    'BA-12' = @{ target = 'Review and verify the full plain-Java domain before web work begins.'; concept = 'Regression tests and explaining design boundaries.'; learner = 'Demonstrate the domain, run focused tests, and explain why UI/SQL do not live in BankAccount.' }
    'BA-13' = @{ target = 'Confirm a single Spring/Thymeleaf interface with dashboard, operation form, history/transfer views, and admin/customer states.'; concept = 'UI scope and access states are product decisions, not a reason to build two frontends.'; learner = 'Sketch the three screens, the acting-demo-user banner/selector, and what each role may display or submit.' }
    'BA-14' = @{ target = 'Bootstrap Spring around the tested domain, preserving the domain package as the source of banking rules.'; concept = 'Framework integration around existing behaviour.'; learner = 'Add only the Spring entry point/configuration needed for the first vertical slice.' }
    'BA-15' = @{ target = 'Render admin and customer dashboards using synthetic account data from the existing domain/application path.'; concept = 'Controller/view separation, read-only display, and role-filtered ownership.'; learner = 'Seed one admin and customer identities, keep template logic for display, and never call the selector authentication.' }
    'BA-16' = @{ target = 'Create accounts through a simple form and assign each new demo account to an explicit owner; customers create for themselves, while admin may choose a demo owner.'; concept = 'Request validation versus domain validation, plus ownership association.'; learner = 'Reuse the domain creation rules and route ownership through a small service rather than duplicating rules in the controller.' }
    'BA-17' = @{ target = 'Connect deposit and withdrawal forms to tested operations after checking the acting demo user may access the account.'; concept = 'A web form is another caller of domain behaviour; authorization is separate from authentication.'; learner = 'Show a readable success/error result and prove a customer cannot change another customer account.' }
    'BA-18' = @{ target = 'Expose transfer and history views using TransferService and Transaction data, filtered by ownership except for admin.'; concept = 'Application-service coordination and authorization across a UI.'; learner = 'Verify one allowed customer flow, one denied customer flow, and one admin flow in the browser.' }
    'BA-19' = @{ target = 'Apply basic Bootstrap styling and keyboard-friendly form behaviour, including visible role/access feedback.'; concept = 'Usability, clear validation, and honest demo boundaries.'; learner = 'Improve clarity without redesigning the app, introducing React, or implying the selector is secure login.' }
    'BA-20' = @{ target = 'Run a full local browser walkthrough for admin and customer flows and record actual limitations.'; concept = 'End-to-end verification is different from a single green build; access denial must be demonstrated.'; learner = 'Verify intended September flows with synthetic data and document that real authentication is not implemented.' }
    'BA-21' = @{ target = 'Write a truthful README and practice explaining code you wrote.'; concept = 'Portfolio evidence distinguishes implemented from planned work.'; learner = 'Describe actual stack, commands, limitations, and decisions without inflated claims.' }
    'BA-22' = @{ target = 'Capture the working flow and assemble the screenshot/PDF submission.'; concept = 'A screenshot should prove one specific behaviour.'; learner = 'Use consistent synthetic data and captions that match the actual implementation.' }
    'BA-23' = @{ target = 'Replace in-memory storage with PostgreSQL only after the core submission is safe.'; concept = 'Persistence mapping and schema constraints.'; learner = 'Keep the repository boundary; do not wire SQL into controllers or BankAccount.' }
    'BA-24' = @{ target = 'Verify database transfer atomicity and concurrent-update behaviour.'; concept = 'Database transactions solve risks that in-memory tests cannot prove.'; learner = 'Write integration checks only after PostgreSQL persistence exists.' }
    'BA-25' = @{ target = 'Replace the seeded demo selector with real authentication and authorization around persisted account ownership.'; concept = 'Login identity is separate from a bank account; the demo role model is not security.'; learner = 'Keep the submitted demo boundary intact, then add password/session/CSRF protections only as a future upgrade; do not use account IDs as passwords or expose records owned by another user.' }
    'BA-26' = @{ target = 'Package with Docker only if it improves the selected local/deployment workflow.'; concept = 'Containers package processes; they do not prove correctness or security.'; learner = 'Containerize a known-working application, not an unfinished one.' }
    'BA-27' = @{ target = 'Deploy a bounded synthetic-data demo only after a local walkthrough is reliable.'; concept = 'Deployment has environment, configuration, and access boundaries.'; learner = 'Verify the exact deployed URL and document limits honestly.' }
    'BA-28' = @{ target = 'Evaluate React as a deliberate alternative, not a second deadline frontend.'; concept = 'A separate frontend requires API contracts, state, and validation work.'; learner = 'Choose React only if the current Spring/Thymeleaf scope is already safe.' }
    'BA-29' = @{ target = 'Prepare an interview demonstration from commits, screenshots, tests, and code you can explain.'; concept = 'Portfolio claims require evidence and personal understanding.'; learner = 'Practice changing or explaining a small rule without generated implementation.' }
}

$previousToken = $env:GH_TOKEN
try {
    if (-not $env:GH_TOKEN) {
        $credentialLines = "protocol=https`nhost=github.com`n`n" | git credential fill
        $credentialToken = $credentialLines | Where-Object { $_ -like 'password=*' } | Select-Object -First 1
        if (-not $credentialToken) { throw 'Sign in to GitHub first using Git Credential Manager or GitHub CLI.' }
        $env:GH_TOKEN = $credentialToken.Substring(9)
        $credentialLines = $null
        $credentialToken = $null
    }

    $issues = @(& $gh api "repos/$Repository/issues?state=all&per_page=100" | ConvertFrom-Json | Where-Object { -not $_.pull_request })
    foreach ($issue in $issues) {
        if ($issue.title -notmatch '^\[(BA-\d{2})\]') { continue }
        $id = $Matches[1]
        if (-not $guidance.ContainsKey($id)) { throw "No guidance defined for $id." }
        $item = $guidance[$id]
        $issueNumber = [int]$id.Substring(3)
        $currentBody = if ($null -eq $issue.body) { '' } else { $issue.body }
        $marker = "`n## Architecture and Coding Flow"
        $markerIndex = $currentBody.IndexOf($marker, [System.StringComparison]::Ordinal)
        $baseBody = if ($markerIndex -ge 0) { $currentBody.Substring(0, $markerIndex).TrimEnd() } else { $currentBody.TrimEnd() }
        $submissionCutLine = if ($issueNumber -ge 23) {
@"

## Submission Cut Line

This is future scope. Do not start it before the September 12 screenshot/PDF submission is complete and the core local web flow is verified.
"@
        } else { '' }
$newBody = @"
$baseBody

## Architecture and Coding Flow

**Read first:** [$blueprintUrl]($blueprintUrl)

**Architecture target:** $($item.target)

**Concept checkpoint:** $($item.concept)

**Your implementation:** $($item.learner)

The coach explains the concept and reviews evidence; the learner writes the banking implementation.
$submissionCutLine
"@

        $normalizedCurrentBody = $currentBody.TrimEnd()
        $normalizedNewBody = $newBody.TrimEnd()
        if ($normalizedNewBody -eq $normalizedCurrentBody) {
            Write-Output "$id #$($issue.number) unchanged"
            continue
        }
        if ($DryRun) {
            Write-Output "$id #$($issue.number) would update"
            continue
        }
        @{ body = $normalizedNewBody } | ConvertTo-Json -Depth 4 | & $gh api -X PATCH "repos/$Repository/issues/$($issue.number)" --input - | Out-Null
        if ($LASTEXITCODE -ne 0) { throw "GitHub update failed for $id (#$($issue.number))." }
        Write-Output "$id #$($issue.number) updated"
    }
} finally {
    $env:GH_TOKEN = $previousToken
}
