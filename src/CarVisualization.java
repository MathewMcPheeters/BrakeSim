import javafx.geometry.Point2D;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import java.util.HashMap;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import java.lang.Math;

  /*
    Consists of a HashMap of shapes indexed by an enumeration.
    update() looks at the state of Car.java to determine how to draw the system.
    The components are then rendered within SimulationArea.
   */
  public class CarVisualization
  {
    private final double m_to_px = 12.5; // Conversion ratio between meters and pixels
    private final double wheelRadius = Car.R*m_to_px; // pixels
    private final double draw_cx = 295; // pixel coordinates
    private final double draw_cy = 265; // pixel coordinates
    public HashMap<ComponentNames, Shape> components;
    public enum ComponentNames
    {
      CHASSIS,
      REAR_WHEEL,
      FRONT_WHEEL,
      REAR_WHEEL_LINE,
      FRONT_WHEEL_LINE,
      CENTER_OF_MASS,
    }

    public CarVisualization()
    {
      // Initialize the map of components.
      components = new HashMap<>();

      // Add all components.
      this.components.put(ComponentNames.CHASSIS, new Line(0,0,0,0));
      this.components.put(ComponentNames.REAR_WHEEL, new Ellipse(0,0,wheelRadius,wheelRadius));
      this.components.put(ComponentNames.FRONT_WHEEL, new Ellipse(0,0,wheelRadius,wheelRadius));
      this.components.put(ComponentNames.REAR_WHEEL_LINE, new Line(0,0,0,0));
      this.components.put(ComponentNames.FRONT_WHEEL_LINE, new Line(0,0,0,0));
      this.components.put(ComponentNames.CENTER_OF_MASS, new Ellipse(0,0,5,5));
      this.update(); // Fix the positions of each component.

      // Color all components appropriately.
      this.components.get( ComponentNames.CHASSIS ).setFill( Color.BLACK );
      this.components.get( ComponentNames.REAR_WHEEL ).setFill( Color.WHITE );
      this.components.get( ComponentNames.FRONT_WHEEL ).setFill( Color.WHITE );
      this.components.get( ComponentNames.REAR_WHEEL_LINE ).setFill( Color.BLACK );
      this.components.get( ComponentNames.FRONT_WHEEL_LINE ).setFill( Color.BLACK );
      this.components.get( ComponentNames.CENTER_OF_MASS ).setFill( Color.YELLOW );

      // Set styles appropriately
      this.components.get( ComponentNames.CHASSIS ).setStyle("-fx-stroke-width: 3;");

    }

    public void update()
    {
      //System.out.println(Car.getXVelocity());

      // Draw the center of mass at (draw_cx, draw_cy)
      double cx_offset = Car.x_C*m_to_px - draw_cx; // offsets between the car's actual position and where it is drawn on the screen
      double cy_offset = -Car.y_C*m_to_px - draw_cy;

      double theta_C = Car.theta_C;
      //double front_theta = Car.rear_theta;
      //double rear_theta = Car.front_theta;
      double front_theta = Car.theta_F;
      double rear_theta = Car.theta_R;

      this.components.get( ComponentNames.CENTER_OF_MASS ).relocate(draw_cx, draw_cy);

      // ----- [ REAR WHEEL ] --------------------------------------------------------------------------------------------
      /*double s_x = (Car.s_x-Car.d) * m_to_px;
      double s_y = Car.s_y*m_to_px;
      double rotated_x = draw_cx + Math.cos(theta_C)*s_x - Math.sin(theta_C)*s_y;
      double rotated_y = draw_cy + Math.sin(theta_C)*s_x + Math.cos(theta_C)*s_y;*/
      Point2D rearWheelPos = Car.getRearWheelPosition();
      double rotated_x = rearWheelPos.getX()*m_to_px - cx_offset;
      double rotated_y = rearWheelPos.getY()*m_to_px - cy_offset;

      // Set the location for the center of mass visual and the width for the chassis line.
      this.components.get( ComponentNames.REAR_WHEEL ).relocate(rotated_x-wheelRadius, rotated_y-wheelRadius);

      // Set the position of the rear wheel.
      ((Line) this.components.get( ComponentNames.CHASSIS )).setStartX( rotated_x);
      ((Line) this.components.get( ComponentNames.CHASSIS )).setStartY( rotated_y);

      // Set the position for the rear wheel-line, which indicates the direction the rear wheel.
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStartX( rotated_x);
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStartY( rotated_y);
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setEndX( rotated_x + Math.cos(theta_C+rear_theta+Math.PI/2)*wheelRadius );
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setEndY( rotated_y + Math.sin(theta_C+rear_theta+Math.PI/2)*wheelRadius );

      // ----- [ FRONT WHEEL ] -------------------------------------------------------------------------------------------
      /*s_x = Car.s_x * m_to_px;
      s_y = Car.s_y;
      rotated_x = draw_cx + Math.cos(theta_C)*s_x - Math.sin(theta_C)*s_y;
      rotated_y = draw_cy + Math.sin(theta_C)*s_x + Math.cos(theta_C)*s_y;*/
      Point2D frontWheelPos = Car.getFrontWheelPosition();
      rotated_x = frontWheelPos.getX()*m_to_px - cx_offset;
      rotated_y = frontWheelPos.getY()*m_to_px - cy_offset;


      // Set the position of the front wheel.
      this.components.get( ComponentNames.FRONT_WHEEL ).relocate(rotated_x-wheelRadius, rotated_y-wheelRadius);
      ((Line) this.components.get( ComponentNames.CHASSIS )).setEndX( rotated_x);
      ((Line) this.components.get( ComponentNames.CHASSIS )).setEndY( rotated_y);

      // Set the position for the front wheel-line, which indicates the direction the front wheel.
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStartX( rotated_x);
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStartY( rotated_y);
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setEndX( rotated_x + Math.cos(theta_C+front_theta+Math.PI/2)*wheelRadius );
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setEndY( rotated_y + Math.sin(theta_C+front_theta+Math.PI/2)*wheelRadius );
    }
  }