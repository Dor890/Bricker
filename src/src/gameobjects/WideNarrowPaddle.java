package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class responsible for a status definer in type of widening.
 * @author Dor Messica.
 */
public class WideNarrowPaddle extends GameObject {
    private final GameObjectCollection gameObjectCollection;
    private final boolean isWide;
    private final Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public WideNarrowPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                      GameObjectCollection gameObjectCollection, boolean isWide, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
        this.isWide = isWide;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Should this object be allowed to collide the the specified other object.
     * If both this object returns true for the other, and the other returns true
     * for this one, the collisions may occur when they overlap, meaning that their
     * respective onCollisionEnter/onCollisionStay/onCollisionExit will be called.
     * Note that this assumes that both objects have been added to the same
     * GameObjectCollection, and that its handleCollisions() method is invoked.
     * @param other The other GameObject.
     * @return true if the objects should collide. This does not guarantee a collision
     * would actually collide if they overlap, since the other object has to confirm
     * this one as well.
     */
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("Disc");
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
        if(other.getDimensions().x() > windowDimensions.x()) {
            other.setDimensions(new Vector2(windowDimensions.x(), other.getDimensions().y()));
        }
        else if(isWide) {
            other.setDimensions(other.getDimensions().mult(1.5f));
        }
        else {
            other.setDimensions(other.getDimensions().mult(0.5f));
        }
        gameObjectCollection.removeGameObject(this);
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
        if(this.getTopLeftCorner().y() > windowDimensions.y()) {
            gameObjectCollection.removeGameObject(this);
        }
    }
}
