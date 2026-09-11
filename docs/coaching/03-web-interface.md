# 03 - Web Interface

## BA-13 - Choose UI and sketch screens

- Do: choose Thymeleaf or explicitly budget React; sketch dashboard, operation, history, roles, and errors.
- Checkpoint: is a separate browser app worth the extra API/build work now?
- Proof: three screen sketches and explicit admin/customer states.

## BA-14 - Integrate Spring Boot

- Do: add only the required web layer around the tested domain.
- Checkpoint: what does Spring connect, and which rules remain ordinary Java?
- Proof: core tests still pass, app starts, version is pinned, controllers do not calculate balances.

## BA-15 - Dashboard and demo accounts

- Do: show seeded synthetic accounts filtered by demo role/ownership.
- Checkpoint: where does displayed state originate?
- Proof: readable admin/customer dashboards and explicit demo/reset notice.

## BA-16 - Account creation form

- Do: connect validated form input to account creation and owner assignment.
- Checkpoint: how do browser and server validation differ?
- Proof: valid account appears under owner; invalid input is rejected; stable ID generated.

## BA-17 - Deposit and withdrawal forms

- Do: expose existing operations after ownership checks.
- Checkpoint: what happens for invalid text, overdraw, or another customer's account?
- Proof: allowed success, readable rejection/denial, unchanged state on rejection, no refresh repeat.

## BA-18 - Transfers and history display

- Do: connect the transfer flow and ownership-filtered history.
- Checkpoint: how will the user know which accounts changed?
- Proof: both balances/history agree; self, missing, ownership, admin, success, and failure paths verified.

## BA-19 - Style and keyboard-friendly pages

- Do: apply deliberate styling, labels, focus states, readable currency, and access feedback.
- Checkpoint: can a user understand and correct an error without guessing?
- Proof: desktop and narrow viewport checks; CSS assets available for submission.
