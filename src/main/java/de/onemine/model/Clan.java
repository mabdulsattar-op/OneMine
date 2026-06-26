package de.onemine.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Clan {
    private String id;
    private String name;
    private String tag;
    private UUID leaderId;
    private Set<UUID> members;
    private Set<String> ownedIslandIds;
    private long createdAt;
    private int flagColor;

    public Clan(String id, String name, String tag, UUID leaderId, int flagColor) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.leaderId = leaderId;
        this.members = new HashSet<>();
        this.members.add(leaderId);
        this.ownedIslandIds = new HashSet<>();
        this.createdAt = System.currentTimeMillis();
        this.flagColor = flagColor;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getTag() { return tag; }
    public UUID getLeaderId() { return leaderId; }
    public Set<UUID> getMembers() { return members; }
    public Set<String> getOwnedIslandIds() { return ownedIslandIds; }
    public long getCreatedAt() { return createdAt; }
    public int getFlagColor() { return flagColor; }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public boolean isLeader(UUID uuid) {
        return leaderId.equals(uuid);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public void addIsland(String islandId) {
        ownedIslandIds.add(islandId);
    }

    public void removeIsland(String islandId) {
        ownedIslandIds.remove(islandId);
    }
}
