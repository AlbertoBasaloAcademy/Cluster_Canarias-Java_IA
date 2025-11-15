---
description: 'Write a Feature Specification'
---

# Feature Specification

Write detailed specifications for a prompted feature in Markdown format composed of three main parts:

1. Problem Specification

2. Solution Design

3. Acceptance Criteria

Define **when** the feature is complete based on testable conditions.

## Context

- [PRD.md](/docs/PRD.md)

## Workflow

- Generate a unique specification ID (e.g., 001-slug_short_description).

- [ ] #think about the user stories that describe the feature from the user's perspective.

- [ ] #think about the data models, software components, user interfaces, and aspects (monitoring, security, error handling) for the feature.

- [ ] #think about the list of acceptance criteria for each user story using the EARS format: SHALL, WHEN, IF, THEN, WHILE, WHERE.

- CHOOSE THE SIMPLEST APPROACH FOR EACH ITEM.

- [ ] Read and follow the [#tpl_feature-spec](../instructions/tpl_feature-spec.instructions.md) instructions.

- [ ] Fill in the placeholders with relevant information.

- [ ] Write the feature specification in Markdown format at `/docs/specs/{specId}.spec.md`.

## Validation

- [ ] [{specId}.spec.md](/docs/specs/{specId}.spec.md) exists

- [ ] Commit changes 

> End of Feature Specification prompt.