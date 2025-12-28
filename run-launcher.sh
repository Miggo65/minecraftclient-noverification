#!/bin/bash
# Build and run the Minecraft No Verification Launcher

echo "==================================="
echo "Minecraft No Verification Launcher"
echo "==================================="
echo ""

cd "$(dirname "$0")/launcher"

echo "Building launcher..."
./gradlew clean build --no-daemon

if [ $? -ne 0 ]; then
    echo ""
    echo "Build failed! Please check the error messages above."
    exit 1
fi

echo ""
echo "Build successful!"
echo ""
echo "Starting launcher..."
echo ""

java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar

if [ $? -ne 0 ]; then
    echo ""
    echo "Failed to start launcher. Make sure Java 17 or higher is installed."
    exit 1
fi
