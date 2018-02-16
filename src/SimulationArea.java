import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Simulation area will be defined in here. Note that this class extends Pane, so we can add
 * a SimulationArea to a Scene just as we would add a Pane.
 * Scale of simulation area (15px/foot): Area = 50ft, Car = 15-17"ft, Dashes = 2ft and 8ft apart
 */
public class SimulationArea extends Pane {

    public static final int PIXELSTOFEET = 15; // 15 pixels/ft
    public static final double MPHTOFPS = 1.46667; //1 mph = 1.46667 fps
    public static final int AREALENGTH = 50;
    public static final int DASHDISTANCEAPART = 10; //10 to include the dash itself + distance to next dash
    Image backgroundImageFile = new Image("Resources/testBackground4.png");
    BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
    BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

    ArrayList<RoadDash> roadDashes = new ArrayList<>();
    public double currentSpeed = 5;
    public boolean animationsPlaying = false;

//    Rectangle roadDash = new Rectangle(560,300,30,8);
//    TranslateTransition dashTranslate = new TranslateTransition(Duration.millis(2000), roadDash);


    public SimulationArea(){

        this.setBackground(new Background(backgroundImage));


//        createRoadDash(560,300);
        createInitialRoadDashes();

    }

    public void startAnimations(){
        for(RoadDash dash: roadDashes){
            dash.transition.play();
        }
    }

    private void createInitialRoadDashes(){
        int numberOfInitialDashes = AREALENGTH/DASHDISTANCEAPART;

        for(int i=560; i > 0; i-=150){
            int transitionDuration = calculateAnimationDuration(i);

            RoadDash newDash = new RoadDash(i,300,transitionDuration);

            this.getChildren().add(newDash.shape);
            roadDashes.add(0,newDash);
        }
    }

    private void createRoadDash(int x,int y){

        int transitionDuration = calculateAnimationDuration(x);

        RoadDash newDash = new RoadDash(x,y,transitionDuration);

        this.getChildren().add(newDash.shape);
        roadDashes.add(newDash);

        if(animationsPlaying){
            newDash.transition.play();
        }
    }

    /**Takes the current speed of the vehicle and calculates how many milliseconds
     * it should take for a road dash to traverse the simulation area.
    **/
    private int calculateAnimationDuration(int x){

        double feetToTraverse = x/PIXELSTOFEET;

        double feetPerSecond = currentSpeed/ MPHTOFPS;
        double millisecondsToTraverseArea = (feetToTraverse/feetPerSecond)*1000;

        return (int) Math.round(millisecondsToTraverseArea);
    }

    /**Takes the current speed and calculates how quickly new road dashes should be created.
     * Returns a time in milliseconds
     */
    private int calculateDashCreationInterval(){

        
        return 0;
    }

    /**
     * Removes the oldest dash from the scene.
     */
    private void removeOldestDash(){
        RoadDash targetDash = roadDashes.get(0);
        this.getChildren().remove(targetDash.shape);
        roadDashes.remove(0);
    }

    /**
     * RoadDash class to create road dashes.
     * This object contains to reference to the shape and to the transition
     */
    private class RoadDash{

        private Rectangle shape;
        private TranslateTransition transition;

        public RoadDash(int x, int y, int transitionDuration){
            System.out.println("created RoadDash at (" + x + "," + y + "): " + transitionDuration);
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

