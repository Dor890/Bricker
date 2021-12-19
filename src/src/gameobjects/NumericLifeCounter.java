package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Display a graphic object on the game window showing a numeric count of lives left.
 * @author Dor Messica
 */
public class NumericLifeCounter extends GameObject {
    private final TextRenderable numericPic;
    private final Counter livesCounter;
    private int livesSaver;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param gameObjects   Global game object collection.
     * @param livesCounter  Global lives counter of game.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.livesSaver = livesCounter.value();
        numericPic = new TextRenderable(String.valueOf(livesCounter.value()));
        numericPic.setColor(Color.WHITE);
        GameObject numericCounterObject = new GameObject(topLeftCorner, dimensions, numericPic);
        gameObjects.addGameObject(numericCounterObject, Layer.BACKGROUND);
    }

    /**
     * Should be called once per frame.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(livesCounter.value() < livesSaver) {
            livesSaver -= 1;
            numericPic.setString(String.valueOf(livesCounter.value()));
        }
    }
}


