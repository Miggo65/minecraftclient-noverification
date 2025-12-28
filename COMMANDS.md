# Befehls-Referenz / Command Reference

Quick reference für alle wichtigen Befehle / Quick reference for all important commands.

---

## 🚀 Launcher starten / Start Launcher

### Windows
```cmd
# Mit Helper-Script
run-launcher.bat

# Direkt (wenn bereits gebaut)
cd launcher
java -jar build\libs\minecraft-noverification-launcher-1.0.0.jar

# Neu bauen und starten
cd launcher
gradlew.bat clean build
java -jar build\libs\minecraft-noverification-launcher-1.0.0.jar
```

### Linux / Mac
```bash
# Mit Helper-Script
./run-launcher.sh

# Direkt (wenn bereits gebaut)
cd launcher
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar

# Neu bauen und starten
cd launcher
./gradlew clean build
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

---

## 🔨 Launcher bauen / Build Launcher

### Vollständiger Build
```bash
cd launcher
./gradlew clean build      # Linux/Mac
gradlew.bat clean build    # Windows
```

### Nur kompilieren (schneller)
```bash
cd launcher
./gradlew build
```

### Build-Ausgabe prüfen
```bash
cd launcher
ls -lh build/libs/         # Linux/Mac
dir build\libs\            # Windows
```

---

## 💻 .exe erstellen / Create .exe

### Methode 1: jpackage (empfohlen)

**Windows**:
```cmd
cd launcher

REM Sicherstellen dass JAR existiert
gradlew.bat clean build

REM EXE erstellen
jpackage ^
  --input build\libs ^
  --name MinecraftLauncher ^
  --main-jar minecraft-noverification-launcher-1.0.0.jar ^
  --main-class com.miggo65.launcher.MinecraftLauncher ^
  --type exe ^
  --win-console ^
  --app-version 1.0.0 ^
  --vendor "Miggo65"
```

**Mit Icon**:
```cmd
jpackage ^
  --input build\libs ^
  --name MinecraftLauncher ^
  --main-jar minecraft-noverification-launcher-1.0.0.jar ^
  --main-class com.miggo65.launcher.MinecraftLauncher ^
  --type exe ^
  --win-console ^
  --app-version 1.0.0 ^
  --icon myicon.ico
```

**MSI Installer erstellen**:
```cmd
jpackage ^
  --input build\libs ^
  --name MinecraftLauncher ^
  --main-jar minecraft-noverification-launcher-1.0.0.jar ^
  --main-class com.miggo65.launcher.MinecraftLauncher ^
  --type msi ^
  --win-console ^
  --app-version 1.0.0
```

### Methode 2: Launch4j

1. Download: https://launch4j.sourceforge.net/
2. Launch4j öffnen
3. Konfigurieren:
   - Output file: `MinecraftLauncher.exe`
   - Jar: `launcher\build\libs\minecraft-noverification-launcher-1.0.0.jar`
   - Main class: `com.miggo65.launcher.MinecraftLauncher`
   - Min JRE version: `17`
4. Build-Button klicken

**Mit XML-Config**:
```cmd
launch4jc.exe launch4j-config.xml
```

---

## 🧪 Testen / Testing

### JAR testen
```bash
# Im launcher-Verzeichnis
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

### Mit mehr Speicher
```bash
java -Xmx4G -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

### Mit Debug-Ausgabe
```bash
java -Djava.util.logging.config.file=logging.properties -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

### Java-Version prüfen
```bash
java -version     # Sollte 17 oder höher sein
```

---

## 📝 Code bearbeiten / Edit Code

### Launcher-Code ändern
1. Dateien in `launcher/src/main/java/com/miggo65/launcher/` bearbeiten
2. Neu bauen: `cd launcher && ./gradlew clean build`
3. Testen: `java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar`

### Versionen hinzufügen
Öffne `launcher/src/main/java/com/miggo65/launcher/MinecraftLauncher.java` und bearbeite:
```java
private static final String[] SUPPORTED_VERSIONS = {
    "1.20.4",
    "1.20.2",
    "1.20.1",
    "1.19.4",
    "1.19.2",
    "1.18.2"  // Neue Version hier hinzufügen
};
```

### GUI-Layout ändern
Bearbeite Methoden in `MinecraftLauncher.java`:
- `createTopPanel()` - Obere Steuerelemente
- `createCenterPanel()` - Log-Ausgabe
- `createBottomPanel()` - Progress-Bar und Button

---

## 🧹 Aufräumen / Cleanup

### Build-Artefakte löschen
```bash
cd launcher
./gradlew clean     # Linux/Mac
gradlew.bat clean   # Windows
```

### Alles neu bauen
```bash
cd launcher
rm -rf .gradle build    # Linux/Mac
rmdir /s .gradle build  # Windows
./gradlew clean build
```

