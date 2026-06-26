# OneMine – System Design

> Piraten-Survival Server im One-Piece-Stil · Paper Plugin · Ein Command: `/menü`

---

## 1. Technologie-Entscheidung

| Aspekt | Entscheidung | Begründung |
|--------|--------------|------------|
| Plattform | **Paper 1.21+** | Kein Modpack nötig, Multiplayer-ready, performant |
| Mod | **Keine** | Zielgruppe: schwache PCs, einfaches Setup |
| NPCs | Vanilla-Mobs + Metadata | Kein Citizens/MythicMobs als Hard-Dependency |
| Schiffe | Vanilla Boats + Metadata | Leichtgewichtig, Multiplayer-tauglich |
| Speicher | **SQLite** | Schnelle Top-10-Queries, eine Datei, backup-freundlich |
| GUI | Inventory API (54 Slots max) | Standard Bukkit, kein Custom-Rendering |

**Soft Dependencies (optional):** PlaceholderAPI, Vault (nur wenn externe Economy gewünscht – Standard ist interne Berrys)

---

## 2. Systemarchitektur

```
┌─────────────────────────────────────────────────────────────────┐
│                        OneMinePlugin                            │
│  Bootstrap · Config · Service-Registry · Event-Bus              │
└───────────────────────────┬─────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        ▼                   ▼                   ▼
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│  /menü        │   │  Listeners    │   │  Persistence  │
│  MenuRouter   │   │  Combat        │   │  SQLite       │
│  → GUI Screens│   │  Protection    │   │  DataStore    │
└───────┬───────┘   │  Marine Spawn  │   └───────┬───────┘
        │           └───────┬───────┘           │
        │                   │                   │
        ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                           │
├──────────┬──────────┬──────────┬──────────┬──────────┬────────┤
│ Island   │ Clan     │ Bounty   │ Economy  │ Marine   │ Ship   │
│ Service  │ Service  │ Service  │ Service  │ Service  │ Service│
└──────────┴──────────┴──────────┴──────────┴──────────┴────────┘
        │                   │                   │
        ▼                   ▼                   ▼
┌─────────────────────────────────────────────────────────────────┐
│                         DATA MODELS                             │
│  Island · Clan · PlayerData · BountyEntry · ShipInstance        │
└─────────────────────────────────────────────────────────────────┘
```

### Datenfluss – Game Loop

```
Spawn auf Insel
    → MarineService spawnt NPCs auf Insel-Region
    → Spieler sammelt Berrys (Kill/Loot)
    → /menü → Clan gründen/beitreten
    → Shop: Schiff kaufen
    → Insel erkunden (WorldMapGUI)
    → PvP / Marine-Kills → Bounty steigt (BountyService)
    → Insel-Angriff (Clan vs. Clan) → Ownership wechselt (IslandService)
    → Killer bei Tod → Berrys aus Kopfgeld (EconomyService)
```

### Insel-Eroberung (State Machine)

```
FREE ──[Clan claimt]──► OWNED
OWNED ──[Angriff startet]──► UNDER_ATTACK
UNDER_ATTACK ──[Timer/Sieger]──► OWNED (neuer Clan) | FREE (Verteidiger siegt)
```

Eroberung: Spieler des angreifenden Clans müssen X Sekunden in Insel-Region bleiben ohne alle zu sterben. Kein komplexes RPG – nur Region-Timer + PvP.

---

## 3. Paketstruktur

```
de.onemine
├── OneMinePlugin.java
├── command
│   └── MenuCommand.java
├── config
│   └── PluginConfig.java
├── model
│   ├── Island.java
│   ├── IslandBiome.java
│   ├── IslandState.java
│   ├── Clan.java
│   ├── ClanRole.java
│   ├── PlayerData.java
│   ├── BountyEntry.java
│   ├── ShipType.java
│   └── ShipInstance.java
├── service
│   ├── IslandService.java
│   ├── ClanService.java
│   ├── BountyService.java
│   ├── EconomyService.java
│   ├── MarineService.java
│   ├── ShipService.java
│   └── ConquestService.java
├── gui
│   ├── GuiHolder.java
│   ├── MenuRouter.java
│   ├── MainMenuGui.java
│   ├── WorldMapGui.java
│   ├── ClanGui.java
│   ├── BountyGui.java
│   ├── ShopGui.java
│   ├── BerryGui.java
│   └── CosmeticGui.java
├── listener
│   ├── MenuListener.java
│   ├── CombatListener.java
│   ├── IslandProtectionListener.java
│   └── MarineListener.java
├── persistence
│   ├── DataStore.java
│   └── SqliteDataStore.java
└── util
    ├── GuiUtil.java
    └── Messages.java
```

