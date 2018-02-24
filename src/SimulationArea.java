import Car.CarPhysics;
import Car.CarVariables;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * Provides rendering for the car and environment.
 */
public class SimulationArea extends Pane
{
  private CarPhysics carPhysics;
  private Image backgroundImageFile = new Image("Resources/testBackground4.png");
  private BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,false,false,true,false);
  private BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
  private CarVisualization carVisualization;
  private ArrayList<Rectangle> dashes = new ArrayList<>();
  private final double m_to_px = 12.5;
  private int previousDash;

  SimulationArea(CarPhysics carPhysics)
  {
    this.carPhysics = carPhysics;
    carVisualization = new CarVisualization(carPhysics);

    // Set the environment and add the car visualization to the scene.
    this.setBackground(new Background(backgroundImage));
    for (int i = 0; i < 6; i++) // Add the dashes to the scene.
    {
      dashes.add(dashes.size(), new Rectangle( i * 9.15*m_to_px, 280, 3.048*m_to_px, 3));
      dashes.get(dashes.size()-1).setFill(Color.WHITE);
    }
    this.getChildren().addAll(dashes);
    carVisualization.addComponents( this );
  }

  void update()
  {
    carPhysics.step(16); // Update the physical model.
    carVisualization.update(); // Update the visualization.
    for (int i  = 0; i < dashes.size(); i++) // Update the dashes.
    {

      //Get the distance traveled by the car this frame and convert it to pixels
      double distanceTraveled = (CarVariables.getV_xC() * 16) / 1000;

      dashes.get(i).setX((dashes.get(i).getX() - distanceTraveled * m_to_px));

      if (dashes.get(i).getX() < 0) {

        //Get the index of the dash usually adjacent to the left of this dash
        previousDash = i-1;

        if(previousDash < 0) {
          previousDash = dashes.size() - 1;
        }

        //Move this dash a fixed distance to the right of the dash it always follows.
        dashes.get(i).setX(dashes.get(previousDash).getX()+9.15*m_to_px);
      }
    }
  }
}