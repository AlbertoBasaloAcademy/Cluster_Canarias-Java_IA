---
description: 'Clean a feature implementation'
---

# Feature Clean 

Refactor latest changes for the feature spec: ${input:featureId} in order to make the code more maintainable and easier to understand.

## Context

Best practice instructions:
- [Clean Code Instructions](../instructions/bst_clean-code.instructions.md) for best practices in writing clean code
- If OOP, then ensure to follow the [Object Calisthenics standard](../instructions/std_object-calisthenics.instructions.md)


## Workflow

- [ ] Use the #runCommands tool run any linter or formatter to clean the code.

- [ ] Use the #runCommands/getTerminalOutput tool to check the output of the linter or formatter and fix any issues.

- [ ] Read the code changed in this spec branch carefully.

- [ ] Look for any code smells or anti-patterns in the code and fix them.

- [ ] Look for duplicated or easy to abstract code and refactor it.

- [ ] Look for any code that can be simplified or made more readable and refactor it

- [ ] Add documentation comments to any public or exported functions, classes, or modules in the feature codebase.

## Validation

- [ ] **Run the Test**: Run the tests to ensure they still pass.
  - [ ] If it fails, fix and try again ONE more time.
  - [ ] If the test still fails, write a report at `/docs/specs/{specId}.test.fail.md`.

- [ ] Commit changes 

> End of Feature Clean prompt.