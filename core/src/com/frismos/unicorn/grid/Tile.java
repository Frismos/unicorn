package com.frismos.unicorn.grid;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Sort;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.frismos.unicorn.actor.Enemy;
import com.frismos.unicorn.actor.Unicorn;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Strings;

import com.badlogic.gdx.utils.Array;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by edgaravanyan on 10/16/15.
 */
public class Tile extends SpineActor {

    //"F2EDE9", "E1D1C1",
    public static String[] randomColors = { "CFBBA2", "C9AF94" };
    public static final boolean CHECK_TO_SEND_ON_ROW = false;

    public int i;
    public int j;

    public ColorType colorType;
    public Array<Enemy> enemies = new Array<>();
    public Unicorn unicorn;

    private String skinNumber;
    private Color tileColor;

    public Grid grid;

    public Array<Tile> matchingTiles = new Array<>();

    private Comparator<Tile> horizontalComp = new Comparator<Tile>() {
        @Override
        public int compare(Tile o1, Tile o2) {
            return o1.j > o2.j ? 1 : o1.j < o2.j ? -1 : 0;
        }
    };
    private Comparator<Tile> verticalComp = new Comparator<Tile>() {
        @Override
        public int compare(Tile o1, Tile o2) {
            return o1.i > o2.i ? 1 : o1.i < o2.i ? -1 : 0;
        }
    };

