package com.codecool.dungeoncrawl.logic;


import com.codecool.dungeoncrawl.logic.actors.Ghost;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Spider;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.model.EnemyModel;


import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String txt) {
        InputStream is = MapLoader.class.getResourceAsStream(txt);
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case '+':
                            cell.setType(CellType.GRASS);
                            break;
                        case 'C':
                            cell.setType(CellType.THREE);
                            break;
                        case 'P':
                            cell.setType(CellType.PATH);
                            break;
                        case 'A':
                            cell.setType(CellType.WATER);
                            break;
                        case 'H':
                            cell.setType(CellType.HOUSE);
                            break;
                        case 'F':
                            cell.setType(CellType.FIRE);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case '!':
                            cell.setType(CellType.FLOOR);
                            map.setEnemies(new Ghost(cell));
                            break;
                        case 'S':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            break;
                        case '$':
                            cell.setType(CellType.FLOOR);
                            map.setEnemies(new Spider(cell));
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSEDDOOR);
                            break;
                        case 'x':
                            cell.setType(CellType.STAIRS);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
