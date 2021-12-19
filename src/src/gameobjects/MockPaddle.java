package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class responsible for the mock paddle in the game.
 * @author Dor Messica.
 */
public class MockPaddle extends Paddle {
    public static boolean isInstantiated = false;

    private final int numCollisionsToDisappear;
    private final GameObjectCollection gameObjectCollection;
    private int collisionsLeft;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     *                         the GameObject will not be rendered.
     * @param inputListener    UserInputListener instance.
     * @param windowDimensions Dimensions of game window.
     * @param gameObjectCollection Global game object collection.
     * @param minDistanceFromEdge Border for paddle movement.
     * @param numCollisionsToDisappear Number of collision has until the mock paddle disappears.
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      UserInputListener inputListener, Vector2 windowDimensions,
                      GameObjectCollection gameObjectCollection,
                      int minDistanceFromEdge, int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistanceFromEdge);
        this.numCollisionsToDisappear = numCollisionsToDisappear;
        this.gameObjectCollection = gameObjectCollection;
        this.collisionsLeft = numCollisionsToDisappear;
    }

    /**
     * Called on the first frame of a collision.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionsLeft--;
        if(collisionsLeft <= 0) {
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
            collisionsLeft = numCollisionsToDisappear;
        }
    }
}