    private AnimationState.AnimationStateListener colorListener = new AnimationState.AnimationStateListener() {
        @Override
        public void event(int trackIndex, Event event) {

        }

        @Override
        public void complete(int trackIndex, int loopCount) {
            skeletonActor.getAnimationState().removeListener(this);
            Color color = Color.valueOf(Strings.BLUE);//blue
            if (colorType == ColorType.GREEN) {
                color = Color.valueOf(Strings.GREEN);
            } else if (colorType == ColorType.YELLOW) {
                color = Color.valueOf(Strings.YELLOW);
            } else if (colorType == ColorType.RED) {
                color = Color.valueOf(Strings.RED);
            }

            for (int i = 0; i < skeletonActor.getSkeleton().getSlots().size; i++) {
                if (skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color")) {
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
                }
            }
            if(CHECK_TO_SEND_ON_ROW) {
                if (grid != null && j != 0) {
                    grid.currentColor = colorType;
                    grid.checkForMatchHorizontal(Tile.this);
                    grid.checkForMatchDiagonal(Tile.this);
                    grid.checkForMatchBackDiagonal(Tile.this);
                    grid.checkForMatchVertical(Tile.this);
                    grid.currentColor = null;

                    //check horizontal tiles
                    if (grid.horizontal.size >= 3) {
                        Sort.instance().sort(grid.horizontal, horizontalComp);

                        boolean sendHere = false;
                        for (int i = 0; i < grid.horizontal.size - 1; i++) {
                            if (grid.distanceColumn(grid.horizontal.get(i), grid.horizontal.get(i + 1)) == 1) {
                                if (!matchingTiles.contains(grid.horizontal.get(i), false)) {
                                    matchingTiles.add(grid.horizontal.get(i));
                                }
                                if (!matchingTiles.contains(grid.horizontal.get(i + 1), false)) {
                                    matchingTiles.add(grid.horizontal.get(i + 1));
                                }
                                sendHere = true;
                                continue;
                            } else {
                                if (matchingTiles.size < 3) {
                                    matchingTiles.clear();
                                } else {
                                    break;
                                }
                            }
                            if (grid.distanceColumn(grid.horizontal.get(i), grid.horizontal.get(i + 1)) == 2) {
                                sendHere = true;
                            }
                        }
                        if (matchingTiles.size >= 3) {
                            sendHere = false;
                            if (grid.horizontal.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = -1;
                            } else if (grid.horizontal.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = -1;
                            } else if (grid.horizontal.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = -1;
                            } else if (grid.horizontal.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = -1;
                            }
                            gameStage.score += matchingTiles.size;
                            if (matchingTiles.size == 4) {
                                gameStage.score++;
                            }
                            if (matchingTiles.size == 5) {
                                gameStage.score += 5;
                            }
                            for (Tile t : matchingTiles) {
                                t.setDefaultSkin();
                            }
                        }
                        if (sendHere) {
                            if (grid.horizontal.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = grid.horizontal.get(0).i;
                            } else if (grid.horizontal.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = grid.horizontal.get(0).i;
                            } else if (grid.horizontal.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = grid.horizontal.get(0).i;
                            } else if (grid.horizontal.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = grid.horizontal.get(0).i;
                            }
                        }
                    }
                    matchingTiles.clear();
                    grid.horizontal.clear();
                    //end of horizontal **************

                    //check diagonal tiles
                    if (grid.diagonal.size >= 2) {
                        Sort.instance().sort(grid.diagonal, verticalComp);

                        int sendingRow = -1;
                        for (int i = 0; i < grid.diagonal.size - 1; i++) {
                            if (grid.distanceRow(grid.diagonal.get(i), grid.diagonal.get(i + 1)) == 1) {
                                if (!matchingTiles.contains(grid.diagonal.get(i), false)) {
                                    matchingTiles.add(grid.diagonal.get(i));
                                }
                                if (!matchingTiles.contains(grid.diagonal.get(i + 1), false)) {
                                    matchingTiles.add(grid.diagonal.get(i + 1));
                                }
                                sendingRow = grid.isTileInsideGrid(grid.diagonal.get(i).i - 1, grid.diagonal.get(i).j - 1) ? grid.diagonal.get(i).i - 1 :
                                        grid.isTileInsideGrid(grid.diagonal.get(i + 1).i + 1, grid.diagonal.get(i + 1).j + 1) ? grid.diagonal.get(i + 1).i + 1 : -1;
                                continue;
                            } else {
                                if (matchingTiles.size < 3) {
                                    matchingTiles.clear();
                                } else {
                                    break;
                                }
                            }
                            if (grid.distanceRow(grid.diagonal.get(i), grid.diagonal.get(i + 1)) == 2) {
                                sendingRow = grid.diagonal.get(i).i + 1;
                            }
                        }
                        if (matchingTiles.size >= 3) {
                            sendingRow = -1;
                            if (grid.diagonal.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = -1;
                            } else if (grid.diagonal.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = -1;
                            } else if (grid.diagonal.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = -1;
                            } else if (grid.diagonal.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = -1;
                            }
                            gameStage.score += matchingTiles.size;
                            if (matchingTiles.size == 4) {
                                gameStage.score++;
                            }
                            if (matchingTiles.size == 5) {
                                gameStage.score += 5;
                            }
                            for (Tile t : matchingTiles) {
                                t.setDefaultSkin();
                            }
                        }
                        if (sendingRow != -1) {
                            if (grid.diagonal.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = sendingRow;
                            } else if (grid.diagonal.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = sendingRow;
                            } else if (grid.diagonal.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = sendingRow;
                            } else if (grid.diagonal.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = sendingRow;
                            }
                        }
                    }
                    grid.diagonal.clear();
                    matchingTiles.clear();
                    //end diagonal *****************

                    //check vertical tiles
                    if (grid.vertical.size >= 2) {
                        Sort.instance().sort(grid.vertical, verticalComp);

                        int sendingRow = -1;
                        for (int i = 0; i < grid.vertical.size - 1; i++) {
                            if (grid.distanceRow(grid.vertical.get(i), grid.vertical.get(i + 1)) == 1) {
                                if (!matchingTiles.contains(grid.vertical.get(i), false)) {
                                    matchingTiles.add(grid.vertical.get(i));
                                }
                                if (!matchingTiles.contains(grid.vertical.get(i + 1), false)) {
                                    matchingTiles.add(grid.vertical.get(i + 1));
                                }
                                sendingRow = grid.isTileInsideGrid(grid.vertical.get(i).i - 1, grid.vertical.get(i).j) ? grid.vertical.get(i).i - 1 :
                                        grid.isTileInsideGrid(grid.vertical.get(i + 1).i + 1, grid.vertical.get(i + 1).j) ? grid.vertical.get(i + 1).i + 1 : -1;
                                continue;
                            } else {
                                if (matchingTiles.size < 3) {
                                    matchingTiles.clear();
                                } else {
                                    break;
                                }
                            }
                            if (grid.distanceRow(grid.vertical.get(i), grid.vertical.get(i + 1)) == 2) {
                                sendingRow = grid.vertical.get(i).i + 1;
                            }
                        }
                        if (matchingTiles.size >= 3) {
                            sendingRow = -1;
                            if (grid.vertical.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = -1;
                            } else if (grid.vertical.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = -1;
                            } else if (grid.vertical.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = -1;
                            } else if (grid.vertical.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = -1;
                            }
                            gameStage.score += matchingTiles.size;
                            if (matchingTiles.size == 4) {
                                gameStage.score++;
                            }
                            if (matchingTiles.size == 5) {
                                gameStage.score += 5;
                            }
                            for (Tile t : matchingTiles) {
                                t.setDefaultSkin();
                            }
                        }
                        if (sendingRow != -1) {
                            if (grid.vertical.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = sendingRow;
                            } else if (grid.vertical.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = sendingRow;
                            } else if (grid.vertical.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = sendingRow;
                            } else if (grid.vertical.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = sendingRow;
                            }
                        }
                    }
                    grid.vertical.clear();
                    matchingTiles.clear();
                    //end vertical *****************

                    //check back diagonal tiles
                    if (grid.backDiagonal.size >= 2) {
                        Sort.instance().sort(grid.backDiagonal, verticalComp);

                        int sendingRow = -1;
                        for (int i = 0; i < grid.backDiagonal.size - 1; i++) {
                            if (grid.distanceRow(grid.backDiagonal.get(i), grid.backDiagonal.get(i + 1)) == 1) {
                                if (!matchingTiles.contains(grid.backDiagonal.get(i), false)) {
                                    matchingTiles.add(grid.backDiagonal.get(i));
                                }
                                if (!matchingTiles.contains(grid.backDiagonal.get(i + 1), false)) {
                                    matchingTiles.add(grid.backDiagonal.get(i + 1));
                                }
                                sendingRow = grid.isTileInsideGrid(grid.backDiagonal.get(i).i - 1, grid.backDiagonal.get(i).j + 1) ? grid.backDiagonal.get(i).i - 1 :
                                        grid.isTileInsideGrid(grid.backDiagonal.get(i + 1).i + 1, grid.backDiagonal.get(i + 1).j - 1) ? grid.backDiagonal.get(i + 1).i + 1 : -1;
                                continue;
                            } else {
                                if (matchingTiles.size < 3) {
                                    matchingTiles.clear();
                                } else {
                                    break;
                                }
                            }
                            if (grid.distanceRow(grid.backDiagonal.get(i), grid.backDiagonal.get(i + 1)) == 2) {
                                sendingRow = grid.backDiagonal.get(i).i + 1;
                            }
                        }
                        if (matchingTiles.size >= 3) {
                            sendingRow = -1;
                            if (grid.backDiagonal.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = -1;
                            } else if (grid.backDiagonal.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = -1;
                            } else if (grid.backDiagonal.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = -1;
                            } else if (grid.backDiagonal.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = -1;
                            }
                            gameStage.score += matchingTiles.size;
                            if (matchingTiles.size == 4) {
                                gameStage.score++;
                            }
                            if (matchingTiles.size == 5) {
                                gameStage.score += 5;
                            }
                            for (Tile t : matchingTiles) {
                                t.setDefaultSkin();
                            }
                        }
                        if (sendingRow != -1) {
                            if (grid.backDiagonal.get(0).colorType == ColorType.RED) {
                                grid.nextRedRow = sendingRow;
                            } else if (grid.backDiagonal.get(0).colorType == ColorType.GREEN) {
                                grid.nextGreenRow = sendingRow;
                            } else if (grid.backDiagonal.get(0).colorType == ColorType.BLUE) {
                                grid.nextBlueRow = sendingRow;
                            } else if (grid.backDiagonal.get(0).colorType == ColorType.YELLOW) {
                                grid.nextYellowRow = sendingRow;
                            }
                        }
                    }
                    grid.backDiagonal.clear();
                    matchingTiles.clear();
                    //end back diagonal *****************
                }
                grid.markedDiagonal.clear();
                grid.markedBackDiagonal.clear();
                grid.markedHorizontal.clear();
                grid.markedVertical.clear();
                gameStage.scoreLabel.setText(gameStage.game.strings.addString("score: ").addString(gameStage.score).toString());
            }
        }

        @Override
        public void start(int trackIndex) {

        }

        @Override
        public void end(int trackIndex) {

        }
    };

