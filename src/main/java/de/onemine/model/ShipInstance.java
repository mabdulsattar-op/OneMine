package de.onemine.model;

import java.util.UUID;

public record ShipInstance(
    UUID ownerId,
    ShipType type,
    UUID entityUuid,
    long purchasedAt
) {}
