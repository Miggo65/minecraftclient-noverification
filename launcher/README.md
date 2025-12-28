# Minecraft No Verification Launcher

Ein eigenständiger Minecraft Launcher mit grafischer Benutzeroberfläche, der es ermöglicht, Minecraft ohne Authentifizierung zu starten. Perfekt für lokale Entwicklungs- und Testumgebungen.

## Features

- ✅ Grafische Benutzeroberfläche zur Auswahl der Minecraft-Version
- ✅ Automatischer Download und Installation von Minecraft-Versionen
- ✅ Automatische Installation des Fabric-Loaders
- ✅ Keine Authentifizierung erforderlich (Offline-Modus)
- ✅ Unterstützt mehrere Minecraft-Versionen (1.20.4, 1.20.2, 1.20.1, 1.19.4, 1.19.2)
- ✅ Einfach zu bedienen - nur JAR-Datei ausführen

## Systemanforderungen

- Java 17 oder höher
- Mindestens 2 GB freier RAM
- Internetverbindung für den ersten Download von Minecraft-Versionen

## Schnellstart

### Schritt 1: Launcher ausführen

```bash
cd launcher
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

Oder unter Windows: Doppelklick auf die JAR-Datei (wenn Java korrekt installiert ist).

### Schritt 2: Im Launcher

1. Wählen Sie eine Minecraft-Version aus dem Dropdown-Menü
2. Geben Sie einen Benutzernamen ein
3. Klicken Sie auf "Install & Launch Minecraft"
4. Der Launcher wird:
   - Die ausgewählte Minecraft-Version herunterladen (falls nicht vorhanden)
   - Fabric Loader installieren
   - Die No-Verification-Mod installieren
   - Minecraft im Offline-Modus starten

## Projekt selbst bauen

### Launcher bauen

```bash
cd launcher
./gradlew clean build
```

Die fertige JAR-Datei befindet sich dann in:
```
launcher/build/libs/minecraft-noverification-launcher-1.0.0.jar
```

### Fabric Mod bauen (optional)

Das Projekt enthält auch eine Fabric-Mod-Version, die separat gebaut werden kann:

```bash
# Im Hauptverzeichnis
./gradlew clean build
```

Die Mod-JAR befindet sich dann in:
```
build/libs/noverification-client-1.0.0.jar
```

## .exe-Datei erstellen

Um eine Windows-EXE-Datei zu erstellen, können Sie eines der folgenden Tools verwenden:

### Option 1: jpackage (empfohlen, seit Java 14 integriert)

```bash
# Im launcher-Verzeichnis
cd launcher

# Stelle sicher, dass die JAR gebaut wurde
./gradlew clean build

# Erstelle eine EXE mit jpackage (Windows)
jpackage --input build/libs \
  --name "MinecraftNoVerificationLauncher" \
  --main-jar minecraft-noverification-launcher-1.0.0.jar \
  --main-class com.miggo65.launcher.MinecraftLauncher \
  --type exe \
  --win-console \
  --app-version 1.0.0 \
  --vendor "Miggo65" \
  --description "Minecraft Launcher without authentication"
```

Dies erstellt eine `MinecraftNoVerificationLauncher-1.0.0.exe` im aktuellen Verzeichnis.

### Option 2: Launch4j

1. Download Launch4j von: https://launch4j.sourceforge.net/
2. Öffnen Sie Launch4j
3. Konfigurieren Sie:
   - **Output file**: `MinecraftLauncher.exe`
   - **Jar**: Pfad zu `minecraft-noverification-launcher-1.0.0.jar`
   - **Main class**: `com.miggo65.launcher.MinecraftLauncher`
   - **JRE minimum version**: `17`
   - **Max heap size**: `2048` (MB)
4. Klicken Sie auf das Zahnrad-Symbol zum Erstellen

### Option 3: javapackager / jlink

Für eine vollständige Anwendung mit gebündelter JRE:

```bash
# Erstelle ein benutzerdefiniertes JRE-Image
jlink --module-path $JAVA_HOME/jmods \
  --add-modules java.base,java.desktop,java.logging,java.net.http \
  --output custom-jre \
  --strip-debug \
  --compress 2 \
  --no-header-files \
  --no-man-pages

# Packe die Anwendung mit dem custom JRE
jpackage --input build/libs \
  --name "MinecraftNoVerificationLauncher" \
  --main-jar minecraft-noverification-launcher-1.0.0.jar \
  --main-class com.miggo65.launcher.MinecraftLauncher \
  --runtime-image custom-jre \
  --type exe