    public Tile(GameStage stage, int x, int y) {
        super(stage);
        int skin = MathUtils.random(3);
        skinNumber = skin + "";
        skeletonActor.getSkeleton().setSkin(skinNumber);
        tileColor = Color.valueOf(randomColors[MathUtils.random(randomColors.length - 1)]);
        for (int i = 0; i < skeletonActor.getSkeleton().getSlots().size; i++) {
            if(skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color")) {
                skeletonActor.getSkeleton().getSlots().get(i).getColor().r = tileColor.r;
                skeletonActor.getSkeleton().getSlots().get(i).getColor().g = tileColor.g;
                skeletonActor.getSkeleton().getSlots().get(i).getColor().b = tileColor.b;
            }
        }
        this.i = x;
        this.j = y;

        skeletonActor.getSkeleton().getRootBone().setScale(gameStage.background.getWidth() / GameStage.COLUMN_LENGTH / Constants.TILE_WIDTH,
                gameStage.background.getHeight() / GameStage.ROW_LENGTH / Constants.TILE_WIDTH);


        setSize(gameStage.background.getWidth() / GameStage.COLUMN_LENGTH,
                gameStage.background.getHeight() / GameStage.ROW_LENGTH);
    }

    @Override
    protected void startDefaultAnimation() {
        skeletonActor.getAnimationState().setAnimation(0, "idle", true);
    }

