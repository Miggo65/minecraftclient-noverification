@echo off
REM Build and run the Minecraft No Verification Launcher

echo ===================================
echo Minecraft No Verification Launcher
echo ===================================
echo.

cd /d "%~dp0launcher"

echo Building launcher...
call gradlew.bat clean build --no-daemon

if errorlevel 1 (
    echo.
    echo Build failed! Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.
echo Starting launcher...
echo.

java -jar build\libs\minecraft-noverification-launcher-1.0.0.jar

if errorlevel 1 (
    echo.
    echo Failed to start launcher. Make sure Java 17 or higher is installed.
    pause
    exit /b 1
)
