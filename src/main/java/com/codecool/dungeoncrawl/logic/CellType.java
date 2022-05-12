package com.codecool.dungeoncrawl.logic;

public enum CellType {
    EMPTY("empty"),
    FLOOR("floor"),
    WALL("wall"),
    OPENEDDOOR("openedDoor"),
    CLOSEDDOOR("closedDoor"),
    PLAYER("player"),
    GRASS("grass"),
    HOUSE("house"),
    WATER("water"),
    FIRE("fire"),
    THREE("three"),
    STAIRS("stairs"),
    PATH("path");



    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }

    public boolean isWalkable() {
        switch (this) {
            case FLOOR:
                return true;
            case GRASS:
                return true;
            case WATER:
                return true;
            case STAIRS:
                return true;
        }
        return false;
    }

}