    @Override
    protected void setResourcesPath() {
        path = "tile";
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = 1.0f;
    }

    public void color(final ColorType colorType) {
        if(colorType != ColorType.RAINBOW) {
            this.colorType = colorType;
            if(this.unicorn != null) {
                this.unicorn.colorType = colorType;
            }

            Color color = Color.valueOf(Strings.BLUE);//blue
            if (colorType == ColorType.GREEN) {
                color = Color.valueOf(Strings.GREEN);
            } else if (colorType == ColorType.YELLOW) {
                color = Color.valueOf(Strings.YELLOW);
            } else if (colorType == ColorType.RED) {
                color = Color.valueOf(Strings.RED);
            }

            for (int i = 0; i < skeletonActor.getSkeleton().getSlots().size; i++) {
                if(skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color-destroy")) {
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
                }
            }
            skeletonActor.getAnimationState().setAnimation(0, "color", false);
            skeletonActor.getAnimationState().removeListener(colorListener);
            skeletonActor.getAnimationState().addListener(colorListener);
        }
    }

    public void setDefaultSkin() {
        if(j != 0) {
            skeletonActor.getAnimationState().setAnimation(0, "color", false);

            if (unicorn != null) {
                unicorn.colorType = null;
            }
            colorType = null;
            skeletonActor.getSkeleton().setSkin(skinNumber);
            for (int i = 0; i < skeletonActor.getSkeleton().getSlots().size; i++) {
                if(skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color")) {
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().r = tileColor.r;
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().g = tileColor.g;
                    skeletonActor.getSkeleton().getSlots().get(i).getColor().b = tileColor.b;
                }
            }
        }
    }
}
