package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Ghost extends Enemy {

    public Ghost(Cell cell) {
        super(cell);
        this.health = 1;
        this.strength = 20;
        this.enemyName = "ghost";
    }
}
