# Minecraft No Verification Client

A Fabric mod and standalone launcher that creates a Minecraft client without authentication verification. This allows connecting to offline servers (servers with `online-mode=false`) without a proper Minecraft account.

## 🚀 NEW: Standalone Launcher with GUI

**The easiest way to use this project is now the standalone launcher!**

The launcher provides a simple GUI where you can:
- Select a Minecraft version from a dropdown menu
- Enter a username
- Click a button to automatically install and launch Minecraft without authentication

👉 **[Go to the Launcher Documentation](launcher/README.md)**

👉 **[How to build a .exe file](launcher/BUILD_EXE.md)**

## Features

- ✅ **Standalone Launcher**: Run Minecraft without mod installation
- ✅ **GUI Interface**: Easy-to-use graphical interface
- ✅ **Multiple Versions**: Support for Minecraft 1.20.4, 1.20.2, 1.20.1, 1.19.4, 1.19.2
- ✅ **Auto-Installation**: Automatically downloads and installs Minecraft versions
- ✅ **No Authentication**: Connect to offline servers without a Minecraft account
- ✅ **Fabric Mod**: Also available as a traditional Fabric mod

## Quick Start Options

### Option 1: Standalone Launcher (Recommended) ⭐

The standalone launcher is the easiest way to get started:

```bash
cd launcher
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

Or build it yourself:

```bash
cd launcher
./gradlew clean build
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

See [launcher/README.md](launcher/README.md) for detailed instructions and [launcher/BUILD_EXE.md](launcher/BUILD_EXE.md) for creating a Windows .exe file.

### Option 2: Traditional Fabric Mod

If you prefer the traditional mod approach:

1. Clone this repository:
   ```bash
   git clone https://github.com/Miggo65/minecraftclient-noverification.git
   cd minecraftclient-noverification
   ```

2. Build the mod:
   ```bash
   ./gradlew build
   ```

3. The compiled mod will be in `build/libs/noverification-client-1.0.0.jar`

4. Install the mod:
   - Install Fabric Loader for Minecraft 1.20.4 from https://fabricmc.net/use/
   - Place the mod jar file in your `.minecraft/mods` folder
   - Launch Minecraft with the Fabric profile

### Option 3: Running Development Client Directly

For development and testing, you can run the client directly without installing:

```bash
./gradlew runClient
```

This will start a Minecraft client with the mod pre-loaded.

## How It Works

### Standalone Launcher

The standalone launcher works by:
1. **GUI Interface**: Provides a Swing-based GUI for easy interaction
2. **Version Management**: Downloads Minecraft versions from Mojang's servers
3. **Fabric Installation**: Automatically installs Fabric Loader for the selected version
4. **Offline UUID Generation**: Creates an offline UUID based on the username
5. **Game Launch**: Starts Minecraft with offline authentication parameters

### Fabric Mod

The Fabric mod uses Fabric's mixin system to modify how Minecraft handles authentication:

1. **Session Modification**: The mod intercepts the session profile creation and generates an offline UUID based on your username
2. **Offline Mode**: This allows the client to connect to servers running with `online-mode=false` in their server.properties
3. **No External Authentication**: No connection to Mojang/Microsoft authentication servers is required

## Project Structure

```
minecraftclient-noverification/
├── launcher/                           # Standalone Launcher (NEW!)
│   ├── src/main/java/
│   │   └── com/miggo65/launcher/
│   │       ├── MinecraftLauncher.java       # Main GUI application
│   │       ├── MinecraftVersionManager.java # Version download/install
│   │       └── GameLauncher.java            # Game launching logic
│   ├── build.gradle                    # Launcher build configuration
│   ├── README.md                       # Launcher documentation
│   └── BUILD_EXE.md                    # Instructions for creating .exe
│
├── src/main/java/                      # Fabric Mod
│   └── com/miggo65/noverification/
│       ├── NoVerificationClient.java        # Mod initializer
│       └── mixin/
│           └── SessionMixin.java            # Authentication bypass
│
├── src/main/resources/
│   ├── fabric.mod.json                 # Mod metadata
│   └── noverification.mixins.json      # Mixin configuration
│
├── build.gradle                        # Main project build
├── gradle.properties                   # Project properties
└── README.md                           # This file
```

## Configuration

### Changing Minecraft Version (Fabric Mod)

To use a different Minecraft version with the Fabric mod, edit `gradle.properties`:

```properties
minecraft_version=1.20.4  # Change this
yarn_mappings=1.20.4+build.3  # Update mappings accordingly
fabric_version=0.91.2+1.20.4  # Update Fabric API version
```

Then run `./gradlew clean build` to rebuild with the new version.

### Adding Versions to Launcher

To add more Minecraft versions to the launcher, edit the `SUPPORTED_VERSIONS` array in `launcher/src/main/java/com/miggo65/launcher/MinecraftLauncher.java`:

```java
private static final String[] SUPPORTED_VERSIONS = {
    "1.20.4",
    "1.20.2",
    "1.20.1",
    "1.19.4",
    "1.19.2",
    "1.18.2"  // Add your version here
};
```

## Development

### Launcher Development

1. Navigate to the launcher directory:
   ```bash
   cd launcher
   ```

2. Make changes to the Java files in `src/main/java/`

3. Test the launcher:
   ```bash
   ./gradlew runLauncher
   ```

4. Build the launcher:
   ```bash
   ./gradlew clean build
   ```

### Fabric Mod Development

### IDE Setup

1. Import the project into IntelliJ IDEA
2. Gradle will automatically set up the development environment
3. Use the `runClient` gradle task to launch the development client
4. Use the `build` gradle task to build the mod jar

### Making Changes

1. Modify the code as needed
2. Run `./gradlew build` to compile
3. Test with `./gradlew runClient`

## Important Notes

⚠️ **Legal Notice**: This mod is for educational and development purposes only. It should only be used on servers you own or have permission to access without authentication.

⚠️ **Server Compatibility**: This only works with servers that have `online-mode=false` in their `server.properties` file. Most public servers use online mode and will not accept connections from this client.

⚠️ **No Piracy**: This is not a tool for piracy. It's designed for legitimate development and testing scenarios where authentication is not needed.

## Troubleshooting

### Client won't start
- Make sure you have Java 17 or higher installed
- Try running `./gradlew clean build` to rebuild from scratch

### Can't connect to server
- Verify the server has `online-mode=false` in server.properties
- Check that you're using a compatible Minecraft version

### Build errors
- Ensure you have an internet connection (Gradle needs to download dependencies)
- Delete the `.gradle` folder and run `./gradlew clean build` again

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
