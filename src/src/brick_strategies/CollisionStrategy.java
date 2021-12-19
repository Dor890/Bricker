package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * General type for brick strategies, part of decorator pattern implementation.
 * All brick strategies implement this interface.
 */
public interface CollisionStrategy {
    /**
     * To be called on brick collision.
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter global brick counter.
     */
    void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter);

    /**
     * All collision strategy objects should hold a reference to the global game object collection
     * and be able to return it.
     * @return global game object collection whose reference is held in object.
     */
    GameObjectCollection getGameObjectCollection();
}
