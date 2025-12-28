# Anleitung zum Erstellen einer Windows .exe-Datei

Diese Anleitung zeigt verschiedene Methoden, um aus der JAR-Datei des Launchers eine ausführbare Windows-.exe-Datei zu erstellen.

## Voraussetzungen

- Java Development Kit (JDK) 17 oder höher installiert
- Die gebaute JAR-Datei: `minecraft-noverification-launcher-1.0.0.jar`
- Windows-Betriebssystem (für die EXE-Erstellung)

## Methode 1: jpackage (Empfohlen)

jpackage ist seit Java 14 im JDK enthalten und ist die offizielle Methode zur Erstellung nativer Installer.

### Schritt-für-Schritt:

1. **JAR-Datei bauen**:
   ```cmd
   cd launcher
   gradlew.bat clean build
   ```

2. **EXE erstellen mit jpackage**:
   ```cmd
   jpackage ^
     --input build\libs ^
     --name MinecraftNoVerificationLauncher ^
     --main-jar minecraft-noverification-launcher-1.0.0.jar ^
     --main-class com.miggo65.launcher.MinecraftLauncher ^
     --type exe ^
     --win-console ^
     --app-version 1.0.0 ^
     --vendor "Miggo65" ^
     --description "Minecraft Launcher ohne Authentifizierung" ^
     --icon src\main\resources\icon.ico
   ```

   Hinweis: Der Parameter `--win-console` ist optional, zeigt aber ein Konsolenfenster für Debug-Ausgaben.

3. **Ergebnis**:
   - Eine EXE-Datei wird im aktuellen Verzeichnis erstellt
   - Optional: Ein Windows-Installer (MSI) mit `--type msi`

### Mit gebündelter Java Runtime:

Um eine EXE zu erstellen, die Java mitbringt (größer, aber benutzerfreundlicher):

```cmd
jpackage ^
  --input build\libs ^
  --name MinecraftNoVerificationLauncher ^
  --main-jar minecraft-noverification-launcher-1.0.0.jar ^
  --main-class com.miggo65.launcher.MinecraftLauncher ^
  --type exe ^
  --win-console ^
  --app-version 1.0.0 ^
  --java-options "-Xmx2G"
```

## Methode 2: Launch4j

Launch4j ist ein beliebtes Tool zum Wrappen von JAR-Dateien in EXE-Dateien.

### Schritt-für-Schritt:

1. **Launch4j herunterladen**:
   - Website: https://launch4j.sourceforge.net/
   - Laden Sie die aktuelle Version herunter und installieren Sie sie

2. **Launch4j konfigurieren**:
   
   Öffnen Sie Launch4j und konfigurieren Sie:

   **Basic Tab**:
   - Output file: `C:\Pfad\zu\MinecraftLauncher.exe`
   - Jar: `C:\Pfad\zu\launcher\build\libs\minecraft-noverification-launcher-1.0.0.jar`
   - Don't wrap jar: Nicht aktivieren (für kleinere EXE)
   - Icon: Optional - Pfad zu einer .ico-Datei

   **Classpath Tab**:
   - Main class: `com.miggo65.launcher.MinecraftLauncher`

   **JRE Tab**:
   - Min JRE version: `17`
   - Max heap size: `2048` (MB)
   - Initial heap size: `512` (MB)

   **Version Info Tab** (Optional):
   - File version: `1.0.0.0`
   - Product version: `1.0.0.0`
   - Company name: `Miggo65`
   - File description: `Minecraft No Verification Launcher`
   - Product name: `Minecraft Launcher`

3. **EXE erstellen**:
   - Klicken Sie auf das Zahnrad-Symbol (Build wrapper)
   - Die EXE wird am angegebenen Speicherort erstellt

### Mit XML-Konfiguration:

Sie können auch eine XML-Konfigurationsdatei erstellen (`launch4j-config.xml`):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<launch4jConfig>
  <dontWrapJar>false</dontWrapJar>
  <headerType>gui</headerType>
  <jar>build\libs\minecraft-noverification-launcher-1.0.0.jar</jar>
  <outfile>MinecraftLauncher.exe</outfile>
  <errTitle></errTitle>
  <cmdLine></cmdLine>
  <chdir>.</chdir>
  <priority>normal</priority>
  <downloadUrl>https://adoptium.net/</downloadUrl>
  <supportUrl></supportUrl>
  <stayAlive>false</stayAlive>
  <restartOnCrash>false</restartOnCrash>
  <manifest></manifest>
  <icon></icon>
  <classPath>
    <mainClass>com.miggo65.launcher.MinecraftLauncher</mainClass>
  </classPath>
  <jre>
    <path>%JAVA_HOME%</path>
    <requiresJdk>false</requiresJdk>
    <requires64Bit>false</requires64Bit>
    <minVersion>17</minVersion>
    <maxVersion></maxVersion>
    <maxHeapSize>2048</maxHeapSize>
    <initialHeapSize>512</initialHeapSize>
  </jre>
  <versionInfo>
    <fileVersion>1.0.0.0</fileVersion>
    <txtFileVersion>1.0.0</txtFileVersion>
    <fileDescription>Minecraft No Verification Launcher</fileDescription>
    <copyright>MIT License</copyright>
    <productVersion>1.0.0.0</productVersion>
    <txtProductVersion>1.0.0</txtProductVersion>
    <productName>Minecraft Launcher</productName>
    <companyName>Miggo65</companyName>
    <internalName>MinecraftLauncher</internalName>
    <originalFilename>MinecraftLauncher.exe</originalFilename>
  </versionInfo>
