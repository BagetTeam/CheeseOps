# Repository Guidelines
This is a javafx-based project with a Gradle build system using fxml and JavaFX.

## Project Structure & Module Organization
Sources live in `src/main/java/ca/mcgill/ecse/cheecsemanager`, with `application` bootstrapping JavaFX, `model` capturing domain logic, `controller` and `persistence` orchestrating workflows and XStream saves, and `fxml/*` providing UI controllers and reusable components. Behavioral specs reside in `src/test/resources` while supporting glue or unit tests belong in `src/test/java`; keep runtime outputs such as `app.data` and `build/` untouched so Gradle can regenerate them.
Styles live in `src/main/resources/ca/mcgill/ecse/cheecsemanager/style/`
Icons live in `./src/main/resources/ca/mcgill/ecse/cheecsemanager/view/icons/`

## Build, Test, and Development Commands
- `./gradlew clean build` — Compile, run all checks, and prepare distributable classes.
- `./gradlew run` — Start the JavaFX client (automatically copies non-Java assets first).
- `./gradlew test` / `./gradlew test -t` — Execute and optionally watch JUnit 5 + Cucumber suites.
- `clang-format --style=Google -i src/**/*.java` — Apply the shared formatting profile before committing.

note: no need to download gradle, simply use the command `gradle` in the terminal

## Coding Style & Naming Conventions
Use JDK 21 locally but limit language features to Java 17 for compatibility. Indent with four spaces, prefer PascalCase classes, camelCase members, and SCREAMING_SNAKE_CASE constants. Keep controller names aligned with their view or feature (e.g., `RobotPageController`) and lean on constructor injection plus short comments only where logic is non-obvious.
Use `prettier` to format fxml code and `clang-format` to format Java code.

## Testing Guidelines
Follow the numbered `.feature` naming already in `src/test/resources` to keep scenarios ordered and readable. Add matching step definitions or unit tests next to the feature’s functional area, asserting through persistence APIs when state changes are involved. Every new workflow or UI change should gain at least one Cucumber scenario and, when practical, a focused JUnit test; run `./gradlew test` until it passes consistently before opening a PR.

## Commit & Pull Request Guidelines
Continue using `name/feature-context` branches (`ewen/fxml-menu`, `ayush/persistence-fixes`). Write imperative, single-purpose commits (`fix: guard empty shelf search`) and avoid bundling unrelated files. Pull requests must describe the problem, the solution, test evidence (screenshots or logs for UI), and any data-reset steps such as regenerating `app.data`, then wait for reviewer sign-off and CI green lights.

## Configuration & Data Tips
Point `JAVA_HOME` to a Gradle-friendly Java 21 build and confirm via `java -version` before running tasks. Treat `app.data` as disposable sample data, exclude secrets, and keep helper tooling like `svg_converter.py` aligned with the resources under `src/main/resources` so the runtime finds the expected assets.
