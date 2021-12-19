package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Abstract decorator to add functionality to the remove brick strategy, following the decorator pattern.
 * All strategy decorators should inherit from this class.
 * @author Dor Messica
 */
public abstract class RemoveBrickStrategyDecorator implements CollisionStrategy {
    CollisionStrategy toBeDecorated;
    /**
     * Constructor for the decorator.
     * @param toBeDecorated Collision strategy object to be decorated.
     */
    RemoveBrickStrategyDecorator(CollisionStrategy toBeDecorated) {
        this.toBeDecorated = toBeDecorated;
    }

    /**
     * Should delegate to held Collision strategy object.
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter Global brick counter.
     */
    @Override
    public abstract void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter);

    /**
     * Return held reference to global game object. Delegate to held object to be decorated.
     * @return global game object collection whose reference is held in object.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return toBeDecorated.getGameObjectCollection();
    }
}
