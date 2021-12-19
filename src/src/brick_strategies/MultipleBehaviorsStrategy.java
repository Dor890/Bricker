package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * Class responsible for a strategy of multiple behaviors.
 * @author Dor Messica.
 */
public class MultipleBehaviorsStrategy implements CollisionStrategy {
    private final CollisionStrategy firstStrategy;
    private final CollisionStrategy secondStrategy;

    /**
     * Constructor for multiple behaviors strategy.
     * @param firstStrategy Random collisionStrategy object.
     * @param secondStrategy Random collisionStrategy object.
     */
    public MultipleBehaviorsStrategy(CollisionStrategy firstStrategy, CollisionStrategy secondStrategy) {
        this.firstStrategy = firstStrategy;
        this.secondStrategy = secondStrategy;
    }

    /**
     * Removes brick from game object collection on collision.
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter Global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        firstStrategy.onCollision(thisObj, otherObj, bricksCounter);
        secondStrategy.onCollision(thisObj, otherObj, bricksCounter);
        bricksCounter.increaseBy(1);
    }

    /**
     * All collision strategy objects should hold a reference to the global game object collection
     * and be able to return it.
     * @return global game object collection whose reference is held in object.
     */
    @Override
    public GameObjectCollection getGameObjectCollection() {
        return firstStrategy.getGameObjectCollection();
    }
}
