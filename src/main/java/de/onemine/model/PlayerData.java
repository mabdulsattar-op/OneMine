package de.onemine.model;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private String lastName;
    private long berrys;
    private long globalBounty;
    private long playerBounty;
    private String clanId;
    private String activeTitle;
    private int cosmeticEffectId;

    public PlayerData(UUID uuid, String lastName) {
        this.uuid = uuid;
        this.lastName = lastName;
        this.berrys = 100;
        this.globalBounty = 0;
        this.playerBounty = 0;
        this.clanId = null;
        this.activeTitle = "Newbie";
        this.cosmeticEffectId = 0;
    }

    public UUID getUuid() { return uuid; }
    public String getLastName() { return lastName; }
    public void setLastName(String name) { this.lastName = name; }
    public long getBerrys() { return berrys; }
    public void setBerrys(long berrys) { this.berrys = Math.max(0, berrys); }
    public void addBerrys(long amount) { setBerrys(berrys + amount); }
    public void removeBerrys(long amount) { setBerrys(berrys - amount); }
    public long getGlobalBounty() { return globalBounty; }
    public void setGlobalBounty(long bounty) { this.globalBounty = Math.max(0, bounty); }
    public void addGlobalBounty(long amount) { setGlobalBounty(globalBounty + amount); }
    public long getPlayerBounty() { return playerBounty; }
    public void setPlayerBounty(long bounty) { this.playerBounty = Math.max(0, bounty); }
    public long getTotalBounty() { return globalBounty + playerBounty; }
    public String getClanId() { return clanId; }
    public void setClanId(String clanId) { this.clanId = clanId; }
    public String getActiveTitle() { return activeTitle; }
    public void setActiveTitle(String title) { this.activeTitle = title; }
    public int getCosmeticEffectId() { return cosmeticEffectId; }
    public void setCosmeticEffectId(int id) { this.cosmeticEffectId = id; }
}