### Minecraft-Installation zurücksetzen
```bash
# Windows
rmdir /s %USERPROFILE%\.minecraft\versions

# Linux/Mac
rm -rf ~/.minecraft/versions
```

---

## 📦 Distribution / Verteilung

### JAR verteilen
```bash
# Die JAR-Datei ist standalone und kann direkt verteilt werden
launcher/build/libs/minecraft-noverification-launcher-1.0.0.jar
```

**Anforderungen für Endnutzer**:
- Java 17 oder höher
- Keine weiteren Dependencies

### EXE verteilen
```bash
# Nach dem Build mit jpackage:
MinecraftLauncher-1.0.0.exe

# ODER mit gebündelter JRE (größer, aber keine Java-Installation nötig):
MinecraftLauncher/  # Kompletter Ordner
```

### Mit Installer verteilen
```bash
# Nach MSI-Build:
MinecraftLauncher-1.0.0.msi

# Doppelklick installiert die Anwendung automatisch
```

---

## 🔍 Debugging

### Classpath prüfen
```bash
java -cp build/libs/minecraft-noverification-launcher-1.0.0.jar com.miggo65.launcher.MinecraftLauncher
```

### JAR-Inhalt inspizieren
```bash
jar -tf build/libs/minecraft-noverification-launcher-1.0.0.jar
```

### Manifest prüfen
```bash
unzip -p build/libs/minecraft-noverification-launcher-1.0.0.jar META-INF/MANIFEST.MF
```

### Dependencies prüfen
```bash
cd launcher
./gradlew dependencies
```

---

## 📚 Dokumentation öffnen / Open Documentation

### Haupt-Dokumentation
- `README.md` - Projekt-Übersicht
- `PROJEKT_OVERVIEW.md` - Komplette Übersicht
- `SCHNELLSTART.md` - Schnellstart

### Launcher-Dokumentation
- `launcher/README.md` - Vollständige Doku
- `launcher/BUILD_EXE.md` - .exe-Anleitung
- `BUILD_SUMMARY.md` - Build-Zusammenfassung

---

## ⚙️ Erweiterte Optionen / Advanced Options

### Custom JRE Image erstellen
```bash
jlink \
  --module-path $JAVA_HOME/jmods \
  --add-modules java.base,java.desktop,java.logging,java.net.http \
  --output custom-jre \
  --strip-debug \
  --compress 2
```

### Mit Custom JRE packen
```bash
jpackage \
  --input build/libs \
  --name MinecraftLauncher \
  --main-jar minecraft-noverification-launcher-1.0.0.jar \
  --main-class com.miggo65.launcher.MinecraftLauncher \
  --runtime-image custom-jre \
  --type exe
```

### GraalVM Native Image
```bash
native-image \
  -jar build/libs/minecraft-noverification-launcher-1.0.0.jar \
  --no-fallback \
  -H:Name=MinecraftLauncher
```

---

## 🆘 Häufige Probleme / Common Issues

### "Command not found: jpackage"
```bash
# JDK 17+ installieren, nicht nur JRE
# Download: https://adoptium.net/

# Prüfen:
jpackage --version
```

### "Unable to access jarfile"
```bash
# JAR existiert?
ls -l launcher/build/libs/

# Neu bauen:
cd launcher
./gradlew clean build
```

### "No main manifest attribute"
```bash
# Manifest prüfen:
unzip -p build/libs/*.jar META-INF/MANIFEST.MF

# Sollte enthalten: Main-Class: com.miggo65.launcher.MinecraftLauncher
```

### "UnsupportedClassVersionError"
```bash
# Java-Version zu alt
java -version  # Sollte 17+ sein

# Java 17+ installieren von: https://adoptium.net/
```

---

## 💡 Tipps / Tips

### Schneller Build
```bash
# Gradle Daemon verwenden (ohne --no-daemon)
cd launcher
./gradlew build  # Beim zweiten Mal viel schneller
```

### JAR umbenennen
```bash
# In launcher/build.gradle:
jar {
    archiveBaseName = 'my-launcher'
    archiveVersion = '2.0.0'
}
```

### Icon konvertieren
```bash
# Online: https://convertio.co/png-ico/
# Oder mit ImageMagick:
convert icon.png -define icon:auto-resize=256,128,64,48,32,16 icon.ico
```

---

## 🎯 Cheat Sheet

```bash
# Häufigste Befehle:

cd launcher                    # Ins Launcher-Verzeichnis
./gradlew clean build          # Neu bauen
java -jar build/libs/*.jar     # Starten
jpackage --input build/libs ... # EXE erstellen
```

---

## 📞 Hilfe / Help

Siehe auch:
- `launcher/README.md` für Details
- `launcher/BUILD_EXE.md` für .exe-Erstellung
- GitHub Issues für Probleme
