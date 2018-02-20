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
public class SimulationArea extends Pane
{

    private Image backgroundImageFile = new Image("Resources/testBackground4.png");
    private BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,false);
    private BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
    public CarVisualization carVis;
    private final double m_to_px = 12.5;

    ArrayList<Rectangle> dashes = new ArrayList<>();

    public void update(double deltaX)
    {
        for (int i  = 0; i < dashes.size(); i++)
        {
            dashes.get(i).setX((dashes.get(i).getX()-deltaX*m_to_px)%600);
            if  (dashes.get(i).getX() < 0)
            dashes.get(i).setX(600+dashes.get(i).getX());
        }
    }

    public SimulationArea()
    {
        for (int i = 0; i < 6; i++)
        {
            dashes.add(dashes.size(), new Rectangle( i * 100, 280, 30, 3));
            dashes.get(dashes.size()-1).setFill(Color.WHITE);
        }
        this.getChildren().addAll(dashes);
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

