# AC Drones (Fabric 1.21)
A drone mod for CC:Tweaked

## Prerequisites
- JDK 21 (Java 21)

## Build
- macOS/Linux: `./gradlew build`
- Windows: `gradlew.bat build`

The built JAR is created in `build/libs/` (example: `ac-drones-<version>.jar`).

## Run in Development
- Client: `./gradlew runClient`
- Server: `./gradlew runServer`

## Install into Minecraft
1. Install Fabric Loader for 1.21.
2. Put the built JAR from `build/libs/` into your Minecraft `mods` folder.
3. Ensure Fabric API and CC: Tweaked (Fabric, 1.21) are also in `mods`.

Common `mods` folders:
- Windows: `%APPDATA%/.minecraft/mods`
- macOS: `~/Library/Application Support/minecraft/mods`
- Linux: `~/.minecraft/mods`

## Notes
- The project targets Java 21; if using an IDE, set Gradle JVM to 21.
- You do not need to install Gradle; the included wrapper (`gradlew`) is used.
