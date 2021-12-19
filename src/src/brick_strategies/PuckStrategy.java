package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces several pucks instead of brick once removed.
 * @author Dor Messica
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator {
    private static final float NUM_OF_MOCK_BALLS = 3;
    private final Renderable ballImage;
    private final Sound collisionSound;

    /**
     * Constructor for a puck strategy.
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                       See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                       disk. See its documentation for help.
     */
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader) {
        super(toBeDecorated);
        this.ballImage = imageReader.readImage("assets/Mockball.png", true);
        this.collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
    }

    /**
     * Removes brick from game object collection on collision.
     * @param thisObj The current used object of the collision.
     * @param otherObj The other object in the collision.
     * @param bricksCounter Global brick counter.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter bricksCounter) {
        toBeDecorated.onCollision(thisObj, otherObj, bricksCounter);
        for(int i = 1; i <= NUM_OF_MOCK_BALLS; i++) {
            GameObject mockBall = new Puck(
                    new Vector2(thisObj.getTopLeftCorner().x(), thisObj.getTopLeftCorner().y()),
                    new Vector2(otherObj.getDimensions()), ballImage, collisionSound);
            float ballVelY = otherObj.getVelocity().y(), ballVelX = otherObj.getVelocity().x();
            Random rand = new Random();
            if(rand.nextBoolean()) {
                ballVelX *= -1+0.1*i;
            }
            if(rand.nextBoolean()) {
                ballVelY *= -1+0.1*i;
            }
            mockBall.setVelocity(new Vector2(ballVelX, ballVelY));
            mockBall.setCenter(thisObj.getCenter());
            getGameObjectCollection().addGameObject(mockBall);
        }
    }
}
