# AGENTS.md

Audience: agentic coding assistants working in this repository.
Scope: how to build/test, and the code conventions to follow.

## Project summary
- Gradle-based Spring Boot 3.5.x application
- Java 17 toolchain
- Layers: presentation (controllers), application (usecases/DTOs), domain (entities/ports), infrastructure (JPA/JDBC/clients), global (config/exception/security)

## Commands
All commands should be run from the repo root (`/Users/zuny/Tave_Project/crezipsa`).

Build
- `./gradlew clean build`

Run tests
- `./gradlew test`

Run a single test class
- `./gradlew test --tests "tave.crezipsa.crezipsa.SomeTest"`

Run a single test method
- `./gradlew test --tests "tave.crezipsa.crezipsa.SomeTest.someMethod"`

Run a single test by pattern
- `./gradlew test --tests "*SomeTest*"`

Run application (local)
- `./gradlew bootRun`
- You can set a profile: `SPRING_PROFILES_ACTIVE=local ./gradlew bootRun`

Lint/format
- No explicit lint/format tasks are configured in `build.gradle`.
- Keep formatting consistent with existing files (tabs, brace style, import order).

## Repository rules from Cursor/Copilot
- No Cursor rules found in `.cursor/rules/` or `.cursorrules`.
- No Copilot rules found in `.github/copilot-instructions.md`.

## Code style guidelines
Follow existing conventions in the repo. If you touch a file, keep its style.

### Formatting
- Indentation uses tabs in existing Java sources; preserve it.
- Braces are on the same line as the declaration (K&R style).
- Keep line lengths reasonable; prefer line breaks over long chained calls.

### Imports
- Use explicit imports; avoid `*` imports.
- Import grouping follows typical order: `java` → `jakarta`/`javax` → `org` → `lombok` → project packages.
- Remove unused imports.

### Naming
- Classes/interfaces: `PascalCase`.
- Methods/fields: `camelCase`.
- Constants: `UPPER_SNAKE_CASE`.
- DTOs: `*Request`, `*Response` suffixes.
- Use case interfaces and implementations follow current naming (e.g., `CommunityUseCase`, `CommunityUseCaseImpl`, `CommentUsecase`). Preserve existing names even if inconsistent.

### Layering and packages
- Controllers live under `presentation/**/controller` and should be thin.
- Business logic belongs in `application/**/usecase`.
- Domain entities/ports live in `domain/**`.
- Infra adapters/repositories live in `infrastructure/**`.
- Cross-cutting concerns live in `global/**`.

### Controllers
- Return `GlobalResponseDto<T>` for all API responses.
- Use `@Valid` for request bodies and `@Validated` on controllers where needed.
- Use `@AuthenticationPrincipal User user` for auth context.
- Keep controllers free of DB access; delegate to usecases.

### DTOs and mapping
- Request/response DTOs are in `application/**/dto`.
- Mappers exist in `application/**/mapper` or `infrastructure/**/mapper`.
- Keep mapping logic out of controllers.

### Error handling
- Use `CommonException` with `ErrorCode` for domain/business errors.
- Don’t throw raw `RuntimeException` for expected errors.
- Global handling is centralized in `global/exception/handler/GlobalExceptionHandler`.

### Transactions
- Use `@Transactional` on usecase classes or methods.
- Prefer `@Transactional(readOnly = true)` for read-only queries.
- Avoid holding transactions open across external HTTP calls where possible.

### JPA/JDBC
- JPA repositories are in `infrastructure/**/repository` and implement domain ports.
- For complex queries, use `@Query` or dedicated adapters.
- Prefer pagination (`Pageable`) for list endpoints.
- Be mindful of N+1; if you add list endpoints, consider batch or join queries.

### HTTP clients
- External HTTP calls use `WebClient`.
- Keep client code in `infrastructure/**/client` or dedicated adapters.
- Handle null/empty responses and map them to `CommonException` as needed.

### Security
- JWT logic is in `global/security`.
- Use existing auth patterns: tokens, `@AuthenticationPrincipal`, and error codes.

### Validation
- Prefer validation annotations on request DTOs (`jakarta.validation`).
- For manual validation, throw `CommonException` with a meaningful `ErrorCode`.

### Logging
- Use `@Slf4j` when logging is needed.
- Log only actionable info; avoid logging secrets/tokens.

### Tests
- Tests are JUnit 5 (`spring-boot-starter-test`).
- Place tests under `src/test/java` mirroring package structure.
- Name tests `*Test` and methods by behavior.

## Practical tips for agents
- Keep changes scoped to the layer you’re working in.
- When adding endpoints, update controller, usecase, DTOs, and repository ports coherently.
- Prefer small, readable methods over long chains.
- Preserve existing API response structure and error codes.
- If you add new error codes, update `ErrorCode` and keep codes/status consistent.

## Known configuration
- Active profile is controlled by `SPRING_PROFILES_ACTIVE`.
- DB settings are in `src/main/resources/application.yml` with profiles for `local`, `dev`, and `prod`.
