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
    private static final double m_to_px = 12.5;

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

    // Variables regarding the environment
    private static double accelerationTorque = 0.0; // Torque due to pressing the accelerator (N-m)
    private static double brakeTorque = 0.0; // torque applied by the brakes (N-m)
    private static double T = 0.0; // Combined torque applied by the brakes and the gas pedal (N-m)

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
    public static void engageBrakes()
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
    }

    /**
     * Sets some Car variables for the given car velocity, assuming rolling contact
     */
    public static void setVariablesRollingContact()
    {
        w_R = v_xC/CarConstants.R;
        w_F = v_xC/CarConstants.R;
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
    }
    /** returns the position of the rear wheel */
    public static Point2D getRearWheelPosition()
    {
        double x_R = x_C - CarConstants.s_x*cos(theta_C) + CarConstants.s_y*sin(theta_C);
        double y_R = y_C - CarConstants.s_x*sin(theta_C) - CarConstants.s_y*cos(theta_C);
        return new Point2D(x_R, y_R);
    }

    /** returns the position of the front wheel */
    public static Point2D getFrontWheelPosition()
    {
        Point2D rearWheelPos = getRearWheelPosition();
        double x_F = rearWheelPos.getX() + CarConstants.d*cos(theta_C);
        double y_F = rearWheelPos.getY() + CarConstants.d*sin(theta_C);
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
        return -(T/CarConstants.R)/(CarConstants.I_zzF/(CarConstants.R*CarConstants.R)-CarConstants.I_zzR/(CarConstants.R*CarConstants.R)+CarConstants.m_C+CarConstants.m_R-CarConstants.m_F) + (CarConstants.s_x*cos(theta_C)*w_C*w_C+CarConstants.s_x*sin(theta_C)*theta_Cpp-CarConstants.s_y*sin(theta_C)*w_C*w_C+CarConstants.s_y*cos(theta_C)*theta_Cpp)*(CarConstants.I_zzR/(CarConstants.R*CarConstants.R)-CarConstants.I_zzF/(CarConstants.R*CarConstants.R)+CarConstants.m_F-CarConstants.m_R)/(CarConstants.I_zzF/(CarConstants.R*CarConstants.R)-CarConstants.I_zzR/(CarConstants.R*CarConstants.R)+CarConstants.m_C+CarConstants.m_R-CarConstants.m_F)+(CarConstants.d*cos(theta_C)*w_C*w_C+CarConstants.d*sin(theta_C)*theta_Cpp)*(CarConstants.I_zzF/(CarConstants.R*CarConstants.R)-CarConstants.m_F)/(CarConstants.I_zzF/(CarConstants.R*CarConstants.R)-CarConstants.I_zzR/(CarConstants.R*CarConstants.R)+CarConstants.m_C+CarConstants.m_R-CarConstants.m_F);
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
        return getCurrentXRAcceleration()/CarConstants.R;
    }

    /**
     * Compute the instantaneous angular acceleration of the front wheel
     * @return the acceleration in radians/s^2
     */
    public static double getCurrentThetaFAcceleration(){
        return getCurrentXFAcceleration()/CarConstants.R;
    }

    /**
     * Compute the instantaneous X-acceleration of the front wheel
     * @return the acceleration in m/s^2
     */
    private static double getCurrentXFAcceleration()
    {
        double theta_Cpp = getCurrentThetaCAcceleration();
        double x_Rpp = getCurrentXRAcceleration();
        return x_Rpp - CarConstants.d*cos(theta_C)*w_C*w_C - CarConstants.d*sin(theta_C)*theta_Cpp;
    }

    /**
     * Compute the instantaneous X-acceleration of the rear wheel
     * @return the acceleration in m/s^2
     */
    private static double getCurrentXRAcceleration()
    {
        double theta_Cpp = getCurrentThetaCAcceleration();
        return getCurrentXCAcceleration() + CarConstants.s_x*cos(theta_C)*w_C*w_C -CarConstants.s_y*sin(theta_C)*w_C*w_C + theta_Cpp*(CarConstants.s_x*sin(theta_C) + CarConstants.s_y*cos(theta_C));
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

        // For the time being, before slipping wheels are considered, this is okay.
        theta_F_next = theta_F + v_xC_next/(2*Math.PI)*m_to_px;
        theta_R_next = theta_F_next;


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

    /** Prints key variables, For debugging purposes */
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
}