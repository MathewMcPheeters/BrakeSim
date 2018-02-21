package Car;

/**
 * This class contains constants for the physics calculations.
 */
public class CarConstants
{
  final static double m_R = 8.0; // mass of rear wheel (kg)
  final static double m_F = 8.0; // mass of front wheel (kg)
  final static double m_C = 1300; // mass of frame (kg)
  final static double I_zzR = 0.72; // moment of inertia of rear wheels (kg-m^2)
  final static double I_zzF = 0.72; // moment of inertia of front wheels (kg-m^2)
  final static double I_zzC = 11700; // moment of inertia of the car frame (kg-m^2)
  final static double R = 0.4318; // wheel radius (m)
  final static double d = 5.0800; // distance between the wheels (m)
  final static double s_x = 3; // x-distance between rear wheel and frame's center of mass (m)
  final static double s_y = 0.0682; // y-distance between wheels and frame's center of mass (m)
  final static double k = 25000.0; // The spring coefficient of the car's suspension system (N/m)
  final static double h = 10000.0; // The dampening coefficient of the car's suspension system (N-s/m)
  final static double mu = 0.7; // coefficient of friction between wheels and ground (unitless)
  final static double f_max = 500; // maximum static friction (N)
  final static double g = 9.81; // gravity (m/s^2)

  // Getters
  public static double getR(){
    return R;
  }
}