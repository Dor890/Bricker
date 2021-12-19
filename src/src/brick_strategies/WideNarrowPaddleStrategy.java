package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.WideNarrowPaddle;

import java.util.Random;

/**
 * Class responsible for the widing and narrowing status strategy.
 * @author Dor Messica.
 */
public class WideNarrowPaddleStrategy extends RemoveBrickStrategyDecorator {
    private static final float STATUS_SPEED = 200;
    private final ImageRenderable widePaddleImage;
    private final ImageRenderable narrowPaddleImage;
    private final Vector2 windowDimensions;
    private final Random rand;

    /**
     * Constructor for the wide paddle strategy.
     * @param toBeDecorated Collision strategy object to be decorated.
     */
    WideNarrowPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, Vector2 statusSize,
                             Vector2 windowDimensions) {
        super(toBeDecorated);
        this.widePaddleImage = imageReader.readImage("assets/buffWiden.png", true);
        this.narrowPaddleImage = imageReader.readImage(
                "assets/buffNarrow.png", true);
        this.windowDimensions = windowDimensions;
        rand = new Random();
    }

    /**
     *
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter Global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        toBeDecorated.onCollision(thisObj, otherObj, bricksCounter);
        GameObject givenStatus;
        if(rand.nextBoolean()) {
            givenStatus = new WideNarrowPaddle(thisObj.getTopLeftCorner(), thisObj.getDimensions(),
                    narrowPaddleImage, getGameObjectCollection(), false, windowDimensions);
        }
        else {
            givenStatus = new WideNarrowPaddle(thisObj.getTopLeftCorner(), thisObj.getDimensions(),
                    widePaddleImage, getGameObjectCollection(), true, windowDimensions);
        }
        givenStatus.setVelocity(new Vector2(0, STATUS_SPEED));
        getGameObjectCollection().addGameObject(givenStatus);
    }
}
