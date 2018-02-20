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

    //Note: constants marked with /**/ have realistic values applied. Everything else is just a guesstimate for now.

    // Constants regarding the mass and rotational inertia of the car
    /**/private final static double m_R = 8.0; // mass of rear wheel (kg)
    /**/private final static double m_F = 8.0; // mass of front wheel (kg)
    /**/private final static double m_C = 1300; // mass of frame (kg)
    private final static double I_zzR = 0.72; // moment of inertia of rear wheels (kg-m^2)
    private final static double I_zzF = 0.72; // moment of inertia of front wheels (kg-m^2)
    private final static double I_zzC = 11700; // moment of inertia of the car frame (kg-m^2)

    // Constants regarding the car's suspension system and the car's physical dimensions:
    /**/public final static double R = 0.4318; // wheel radius (m)
    /**/public final static double d = 5.0800; // distance between the wheels (m)
    public final static double s_x = 3; // x-distance between rear wheel and frame's center of mass (m)
    public final static double s_y = 0.5; // y-distance between wheels and frame's center of mass (m)
    /**/private final static double k = 2500.0; // The spring coefficient of the car's suspension system (N/m)
    private final static double h = 1000.0; // The dampening coefficient of the car's suspension system (N-s/m)

    // Constants regarding the environment
    /**/private final static double mu = 0.7; // coefficient of friction between wheels and ground (unitless)
    private final static double f_max = 500; // maximum static friction (N)
    /**/private final static double g = 9.81; // gravity (m/s^2)

    // Variables regarding the position and velocity of the car.
    public static double x_C = 5; // x-position of car's center of mass (m)
    public static double y_C = 0.49; // y-position of car's center of mass (m)
    public static double theta_C = 0.0; // car frame's angle of rotation (radians)
    private static double v_xC = 10.0; // x-velocity of car's center of mass (m/s)
    private static double v_yC = 0.0; // y-velocity of car's center of mass (m/s)
    private static double w_C = 0.0; // rate of rotation of car frame (radians/s)

    // Variables regarding the rotation of the wheels:
    public static double theta_R = 0.0; // Rear wheel's angle of rotation (radians)
    private static double w_R = 0.0; // Rear wheel's rate of rotation (radians/s)
    public static double theta_F = 0.0; // Front wheel's angle of rotation (radians)
    private static double w_F = 0.0; // Front wheel's rate of rotation (radians/s)

    // Duplicate variables (Todo: these need to be collapsed into other variables or removed):
    public static double rear_theta = 0;
    public static double front_theta = 0;
    private static SimpleBooleanProperty velocityChange = new SimpleBooleanProperty(false);
    private static SimpleBooleanProperty accelerationChange = new SimpleBooleanProperty(false);

    // Variables regarding the environment
    private static double accelerationTorque = 0.0; // Torque due to pressing the accelerator (N-m)
    private static double brakeTorque = 0.0; // torque applied by the brakes (N-m)
    private static double T = 0.0; // Combined torque applied by the brakes and the gas pedal (N-m)

    // Setters
    public static void setTorque(double torque){T = torque;}
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
    public static void setW_R(double newW_R){w_R = newW_R;}
    public static double getTorque(){return T;}
    public static void setAccelerationTorque(double newAccelerationTorque)
    {
        accelerationTorque = newAccelerationTorque;
        T = brakeTorque-accelerationTorque;
    }
    public static void setBrakeTorque(double newBrakeTorque)
    {
        brakeTorque = newBrakeTorque;
        T = brakeTorque-accelerationTorque;
    }
    public static void engageBrakesLongPress()
    {
        brakeSystem.engageBrakes();
    }
    public static void engageBrakesShortPress()
    {
        brakeSystem.engageBrakes();
    }
    public static void disengageBrakes()
    {
        setBrakeTorque(0.0);
        brakeSystem.disengageBrakes();
    }
    public static Double getAccelerationTorque()
    {
        return accelerationTorque;
    }
    public static Double getXVelocity(){return v_xC;}
    public static Gear getGear()
    {
        return gear;
    }
    private static EBS brakeSystem;
    public static EBS getBrakeSystem(){return brakeSystem;}
    public static double getBrakeTorque(){return brakeTorque;}

    static
    {
        brakeSystem = new EBS();
        brakeSystem.start();
    }

    /**
     * Sets some Car variables for the given car velocity, assuming rolling contact
     */
    public static void setVariablesRollingContact()
    {
        w_R = v_xC/R;
        w_F = v_xC/R;
    }
    public static void resetVariables()
    {
        x_C = 5;
        y_C = 0.49;
        theta_C = 0.0;
        v_xC = 10.0;
        v_yC = 0.0;
        w_C = 0.0;
        theta_R = 0.0;
        w_R = 0.0;
        theta_F = 0.0;
        w_F = 0.0;
        rear_theta = 0;
        front_theta = 0;
        velocityChange = new SimpleBooleanProperty(false);
        accelerationChange = new SimpleBooleanProperty(false);
    }
    /** returns the position of the rear wheel */
    public static Point2D getRearWheelPosition()
    {
        double x_R = x_C - s_x*cos(theta_C) + s_y*sin(theta_C);
        double y_R = y_C - s_x*sin(theta_C) - s_y*cos(theta_C);
        return new Point2D(x_R, y_R);
    }

    /** returns the position of the front wheel */
    public static Point2D getFrontWheelPosition()
    {
        Point2D rearWheelPos = getRearWheelPosition();
        double x_F = rearWheelPos.getX() + d*cos(theta_C);
        double y_F = rearWheelPos.getY() + d*sin(theta_C);
        return new Point2D(x_F, y_F);
    }

    /**
     * compute the instantaneous rate of rotational acceleration of the car frame, as a function of the current values
     * of y_C, v_yC, theta_C and w_C.
     */
    public static double getCurrentThetaCAcceleration()
    {
        /*double numerator = (d-2*s_x)*(k*(R-y_C+s_x*sin(theta_C)+s_y*cos(theta_C))-h*(v_yC-s_x*cos(theta_C)*w_C+s_y*sin(theta_C)*w_C)) + m_F*(d-s_x)*(d*sin(theta_C)*w_C*w_C) + s_y*(I_zzF/(R*R)-m_F)*(d*cos(theta_C)*w_C*w_C) -s_y*T/R
                -(m_F*(d-s_x)-s_x*m_R)*(g+s_x*sin(theta_C)*w_C*w_C+s_y*cos(theta_C)*w_C*w_C+(2*k/(m_C+m_R+m_F))*(R-y_C+(s_x-d/2)*sin(theta_C)+s_y*cos(theta_C))-(2*h/(m_C+m_R+m_F))*(v_yC-(s_x-d/2)*cos(theta_C)*w_C+s_y*sin(theta_C)*w_C)-((m_R+m_F)/(m_C+m_R+m_F))*(g+((s_x-m_F*d/(m_R+m_F))*sin(theta_C)+s_y*cos(theta_C))*w_C*w_C)-m_C*g/(m_C+m_R+m_F))
                -s_y*(I_zzF/(R*R)-m_F-I_zzR/(R*R)+m_R)*(s_x*cos(theta_C)*w_C*w_C-s_y*sin(theta_C)*w_C*w_C -(T/R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + (s_x*cos(theta_C)-s_y*sin(theta_C))*w_C*w_C*(I_zzR/(R*R)-I_zzF/(R*R)+m_F-m_R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + d*cos(theta_C)*w_C*w_C*(I_zzF/(R*R)-m_F)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F));
        double denominator = I_zzC-(m_F*(d-s_x)-s_x*m_R)*(s_x*cos(theta_C)-s_y*sin(theta_C))+m_F*(d-s_x)*d*cos(theta_C)+s_y*(I_zzF/(R*R)-m_F-I_zzR/(R*R)+m_R)*(s_x*sin(theta_C)+s_y*cos(theta_C))-s_y*(I_zzF/(R*R)-m_F)*d*sin(theta_C)
                +(m_F*(d-s_x)-2*s_x*m_R)*((m_R+m_F)/(m_C+m_R+m_F))*((s_x-m_F*d/(m_R+m_F))*cos(theta_C)+s_y*sin(theta_C))
                +s_y*(I_zzF/(R*R)-m_F-I_zzR/(R*R)+m_R)*(d*sin(theta_C)*(I_zzF/(R*R)-m_F)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + (s_x*sin(theta_C)+s_y*cos(theta_C))*(I_zzR/(R*R)-I_zzF/(R*R)+m_F-m_R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F));
        return numerator/denominator;*/
        return 0.0;
    }

    /**
     * compute the instantaneous x-acceleration of the car frame, as a function of the current values of theta_C and
     * theta_Cpp (angular acceleration).
     */
    public static double getCurrentXCAcceleration()
    {
        // sanity check before big computations
        if(v_xC==0 && gear!=Gear.REVERSE && T>0) return 0.0;

        double theta_Cpp = getCurrentThetaCAcceleration();
        return -(T/R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F) + (s_x*cos(theta_C)*w_C*w_C+s_x*sin(theta_C)*theta_Cpp-s_y*sin(theta_C)*w_C*w_C+s_y*cos(theta_C)*theta_Cpp)*(I_zzR/(R*R)-I_zzF/(R*R)+m_F-m_R)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F)+(d*cos(theta_C)*w_C*w_C+d*sin(theta_C)*theta_Cpp)*(I_zzF/(R*R)-m_F)/(I_zzF/(R*R)-I_zzR/(R*R)+m_C+m_R-m_F);
    }

    /**
     * compute the instantaneous y-acceleration of the car frame, as a function of the current values of y_C, v_yC,
     * theta_C and theta_Cpp (angular acceleration).
     */
    public static double getCurrentYCAcceleration()
    {
        /*double theta_Cpp = getCurrentThetaCAcceleration();
        return (2*k/(m_C+m_R+m_F))*(R-y_C+(s_x-d/2)*sin(theta_C)+s_y*cos(theta_C)) - (2*h/(m_C+m_R+m_F))*(v_yC-(s_x-d/2)*cos(theta_C)*w_C+s_y*sin(theta_C)*w_C) - ((m_R+m_F)/(m_C+m_R+m_F))*(g+(s_x-m_F*d/(m_R+m_F))*sin(theta_C)*w_C*w_C-(s_x-m_F*d/(m_R+m_F))*cos(theta_C)*theta_Cpp+s_y*cos(theta_C)*w_C*w_C+s_y*sin(theta_C)*theta_Cpp)-m_C*g/(m_C+m_R+m_F);*/
        return 0.0;
    }

    /**
     * Compute the instantaneous angular acceleration of the rear wheel
     * @return the acceleration in radians/s^2
     */
    public static double getCurrentThetaRAcceleration()
    {
        //double v_yR = v_yC - s_x*cos(theta_C)*w_C + s_y*sin(theta_C)*w_C;
        //double y_R = getRearWheelPosition().getY();
        //return (T-(N+h*v_yR)/k+y_R)*f_R/I_zzR;
        return getCurrentXRAcceleration()/R;
    }

    /**
     * Compute the instantaneous angular acceleration of the front wheel
     * @return the acceleration in radians/s^2
     */
    public static double getCurrentThetaFAcceleration(){
        return getCurrentXFAcceleration()/R;
    }

    /**
     * Compute the instantaneous X-acceleration of the front wheel
     * @return the acceleration in m/s^2
     */
    private static double getCurrentXFAcceleration()
    {
        double theta_Cpp = getCurrentThetaCAcceleration();
        double x_Rpp = getCurrentXRAcceleration();
        return x_Rpp - d*cos(theta_C)*w_C*w_C - d*sin(theta_C)*theta_Cpp;
    }

    /**
     * Compute the instantaneous X-acceleration of the rear wheel
     * @return the acceleration in m/s^2
     */
    private static double getCurrentXRAcceleration()
    {
        double theta_Cpp = getCurrentThetaCAcceleration();
        return getCurrentXCAcceleration() + s_x*cos(theta_C)*w_C*w_C -s_y*sin(theta_C)*w_C*w_C + theta_Cpp*(s_x*sin(theta_C) + s_y*cos(theta_C));
    }

    /**
     * updates the car's position and velocity for the next timestep
     * @param deltaTime The amount of time (in milliseconds) that has passed since the last timestep.
     * @return The distance traveled by the car during this timestep (in meters).
     */
    public static double step(long deltaTime)
    {
        // Compute values for next timestep (car frame)
        double deltaTimeInSeconds = ((double)deltaTime/1000.0);
        double v_xC_next = v_xC + getCurrentXCAcceleration()*deltaTimeInSeconds;
        double x_C_next = x_C + v_xC*deltaTimeInSeconds;
        double v_yC_next = v_yC + getCurrentYCAcceleration()*deltaTimeInSeconds;
        double y_C_next = y_C + v_yC*deltaTimeInSeconds;
        double w_C_next = w_C + getCurrentThetaCAcceleration()*deltaTimeInSeconds;
        double theta_C_next = theta_C + w_C*deltaTimeInSeconds;

        // Compute values for next timestep (wheels)
        double w_R_next = w_R + getCurrentThetaRAcceleration()*deltaTimeInSeconds;
        double theta_R_next = theta_R + w_R*deltaTimeInSeconds;
        double w_F_next = w_F + getCurrentThetaFAcceleration()*deltaTimeInSeconds;
        double theta_F_next = theta_F + w_F*deltaTimeInSeconds;

        // Do some sanity checks:
        if(v_xC_next<0 && gear!=Gear.REVERSE) v_xC_next = 0;
        if(w_R_next<0 && gear!=Gear.REVERSE) w_R_next = 0.0;
        if(w_F_next<0 && gear!=Gear.REVERSE) w_F_next = 0.0;

        // Get the return value ready:
        double deltaX = x_C_next - x_C;

        // Update values (car frame)
        x_C = x_C_next;
        v_xC = v_xC_next;
        y_C = y_C_next;
        v_yC = v_yC_next;
        theta_C = theta_C_next;
        w_C = w_C_next;

        // Update values (wheels)
        w_R = w_R_next;
        theta_R = theta_R_next;
        w_F = w_F_next;
        theta_F = theta_F_next;

        //printCarStats();
        return deltaX;
    }

    /**
     * Prints key variables, For debugging purposes
     */
    public static void printCarStats()
    {
        System.out.println("CARFRAME");
        System.out.println("x_C = " + x_C);
        System.out.println("y_C = " + y_C);
        System.out.println("v_xC = " + v_xC);
        System.out.println("v_yC = " + v_yC);
        System.out.println("theta_C = " + theta_C);
        System.out.println("w_C = " + w_C);
        System.out.println("REARWHEEL");
        System.out.println("theta_R = " + theta_R);
        System.out.println("w_R = " + w_R);
        System.out.println("FRONTWHEEL");
        System.out.println("theta_F = " + theta_F);
        System.out.println("w_F = " + w_F);
        System.out.println("*********timestep**********");
    }

    /**
     * Compute the instantaneous accelerations for the current position and velocity of the car, to see if they look like they're in the right ballpark:
     */
    public static void runTests()
    {
        double thetaAccel = Car.getCurrentThetaCAcceleration();
        double xAccel = Car.getCurrentXCAcceleration();
        double yAccel = Car.getCurrentYCAcceleration();
        /**
        System.out.println("thetaAccel: " + thetaAccel);
        System.out.println("xAccel: " + xAccel);
        System.out.println("yAccel: " + yAccel);

        System.out.println("position of rear wheel is " + getRearWheelPosition());
        System.out.println("position of front wheel is " + getFrontWheelPosition());
        **/
        Car.v_xC += xAccel;
        Car.v_yC += 0; //yAccel;

        Car.x_C += Car.v_xC;
        Car.y_C += Car.v_yC;

        Car.rear_theta += Car.v_xC;
        Car.front_theta += Car.v_xC;

    }
}