```

## Projektstruktur

```
.
├── launcher/                           # Standalone Launcher-Anwendung
│   ├── src/main/java/
│   │   └── com/miggo65/launcher/
│   │       ├── MinecraftLauncher.java  # Hauptklasse mit GUI
│   │       ├── MinecraftVersionManager.java  # Version-Management
│   │       └── GameLauncher.java       # Spiel-Start-Logik
│   ├── build.gradle                    # Launcher Build-Konfiguration
│   └── build/libs/                     # Gebaute JAR-Dateien
│
├── src/main/java/                      # Fabric Mod (optional)
│   └── com/miggo65/noverification/
│       ├── NoVerificationClient.java
│       └── mixin/
│           └── SessionMixin.java
│
├── build.gradle                        # Hauptprojekt Build-Konfiguration
└── README.md                           # Diese Datei
```

## Wie es funktioniert

1. **GUI**: Der Launcher bietet eine Swing-basierte GUI zur Auswahl der Minecraft-Version
2. **Version-Download**: Minecraft-Versionen werden von Mojangs offiziellen Servern heruntergeladen
3. **Fabric-Installation**: Der Fabric-Loader wird automatisch für die ausgewählte Version installiert
4. **Offline-Authentifizierung**: Es wird eine Offline-UUID generiert, die keine Verbindung zu Microsoft/Mojang erfordert
5. **Game-Start**: Das Spiel wird mit den generierten Offline-Credentials gestartet

## Verwendung

### Launcher-GUI

![Launcher Screenshot](docs/launcher-screenshot.png)

Die GUI bietet folgende Funktionen:
- **Minecraft Version**: Dropdown-Menü zur Auswahl der Version
- **Username**: Eingabefeld für den Spielernamen (wird für Offline-UUID verwendet)
- **Log Output**: Zeigt den Fortschritt und eventuelle Fehler an
- **Progress Bar**: Zeigt den aktuellen Installations-/Start-Fortschritt
- **Launch Button**: Startet den Installations- und Launch-Prozess

## Wichtige Hinweise

⚠️ **Rechtlicher Hinweis**: Dieses Tool ist nur für Bildungs- und Entwicklungszwecke gedacht. Es sollte nur auf Servern verwendet werden, die Sie besitzen oder für die Sie die Erlaubnis haben, ohne Authentifizierung darauf zuzugreifen.

⚠️ **Server-Kompatibilität**: Dies funktioniert nur mit Servern, die `online-mode=false` in ihrer `server.properties`-Datei haben. Die meisten öffentlichen Server verwenden den Online-Modus und akzeptieren keine Verbindungen von diesem Client.

⚠️ **Keine Piraterie**: Dies ist kein Tool für Piraterie. Es ist für legitime Entwicklungs- und Testszenarien konzipiert, in denen keine Authentifizierung erforderlich ist.

## Fehlerbehebung

### Launcher startet nicht
- Stellen Sie sicher, dass Java 17 oder höher installiert ist
- Überprüfen Sie, ob genügend Arbeitsspeicher verfügbar ist
- Führen Sie den Launcher über die Kommandozeile aus, um Fehler zu sehen

### Download schlägt fehl
- Überprüfen Sie Ihre Internetverbindung
- Stellen Sie sicher, dass keine Firewall die Verbindung blockiert
- Versuchen Sie es erneut - manchmal gibt es temporäre Serverprobleme

### Spiel startet nicht
- Überprüfen Sie die Log-Ausgabe im Launcher
- Stellen Sie sicher, dass genügend Festplattenspeicher verfügbar ist (mindestens 2 GB)
- Versuchen Sie, das `.minecraft`-Verzeichnis zu löschen und erneut zu installieren

## Entwicklung

### Launcher lokal testen

```bash
cd launcher
./gradlew runLauncher
```

### Änderungen vornehmen

1. Bearbeiten Sie die Java-Dateien in `launcher/src/main/java/`
2. Führen Sie `./gradlew clean build` aus
3. Testen Sie mit `./gradlew runLauncher` oder `java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar`

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert - siehe die LICENSE-Datei für Details.

## Mitwirken

Beiträge sind willkommen! Bitte fühlen Sie sich frei, einen Pull Request einzureichen.

## Unterstützung

Bei Problemen oder Fragen öffnen Sie bitte ein Issue auf GitHub.
