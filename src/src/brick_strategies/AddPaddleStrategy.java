package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator. Introduces extra paddle to game
 * window which remains until colliding NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with other game objects.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator {
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 10;
    private static final int NUM_OF_COLLISIONS = 3;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_WIDTH = 100;
    private final MockPaddle mockPaddle;

    /**
     * Constructor for adding a paddle strategy.
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                             See its documentation for help.
     * @param inputListener UserInputListener instance.
     * @param windowDimensions Width and height in window coordinates.
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                              UserInputListener inputListener, Vector2 windowDimensions) {
        super(toBeDecorated);
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        mockPaddle = new MockPaddle(new Vector2(windowDimensions.x()/2, windowDimensions.y()/2),
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, inputListener, windowDimensions,
                getGameObjectCollection(), MIN_DISTANCE_FROM_SCREEN_EDGE, NUM_OF_COLLISIONS);
    }

    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        toBeDecorated.onCollision(thisObj, otherObj, bricksCounter);
        if(!MockPaddle.isInstantiated) {
            getGameObjectCollection().addGameObject(mockPaddle);
            MockPaddle.isInstantiated = true;
        }
    }
}
