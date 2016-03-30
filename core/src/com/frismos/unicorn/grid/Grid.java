package com.frismos.unicorn.grid;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.actor.Bullet;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;

/**
 * Created by edgaravanyan on 10/16/15.
 */
public class Grid extends Group {
    public Tile[][] grid = new Tile[GameStage.ROW_LENGTH][GameStage.COLUMN_LENGTH];

    public float tileWidth;
    public float tileHeight;

    private Vector2 offset;

    public Array<Tile> markedDiagonal = new Array<>();
    public Array<Tile> markedBackDiagonal = new Array<>();
    public Array<Tile> markedHorizontal = new Array<>();
    public Array<Tile> markedVertical = new Array<>();
    public Array<Tile> diagonal = new Array<>();
    public Array<Tile> backDiagonal = new Array<>();
    public Array<Tile> horizontal = new Array<>();
    public Array<Tile> vertical = new Array<>();
    public ColorType currentColor = null;

    public int nextRedRow = -1;
    public int nextGreenRow = -1;
    public int nextBlueRow = -1;
    public int nextYellowRow = -1;

    private GameStage gameStage;

    public Grid(GameStage stage) {
        gameStage = stage;
        Array<Integer> preFilledColors = new Array<>();
        offset = new Vector2(gameStage.background.getZero().x - new Tile(gameStage, 0, 0).getWidth(), gameStage.background.getZero().y);
        Array<Tile> tiles = new Array<>();
        for (int i = 0; i < GameStage.ROW_LENGTH; i++) {
            for (int j = 0; j < GameStage.COLUMN_LENGTH; j++) {
                grid[i][j] = new Tile(gameStage, i, j);
                grid[i][j].grid = this;
                tiles.add(grid[i][j]);
                addActor(grid[i][j]);
                grid[i][j].setPosition(offset.x + j * (grid[i][j].getWidth()), offset.y + i * (grid[i][j].getHeight()));
                if(j == 0) {
                    if(i == 0) {
                        grid[i][j].color(ColorType.RED);
                    } else if(i == 1) {
                        grid[i][j].color(ColorType.GREEN);
                    } else if(i == 2) {
                        grid[i][j].color(ColorType.BLUE);
                    }
//                } else if(preFilledColors.size > 0) {
//                    if(MathUtils.randomBoolean((i+1) * 0.1f)) {
//                        int index = preFilledColors.get(MathUtils.random(preFilledColors.size - 1));
//                        preFilledColors.removeValue(index, true);
//                        grid[i][j].color(ColorType.values()[index]);
//                    }
                }
            }
        }

        tileWidth = grid[0][0].getWidth();
        tileHeight = grid[0][0].getHeight();
    }

    public boolean isPointInsideGrid(float x, float y) {
        return GameStage.COLUMN_LENGTH * tileWidth  + offset.x > x &&
                GameStage.ROW_LENGTH * tileHeight  + offset.y > y &&
                offset.x < x &&
                offset.y < y;
    }

    public Tile getTileByPoint(float x, float y) {
        for (int i = GameStage.ROW_LENGTH - 1; i >= 0; i--) {
            if (y > i * tileHeight + offset.y) {
                for (int j = GameStage.COLUMN_LENGTH - 1; j >= 0; j--) {
                    if (x > j * tileWidth + offset.x) {
                        return grid[i][j];
                    }
                }
                return grid[i][0];
            }
        }

        for (int j = GameStage.COLUMN_LENGTH - 1; j >= 0; j--) {
            if (x > j * tileWidth + offset.x) {
                return grid[0][j];
            }
        }
        return null;
    }

    public void checkForMatchDiagonal(Tile tile) {
        markedDiagonal.add(tile);
        if(tile.colorType != null && tile.colorType == currentColor) {
            diagonal.add(tile);
        }
        if (isTileInsideGrid(tile.i - 1, tile.j - 1)) {
            if (!markedDiagonal.contains(grid[tile.i - 1][tile.j - 1], false)) {
                Tile tmp = grid[tile.i - 1][tile.j - 1];
                checkForMatchDiagonal(tmp);
            }
        }
        if (isTileInsideGrid(tile.i + 1, tile.j + 1)) {
            if (!markedDiagonal.contains(grid[tile.i + 1][tile.j + 1], false)) {
                Tile tmp = grid[tile.i + 1][tile.j + 1];
                checkForMatchDiagonal(tmp);
            }
        }
    }

