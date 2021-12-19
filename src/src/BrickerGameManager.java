package src;

import src.brick_strategies.BrickStrategyFactory;
import src.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.brick_strategies.RemoveBrickStrategy;
import src.gameobjects.*;

import java.awt.*;
import java.util.Random;

/**
 * Class responsible for managing the "Bricker" game.
 * @author Dor Messica.
 */
public class BrickerGameManager extends GameManager {
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_WIDTH = 700;
    private static final int BORDER_WIDTH = 10;
    private static final int FRAME_RATE = 80;
    private static final int BRICKS_HEIGHT = 15;
    private static final int BRICK_LINES = 5;
    private static final int BRICKS_PER_LINE = 8;
    private static final int NUMERIC_HEIGHT = 50;
    private static final int MIN_DISTANCE_FROM_SCREEN_EDGE = 10;
    private static final int HEART_XPOS = BORDER_WIDTH+5;
    private static final float BALL_SPEED = 200;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_WIDTH = 100;
    private static final float BALL_RADIUS = 20;
    private final int numOfLives = 4;
    private GameObject ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter bricksCounter;
    private Counter livesLeft = new Counter(numOfLives);
    /**
     * Constructs new BrickerGameManager object.
     * Creates a new window with the specified title and of the specified dimensions.
     * @param windowTitle can be null to indicate the usage of the default window title.
     * @param windowDimensions dimensions in pixels. can be null to indicate a
     *                             full-screen window whose size in pixels is the main screen's resolution.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     * @see ImageReader
     * @see SoundReader
     * @see UserInputListener
     * @see WindowController
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        this.windowController = windowController;
        // Initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(FRAME_RATE);
        windowDimensions = windowController.getWindowDimensions();
        bricksCounter = new Counter(BRICK_LINES*BRICKS_PER_LINE);
        BrickStrategyFactory strategyFactory = new BrickStrategyFactory(gameObjects(), this,
                imageReader, soundReader, inputListener, windowController, windowDimensions);

        // Setting background
        Renderable bgImage = imageReader.readImage("assets/DARK_BG2_small.jpeg",
                false);
        GameObject background = new GameObject(Vector2.ZERO, windowController.getWindowDimensions(), bgImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

        // Creating ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO,
                new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);
        float ballVelY = BALL_SPEED, ballVelX = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()) {
            ballVelX *= -1;
        }
        if(rand.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        Vector2 windowDimensions = windowController.getWindowDimensions();
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball);

        // Creating paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions, MIN_DISTANCE_FROM_SCREEN_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2, (int)windowDimensions.y()-30));
        gameObjects().addGameObject(paddle);

        // Creating bricks
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        createBricks(brickImage, strategyFactory);

        // Creating borders
        createBorders();

        // Creating Multiple disqualifications - Graphic
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        new GraphicLifeCounter(new Vector2(HEART_XPOS, windowDimensions.y()-BALL_RADIUS),
                new Vector2(BALL_RADIUS, BALL_RADIUS), livesLeft, heartImage, gameObjects(), numOfLives);

        // Creating Multiple disqualifications - Numeric
        new NumericLifeCounter(livesLeft, new Vector2(HEART_XPOS, windowDimensions.y()-NUMERIC_HEIGHT),
                new Vector2(BALL_RADIUS, BALL_RADIUS), gameObjects());
        }

    /**
     * Rendering the bricks images on the screen and appending the brick objects to the current game.
     * @param brickImage Rendered images of a brick.
     */
    private void createBricks(Renderable brickImage, BrickStrategyFactory strategyFactory) {
        float brickLength = (windowDimensions.x() - 2*BORDER_WIDTH) / BRICKS_PER_LINE;
        for(int i = 0; i < BRICK_LINES; i++) {
            for(int j = 0; j < BRICKS_PER_LINE; j++) {
                CollisionStrategy breakStrategy = strategyFactory.getStrategy();
                GameObject newBrick = new Brick(
                        new Vector2(BORDER_WIDTH + j*brickLength, BORDER_WIDTH + BRICKS_HEIGHT*i),
                        new Vector2(brickLength, BRICKS_HEIGHT),
                        brickImage, breakStrategy, bricksCounter);
                gameObjects().addGameObject(newBrick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Rendering the borders images on the screen and appending the border objects to the current game.
     */
    private void createBorders() {
        GameObject leftBorder = new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), new RectangleRenderable(Color.WHITE));
        this.gameObjects().addGameObject(leftBorder);
        GameObject rightBorder = new GameObject(Vector2.RIGHT.mult(windowDimensions.x()-BORDER_WIDTH),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), new RectangleRenderable(Color.WHITE));
        gameObjects().addGameObject(rightBorder);
        GameObject upperBorder = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), new RectangleRenderable(Color.GRAY));
        this.gameObjects().addGameObject(upperBorder);
    }

    /**
     * Called once per frame. Any logic is put here. Rendering, on the other hand,
     * should only be done within 'render'.
     * Note that the time that passes between subsequent calls to this method is not constant.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        pucksRemover();
    }

    /**
     * Removes the pucks that got out of the screen from the game objects.
     */
    private void pucksRemover() {
        for(GameObject obj: gameObjects()) {
            if(obj.getTopLeftCorner().y() > windowDimensions.y()) {
                gameObjects().removeGameObject(obj);
            }
        }
    }

    /**
     * Checks if the player has won or lost the current game, and prompts a message to the screen
     * according to the given situation.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        if(bricksCounter.value() <= 0) { // We won
            prompt = "You Win!";
        }
        if(ballHeight > windowDimensions.y()) { // We lost
            livesLeft.decrement();
            windowController.resetGame();
        }
        if(livesLeft.value() == 0) {
            prompt = "You Lose!";
        }
        if(!prompt.isEmpty()) {
            prompt += " Play Again?";
            if(windowController.openYesNoDialog(prompt)) {
                livesLeft = new Counter(numOfLives);
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    /**
     * The main function of the program, responsible for creating new gameManager and run it.
     * @param args arguments given from the command-line.
     */
    public static void main(String[] args) {
        new BrickerGameManager("Brick Game", new Vector2(WINDOW_WIDTH, WINDOW_HEIGHT)).run();
    }
}
