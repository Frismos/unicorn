package com.frismos.unicorn.actor;

import com.badlogic.gdx.utils.Pool;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;

public abstract class GameActor extends SpineActor implements Pool.Poolable {
    public ColorType colorType;

//    ShapeRenderer shapeRenderer = new ShapeRenderer();
    public GameActor(GameStage stage, ColorType colorType) {
        super(stage);
        this.colorType = colorType;
//        this.debug();
//        this.shapeRenderer.setAutoShapeType(true);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        if(bounds.getY() != getY() && !(this instanceof Bullet)) {
            gameStage.layerStage(null);
        }
        this.bounds.setPosition(getX(), getY());
    }

    @Override
    protected void rotationChanged() {
        super.rotationChanged();
        this.bounds.setRotation(getRotation());
    }

    @Override
    public boolean remove(boolean dispose) {
        gameStage.collisionDetector.removeListenerActor(this);

        gameStage.actors.removeValue(this, false);
        gameStage.game.aiManager.unFocus(this);
        return super.remove(dispose);
    }

    @Override
    public void reset() {

    }
}