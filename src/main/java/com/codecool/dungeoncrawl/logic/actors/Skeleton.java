package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Skeleton extends Enemy {
    public Skeleton(Cell cell) {
        super(cell);
        this.health = 5;
        this.strength = 2;
        this.enemyName = "skeleton";

    }

    @Override
    public void move(int dx, int dy) {

    }

    @Override
    public String getTileName() {
        return "skeleton";
    }
}
