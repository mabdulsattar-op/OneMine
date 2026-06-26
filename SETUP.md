# OneMine – Build & Test (Schritt für Schritt)

Diese Anleitung führt dich von **0** bis **laufender Test-Server**.

---

## Voraussetzungen

| Tool | Version | Zweck |
|------|---------|--------|
| Java | **21** | Minecraft + Plugin bauen |
| Maven | 3.9+ | Plugin kompilieren |
| Minecraft Java Edition | 1.21.x | Client zum Verbinden |

---

## Schritt 1 – Java & Maven installieren (Windows)

PowerShell **als Administrator** öffnen und ausführen:

```powershell
winget install -e --id EclipseAdoptium.Temurin.21.JDK --accept-package-agreements --accept-source-agreements
winget install -e --id Apache.Maven --accept-package-agreements --accept-source-agreements
```

Danach **PowerShell neu starten** (PATH wird aktualisiert).

Prüfen:

```powershell
java -version
mvn -version
```

**Alternative ohne winget:**  
- Java 21: https://adoptium.net/  
- Maven: https://maven.apache.org/download.cgi  

---

## Schritt 2 – Plugin bauen

```powershell
cd c:\Minecraft\OneMine
mvn package -DskipTests
```

Erfolg → Datei existiert:

```
c:\Minecraft\OneMine\target\OneMine-1.0.0-SNAPSHOT.jar
```

---

## Schritt 3 – Paper Test-Server einrichten

Automatisch (empfohlen):

```powershell
cd c:\Minecraft\OneMine\scripts
powershell -ExecutionPolicy Bypass -File .\setup-server.ps1
```

Das Skript:
1. Lädt Paper 1.21.4 herunter
2. Erstellt `c:\Minecraft\OneMine-Server\`
3. Kopiert die Plugin-JAR nach `plugins/`
4. Erzeugt `eula.txt` (EULA akzeptiert) und `server.properties`

---

## Schritt 4 – Server starten

```powershell
cd c:\Minecraft\OneMine\scripts
powershell -ExecutionPolicy Bypass -File .\start-server.ps1
```

Beim **ersten Start** dauert es 1–2 Minuten (Welt wird generiert).

Server stoppen: im Server-Fenster `stop` eingeben.

---

## Schritt 5 – Im Spiel verbinden

1. Minecraft Java Edition **1.21.4** starten
2. Multiplayer → Direct Connect
3. Adresse: **`localhost`**
4. Im Chat: **`/menü`**

---

## Schritt 6 – Inseln konfigurieren (wichtig!)

Die Beispiel-Inseln in `plugins/OneMine/islands.yml` passen **nicht** zu deiner Welt.

**Option A – Schnelltest (Admin bypass):**
- Als OP eingeloggt → Bau-Schutz greift nicht (`onemine.admin`)
- Menü, Clans, Berrys, Shop sofort testbar

**Option B – Echte Insel-Regionen:**
1. WorldEdit installieren (optional): `/plugins` Ordner
2. Ingame: `//pos1` und `//pos2` an Insel-Ecken setzen
3. Koordinaten in `plugins/OneMine/islands.yml` eintragen
4. `/reload confirm` oder Server neu starten

---

## Schnell-Checkliste im Spiel

| Aktion | Erwartung |
|--------|-----------|
| `/menü` | Haupt-GUI öffnet sich |
| Berrys | Startguthaben 100 (config.yml) |
| Clan gründen | 5000 Berrys nötig → erst `/menü` → Berrys prüfen |
| Marines | Spawnen auf Inseln wenn Spieler dort ist |
| PvP | Kopfgeld steigt, Killer bekommt Berrys |

**Tipp für schnellen Test:** In `plugins/OneMine/config.yml` temporär setzen:

```yaml
economy:
  start-berrys: 10000
  clan-create-cost: 100
```

Dann Server neu starten.

---

## Ordnerstruktur nach Setup

```
c:\Minecraft\
├── OneMine\                  ← Quellcode + Build
│   └── target\*.jar
└── OneMine-Server\           ← Paper Test-Server
    ├── paper.jar
    ├── plugins\
    │   └── OneMine-*.jar
    ├── plugins\OneMine\
    │   ├── config.yml
    │   └── islands.yml
    └── world\
```

---

## Häufige Probleme

| Problem | Lösung |
|---------|--------|
| `java` nicht gefunden | PowerShell neu starten nach Installation |
| `mvn` nicht gefunden | Maven installieren, PATH prüfen |
| Plugin lädt nicht | Paper **1.21+** verwenden, Server-Log prüfen |
| `/menü` unbekannt | Plugin-Fehler in `logs/latest.log` suchen |
| Keine Marines | Spieler muss auf definierter Insel stehen |
| Eroberung klappt nicht | `islands.yml` Koordinaten anpassen |

---

## Nach Code-Änderungen

```powershell
cd c:\Minecraft\OneMine
mvn package -DskipTests
Copy-Item target\OneMine-1.0.0-SNAPSHOT.jar ..\OneMine-Server\plugins\ -Force
# Server neu starten
```
