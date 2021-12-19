package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;

import java.util.Random;

/**
 * Factory class for creating Collision strategies.
 * @author Dor Messica.
 */
public class BrickStrategyFactory {
    private static final int STATUS_HEIGHT = 15;
    private static final int BORDER_WIDTH = 10;
    private static final int BRICKS_PER_LINE = 8;
    private final GameObjectCollection gameObjectCollection;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final BrickerGameManager gameManager;
    private final UserInputListener inputListener;
    private final WindowController windowController;
    private final Vector2 windowDimensions;
    private static final int NUM_OF_STRATEGIES = 5;  // Without multiple strategies
    private final Random rand;
    CollisionStrategy[] strategiesList;

    /**
     * Constructor for a brick strategy.
     * @param gameObjectCollection  Global game object collection.
     * @param gameManager The brick game manager object.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                             See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                                disk. See its documentation for help.
     * @param inputListener UserInputListener instance.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                               concerning the window.
     * @param windowDimensions Width and height in window coordinates.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection, BrickerGameManager gameManager,
                                ImageReader imageReader, SoundReader soundReader,
                                UserInputListener inputListener, WindowController windowController,
                                Vector2 windowDimensions) {
        this.gameObjectCollection = gameObjectCollection;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
        this.gameManager = gameManager;
        rand = new Random();
    }

    /**
     * method randomly selects between 5 strategies and returns one CollisionStrategy object which is a
     * RemoveBrickStrategy decorated by one of the decorator strategies,
     * or decorated by two randomly selected strategies, or decorated by one of the decorator strategies
     * and a pair of additional two decorator strategies.
     * @return CollisionStrategy object.
     */
    public CollisionStrategy getStrategy() {
        strategiesList = new CollisionStrategy[NUM_OF_STRATEGIES];
        CollisionStrategy removeBrickStrategy = new RemoveBrickStrategy(gameObjectCollection);
        strategiesList[0] = removeBrickStrategy;
        CollisionStrategy puckStrategy = new PuckStrategy(removeBrickStrategy, imageReader, soundReader);
        strategiesList[1] = puckStrategy;
        CollisionStrategy addPaddleStrategy = new AddPaddleStrategy(removeBrickStrategy,
                imageReader, inputListener, windowDimensions);
        strategiesList[2] = addPaddleStrategy;
        Vector2 statusSize = new Vector2((windowDimensions.x() - 2*BORDER_WIDTH) / BRICKS_PER_LINE,
                STATUS_HEIGHT);
        CollisionStrategy widePaddleStrategy = new WideNarrowPaddleStrategy(removeBrickStrategy, imageReader,
                statusSize, windowDimensions);
        strategiesList[3] = widePaddleStrategy;
        CollisionStrategy changeCameraStrategy = new ChangeCameraStrategy(removeBrickStrategy,
                windowController, gameManager);
        strategiesList[4] = changeCameraStrategy;
        int numOfStrategy = rand.nextInt(NUM_OF_STRATEGIES+1);
        if(numOfStrategy == NUM_OF_STRATEGIES)
        {
            return chooseMultipleStrategies();
        }
        return strategiesList[numOfStrategy];
    }

    /* Private helper function for creating a multiple behaviors strategy */
    private CollisionStrategy chooseMultipleStrategies() {
        int firstStrategyNumber = rand.nextInt(NUM_OF_STRATEGIES)+1; // Randomize from 1-5
        int secondStrategyNumber = rand.nextInt(NUM_OF_STRATEGIES)+1;
        CollisionStrategy firstStrategy;
        if(firstStrategyNumber == NUM_OF_STRATEGIES || secondStrategyNumber == NUM_OF_STRATEGIES) {
            firstStrategy = new MultipleBehaviorsStrategy(
                    strategiesList[rand.nextInt(NUM_OF_STRATEGIES-1)+1],  // Randomize from 1-4
                    strategiesList[rand.nextInt(NUM_OF_STRATEGIES-1)+1]);
        }
        else {
            firstStrategy = strategiesList[firstStrategyNumber];
        }
        CollisionStrategy secondStrategy = strategiesList[rand.nextInt(NUM_OF_STRATEGIES-1)+1];

        return new MultipleBehaviorsStrategy(firstStrategy, secondStrategy);
    }
}
