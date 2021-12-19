package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Changes camera focus from ground to ball until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 * @author Dor Messica.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {
    private static final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private final GameManager gameManager;
    private final WindowController windowController;
    private BallCollisionCountdownAgent countDownAgent;

    /**
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                                     concerning the window.
     * @param gameManager The brick game manager object.
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.gameManager = gameManager;
        this.windowController = windowController;
    }

    /**
     * Change camere position on collision and delegate to held CollisionStrategy.
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter Global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        toBeDecorated.onCollision(thisObj, otherObj, bricksCounter);
        if(gameManager.getCamera() == null && otherObj.getTag().equals("mainBall")) {
            gameManager.setCamera(new Camera(otherObj, Vector2.ZERO,
                    windowController.getWindowDimensions().mult(1.2f),
                    windowController.getWindowDimensions()));
            countDownAgent = new BallCollisionCountdownAgent((Ball)otherObj,
                    this, NUM_BALL_COLLISIONS_TO_TURN_OFF);
            getGameObjectCollection().addGameObject(countDownAgent, Layer.BACKGROUND);
        }
    }

    /**
     * Return camera to normal ground position.
     */
    public void turnOffCameraChange() {
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(countDownAgent, Layer.BACKGROUND);
    }
}
