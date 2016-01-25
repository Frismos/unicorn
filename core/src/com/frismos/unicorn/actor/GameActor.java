package com.frismos.unicorn.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.frismos.unicorn.userdata.UserData;
import com.frismos.unicorn.enums.ColorType;
import com.frismos.unicorn.spine.SpineActor;
import com.frismos.unicorn.stage.GameStage;
import com.frismos.unicorn.util.Constants;

public abstract class GameActor extends SpineActor {

    protected UserData userData;
    public ColorType colorType;

    public Polygon bounds;

    public GameActor(GameStage stage, ColorType colorType) {
        this(stage, null, colorType);
    }


//    ShapeRenderer shapeRenderer = new ShapeRenderer();
    public GameActor(GameStage stage, UserData userData, ColorType colorType) {
        super(stage);
        this.userData = userData;
        this.userData.actor = this;
        this.colorType = colorType;
        this.userData.colorType = this.colorType;
        this.debug();
        if(this instanceof Spell) {
            setSize(10 / 4.0f, 15 / 4.0f);
        }
//        this.shapeRenderer.setAutoShapeType(true);
        this.bounds = new Polygon(new float[] {
                getX(), getY(),
                getX(), getY() + getHeight(),
                getX() + getWidth(), getY() + getHeight(),
                getX() + getWidth(), getY()
        });
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        this.bounds.setPosition(getX(), getY());
//        this.bounds.getVertices()[0] = getX();
//        this.bounds.getVertices()[1] = getY();
//        this.bounds.getVertices()[2] = getX();
//        this.bounds.getVertices()[3] = getY() + getHeight();
//        this.bounds.getVertices()[4] = getX() + getWidth();
//        this.bounds.getVertices()[5] = getY() + getHeight();
//        this.bounds.getVertices()[6] = getX() + getWidth();
//        this.bounds.getVertices()[7] = getY();
    }

    @Override
    protected void rotationChanged() {
        super.rotationChanged();
        this.bounds.setRotation(getRotation());
    }

    @Override
    public boolean remove() {
        if(!this.hasParent()) {
            return false;
        }
        gameStage.collisionDetector.collisionListeners.removeValue(this, false);
        return super.remove();
    }

    public abstract UserData getUserData();

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        shapeRenderer.begin();
//        shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getOriginX(), bounds.getOriginY(), bounds.getBoundingRectangle().getWidth(), bounds.getBoundingRectangle().getHeight(), 1, 1, bounds.getRotation());
//        shapeRenderer.end();
    }
}