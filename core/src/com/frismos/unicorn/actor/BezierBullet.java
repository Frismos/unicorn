package com.frismos.unicorn.actor;

import com.badlogic.gdx.math.Vector2;
import com.frismos.unicorn.enums.ActorDataType;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.enums.UnicornType;
import com.frismos.unicorn.grid.Tile;
import com.frismos.unicorn.stage.GameStage;

import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.util.Constants;
import com.frismos.unicorn.util.Debug;
import com.frismos.unicorn.util.Strings;

/**
 * Created by edgar on 12/16/2015.
 */
public class BezierBullet extends Bullet {

//    public static final int SEGMENTS = 100;
    private float index = 0;
    public float t = 0.5f;
    public Vector2 p0;
    public Vector2 p1;
    public Vector2 p2;
    public Vector2 p3;

    private Vector2 p = new Vector2();
    private Tile tile;

    private Array<Enemy> enemiesToHit = new Array<>();

    public BezierBullet(GameStage stage, float x, float y) {
        super(stage, x, y, ActorDataType.CANNON_BULLET);
    }

    public BezierBullet(GameStage stage) {
        super(stage, 0, ActorDataType.CANNON_BULLET);
    }

    @Override
    public void act(float delta) {
        if (isFiring && getX() >= destPoint.x) {
            explode();
        }
        if(enemiesToHit.size > 0) {
            for (Enemy e : enemiesToHit) {
                e.hit(damage);
            }
            enemiesToHit.clear();
        }
        super.act(delta);
    }

    private void explode() {
        tile = gameStage.grid.getTileByPoint(getX() + getWidth() / 2, getY());
        this.destroy();
        if(getTile() != null) {
            Array<Tile> neighbours = gameStage.grid.getNeighbourTiles(getTile());
            for (int i = 0; i < neighbours.size; i++) {
                for (int j = 0; j < neighbours.get(i).enemies.size; j++) {
                    if(!(neighbours.get(i).enemies.get(j) instanceof AttackingEnemy) && neighbours.get(i).enemies.get(j) instanceof Boss || (this.colorType == neighbours.get(i).enemies.get(j).colorType || this.colorType == ColorType.RAINBOW) && neighbours.get(i).enemies.get(j).isAttacking()) {
                        enemiesToHit.add(neighbours.get(i).enemies.get(j));
                    }
                }
            }
        }
        if(!gameStage.isWorldShaking) {
            gameStage.shakeWorld(2);
        }
    }

    @Override
    public void move(float delta) {
        t = index / (Math.abs(p3.x - p0.x) + 75) * 2;
        index += 60 * delta;
        Vector2 p = calculateBezierPoint();
        setPosition(p.x, p.y);
    }

    public Vector2 calculateBezierPoint() {
        float u = 1 - t;
        float uu = u*u;
        float uuu = u*u*u;
        float tt = t*t;
        float ttt = t*t*t;

        p.x = uuu * p0.x + 3 * uu * t * p1.x + 3 * u * tt * p2.x + ttt * p3.x;
        p.y = uuu * p0.y + 3 * uu * t * p1.y + 3 * u * tt * p2.y + ttt * p3.y;

        return p;
    }

    public Tile getTile() {
        return tile;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    @Override
    public float calculateAngle() {
        p0 = new Vector2(getX(), getY());
        p3 = new Vector2(destPoint.x, destPoint.y);
        p1 = new Vector2((destPoint.x - getX()) / 2 + getX(), getY() + 10);
        p2 = new Vector2(destPoint.x - (destPoint.x - getX()) / 2, getY() + 10);

        return super.calculateAngle();
    }

    @Override
    protected void setResourcesPath() {
        path = Strings.BOMB;
    }

    @Override
    protected void setScaleRatio() {
        scaleRatio = Constants.BEZIER_BULLET_SCALE_RATIO;
    }

    @Override
    public void resetPosition() {
        index = 0;
        super.resetPosition();
    }

    @Override
    public void setColorType(ColorType colorType) {
        if(((Rhino)gameStage.unicorns.get(UnicornType.RHINO)).isAbilityMode) {
            colorType = ColorType.getRandomColor();
        }
        super.setColorType(colorType);
    }
}
