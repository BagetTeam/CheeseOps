# 🧀 ECSE223 CheECSEManager Project: Team 2

## Getting started

1. Clone the repository.
2. Make a branch with a your name and what you are working on (e.g. git branch "mingli/the-feature-you-are-working-on").
3. Make changes, commit, and push.
4. When you are done, make a Pull Request into main. You can request reviewers to review your code if you want to confirm your changes.
5. If there is any conflicts, ask the person who worked on the file before you to make sure you are not overridding important stuff, then resolve it.
6. Merge the PR

## Important Notes

Make sure to have Java 21. The TA uses an old verison of Gradle (8.6) which only Java <=21 supports. If you have a version higher than 21, your LSP will not work and you won't get autocomplete in VSCode/Neovim/Zed/etc.. I haven't tried on Eclipse since I wasn't able to install it, but the Neovim plugin jdtls-nvim that I use is based on the jdtls from Eclipse so chances are it won't work.

## Building the app

Install gradle and run `gradle build` or `./gradlew build` or `gradlew.bat build` on Windows

## Running tests

Install gradle and run `gradle test -t` or `./gradlew test -t` or `gradlew.bat test -t` on Windows

## Formatting your code

Install `clang-format` and run `clang-format --style=Google -i <filename>` or `clang-format --style=Google -i src/**/*.java` for all files in the `src` directory.

## Project Overview

CheecseManager is an innovative application that allows businesses to effectively manage the flow of local cheese products from the production line all the way to the clients!

For more information about the CheECSEManager application, please consult the [wiki](../../wiki). there is nothing

## Team Members

| Name                    | GitHub username |
|-------------------------|-----------------|
| Ming Li Liu             | mingli202       |
| Olivier Mao             | OlivierMao19    |
| Eun-Jun Chang           | 1313eunjun      |
| Ayush Patel             | 4yushP4tel      |
| Benjamin Curis-Friedman | Benjamincf0     |
| Ewen Gueguen            | BagetTeam       |
| David Tang              | Tangdavid1      |
