# OneMine

Piraten-Survival Plugin im One-Piece-Stil für **Paper 1.21+**.

Ein Command. Ein Menü. Alles andere passiert ingame.

## Features

- **Weltkarte** – Inseln mit Biomen, Clan-Besitz, Eroberung
- **Piratenbanden** – Clans mit Tag, Chat (Phase 2), Insel-Besitz
- **Kopfgeld** – Global + spielergesetzt, Wanted Top 10
- **Berrys** – Interne Währung
- **Marine NPCs** – Spawnen auf Inseln, droppen Berrys
- **Schiffe** – Boote als Transport (Shop)
- **Insel-Schutz** – Nur Clan-Mitglieder dürfen bauen

## Installation

1. Paper 1.21+ Server starten
2. `OneMine.jar` nach `plugins/` kopieren
3. Server neu starten
4. `plugins/OneMine/islands.yml` – Insel-Koordinaten anpassen
5. Spieler nutzen **`/menü`** (Alias: `/menu`, `/menue`)

## Build

```bash
cd OneMine
mvn package
# Output: target/OneMine-1.0.0-SNAPSHOT.jar
```

**Voraussetzung:** Java 21, Maven

## Konfiguration

| Datei | Inhalt |
|-------|--------|
| `config.yml` | Economy, Bounty, Marines, Eroberung |
| `islands.yml` | Insel-Regionen (Admin) |

Insel-Koordinaten mit WorldEdit ermitteln: `//pos1`, `//pos2`, dann min/max in `islands.yml` eintragen.

## Architektur

Siehe [DESIGN.md](DESIGN.md) für:

- Systemarchitektur-Diagramm
- Klassenstruktur
- Datenbank-Schema
- GUI-Menüstruktur
- Performance-Richtlinien
- Roadmap

## Permissions

| Permission | Default | Beschreibung |
|------------|---------|--------------|
| `onemine.use` | true | Menü öffnen |
| `onemine.admin` | op | Admin (Bau-Schutz bypass) |

## Game Loop

```
Spawn → Berrys sammeln → /menü → Clan → Schiff → Inseln → Marines → Kopfgeld → Erobern
```

## Design-Regeln

- Keine Teufelsfrüchte / Haki
- Kein komplexes RPG
- Nur `/menü` als Spieler-Command
- GUI-basiert, performance-optimiert
