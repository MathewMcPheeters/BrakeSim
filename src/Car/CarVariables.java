package Car;

/**
 * Contains information about the car. Everything in here will be static so that the information can be accessed
 * globally by any interface and by the physics simulation without having to pass an instance of Car around.
 */
public class CarVariables
{
    private static Gear gear;

    // Variables regarding the position and velocity of the car.
    static double x_C = 5; // x-position of car's center of mass (m)
    static double y_C = 0.5; // y-position of car's center of mass (m)
    static double theta_C = 0.0; // car frame's angle of rotation (radians)
    static double v_xC = 10.0; // x-velocity of car's center of mass (m/s)
    static double v_yC = 0.0; // y-velocity of car's center of mass (m/s)
    static double w_C = 0.0; // rate of rotation of car frame (radians/s)

    // Variables regarding the rotation of the wheels:
    static double theta_R = 0.0; // Rear wheel's angle of rotation (radians)
    static double w_R = 0.0; // Rear wheel's rate of rotation (radians/s)
    static double theta_F = 0.0; // Front wheel's angle of rotation (radians)
    static double w_F = 0.0; // Front wheel's rate of rotation (radians/s)

    // Variables regarding the environment
    private static double accelerationTorque = 0.0; // Torque due to pressing the accelerator (N-m)
    private static double brakeTorque = 0.0; // torque applied by the brakes (N-m)

    // Setters
    public static void setGear(Gear newGear){
        gear = newGear;
    }
    public static void setAccelerationTorque(double newAccelerationTorque){
        accelerationTorque = newAccelerationTorque;
    }
    public static void setV_xC(double newV_xC){
        v_xC = newV_xC;
    }
    public static void setBrakeTorque(double newBrakeTorque)
    {
        brakeTorque = newBrakeTorque;
    }
    /**
     * Sets wheel rotational velocities for the given car velocity, assuming rolling contact
     */
    public static void setRollingContact()
    {
        w_R = v_xC/CarConstants.R;
        w_F = v_xC/CarConstants.R;
    }



    // Getters
    public static double getX_C(){
        return x_C;
    }
    public static double getY_C(){
        return y_C;
    }
    public static double getTheta_C(){
        return theta_C;
    }
    public static double getTheta_R(){
        return theta_R;
    }
    public static double getTheta_F(){
        return theta_F;
    }
    public static double getV_xC(){return v_xC;}
    public static Gear getGear()
    {
        return gear;
    }
    public static double getBrakeTorque(){return brakeTorque;}
    public static double getAccelerationTorque(){
        return accelerationTorque;
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
}