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

    private Image backgroundImageFile = new Image("Resources/testBackground4.png");
    private BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
    private BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

    public CarVisualization carVis;
    public SimulationArea(){

        this.setBackground(new Background(backgroundImage));

        carVis = new CarVisualization();
        this.getChildren().add( carVis.components.get( CarVisualization.ComponentNames.CHASSIS ));
        this.getChildren().add( carVis.components.get( CarVisualization.ComponentNames.REAR_WHEEL ));
        this.getChildren().add( carVis.components.get( CarVisualization.ComponentNames.FRONT_WHEEL ));
        this.getChildren().add( carVis.components.get( CarVisualization.ComponentNames.REAR_WHEEL_LINE ));
        this.getChildren().add( carVis.components.get( CarVisualization.ComponentNames.FRONT_WHEEL_LINE ));
        this.getChildren().add( carVis.components.get( CarVisualization.ComponentNames.CENTER_OF_MASS ));


    }


}

