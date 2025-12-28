# Schnellstart-Anleitung

## Was ist das?

Dies ist ein **eigenständiger Minecraft Launcher**, der es ermöglicht, Minecraft ohne Microsoft/Mojang-Account zu starten. Perfect für lokale Entwicklung und Testing.

## Unterschied zur Mod-Version

**WICHTIG**: Das Projekt wurde umgebaut!

- ❌ **NICHT MEHR**: Eine Fabric-Mod, die mit dem Fabric Launcher gestartet werden muss
- ✅ **JETZT**: Ein eigenständiges Programm mit GUI, das direkt ausgeführt werden kann

## Sofort starten

### Windows

1. Doppelklick auf `run-launcher.bat`
2. Warten während das Programm gebaut wird (beim ersten Mal)
3. Der Launcher öffnet sich automatisch

Oder manuell:
```cmd
cd launcher
java -jar build\libs\minecraft-noverification-launcher-1.0.0.jar
```

### Linux/Mac

```bash
./run-launcher.sh
```

Oder manuell:
```bash
cd launcher
java -jar build/libs/minecraft-noverification-launcher-1.0.0.jar
```

## Verwendung

1. **Minecraft Version wählen**: Dropdown-Menü öffnen und Version auswählen (z.B. 1.20.4)
2. **Username eingeben**: Einen beliebigen Spielernamen eingeben
3. **"Install & Launch Minecraft" klicken**: 
   - Beim ersten Mal wird die Minecraft-Version heruntergeladen (~50-100 MB)
   - Fabric Loader wird installiert
   - Minecraft startet automatisch

## Das erste Mal

Beim ersten Start einer neuen Minecraft-Version:

1. **Download dauert ca. 2-5 Minuten** (abhängig von der Internetgeschwindigkeit)
2. **Fortschritt wird im Log-Fenster angezeigt**
3. **Die Progress-Bar zeigt den aktuellen Status**
4. **Beim zweiten Start ist es viel schneller** (Version ist bereits installiert)

## .exe-Datei erstellen

Siehe [launcher/BUILD_EXE.md](launcher/BUILD_EXE.md) für detaillierte Anweisungen.

**Schnellste Methode** (wenn JDK installiert ist):

```cmd
cd launcher
jpackage --input build\libs --name MinecraftLauncher --main-jar minecraft-noverification-launcher-1.0.0.jar --main-class com.miggo65.launcher.MinecraftLauncher --type exe --win-console
```

## Systemanforderungen

- ✅ Java 17 oder höher
- ✅ Mindestens 2 GB RAM
- ✅ Internetverbindung (nur für ersten Download)
- ✅ ~500 MB Festplattenspeicher pro Minecraft-Version

## Was wird heruntergeladen?

- Minecraft Client JAR (~50 MB)
- Minecraft Assets (Texturen, Sounds, etc.) (~200 MB)
- Bibliotheken (~100 MB)
- Fabric Loader (~5 MB)

Alle Dateien werden im Standard-`.minecraft`-Ordner gespeichert:
- Windows: `C:\Users\<Username>\.minecraft`
- Linux: `~/.minecraft`
- Mac: `~/Library/Application Support/minecraft`

## Häufige Fragen

### Warum wurde es umgebaut?

Die alte Version war eine Fabric-Mod, die:
- ❌ Manuell mit Fabric Launcher gestartet werden musste
- ❌ Fabric Loader vorher installiert werden musste
- ❌ Komplizierter zu verwenden war

Die neue Version ist:
- ✅ Ein eigenständiges Programm mit GUI
- ✅ Installiert alles automatisch
- ✅ Viel einfacher zu verwenden

### Funktioniert die alte Mod-Version noch?

Ja! Der Mod-Code ist noch im Projekt enthalten:
```bash
./gradlew build  # Im Hauptverzeichnis
```

Die Mod ist dann in `build/libs/noverification-client-1.0.0.jar`

### Kann ich auf öffentliche Server?

**Nein.** Dieses Tool funktioniert nur mit Servern, die `online-mode=false` haben. Das sind normalerweise nur:
- Lokale Test-Server
- Entwicklungs-Server
- Private Server mit deaktivierter Authentifizierung

### Ist das legal?

✅ **Ja**, wenn du es für:
- Eigene Server
- Lokale Entwicklung
- Testing

❌ **Nein** für:
- Umgehen von gekauften Accounts
- Zugriff auf Server ohne Erlaubnis

## Probleme?

1. **"Java nicht gefunden"**: Java 17 installieren von [Adoptium.net](https://adoptium.net/)
2. **Download schlägt fehl**: Internetverbindung prüfen, Firewall deaktivieren
3. **Launcher startet nicht**: JAR über Kommandozeile starten um Fehler zu sehen
4. **Spiel startet nicht**: Log-Ausgabe im Launcher prüfen

## Weiter Hilfe

- Vollständige Dokumentation: [launcher/README.md](launcher/README.md)
- .exe erstellen: [launcher/BUILD_EXE.md](launcher/BUILD_EXE.md)
- Issues auf GitHub öffnen
