import javafx.geometry.Point2D;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Contains information about the car. Everything in here will be static so that the information can be accessed
 * globally by any interface and by the physics simulation without having to pass an instance of Car around.
 */
public class Car
{
    private static Gear gear;
    private static double x_C = 5; // x-position of car's center of mass (m)
    private static double y_C = 0.2; // y-position of car's center of mass (m)
    private static double theta_C = 0.0; // car frame's angle of rotation (radians)
    private static double v_xC = 0.0; // x-velocity of car's center of mass (m/s)
    private static double v_yC = 0.0; // y-velocity of car's center of mass (m/s)
    private static double acceleration = 0.0;
    private static double w_C = 0.0; // rate of rotation of car frame (radians/s)
    private static double m_C = 100; // mass of frame (kg)

    private static double m_R = 1.0; // mass of rear wheel (kg)
    private static double m_F = 1.0; // mass of front wheel (kg)
    private static double I_zzR = 1.0; // moment of inertia of rear wheels (kg-m^2)
    private static double I_zzF = 1.0; // moment of inertia of front wheels (kg-m^2)
    private static double R = 0.3; // wheel radius (m)
    private static double mu = 0.0; // coefficient of friction between wheels and ground
    private static double f_max = 0.0; // maximum static friction (N)

    private static double d = 4; // distance between the wheels (m)
    private static double s_x = 2; // x-distance between rear wheel and frame's center of mass (m)
    private static double s_y = 0.2; // y-distance between wheels and frame's center of mass (m)

    // Setters
    public static void setGear(Gear newGear){
        gear = newGear;
    }
    public static void setX_C(double newX_C){
        x_C = newX_C;
    }
    public static void setY_C(double newY_C){
        y_C = newY_C;
    }
    public static void setTheta_C(double newTheta_C){
        theta_C = newTheta_C;
    }
    public static void setV_xC(double newV_xC)
    {
        v_xC = newV_xC;
    }
    public static void setAcceleration(double newAcceleration)
    {
        acceleration = newAcceleration;
    }

    public Car()
    {
      EBS brakeSystem = new EBS();
    }

    /** returns the position of the rear wheel */
    public static Point2D getRearWheelPosition(){
        double x_R = x_C - s_x*cos(theta_C) + s_y*sin(theta_C);
        double y_R = y_C - s_x*sin(theta_C) + s_y*cos(theta_C);
        return new Point2D(x_R, y_R);
    }

    /** returns the position of the front wheel */
    public static Point2D getFrontWheelPosition(){
        Point2D rearWheelPos = getRearWheelPosition();
        double x_F = rearWheelPos.getX() + d*cos(theta_C);
        double y_F = rearWheelPos.getY() + d*sin(theta_C);
        return new Point2D(x_F, y_F);
    }
}
