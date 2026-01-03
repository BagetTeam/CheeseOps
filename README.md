# 🧀 CheeseOps

CheeseOps is an innovative application that allows businesses to effectively manage the flow of local cheese products from the production line all the way to the clients!

For more information about the CheeseOps application, please consult the [wiki](../../wiki). (this is fake there isn't anything)


## Building the app
Make sure to have Java 21. If you have a version higher than 21, your LSP will not work and you won't get autocomplete in VSCode/Neovim/Zed/etc.. I haven't tried on Eclipse since I wasn't able to install it, but the Neovim plugin jdtls-nvim that I use is based on the jdtls from Eclipse so chances are it won't work.

Install gradle and run `gradle build` or `./gradlew build` or `gradlew.bat build` on Windows

## Running tests

Install gradle and run `gradle test -t` or `./gradlew test -t` or `gradlew.bat test -t` on Windows

## Formatting your code

Install `clang-format` and run `clang-format --style=Google -i <filename>` or `clang-format --style=Google -i src/**/*.java` for all files in the `src` directory.

## Demo