    public void checkForMatchBackDiagonal(Tile tile) {
        markedBackDiagonal.add(tile);
        if(tile.colorType != null && tile.colorType == currentColor) {
            backDiagonal.add(tile);
        }
        if (isTileInsideGrid(tile.i - 1, tile.j + 1)) {
            if (!markedBackDiagonal.contains(grid[tile.i - 1][tile.j + 1], false)) {
                Tile tmp = grid[tile.i - 1][tile.j + 1];
                checkForMatchBackDiagonal(tmp);
            }
        }
        if (isTileInsideGrid(tile.i + 1, tile.j - 1)) {
            if (!markedBackDiagonal.contains(grid[tile.i + 1][tile.j - 1], false)) {
                Tile tmp = grid[tile.i + 1][tile.j - 1];
                checkForMatchBackDiagonal(tmp);
            }
        }
    }

    public void checkForMatchHorizontal(Tile tile) {
        markedHorizontal.add(tile);
        if(tile.colorType != null && tile.colorType == currentColor) {
            horizontal.add(tile);
        }
        if (isTileInsideGrid(tile.i, tile.j - 1)) {
            if (!markedHorizontal.contains(grid[tile.i][tile.j - 1], false)) {
                Tile tmp = grid[tile.i][tile.j - 1];
                checkForMatchHorizontal(tmp);
            }
        }
        if (isTileInsideGrid(tile.i, tile.j + 1)) {
            if (!markedHorizontal.contains(grid[tile.i][tile.j + 1], false)) {
                Tile tmp = grid[tile.i][tile.j + 1];
                checkForMatchHorizontal(tmp);
            }
        }
    }

    public void checkForMatchVertical(Tile tile) {
        markedVertical.add(tile);
        if(tile.colorType != null && tile.colorType == currentColor) {
            vertical.add(tile);
        }
        if (isTileInsideGrid(tile.i - 1, tile.j)) {
            if (!markedVertical.contains(grid[tile.i - 1][tile.j], false)) {
                Tile tmp = grid[tile.i - 1][tile.j];
                checkForMatchVertical(tmp);
            }
        }
        if (isTileInsideGrid(tile.i + 1, tile.j)) {
            if (!markedVertical.contains(grid[tile.i + 1][tile.j], false)) {
                Tile tmp = grid[tile.i + 1][tile.j];
                checkForMatchVertical(tmp);
            }
        }
    }

    public Array<Tile> getNeighbourTiles(Tile tile) {
        Array<Tile> neighbours = new Array<>();
        if(isTileInsideGrid(tile.i-1, tile.j)) {
            neighbours.add(grid[tile.i-1][tile.j]);
        }
        if(isTileInsideGrid(tile.i-1, tile.j-1)) {
            neighbours.add(grid[tile.i-1][tile.j-1]);
        }
        if(isTileInsideGrid(tile.i-1, tile.j+1)) {
            neighbours.add(grid[tile.i-1][tile.j+1]);
        }
        if(isTileInsideGrid(tile.i, tile.j-1)) {
            neighbours.add(grid[tile.i][tile.j-1]);
        }
        if(isTileInsideGrid(tile.i, tile.j+1)) {
            neighbours.add(grid[tile.i][tile.j+1]);
        }
        if(isTileInsideGrid(tile.i+1, tile.j-1)) {
            neighbours.add(grid[tile.i+1][tile.j-1]);
        }
        if(isTileInsideGrid(tile.i+1, tile.j)) {
            neighbours.add(grid[tile.i+1][tile.j]);
        }
        if(isTileInsideGrid(tile.i+1, tile.j+1)) {
            neighbours.add(grid[tile.i+1][tile.j+1]);
        }
        neighbours.add(tile);
        return neighbours;
    }
    