---

## 4. Datenmodelle

### Island

```java
record Island(
    String id,              // "island_01"
    String displayName,     // "Whiskey Insel"
    IslandBiome biome,      // JUNGLE, DESERT, SNOW, VOLCANO, ...
    String worldName,
    int minX, int minZ, int maxX, int maxZ,
    int centerX, int centerZ,
    String ownerClanId,     // null = frei
    IslandState state,      // FREE, OWNED, UNDER_ATTACK
    long attackStartedAt
)
```

### Clan

```java
record Clan(
    String id,
    String name,
    String tag,             // max 4 Zeichen, z.B. "MUG"
    UUID leaderId,
    Set<UUID> members,
    List<String> ownedIslandIds,
    long createdAt,
    int flagColor            // Map-GUI Farbe (DYE ordinal)
)
```

### PlayerData

```java
record PlayerData(
    UUID uuid,
    String lastName,
    long berrys,
    long globalBounty,       // System-Kopfgeld
    long playerBounty,       // Von Spielern gesetzt
    String clanId,           // null
    String activeTitle,
    int cosmeticEffectId
)
// totalBounty = globalBounty + playerBounty
```

### BountyEntry (Wanted Board)

```java
record BountyEntry(UUID uuid, String name, long totalBounty)
```

### ShipType / ShipInstance

```java
enum ShipType {
    SMALL_BOAT(500, 1, Material.OAK_BOAT),
    MEDIUM_SHIP(2000, 4, Material.SPRUCE_BOAT),
    CLAN_GALLEON(10000, 8, Material.DARK_OAK_BOAT);
}
record ShipInstance(UUID ownerId, ShipType type, UUID entityUuid, long purchasedAt)
```

---

## 5. Datenbank-Schema (SQLite)

```sql
CREATE TABLE islands (
    id TEXT PRIMARY KEY,
    display_name TEXT NOT NULL,
    biome TEXT NOT NULL,
    world TEXT NOT NULL,
    min_x INT, min_z INT, max_x INT, max_z INT,
    center_x INT, center_z INT,
    owner_clan_id TEXT,
    state TEXT DEFAULT 'FREE',
    attack_started_at INTEGER DEFAULT 0
);

CREATE TABLE clans (
    id TEXT PRIMARY KEY,
    name TEXT UNIQUE NOT NULL,
    tag TEXT UNIQUE NOT NULL,
    leader_id TEXT NOT NULL,
    flag_color INT DEFAULT 0,
    created_at INTEGER NOT NULL
);

CREATE TABLE clan_members (clan_id TEXT, player_id TEXT, PRIMARY KEY(clan_id, player_id));
CREATE TABLE clan_islands (clan_id TEXT, island_id TEXT, PRIMARY KEY(clan_id, island_id));

CREATE TABLE players (
    uuid TEXT PRIMARY KEY,
    name TEXT,
    berrys INTEGER DEFAULT 0,
    global_bounty INTEGER DEFAULT 0,
    player_bounty INTEGER DEFAULT 0,
    clan_id TEXT,
    active_title TEXT,
    cosmetic_effect_id INTEGER DEFAULT 0
);

CREATE TABLE player_bounties (
    target_uuid TEXT,
    setter_uuid TEXT,
    amount INTEGER,
    PRIMARY KEY(target_uuid, setter_uuid)
);

CREATE TABLE ships (
    owner_uuid TEXT,
    entity_uuid TEXT PRIMARY KEY,
    ship_type TEXT,
    purchased_at INTEGER
);
```

---

## 6. GUI-Menüstruktur

### Hauptmenü (54 Slots, zentriert)

| Slot | Icon | Aktion |
|------|------|--------|
| 10 | Map | → WorldMapGui |
| 12 | Banner | → ClanGui |
| 14 | Skull | → BountyGui |
| 16 | Boat | → ShopGui (Schiffe) |
| 28 | Gold | → BerryGui |
| 30 | Chest | → ShopGui (Items) |
| 32 | Name Tag | → CosmeticGui |

