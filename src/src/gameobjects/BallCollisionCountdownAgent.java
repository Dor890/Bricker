package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * An object of this class is instantiated on collision of ball with a brick with a change camera strategy.
 * It checks ball's collision counter every frame, and once the it finds the ball has collided countDownValue
 * times since instantiation, it calls the strategy to reset the camera to normal.
 * @author Dor Messica
 */
public class BallCollisionCountdownAgent extends GameObject {
    private final int countDownValue;
    private final int initCounter;
    private final Ball ball;
    private final ChangeCameraStrategy owner;

    /**
     * Constructor of the ball collision countdown agent.
     * @param ball Ball object whose collisions are to be counted.
     * @param owner Object asking for countdown notification.
     * @param countDownValue Number of ball collisions. Notify caller object that the ball collided
     *                         countDownValue times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.countDownValue = countDownValue;
        this.owner = owner;
        this.ball = ball;
        initCounter = ball.getCollisionCount();
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
        if(ball.getCollisionCount() >= initCounter + countDownValue) {
            owner.turnOffCameraChange();
        }
    }
}
