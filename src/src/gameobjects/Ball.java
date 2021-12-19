package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class represents a ball in the game.
 * @author Dor Messica.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCount = 0;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param collisionSound Sound object of the ball collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.setTag("mainBall");
    }

    /**
     * On collision, object velocity is reflected about the normal vector of the surface it collides with.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionCount++;
    }

    /**
     * Ball object maintains a counter which keeps count of collisions from start of game. This getter
     * method allows access to the collision count in case any behavior should need to be based
     * on number of ball collisions.
     * @return Number of times ball collided with an object since start of game.
     */
    public int getCollisionCount() {
        return collisionCount;
    }

}
