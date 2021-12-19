package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Display a graphic object on the game window showing as many widgets as lives left.
 * @author Dor Messica
 */
public class GraphicLifeCounter extends GameObject {
    private final GameObjectCollection gameObjects;
    GameObject[] heartsList;
    private static final int HEART_SIZE = 20;
    private int livesSaver;
    private final Counter livesCounter;
    /**
     * Construct a new GameObject instance.
     *
     * @param widgetTopLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param widgetDimensions    Width and height in window coordinates.
     * @param widgetRenderable    The renderable representing the object. Can be null, in which case
     * @param livesCounter  Global lives counter of game.
     * @param gameObjects   Global game object collection.
     * @param numOfLives    Global setting of number of lives a player will have in a game.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions,
                              Counter livesCounter, Renderable widgetRenderable,
                              GameObjectCollection gameObjects, int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.heartsList = new GameObject[numOfLives];
        this.gameObjects = gameObjects;
        this.livesCounter = livesCounter;
        for (int i = 0; i < livesCounter.value(); i++) {
            GameObject heart = new GameObject(new Vector2(widgetTopLeftCorner.x() + i * HEART_SIZE,
                    widgetTopLeftCorner.y()), widgetDimensions, widgetRenderable);
            this.gameObjects.addGameObject(heart, Layer.BACKGROUND);
            heartsList[i] = heart;
        }
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
        if (livesCounter.value() < livesSaver) {
            livesSaver -= 1;
            gameObjects.removeGameObject(heartsList[livesCounter.value()], Layer.BACKGROUND);
        }
    }
}
