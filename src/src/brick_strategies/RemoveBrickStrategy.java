package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Concrete brick strategy implementing CollisionStrategy interface.
 * Removes holding brick on collision.
 * @author Dor Messica
 */
public class RemoveBrickStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjects;

    /**
     * Constructor for a brick remover strategy.
     * @param gameObjectCollection Global game object collection.
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjects = gameObjectCollection;
    }

    /**
     * Removes brick from game object collection on collision.
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter Global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
        bricksCounter.decrement();
    }

    /**
     * All collision strategy objects should hold a reference to the global game object collection
     * and be able to return it.
     * @return global game object collection whose reference is held in object.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return gameObjects;
    }
}
