package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Spider extends Enemy {

    public Spider(Cell cell) {
        super(cell);
        health = 15;
        strength = 5;
        this.enemyName = "spider";
    }
}