</launch4jConfig>
```

Dann ausführen:
```cmd
launch4jc.exe launch4j-config.xml
```

## Methode 3: jlink + jpackage (Optimiert)

Für eine optimierte EXE mit kleinerer Java Runtime:

### Schritt 1: Custom Java Runtime erstellen

```cmd
jlink ^
  --module-path "%JAVA_HOME%\jmods" ^
  --add-modules java.base,java.desktop,java.logging,java.net.http,java.naming,java.xml ^
  --output custom-jre ^
  --strip-debug ^
  --compress 2 ^
  --no-header-files ^
  --no-man-pages
```

### Schritt 2: EXE mit custom Runtime erstellen

```cmd
jpackage ^
  --input build\libs ^
  --name MinecraftNoVerificationLauncher ^
  --main-jar minecraft-noverification-launcher-1.0.0.jar ^
  --main-class com.miggo65.launcher.MinecraftLauncher ^
  --runtime-image custom-jre ^
  --type exe ^
  --win-console ^
  --app-version 1.0.0
```

Dies erstellt eine kleinere EXE mit nur den benötigten Java-Modulen.

## Methode 4: GraalVM Native Image (Fortgeschritten)

Für eine komplett native EXE ohne Java-Abhängigkeit:

**Hinweis**: Dies ist komplex und erfordert GraalVM und native-image Tool.

1. **GraalVM installieren**:
   - Download von: https://www.graalvm.org/downloads/
   - GraalVM als JAVA_HOME setzen

2. **Native Image Tool installieren**:
   ```cmd
   gu install native-image
   ```

3. **Native EXE erstellen**:
   ```cmd
   native-image ^
     -jar build\libs\minecraft-noverification-launcher-1.0.0.jar ^
     --no-fallback ^
     -H:+ReportExceptionStackTraces ^
     -H:Name=MinecraftLauncher
   ```

**Warnung**: Swing-GUI funktioniert möglicherweise nicht optimal mit GraalVM Native Image.

## Empfehlung

Für die beste Balance zwischen Einfachheit und Funktionalität:

1. **Für Entwickler mit JDK**: Nutzen Sie **jpackage** (Methode 1)
2. **Für einfaches Wrapping**: Nutzen Sie **Launch4j** (Methode 2)
3. **Für optimierte Größe**: Nutzen Sie **jlink + jpackage** (Methode 3)

## Testen der EXE

Nach der Erstellung:

1. Testen Sie die EXE auf einem frischen Windows-System ohne Java (wenn mit jpackage + Runtime gebaut)
2. Überprüfen Sie, ob alle Features funktionieren
3. Testen Sie verschiedene Minecraft-Versionen
4. Überprüfen Sie die Fehlerbehandlung

## Häufige Probleme

### "java.lang.module.FindException"
- Stellen Sie sicher, dass alle benötigten Java-Module hinzugefügt wurden
- Fügen Sie fehlende Module mit `--add-modules` hinzu

### "Could not find or load main class"
- Überprüfen Sie den Main-Class-Namen
- Stellen Sie sicher, dass die JAR korrekt gebaut wurde

### EXE startet nicht
- Überprüfen Sie die JRE-Mindestversion
- Testen Sie die ursprüngliche JAR-Datei zuerst
- Aktivieren Sie Console-Output für Debug-Informationen

## Icon erstellen (Optional)

Um ein benutzerdefiniertes Icon zu erstellen:

1. Erstellen Sie ein PNG-Bild (256x256 oder 512x512)
2. Konvertieren Sie es zu ICO:
   - Online-Tools: https://convertio.co/png-ico/
   - Oder mit ImageMagick: `convert icon.png -define icon:auto-resize=256,128,64,48,32,16 icon.ico`
3. Verwenden Sie die ICO-Datei mit `--icon` Parameter

## Verteilung

Die erstellte EXE kann verteilt werden:

- Als eigenständige Datei (bei Launch4j, erfordert Java beim Benutzer)
- Mit Installer (jpackage MSI/EXE)
- Mit gebündelter Runtime (größere Dateigröße, aber keine Java-Installation nötig)

## Weitere Ressourcen

- jpackage Dokumentation: https://docs.oracle.com/en/java/javase/17/jpackage/
- Launch4j Dokumentation: https://launch4j.sourceforge.net/docs.html
- GraalVM Native Image: https://www.graalvm.org/native-image/
