package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;


public abstract class Actor implements Drawable {
    private String name;
    private boolean hasKey;
    protected Cell cell;
    protected Cell nextCell;
    protected int health;
    protected int strength;
    public boolean isInFightMode = false;


    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public abstract void move(int dx, int dy);

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public void setHealth(int health) { this.health += health; }

    public void setStrength(int strength) { this.strength += strength; }

    public int getStrength() { return strength; }

    public int getHealth() {
        return health;
    }

    public void setHasKey(boolean hasKey) { this.hasKey = hasKey; }

    public boolean isHasKey() { return hasKey; }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    protected void setMovement() {
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }


    public boolean isNextMap() {
        return cell.getType() == CellType.STAIRS;
    }


    protected void fight() {
        if (nextCell.getActor().isDead()) {
            nextCell.getActor().die();
            isInFightMode = false;
            return;
        } else if (cell.getActor().isDead()) {
            cell.getActor().die();
            isInFightMode = false;
            return;
        }

        nextCell.getActor().setHealth(-getStrength());
        setHealth(-nextCell.getActor().getStrength());

    }

    protected boolean isEnemy() {return nextCell.getActor() != null; }

    public boolean isDead() { return this.health < 1; }

    protected void die() {
        if (cell.getActor() instanceof Enemy) cell.removeEnemy(this);
        cell.setActor(null);
    }

}