    public Array<Tile> getNeighbourTiles(int i, int j) {
        Array<Tile> neighbours = new Array<>();
        if(isTileInsideGrid(i-1, j)) {
            neighbours.add(grid[i-1][j]);
        }
        if(isTileInsideGrid(i-1, j-1)) {
            neighbours.add(grid[i-1][j-1]);
        }
        if(isTileInsideGrid(i-1, j+1)) {
            neighbours.add(grid[i-1][j+1]);
        }
        if(isTileInsideGrid(i, j-1)) {
            neighbours.add(grid[i][j-1]);
        }
        if(isTileInsideGrid(i, j+1)) {
            neighbours.add(grid[i][j+1]);
        }
        if(isTileInsideGrid(i+1, j-1)) {
            neighbours.add(grid[i+1][j-1]);
        }
        if(isTileInsideGrid(i+1, j)) {
            neighbours.add(grid[i+1][j]);
        }
        if(isTileInsideGrid(i+1, j+1)) {
            neighbours.add(grid[i+1][j+1]);
        }
        if(isTileInsideGrid(i, j)) {
            neighbours.add(grid[i][j]);
        }

        return neighbours;
    }
    
    public Enemy isEnemyOnDirection(Tile tile, int direction) {
        if(tile != null) {
            switch (direction) {
                case Bullet.DOWN_DIAGONAL:
                    return isEnemyDownDiagonal(tile);
                case Bullet.RIGHT:
                    return isEnemyRight(tile);
                case Bullet.UP_DIAGONAL:
                    return isEnemyUpDiagonal(tile);
                case Bullet.UP:
                    return isEnemyUp(tile);
                case Bullet.DOWN:
                    return isEnemyDown(tile);
                case Bullet.LEFT:
                    return isEnemyLeft(tile);
                case Bullet.DOWN_BACK_DIAGONAL:
                    return isEnemyDownBackDiagonal(tile);
                case Bullet.UP_BACK_DIAGONAL:
                    return isEnemyUpBackDiagonal(tile);
            }
        }
        return null;
    }

    private Enemy isEnemyUpBackDiagonal(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i + 1, tile.j - 1)) {
            return isEnemyUpBackDiagonal(grid[tile.i + 1][tile.j - 1]);
        }
        return null;
    }

    private Enemy isEnemyDownBackDiagonal(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i - 1, tile.j - 1)) {
            return isEnemyDownBackDiagonal(grid[tile.i - 1][tile.j - 1]);
        }
        return null;
    }

    private Enemy isEnemyUpDiagonal(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i + 1, tile.j + 1)) {
            return isEnemyUpDiagonal(grid[tile.i + 1][tile.j + 1]);
        }
        return null;
    }

    private Enemy isEnemyUp(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i + 1, tile.j)) {
            return isEnemyUp(grid[tile.i + 1][tile.j]);
        }
        return null;
    }

    private Enemy isEnemyDown(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i - 1, tile.j)) {
            return isEnemyDown(grid[tile.i - 1][tile.j]);
        }
        return null;
    }

    private Enemy isEnemyLeft(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i, tile.j - 1)) {
            return isEnemyLeft(grid[tile.i][tile.j - 1]);
        }
        return null;
    }

    private Enemy isEnemyRight(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i, tile.j + 1)) {
            return isEnemyRight(grid[tile.i][tile.j + 1]);
        }
        return null;
    }

    private Enemy isEnemyDownDiagonal(Tile tile) {
        if(tile.enemies.size > 0) {
            for (int i = 0; i < tile.enemies.size; i++) {
                if (gameStage.unicorn.colorType == tile.enemies.get(i).colorType) {
                    return tile.enemies.get(i);
                }
            }
        }
        if(isTileInsideGrid(tile.i - 1, tile.j + 1)) {
            return isEnemyDownDiagonal(grid[tile.i - 1][tile.j + 1]);
        }
        return null;
    }

    public boolean isTileInsideGrid(int i, int j) {
        return i >= 0 && j >= 0 && i < GameStage.ROW_LENGTH && j < GameStage.COLUMN_LENGTH;
    }


    public boolean isTileInsideGrid(Tile t) {
        return isTileInsideGrid(t.i, t.j);
    }

    public int distanceRow(Tile tile1, Tile tile2) {
        return Math.abs(tile1.i - tile2.i);
    }

    public int distanceColumn(Tile tile1, Tile tile2) {
        return Math.abs(tile1.j - tile2.j);
    }

    @Override
    public void addActor(Actor actor) {
        if(actor instanceof SpineActor) {
            ((SpineActor)actor).actorAddedToStage();
        }
        super.addActor(actor);
    }
}
