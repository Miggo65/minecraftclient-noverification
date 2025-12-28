# Minecraft No Verification Client

A Fabric mod that creates a Minecraft development client without authentication verification. This allows connecting to offline servers (servers with `online-mode=false`) without a proper Minecraft account.

## Features

- ✅ Connect to offline servers without authentication
- ✅ Easy setup with Fabric mod loader
- ✅ Works with Minecraft 1.20.4
- ✅ No need for a real Minecraft account
- ✅ Perfect for local development and testing

## Requirements

- Java 17 or higher
- Minecraft 1.20.4
- Fabric Loader 0.15.3 or higher

## Quick Start

### Option 1: Using IntelliJ IDEA (Development)

1. Clone this repository
2. Open the project in IntelliJ IDEA
3. Wait for Gradle to sync
4. Run the `runClient` gradle task to start the development client

### Option 2: Building and Running

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

This mod uses Fabric's mixin system to modify how Minecraft handles authentication:

1. **Session Modification**: The mod intercepts the session profile creation and generates an offline UUID based on your username
2. **Offline Mode**: This allows the client to connect to servers running with `online-mode=false` in their server.properties
3. **No External Authentication**: No connection to Mojang/Microsoft authentication servers is required

## Configuration

### Changing Minecraft Version

To use a different Minecraft version, edit `gradle.properties`:

```properties
minecraft_version=1.20.4  # Change this
yarn_mappings=1.20.4+build.3  # Update mappings accordingly
fabric_version=0.91.2+1.20.4  # Update Fabric API version
```

Then run `./gradlew clean build` to rebuild with the new version.

## Project Structure

```
minecraftclient-noverification/
├── src/main/java/com/miggo65/noverification/
│   ├── NoVerificationClient.java     # Main mod initializer
│   └── mixin/
│       └── SessionMixin.java          # Authentication bypass mixin
├── src/main/resources/
│   ├── fabric.mod.json                # Mod metadata
│   └── noverification.mixins.json    # Mixin configuration
├── gradle/
├── build.gradle                       # Build configuration
├── gradle.properties                  # Project properties
└── settings.gradle                    # Gradle settings
```

## Development

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
