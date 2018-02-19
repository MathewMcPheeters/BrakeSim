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

    private final double m_to_px = 12.5;
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

    public CarVisualization ( )
    {
      // Initialize the map of components.
      components = new HashMap<>();

      // Add all components.
      this.components.put(ComponentNames.CHASSIS, new Line(0,0,0,0));
      this.components.put(ComponentNames.REAR_WHEEL, new Ellipse(0,0,10,10));
      this.components.put(ComponentNames.FRONT_WHEEL, new Ellipse(0,0,10,10));
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

    }

    public void update()
    {
      //System.out.println(Car.getXVelocity());

      // Draw the center of mass at (draw_cx, draw_cy)
      double draw_cx = 290;
      double draw_cy = 235 + (Car.y_C * m_to_px);

      double theta_C = Car.theta_C;
      double front_theta = Car.rear_theta;
      double rear_theta = Car.front_theta;

      this.components.get( ComponentNames.CENTER_OF_MASS ).relocate(draw_cx+5, draw_cy+5);

      // ----- [ REAR WHEEL ] --------------------------------------------------------------------------------------------
      double s_x = (Car.s_x-Car.d) * m_to_px;
      double s_y = Car.s_y*m_to_px;
      double rotated_x = draw_cx + Math.cos(theta_C)*s_x - Math.sin(theta_C)*s_y;
      double rotated_y = draw_cy + Math.sin(theta_C)*s_x + Math.cos(theta_C)*s_y;

      // Set the location for the center of mass visual and the width for the chassis line.
      this.components.get( ComponentNames.REAR_WHEEL ).relocate(rotated_x, rotated_y);
      this.components.get( ComponentNames.CHASSIS ).setStyle("-fx-stroke-width: 3;");

      // Set the position of the rear wheel.
      ((Line) this.components.get( ComponentNames.CHASSIS )).setStartX( rotated_x+10);
      ((Line) this.components.get( ComponentNames.CHASSIS )).setStartY( rotated_y+10);

      // Set the position for the rear wheel-line, which indicates the direction the rear wheel.
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStartX( rotated_x+10);
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setStartY( rotated_y+10);
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setEndX( rotated_x+10 + Math.cos(theta_C+rear_theta+Math.PI/2)*10 );
      ((Line) this.components.get( ComponentNames.REAR_WHEEL_LINE )).setEndY( rotated_y+10 + Math.sin(theta_C+rear_theta+Math.PI/2)*10 );

      // ----- [ FRONT WHEEL ] -------------------------------------------------------------------------------------------
      s_x = Car.s_x * m_to_px;
      s_y = Car.s_y;
      rotated_x = draw_cx + Math.cos(theta_C)*s_x - Math.sin(theta_C)*s_y;
      rotated_y = draw_cy + Math.sin(theta_C)*s_x + Math.cos(theta_C)*s_y;

      // Set the position of the front wheel.
      this.components.get( ComponentNames.FRONT_WHEEL ).relocate(rotated_x, rotated_y);
      ((Line) this.components.get( ComponentNames.CHASSIS )).setEndX( rotated_x+10);
      ((Line) this.components.get( ComponentNames.CHASSIS )).setEndY( rotated_y+10);

      // Set the position for the front wheel-line, which indicates the direction the front wheel.
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStartX( rotated_x+10);
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setStartY( rotated_y+10);
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setEndX( rotated_x+10 + Math.cos(theta_C+front_theta+Math.PI/2)*10 );
      ((Line) this.components.get( ComponentNames.FRONT_WHEEL_LINE )).setEndY( rotated_y+10 + Math.sin(theta_C+front_theta+Math.PI/2)*10 );
    }
  }