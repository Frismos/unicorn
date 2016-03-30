package com.frismos.unicorn.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Slot;
import com.frismos.unicorn.actor.Bullet;
import com.frismos.unicorn.actor.ColorsPlatform;
import com.frismos.unicorn.actor.GameActor;
import com.frismos.unicorn.enums.ColorType;

/**
 * Created by edgaravanyan on 10/12/15.
 */
public class Utils {

    public static Array<Color> colors = new Array<>();
    static {
        colors.add(Color.valueOf(Strings.BLUE));
        colors.add(Color.valueOf(Strings.RED));
        colors.add(Color.valueOf(Strings.GREEN));
    }

    public static void setActorColorType(GameActor actor, ColorType colorType) {
        actor.colorType = colorType;

        Color color = Color.valueOf(Strings.BLUE);//blue
        if (colorType == ColorType.GREEN) {
            color = Color.valueOf(Strings.GREEN);
        } else if (colorType == ColorType.RED) {
            color = Color.valueOf(Strings.RED);
        } else if (colorType == ColorType.DEBUG) {
            color = Color.GOLDENROD;
        }
        actor.setColor(color);
        for (int i = 0; i < actor.skeletonActor.getSkeleton().getSlots().size; i++) {
            if(colorType == ColorType.RAINBOW) {
                color = colors.get(MathUtils.random(colors.size - 1));
            }
            if(actor.skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color") && !actor.skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("mini")) {
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
            }
        }
    }

    public static void colorActor(GameActor actor, Color color) {
        for (int i = 0; i < actor.skeletonActor.getSkeleton().getSlots().size; i++) {
            if(actor.skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color")) {
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
            }
        }
    }

    public static boolean intersects(GameActor one, GameActor another) {
        return Intersector.overlapConvexPolygons(one.bounds, another.bounds);
    }

    public static float areaOfTriangle(float ax, float ay, float bx, float by, float cx, float cy) {
        float area = Math.abs(ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)) / 2;
        return area;
    }

    public static void colorPlatform(ColorsPlatform actor) {
        Color color = Color.valueOf(Strings.BLUE);
        for (int i = 0; i < actor.skeletonActor.getSkeleton().getSlots().size; i++) {
            if(actor.skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color2")) {
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
            }
        }

        color = Color.valueOf(Strings.GREEN);
        for (int i = 0; i < actor.skeletonActor.getSkeleton().getSlots().size; i++) {
            if(actor.skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color1")) {
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
            }
        }

        color = Color.valueOf(Strings.RED);
        for (int i = 0; i < actor.skeletonActor.getSkeleton().getSlots().size; i++) {
            if(actor.skeletonActor.getSkeleton().getSlots().get(i).getData().getName().contains("color0")) {
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().r = color.r;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().g = color.g;
                actor.skeletonActor.getSkeleton().getSlots().get(i).getColor().b = color.b;
            }
        }
    }

    public static void colorSlot(Slot slot, ColorType colorType) {
        Color color = Color.valueOf(Strings.BLUE);//blue
        if (colorType == ColorType.GREEN) {
            color = Color.valueOf(Strings.GREEN);
        } else if (colorType == ColorType.RED) {
            color = Color.valueOf(Strings.RED);
        } else if (colorType == ColorType.DEBUG) {
            color = Color.GOLDENROD;
        }
        slot.getColor().r = color.r;
        slot.getColor().g = color.g;
        slot.getColor().b = color.b;

    }
}
