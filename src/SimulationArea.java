import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SimulationArea extends Pane
{
  private Image backgroundImageFile = new Image("Resources/testBackground4.png");
  private BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,false,false,true,false);
  private BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
  private CarVisualization carVisualization = new CarVisualization();
  private ArrayList<Rectangle> dashes = new ArrayList<>();
  private final double m_to_px = 12.5;

  SimulationArea()
  {
    // Set the environment and add the car visualization to the scene.
    this.setBackground(new Background(backgroundImage));
    for (int i = 0; i < 6; i++) // Add the dashes to the scene.
    {
      dashes.add(dashes.size(), new Rectangle( i * 100, 280, 30, 3));
      dashes.get(dashes.size()-1).setFill(Color.WHITE);
    }
    this.getChildren().addAll(dashes);
    carVisualization.addComponents( this );
  }

  void update()
  {
    Car.step(16); // Update the physical model.
    carVisualization.update(); // Update the visualization.
    for (int i  = 0; i < dashes.size(); i++) // Update the dashes.
    {
      // AWG: I took out 'deltaX' and put in getXVelocity...seems weird to just pass in this one parameter.
      dashes.get(i).setX((dashes.get(i).getX()-Car.getXVelocity()*m_to_px)%600);
      if  (dashes.get(i).getX() < 0)
        dashes.get(i).setX(600+dashes.get(i).getX());
    }
  }


}
