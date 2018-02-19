import javafx.beans.property.SimpleBooleanProperty;
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

    // Constants regarding the mass and rotational inertia of the car
    private static double m_R = 1.0; // mass of rear wheel (kg)
    private static double m_F = 1.0; // mass of front wheel (kg)
    private static double m_C = 100; // mass of frame (kg)
    private static double I_zzR = 1.0; // moment of inertia of rear wheels (kg-m^2)
    private static double I_zzF = 1.0; // moment of inertia of front wheels (kg-m^2)
    private static double I_zzC = 100.0; // moment of inertia of the car frame (kg-m^2)

    // Constants regarding the car's suspension system and the car's physical dimensions:
    private static double R = 0.3; // wheel radius (m)
    private static double d = 4; // distance between the wheels (m)
    private static double s_x = 2; // x-distance between rear wheel and frame's center of mass (m)
    private static double s_y = 0.2; // y-distance between wheels and frame's center of mass (m)
    private static double k = 50000.0; // The spring coefficient of the car's suspension system (N/m)
    private static double h = 1.0; // The dampening coefficient of the car's suspension system (N-s/m)

    // Constants regarding the environment
    private static double mu = 0.0; // coefficient of friction between wheels and ground (unitless)
    private static double f_max = 0.0; // maximum static friction (N)
    private static double g = 9.81; // gravity (m/s^2)

    // Variables regarding the position and velocity of the car.
    private static double x_C = 5; // x-position of car's center of mass (m)
    private static double y_C = 0.49; // y-position of car's center of mass (m)
    private static double theta_C = 0.0; // car frame's angle of rotation (radians)
    private static double v_xC = 10.0; // x-velocity of car's center of mass (m/s)
    private static double v_yC = 0.0; // y-velocity of car's center of mass (m/s)
    private static double w_C = 0.0; // rate of rotation of car frame (radians/s)

    private static double acceleration = 0.0;
    private static SimpleBooleanProperty velocityChange = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty accelerationChange = new SimpleBooleanProperty(false);
    // Variables regarding the environment
    private static double T = -500.0; // torque applied by the brakes

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
        velocityChange.setValue(true);
    }
    public static void setAcceleration(double newAcceleration)
    {
        acceleration = newAcceleration;
        accelerationChange.setValue(true);
    }
    public static Double getAcceleration()
    {
        return acceleration;
    }
    public static Double getXVelocity(){return v_xC;}
    public static Gear getGear()
    {
        return gear;
    }
    public static SimpleBooleanProperty getVelocityChange()
    {
        return velocityChange;
    }
    public static SimpleBooleanProperty getAccelerationChange()
    {
        return accelerationChange;
    }
    private static EBS brakeSystem;
    public static void setEBSSystem(EBS system){brakeSystem = system;}
    public static EBS getBrakeSystem(){return brakeSystem;}

    static
    {
        brakeSystem = new EBS();
        brakeSystem.start();
    }

    /** returns the position of the rear wheel */
    public static Point2D getRearWheelPosition(){
        double x_R = x_C - s_x*cos(theta_C) + s_y*sin(theta_C);
        double y_R = y_C - s_x*sin(theta_C) - s_y*cos(theta_C);
        return new Point2D(x_R, y_R);
    }

    /** returns the position of the front wheel */
    public static Point2D getFrontWheelPosition(){
        Point2D rearWheelPos = getRearWheelPosition();
        double x_F = rearWheelPos.getX() + d*cos(theta_C);
        double y_F = rearWheelPos.getY() + d*sin(theta_C);
        return new Point2D(x_F, y_F);
    }

    /**
     * compute the instantaneous rate of rotational acceleration of the car frame, as a function of the current values
     * of y_C, v_yC, theta_C and w_C.
     */
    public static double getCurrentThetaAcceleration(){
        double numerator = (d-2*s_x)*(k*(R-y_C+s_x*sin(theta_C)+s_y*cos(theta_C))-h*(v_yC-s_x*cos(theta_C)*w_C+s_y*sin(theta_C)*w_C)) + m_F*(d-s_x)*(d*sin(theta_C)*w_C*w_C) + s_y*(I_zzF/(R*R)-m_F)*(d*cos(theta_C)*w_C*w_C) -s_y*T/R
                -(m_F*(d-s_x)-s_x*m_R)*(g+s_x*sin(theta_C)*w_C*w_C+s_y*cos(theta_C)*w_C*w_C+(2*k/(m_C+m_R+m_F))*(R-y_C+(s_x-d/2)*sin(theta_C)+s_y*cos(theta_C))-(2*h/(m_C+m_R+m_F))*(v_yC-(s_x-d/2)*cos(theta_C)*w_C+s_y*sin(theta_C)*w_C)-((m_R+m_F)/(m_C+m_R+m_F))*(g+((s_x-m_F*d/(m_R+m_F))*sin(theta_C)+s_y*cos(theta_C))*w_C*w_C)-m_C*g/(m_C+m_R+m_F))
                -s_y*(I_zzF/(R*R)-m_F-I_zzR/(R*R)+m_R)*(s_x*cos(theta_C)*w_C*w_C-s_y*sin(theta_C)*w_C*w_C -(T/R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + (s_x*cos(theta_C)-s_y*sin(theta_C))*w_C*w_C*(I_zzR/(R*R)-I_zzF/(R*R)+m_F-m_R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + d*cos(theta_C)*w_C*w_C*(I_zzF/(R*R)-m_F)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F));
        double denominator = I_zzC-(m_F*(d-s_x)-s_x*m_R)*(s_x*cos(theta_C)-s_y*sin(theta_C))+m_F*(d-s_x)*d*cos(theta_C)+s_y*(I_zzF/(R*R)-m_F-I_zzR/(R*R)+m_R)*(s_x*sin(theta_C)+s_y*cos(theta_C))-s_y*(I_zzF/(R*R)-m_F)*d*sin(theta_C)
                +(m_F*(d-s_x)-2*s_x*m_R)*((m_R+m_F)/(m_C+m_R+m_F))*((s_x-m_F*d/(m_R+m_F))*cos(theta_C)+s_y*sin(theta_C))
                +s_y*(I_zzF/(R*R)-m_F-I_zzR/(R*R)+m_R)*(d*sin(theta_C)*(I_zzF/(R*R)-m_F)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + (s_x*sin(theta_C)+s_y*cos(theta_C))*(I_zzR/(R*R)-I_zzF/(R*R)+m_F-m_R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F));
        return numerator/denominator;
    }

    /**
     * compute the instantaneous x-acceleration of the car frame, as a function of the current values of theta_C and
     * theta_Cpp (angular acceleration).
     */
    public static double getCurrentXAcceleration(){
        double theta_Cpp = getCurrentThetaAcceleration();
        return -(T/R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + (s_x*cos(theta_C)*w_C*w_C+s_x*sin(theta_C)*theta_Cpp-s_y*sin(theta_C)*w_C*w_C+s_y*cos(theta_C)*theta_Cpp)*(I_zzR/(R*R)-I_zzF/(R*R)+m_F-m_R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F)+(d*cos(theta_C)*w_C*w_C+d*sin(theta_C)*theta_Cpp)*(I_zzF/(R*R)-m_F)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F);
    }

    /**
     * compute the instantaneous y-acceleration of the car frame, as a function of the current values of y_C, v_yC,
     * theta_C and theta_Cpp (angular acceleration).
     */
    public static double getCurrentYAcceleration(){
        double theta_Cpp = getCurrentThetaAcceleration();
        return (2*k/(m_C+m_R+m_F))*(R-y_C+(s_x-d/2)*sin(theta_C)+s_y*cos(theta_C)) - (2*h/(m_C+m_R+m_F))*(v_yC-(s_x-d/2)*cos(theta_C)*w_C+s_y*sin(theta_C)*w_C) - ((m_R+m_F)/(m_C+m_R+m_F))*(g+(s_x-m_F*d/(m_R+m_F))*sin(theta_C)*w_C*w_C-(s_x-m_F*d/(m_R+m_F))*cos(theta_C)*theta_Cpp+s_y*cos(theta_C)*w_C*w_C+s_y*sin(theta_C)*theta_Cpp)-m_C*g/(m_C+m_R+m_F);
    }

    /**
     * Compute the instantaneous accelerations for the current position and velocity of the car, to see if they look like they're in the right ballpark:
     */
    public static void runTests(){
        double thetaAccel = Car.getCurrentThetaAcceleration();
        double xAccel = Car.getCurrentXAcceleration();
        double yAccel = Car.getCurrentYAcceleration();
        System.out.println("thetaAccel: " + thetaAccel);
        System.out.println("xAccel: " + xAccel);
        System.out.println("yAccel: " + yAccel);

        System.out.println("position of rear wheel is " + getRearWheelPosition());
        System.out.println("position of front wheel is " + getFrontWheelPosition());
    }
}
