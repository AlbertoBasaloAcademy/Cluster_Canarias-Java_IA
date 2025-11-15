---
description: 'Implement a feature following its specification.'
---

# Feature Code Generation

Write code to implement the specification {specId} solution.

## Context

Specification document:
- [{specId}.spec.md](/docs/specs/{specId}.spec.md)

Best practice instructions:

- [Clean Code Instructions](../instructions/bst_clean-code.instructions.md)

Technical instructions:
- [lng-java Instructions](../instructions/lng_java.instructions.md) for any specific language involved

## Workflow

- [ ] Create a new git branch named `dev/{specId}` and switch to it.

- [ ] Use #think and #todos tools to outline your approach before starting coding tasks. Focus only on coding tasks (no deployment, no testing, no documentation, etc. ONLY WORKING CODE).

- [ ] Execute the tasks in order. CHOOSE THE SIMPLEST APPROACH FOR EACH TASK. DO NOT INCLUDE TESTING NOR COMMENTS AT THIS STAGE.

## Validation

- [ ] **Smoke Test**: The code builds and runs successfully. Do not run tests or lint the code at this stage.
  - [ ] If it fails, fix and try again ONE more time.
  - [ ] If the build/run still fails, write a report at `/docs/specs/{specId}.code.fail.md`.

- [ ] Commit changes 

> End of Feature Code Generation prompt.