package src.gameobjects;

import danogl.util.Counter;
import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class represents a brick in the game.
 * @author Dor Messica.
 */
public class Brick extends GameObject {
    private final CollisionStrategy breakStrategy;
    private final Counter bricksCounter;
    private boolean isBroken = false;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param breakStrategy Responsible for acting on a collision according to a wanted strategy.
     * @param bricksCounter Counter for current number of bricks in the game.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy breakStrategy, Counter bricksCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.breakStrategy = breakStrategy;
        this.bricksCounter = bricksCounter;
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
        if(!isBroken) {
            super.onCollisionEnter(other, collision);
            breakStrategy.onCollision(this, other, bricksCounter);
            isBroken = true;
        }
    }
}
