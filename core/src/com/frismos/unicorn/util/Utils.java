package com.frismos.unicorn.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.enums.ColorType;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class Utils {

    public static Array<Color> colors = new Array<Color>();
    static {
        colors.add(Color.valueOf(Strings.BLUE));
        colors.add(Color.valueOf(Strings.RED));
        colors.add(Color.valueOf(Strings.GREEN));
        colors.add(Color.valueOf(Strings.YELLOW));
    }

    public static BoundingBox calculateBodyBoundingBox(Body body) {
        Fixture fixture = body.getFixtureList().get(0);
        PolygonShape shape = (PolygonShape) fixture.getShape();

        Vector2 tmp = new Vector2();
        shape.getVertex(0, tmp);
        tmp = fixture.getBody().getWorldPoint(tmp);
        BoundingBox boundingBox = new BoundingBox(new Vector3(tmp, 0), new Vector3(tmp, 0));
        for (int i = 1; i < shape.getVertexCount(); i++) {
            shape.getVertex(i, tmp);
            boundingBox.ext(new Vector3(fixture.getBody().getWorldPoint(tmp), 0));
        }

        return boundingBox;
    }

    public static void setActorColorType(GameActor actor, ColorType colorType) {
        actor.colorType = colorType;
//        actor.getUserData().colorType = colorType;

        Color color = Color.valueOf(Strings.BLUE);//blue
        if (colorType == ColorType.GREEN) {
            color = Color.valueOf(Strings.GREEN);
        } else if (colorType == ColorType.YELLOW) {
            color = Color.valueOf(Strings.YELLOW);
        } else if (colorType == ColorType.RED) {
            color = Color.valueOf(Strings.RED);
        }
        for (int i = 0; i < actor.skeleton.getSlots().size; i++) {
            if(colorType == ColorType.RAINBOW) {
                color = colors.get(MathUtils.random(colors.size - 1));
            }
            if(actor.skeleton.getSlots().get(i).getData().getName().contains("color") && !actor.skeleton.getSlots().get(i).getData().getName().contains("mini")) {
                actor.skeleton.getSlots().get(i).getColor().r = color.r;
                actor.skeleton.getSlots().get(i).getColor().g = color.g;
                actor.skeleton.getSlots().get(i).getColor().b = color.b;
            }
        }
    }

    public static void colorActor(GameActor actor, Color color) {
        for (int i = 0; i < actor.skeleton.getSlots().size; i++) {
            if(actor.skeleton.getSlots().get(i).getData().getName().contains("color")) {
                actor.skeleton.getSlots().get(i).getColor().r = color.r;
                actor.skeleton.getSlots().get(i).getColor().g = color.g;
                actor.skeleton.getSlots().get(i).getColor().b = color.b;
            }
        }
    }

    public static boolean intersects(GameActor one, GameActor another) {
        return Intersector.overlapConvexPolygons(one.bounds, another.bounds);
    }
//        float angleOne = one.getRotation() >= 0 ? one.getRotation() + 45 : one.getRotation() - 45;
//        float angleAnother = another.getRotation() >= 0 ? another.getRotation() + 45 : another.getRotation() - 45;
//
//        float cosOne = MathUtils.cosDeg(angleOne) * (float)Math.sqrt(Math.pow(one.getWidth() * one.getScaleX(), 2) + Math.pow(one.getHeight() * one.getScaleY(), 2)) / 2;
//        float sinOne = MathUtils.sinDeg(angleOne) * (float)Math.sqrt(Math.pow(one.getWidth() * one.getScaleX(), 2) + Math.pow(one.getHeight() * one.getScaleY(), 2)) / 2;
//        if(one.getRotation() < 0) {
//            sinOne = -sinOne;
//        }
//        float cosAnother = MathUtils.cosDeg(angleAnother) * (float)Math.sqrt(Math.pow(another.getWidth() * another.getScaleX(), 2) + Math.pow(another.getHeight() * another.getScaleY(), 2)) / 2;
//        float sinAnother = MathUtils.sinDeg(angleAnother) * (float)Math.sqrt(Math.pow(another.getWidth() * another.getScaleX(), 2) + Math.pow(another.getHeight() * another.getScaleY(), 2)) / 2;
//        if(another.getRotation() < 0) {
//            sinAnother = -sinAnother;
//        }
//
////        float area1 = areaOfTriangle(one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleX() * MathUtils.sinDeg(one.getRotation()),
////                another.getX(), another.getY(),
////                another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleX() * MathUtils.sinDeg(another.getRotation()));
////        float area2 = areaOfTriangle(one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleX() * MathUtils.sinDeg(one.getRotation()),
////                another.getX() + cosAnother, another.getY() + sinAnother,
////                another.getX() - another.getHeight() * another.getScaleY() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getHeight() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()));
////        float area3 = areaOfTriangle(one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleX() * MathUtils.sinDeg(one.getRotation()),
////                another.getX() - another.getHeight() * another.getScaleY() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getHeight() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()),
////                another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()));
////        float area4 = areaOfTriangle(one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleX() * MathUtils.sinDeg(one.getRotation()),
////                another.getX() + cosAnother, another.getY() + sinAnother,
////                another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()));
////
////        if(area1 + area2 + area3 + area4 > (another.getWidth() * another.getHeight() + 0.5f)) {
////            return false;
////        }
////
////        area1 = areaOfTriangle(another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()),
////                one.getX(), one.getY(),
////                one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleY() * MathUtils.sinDeg(one.getRotation()));
////        area2 = areaOfTriangle(another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()),
////                one.getX() + cosOne, one.getY() + sinOne,
////                one.getX() - one.getHeight() * one.getScaleY() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getHeight() * one.getScaleY() * MathUtils.sinDeg(one.getRotation()));
////        area3 = areaOfTriangle(another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()),
////                one.getX() - one.getHeight() * one.getScaleY() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getHeight() * one.getScaleY() * MathUtils.sinDeg(one.getRotation()),
////                one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleY() * MathUtils.sinDeg(one.getRotation()));
////        area4 = areaOfTriangle(another.getX() + another.getWidth() * another.getScaleX() * MathUtils.cosDeg(another.getRotation()), another.getY() + another.getWidth() * another.getScaleY() * MathUtils.sinDeg(another.getRotation()),
////                one.getX() + cosOne, one.getY() + sinOne,
////                one.getX() + one.getWidth() * one.getScaleX() * MathUtils.cosDeg(one.getRotation()), one.getY() + one.getWidth() * one.getScaleY() * MathUtils.sinDeg(one.getRotation()));
////
////        if(area1 + area2 + area3 + area4 > one.getWidth() * one.getHeight() + 0.5f) {
////            return false;
////        }
////        return true;
//
//        if(one.getX() + cosOne >= another.getX() &&
//                one.getX() + cosOne <= another.getX() + another.getWidth() * another.getScaleX() &&
//                one.getY() + sinOne >= another.getY() &&
//                one.getY() + sinOne <= another.getY() + another.getHeight() * another.getScaleY()) {
//            return true;
//        }
//        if(another.getX() + cosAnother >= one.getX() &&
//                another.getX() + cosAnother <= one.getX() + one.getWidth() * one.getScaleX() &&
//                another.getY() + sinAnother >= one.getY() &&
//                another.getY() + sinAnother <= one.getY() + one.getHeight() * one.getScaleY()) {
//            return true;
//        }
//        return false;
//    }

    public static float areaOfTriangle(float ax, float ay, float bx, float by, float cx, float cy) {
        float area = Math.abs(ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) / 2;
        return area;
    }
}