Navigation: Jede Sub-GUI hat Zurück-Pfeil (Slot 49) → MainMenuGui

### WorldMapGui

- Raster aus Insel-Icons (grün=frei, rot=besetzt, gelb=Kampf)
- Hover: Inselname, Biom, Besitzer-Clan
- Klick auf besetzte Insel (Clan-Mitglied): Angriff starten

### BountyGui

- Eigener Status (global + player bounty)
- Spieler suchen (Chat-Eingabe via AnvilGUI oder SignGUI – MVP: nächster Online-Spieler-Liste)
- Berrys setzen
- Top 10 Wanted Board

---

## 7. Economy – Berrys

| Aktion | Standard-Wert (config) |
|--------|--------------------------|
| Marine Kill | 50–200 |
| PvP Kill | 100 + 10% Opfer-Bounty |
| Schatzkiste | 500–2000 |
| Clan gründen | -5000 |
| Kleines Boot | -500 |
| Globales Bounty +100 pro PvP-Kill | auto |

---

## 8. Marine NPC System

- **Spawn:** Pro Insel max. N Marines, Intervall T Sekunden
- **Entity:** Zombie/Pillager mit Marine-Nametag, Lederrüstung
- **Offizier:** 5% Chance, mehr HP, besseres Loot
- **Gruppen:** 1–3 Marines pro Spawn-Welle
- **Loot:** Berrys direkt auf Killer-Konto, seltene Items als Drop

Performance: Chunk-basiertes Spawning nur wenn Spieler in Insel-Region.

---

## 9. Schutz-System (Insel-Build)

- `IslandProtectionListener` cancelt BlockPlace/Break wenn:
  - Insel hat Owner-Clan
  - Spieler ist kein Mitglied
  - Config `protection.enabled = true`
- Optional: Clan-Leader kann Baurechte für Allianzen freigeben (Phase 2)

---

## 10. Performance-Richtlinien

1. **Keine tick()-Loops** – nutze BukkitScheduler mit festen Intervallen (20–100 Ticks)
2. **In-Memory Cache** – Islands/Clans beim Start laden, async DB-Writes
3. **Region-Check** – O(1) über vordefinierte Insel-Bounding-Boxes, kein WorldGuard nötig
4. **Marine Cap** – global max. 50 aktive Marines, pro Insel max. 5
5. **GUI** – statische Item-Templates, keine NBT-heavy Custom Items

---

## 11. Config (`config.yml`)

```yaml
language: de
command: menu  # Alias: menü

economy:
  start-berrys: 100
  clan-create-cost: 5000
  pvp-kill-reward: 100
  marine-kill-min: 50
  marine-kill-max: 200

bounty:
  pvp-increase: 100
  marine-increase: 25
  top-list-size: 10

islands:
  protection-enabled: true
  conquest-duration-seconds: 120
  conquest-min-attackers: 2

marines:
  spawn-interval-ticks: 600
  max-per-island: 5
  max-global: 50
  officer-chance: 0.05

ships:
  small-boat-cost: 500
  medium-ship-cost: 2000
  clan-galleon-cost: 10000
```

Insel-Definitionen in separater `islands.yml` (Admin-setup, nicht ingame).

---

## 12. Deployment

1. Paper 1.21+ Server
2. `OneMine.jar` in `/plugins`
3. `islands.yml` mit Regionen definieren (WorldEdit-Koordinaten)
4. Optional: Multiverse für Ozean-Welt
5. Spieler: nur `/menü` kennen lernen

---

## 13. Roadmap (Phasen)

| Phase | Inhalt |
|-------|--------|
| **MVP** | Menü, Berrys, Clans, Insel-Map, Bounty, SQLite |
| **v1.1** | Marines, Schiffe, Insel-Schutz, Eroberung |
| **v1.2** | Kosmetik, Schatzkarten, Events |
| **v2** | Web-Map, PlaceholderAPI, optionale Vault-Anbindung |

---

## 14. Abgrenzung (Design Rules)

- ❌ Keine Teufelsfrüchte, kein Haki, kein Level-RPG
- ❌ Keine weiteren Commands (Admin-Commands nur via Permission + Console)
- ✅ Fokus: PvP, Clans, Inseln, Economy, ein GUI
