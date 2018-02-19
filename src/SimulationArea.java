import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.ArrayList;

/**
 * Simulation area will be defined in here. Note that this class extends Pane, so we can add
 * a SimulationArea to a Scene just as we would add a Pane.
 * Scale of simulation area (10.2 px/foot): Area = 50ft, Car = 15-17"ft, Dashes = 10ft and 30ft apart
 * Resource: http://www.ctre.iastate.edu/pubs/itcd/pavement%20markings.pdf -page 8
 */
public class SimulationArea extends Pane {

    private static final double PIXELSTOFEET = 10.2; // 10.2 pixels/ft
    private static final double MPHTOFPS = 1.46667; //1 mph = 1.46667 fps
    private static final int DASHDISTANCEAPART = 10; //2 to include the dash itself + distance to next dash
    private Image backgroundImageFile = new Image("Resources/testBackground4.png");
    private BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
    private BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

    ArrayList<RoadDash> roadDashes = new ArrayList<>(20);
    private double currentSpeed = 1;
    private int dashCreationInterval;
    public boolean animationsPlaying = false;
    private double lastTick;
    private Polygon startingLine;
    private TranslateTransition startingLineAnimation;
    private Rectangle leftSidePanel;
    private Rectangle rightSidePanel;
    Group panelGroup = new Group();

    public SimulationArea(){

        this.setBackground(new Background(backgroundImage));

        initializeSidePanels();
        initializeStartingLine();
    }

    public void setSpeed(double speed){
        //Update speed
        currentSpeed = speed;

        //Recalculate dash creation interval
        dashCreationInterval = calculateDashCreationInterval();

        startingLineAnimation.setDuration(new Duration(calculateAnimationDuration(440)));
    }

    public void updateAnimations(){

        if(animationsPlaying){
            double currentTime = System.currentTimeMillis();
            double deltaTime = currentTime - lastTick;
            if(deltaTime >= dashCreationInterval){
                lastTick = currentTime;
                createRoadDash(560,300);
            }
        }
    }

    //Called by simulation worker to start animations
    public void startAnimations(){

        animationsPlaying = true;
        startingLineAnimation.play();
    }

    private void initializeSidePanels(){
        leftSidePanel = new Rectangle(0,95,36,255);
        leftSidePanel.setFill(Color.valueOf("F4F4F4"));

        rightSidePanel = new Rectangle(558,95,36,255);
        rightSidePanel.setFill(Color.valueOf("F4F4F4"));

        panelGroup.getChildren().addAll(leftSidePanel,rightSidePanel);
        this.getChildren().add(panelGroup);
    }

    private void initializeStartingLine(){
        startingLine = new Polygon(440,340,450,340,480,265,470,265);
        startingLine.setFill(Color.YELLOW);

        startingLineAnimation = new TranslateTransition(new Duration(calculateAnimationDuration(440)),startingLine);
        startingLineAnimation.setByX(-440);
        startingLineAnimation.setOnFinished(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                removeStartingLine(); //Works on the assumption that the oldest dash will be the next to finish
            }
        });

        panelGroup.getChildren().add(startingLine);
        startingLine.toBack();
    }

    private void removeStartingLine(){
        panelGroup.getChildren().remove(startingLine);
    }

    private void createRoadDash(int x,int y){

        int transitionDuration = calculateAnimationDuration(x);

        RoadDash newDash = new RoadDash(x,y,transitionDuration);

        roadDashes.add(newDash);
        System.out.println(roadDashes.size());
        //Kind of hacky solution to update UI on JavaFX thread. If it is not done this way, throws an exception
        Pane context = this;
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                context.getChildren().add(newDash.shape);
                newDash.shape.toBack();
            }
        });

        if(animationsPlaying)
        {
            newDash.transition.play();
        }
    }

    /**Takes the current speed of the vehicle and calculates how many milliseconds
     * it should take for a road dash to traverse the simulation area.
    **/
    private int calculateAnimationDuration(int x){

        double feetToTraverse = x/PIXELSTOFEET;
        double feetPerSecond = currentSpeed * MPHTOFPS;
        double millisecondsToTraverseArea = (feetToTraverse/feetPerSecond)*1000;

        return (int) Math.round(millisecondsToTraverseArea);
    }

    /**Takes the current speed and calculates how quickly new road dashes should be created.
     * Returns a time in milliseconds
     */
    private int calculateDashCreationInterval(){
        //calculate time in ms it takes for car to travel 10 feet
        double creationInterval = (DASHDISTANCEAPART / (currentSpeed * MPHTOFPS))*1000;

        return (int) Math.round(creationInterval);
    }

    /**
     * Removes the oldest dash from the scene.
     */
    private void removeOldestDash(){

        if(roadDashes.size() > 0){
            RoadDash targetDash = roadDashes.get(0);
            panelGroup.getChildren().remove(targetDash.shape);
            roadDashes.remove(0);
        }
    }

    /**
     * RoadDash class to create road dashes.
     * This object contains to reference to the shape and to the transition
     */
    private class RoadDash{

        private Rectangle shape;
        private TranslateTransition transition;

        public RoadDash(int x, int y, int transitionDuration){
            this.shape = new Rectangle(x,y,30,8);
            this.shape.setFill(Color.WHITE);
            this.transition = new TranslateTransition(Duration.millis(transitionDuration), shape);
            this.transition.setByX(-1*x);
            this.transition.setOnFinished(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event) {
                    removeOldestDash(); //Works on the assumption that the oldest dash will be the next to finish
                }
            });
        }
    }

}

