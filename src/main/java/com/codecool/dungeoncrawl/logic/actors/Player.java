package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Player extends Actor {
    public ArrayList<Item> inventory = new ArrayList<>();

    public Player(Cell cell) {
        super(cell);
        this.setHealth(100);
        this.setStrength(5);
    }

    @Override
    public void move(int dx, int dy) {
        nextCell = cell.getNeighbor(dx, dy);

        if(this.getName() != null && (this.getName().equals("sebastian") || this.getName().equals("claudiu"))) {
            if(isEnemy()) fight();
            else setMovement();
        }
        else if (!isEnemy() && isValidMove()) setMovement();
        else if (isEnemy()) fight();
        else if (nextCell.getType() == CellType.CLOSEDDOOR && isHasKey()){
            nextCell.setType(CellType.OPENEDDOOR);
            setMovement();
        }


    }


    public String getTileName() {
        return "player";
    }



    private boolean isValidMove() {

        return nextCell.getType() == CellType.FLOOR
                || nextCell.getType() == CellType.OPENEDDOOR
                || nextCell.getType() == CellType.GRASS
                || nextCell.getType() == CellType.WATER
                || nextCell.getType() == CellType.STAIRS
                || nextCell.getType() == CellType.PATH;
    }

        
    public void itemPickUp(){
        inventory.add(this.getCell().getItem());
        if (this.getCell().getItem() instanceof Sword) {
            this.setStrength(10);
        } else if (this.getCell().getItem() instanceof Key) {
            this.setHasKey(true);
        }
        this.getCell().setItem(null);
    }

    public String inventoryToString() {
        StringJoiner sb = new StringJoiner("\n");
        for(Item item : inventory){
            if(item!=null){
                sb.add(item.getTileName() + " ");
            }
        }
        return sb.toString();

    }
}

