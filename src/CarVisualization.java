import static Car.CarConstants.*;

import Car.CarConstants;
import Car.CarPhysics;
import Car.CarVariables;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.HashMap;

/**
  Consists of a HashMap of nodes indexed by an enumeration.
  update() looks at the state of Car.java to determine how to draw the system.
  The components are then rendered within SimulationArea.
 */
public class CarVisualization
{
    private final double m_to_px = 12.5; // Conversion ratio between meters and pixels
    private final double wheelRadius = CarConstants.getR()*m_to_px; // pixels
    private final double draw_cx = 295;
    private final double draw_cy = 265;
    private final CarPhysics carPhysics;

    // For correcting an image's center of rotation.
    private final double chassis_px_offset_x =-64;
    private final double chassis_px_offset_y =-26;

    HashMap<ComponentNames, Node> components;
    enum ComponentNames
    {
      CHASSIS,
      REAR_WHEEL,
      FRONT_WHEEL,
      REAR_WHEEL_LINE,
      FRONT_WHEEL_LINE,
      CENTER_OF_MASS,
    }

    void addComponents( SimulationArea simulationArea )
    {
      simulationArea.getChildren().add( this.components.get( ComponentNames.CHASSIS ));
      simulationArea.getChildren().add( this.components.get( ComponentNames.REAR_WHEEL ));
      simulationArea.getChildren().add( this.components.get( ComponentNames.FRONT_WHEEL ));
      simulationArea.getChildren().add( this.components.get( ComponentNames.REAR_WHEEL_LINE ));
      simulationArea.getChildren().add( this.components.get( ComponentNames.FRONT_WHEEL_LINE ));
      simulationArea.getChildren().add( this.components.get( ComponentNames.CENTER_OF_MASS ));
    }

    CarVisualization(CarPhysics carPhysics)
    {
      this.carPhysics = carPhysics;

      // Initialize the map of components.
      components = new HashMap<>();

      // Add all components.
      ImageView selectedImage = new ImageView();
      Image image1 = new Image("Resources/car_img_w_com.png");
      selectedImage.setImage(image1);
      selectedImage.relocate(0,0);

      // Add each component to the hashmap.
      this.components.put(ComponentNames.CHASSIS, selectedImage);
      this.components.put(ComponentNames.REAR_WHEEL, new Ellipse(0,0,wheelRadius,wheelRadius));
      this.components.put(ComponentNames.FRONT_WHEEL, new Ellipse(0,0,wheelRadius,wheelRadius));
      this.components.put(ComponentNames.REAR_WHEEL_LINE, new Line(0,0,0,0));
      this.components.put(ComponentNames.FRONT_WHEEL_LINE, new Line(0,0,0,0));
      this.components.put(ComponentNames.CENTER_OF_MASS, new Ellipse(0,0,5,5));
      this.update(); // Fix the positions of each component.

      // Color all components appropriately.
      ((Shape) this.components.get( ComponentNames.REAR_WHEEL )).setFill( Color.BLACK );
      ((Shape) this.components.get( ComponentNames.FRONT_WHEEL )).setFill( Color.BLACK );
      ((Shape) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStroke( Color.WHITE );
      ((Shape) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStroke( Color.WHITE );
      ((Shape) this.components.get( ComponentNames.CENTER_OF_MASS )).setFill( Color.RED );
      ((Shape) this.components.get(ComponentNames.CENTER_OF_MASS)).setStrokeWidth(1);
      ((Shape) this.components.get(ComponentNames.CENTER_OF_MASS)).setFill(Color.TRANSPARENT);
      ((Shape) this.components.get(ComponentNames.CENTER_OF_MASS)).setStroke(Color.RED);

    }

    void update()
    {
      // Draw the center of mass at (draw_cx, draw_cy).
      // Offsets between the car's actual position and where it is drawn on the screen.
      double cx_offset = CarVariables.getX_C()*m_to_px - draw_cx;
      double cy_offset = -CarVariables.getY_C()*m_to_px - draw_cy;

      double theta_C = CarVariables.getTheta_C();
      this.components.get( ComponentNames.CHASSIS ).setRotate(Math.toDegrees(theta_C));
      this.components.get(ComponentNames.CHASSIS).relocate( draw_cx + Math.cos(theta_C)* chassis_px_offset_x + Math.sin(theta_C)* chassis_px_offset_y,
                                                            draw_cy + Math.sin(theta_C)* chassis_px_offset_x + Math.cos(theta_C)* chassis_px_offset_y);
      double front_theta = CarVariables.getTheta_F();
      double rear_theta = CarVariables.getTheta_R();

      this.components.get( ComponentNames.CENTER_OF_MASS ).relocate(draw_cx-5, draw_cy-5);

      // ----- [ REAR WHEEL ] --------------------------------------------------------------------------------------------
      double rotated_x = carPhysics.getX_R()*m_to_px - cx_offset;
      double rotated_y = carPhysics.getY_R()*m_to_px - cy_offset;

      // Set the location for the center of mass visual and the width for the chassis line.
      this.components.get( ComponentNames.REAR_WHEEL ).relocate(rotated_x-wheelRadius, rotated_y-wheelRadius);

      // Set the position for the rear wheel-line, which indicates the direction the rear wheel.
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStartX( rotated_x);
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStartY( rotated_y);
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setEndX( rotated_x + Math.cos(theta_C+rear_theta+ Math.PI/2)*wheelRadius );
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setEndY( rotated_y + Math.sin(theta_C+rear_theta+ Math.PI/2)*wheelRadius );

      // ----- [ FRONT WHEEL ] -------------------------------------------------------------------------------------------
      rotated_x = carPhysics.getX_F()*m_to_px - cx_offset;
      rotated_y = carPhysics.getY_F()*m_to_px - cy_offset;

      this.components.get( ComponentNames.FRONT_WHEEL ).relocate(rotated_x-wheelRadius, rotated_y-wheelRadius);

      // Set the position for the front wheel-line, which indicates the direction the front wheel.
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStartX( rotated_x);
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStartY( rotated_y);
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setEndX( rotated_x + Math.cos(theta_C+front_theta+ Math.PI/2)*wheelRadius );
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setEndY( rotated_y + Math.sin(theta_C+front_theta+ Math.PI/2)*wheelRadius );

    }
}