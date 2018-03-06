package Car;

import static Car.CarVariables.*;
import static Car.CarConstants.*;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CarPhysics
{
  private double slipTolerance = 0.02; // how small must (w_slip-w_actual)/w_slip be before we assume rolling contact?

  /** computes the current x-coordinate of the rear wheel */
  public double getX_R(){
        return x_C - s_x*cos(theta_C) + s_y*sin(theta_C);
    }
  /** computes the current y-coordinate of the rear wheel */
  public double getY_R(){
        return y_C - s_x*sin(theta_C) - s_y*cos(theta_C);
    }
  /** computes the current x-coordinate of the front wheel */
  public double getX_F(){
        return x_C+(d-s_x)*cos(theta_C) + s_y*sin(theta_C);
    }
  /** computes the current y-coordinate of the front wheel */
  public double getY_F(){
        return y_C + (d-s_x)*sin(theta_C) - s_y*cos(theta_C);
    }
  /** computes the current x-velocity of the rear wheel */
  private double getX_Rp(){
        return v_xC + s_x*sin(theta_C)*w_C + s_y*cos(theta_C)*w_C;
    }
  /** computes the current y-velocity of the rear wheel */
  private double getY_Rp(){
        return v_yC - s_x*cos(theta_C)*w_C + s_y*sin(theta_C)*w_C;
    }
  /** computes the current x-velocity of the front wheel */
  private double getX_Fp(){
        return v_xC - (d-s_x)*sin(theta_C)*w_C + s_y*cos(theta_C)*w_C;
    }
  /** computes the current y-velocity of the front wheel */
  private double getY_Fp(){
        return v_yC + (d-s_x)*cos(theta_C)*w_C + s_y*sin(theta_C)*w_C;
    }

  /** computes the current x-acceleration of the rear wheel minus the x-acceleration of the car frame */
  private double getX_Rpp_rel(double theta_Cpp)
  {
    return (s_x*cos(theta_C) - s_y*sin(theta_C))*w_C*w_C + (s_x*sin(theta_C) + s_y*cos(theta_C))*theta_Cpp;
  }

  /** computes the current y-acceleration of the rear wheel minus the y-acceleration of the car frame */
  private double getY_Rpp_rel(double theta_Cpp)
  {
    return (s_x*sin(theta_C)+s_y*cos(theta_C))*w_C*w_C + (s_y*sin(theta_C)-s_x*cos(theta_C))*theta_Cpp;
  }

  /** computes the current x-acceleration of the front wheel minus the x-acceleration of the car frame */
  private double getX_Fpp_rel(double theta_Cpp)
  {
    return -((d-s_x)*cos(theta_C)+s_y*sin(theta_C))*w_C*w_C + (s_y*cos(theta_C)-(d-s_x)*sin(theta_C))*theta_Cpp;
  }

  /** computes the current y-acceleration of the front wheel minus the y-acceleration of the car frame */
  private double getY_Fpp_rel(double theta_Cpp)
  {
    return (s_y*cos(theta_C)-(d-s_x)*sin(theta_C))*w_C*w_C + ((d-s_x)*cos(theta_C)+s_y*sin(theta_C))*theta_Cpp;
  }

  /** computes the current y-acceleration of the car frame */
  private double getY_Cpp(double theta_Cpp)
  {
    double numerator = k*(R-getY_R()) - h*getY_Rp() - m_R*g - m_R*getY_Rpp_rel(theta_Cpp) + k*(R-getY_F()) - h*getY_Fp() - m_F*g - m_F*getY_Fpp_rel(theta_Cpp) - m_C*g;
    double denominator = m_C + m_R + m_F;
    return numerator/denominator;
  }
  /**
   * computes the current x-acceleration of the car frame. Input specifies whether the front and rear wheels are in
   * rolling contact or are sliding on the pavement.
  */
  public double getX_Cpp(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    double numerator = 1.0;
    double denominator = 1.0;
    if(!isRolling_R && !isRolling_F)
    {
      numerator = -m_R*getX_Rpp_rel(theta_Cpp) + mu*k*(getY_R()-R) + mu*h*getY_Rp() - m_F*getX_Fpp_rel(theta_Cpp) - mu*k*(R-getY_F()) + mu*h*getY_Fp();
      denominator = m_C + m_R + m_F;
    }
    else if(!isRolling_R && isRolling_F)
    {
      numerator = -m_R*getX_Rpp_rel(theta_Cpp) + mu*k*(getY_R()-R) + mu*h*getY_Rp() - (m_F-I_zzF/(R*R))*getX_Fpp_rel(theta_Cpp);
      denominator = m_C + m_R + (m_F-I_zzF/(R*R));
    }
    else if(isRolling_R && !isRolling_F)
    {
      numerator = -getTorque(theta_Cpp, isRolling_R, isRolling_F, deltaTime)/R - (m_R-I_zzR/(R*R))*getX_Rpp_rel(theta_Cpp) - m_F*getX_Fpp_rel(theta_Cpp) + mu*k*(getY_F()-R) + mu*h*getY_Fp();
      denominator = m_C + (m_R-I_zzR/(R*R)) + m_F;
    }
    else
    {
      numerator = -getTorque(theta_Cpp, isRolling_R, isRolling_F, deltaTime)/R - (m_R-I_zzR/(R*R))*getX_Rpp_rel(theta_Cpp) - (m_F-I_zzF/(R*R))*getX_Fpp_rel(theta_Cpp);
      denominator = m_C + (m_R-I_zzR/(R*R)) + (m_F-I_zzF/(R*R));
    }
    return numerator/denominator;
  }

  /** computes the y-directional force from the rear wheel onto the frame */
  private double getFy_R(double theta_Cpp)
  {
    return k*(R-getY_R()) - h*getY_Rp() - m_R*g - m_R*(getY_Rpp_rel(theta_Cpp)+getY_Cpp(theta_Cpp));
  }

  /** computes the y-directional force from the front wheel onto the frame */
  private double getFy_F(double theta_Cpp)
  {
    return k*(R-getY_F()) - h*getY_Fp() - m_F*g - m_F*(getY_Fpp_rel(theta_Cpp)+getY_Cpp(theta_Cpp));
  }

  /**
   * computes the x-directional force from the rear wheel onto the frame under the following conditions:
   *      rear wheel is slipping
   */
  private double getFx_R_S(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    return getTorque(theta_Cpp,isRolling_R, isRolling_F, deltaTime)/R + (m_R - I_zzR/(R*R))*(getX_Rpp_rel(theta_Cpp)+getX_Cpp(theta_Cpp,isRolling_R,isRolling_F,deltaTime));
  }

  /**
   * computes the x-directional force from the rear wheel onto the frame under the following conditions:
   *      rear wheel is in rolling contact (no slippage)
   */
  private double getFx_R_R(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    return m_R*(getX_Rpp_rel(theta_Cpp)+getX_Cpp(theta_Cpp,isRolling_R,isRolling_F, deltaTime)) + mu*k*(R-getY_R()) - mu*h*getY_Rp();
  }

  /**
   * computes the x-directional force from the front wheel onto the frame under the following conditions:
   *      front wheel is slipping
   */
  private double getFx_F_S(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    return (m_F - I_zzF/(R*R))*(getX_Fpp_rel(theta_Cpp)+getX_Cpp(theta_Cpp,isRolling_R,isRolling_F,deltaTime));
  }

  /**
   * computes the x-directional force from the front wheel onto the frame under the following conditions:
   *      front wheel is in rolling contact (no slippage)
   */
  private double getFx_F_R(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    return m_F*(getX_Fpp_rel(theta_Cpp)+getX_Cpp(theta_Cpp,isRolling_R,isRolling_F, deltaTime)) + mu*k*(R-getY_F()) - mu*h*getY_Fp();
  }

  /** computes the torque acting on the rear wheel */
  private double getTorque(double theta_Cpp, boolean isRolling_R, boolean isRolling_F , long deltaTime)
  {
    double torque = getBaseTorque();
    double deltaTimeInSeconds = (double)deltaTime/1000.0;

    // If the base torque would cause the rear wheel to spin backwards while the car is in Drive, then adjust the torque
    // Todo: this seems kinda kludgy. Is there a better way to handle this?
    if(w_R <= 0.0 && getGear()==Gear.DRIVE)
    {
      torque = 0.0;
    }
    // If the base torque would cause the rear wheel to spin forwards while the car is in Reverse, then adjust the torque
    // Todo: this seems kinda kludgy. Is there a better way to handle this?
    if(w_R >= 0.0 && getGear()==Gear.REVERSE)
    {
      torque = 0.0;
    }
    // If the base torque would cause the rear wheel to spin at all while the car is in Park, then adjust the torque
    // Todo: this seems kinda kludgy. Is there a better way to handle this?
    if(abs(w_R) >= 0.0 && getGear()==Gear.PARK)
    {
      torque = 0.0;
    }
    return torque;
  }
  private double getBaseTorque(){
        return getBrakeTorque() - getAccelerationTorque();
    }

  /**
   * Adjust theta_Cpp until this formula returns 0. Upon success, you have found the current value of theta_Cpp.
   * This formula is valid under the following conditions:
   *      rear wheel is sliding
   *      front wheel is sliding
   */
  private double computeZero(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    if(isRolling_R && isRolling_F) return getFy_F(theta_Cpp)*(d-s_x) - getFx_F_R(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - getFy_R(theta_Cpp)*s_x - getFx_R_R(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - I_zzC*theta_Cpp;
    else if(isRolling_R && !isRolling_F) return getFy_F(theta_Cpp)*(d-s_x) - getFx_F_S(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - getFy_R(theta_Cpp)*s_x - getFx_R_R(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - I_zzC*theta_Cpp;
    else if(!isRolling_R && isRolling_F) return getFy_F(theta_Cpp)*(d-s_x) - getFx_F_R(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - getFy_R(theta_Cpp)*s_x - getFx_R_S(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - I_zzC*theta_Cpp;
    else return getFy_F(theta_Cpp)*(d-s_x) - getFx_F_S(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - getFy_R(theta_Cpp)*s_x - getFx_R_S(theta_Cpp,isRolling_R,isRolling_F, deltaTime)*s_y - I_zzC*theta_Cpp;
  }


  /** Computes theta_Cpp (the angular acceleration of the car frame) using root-finding (bisection method)*/
  public double getTheta_Cpp(boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    double errorTolerance = 0.001;
    double upperBound = 5;
    double lowerBound = -500;
    double middle;

    double zero_upper = computeZero(upperBound, isRolling_R, isRolling_F, deltaTime);
    double zero_lower = computeZero(lowerBound, isRolling_R, isRolling_F, deltaTime);
    double zero_middle;

    if(zero_lower*zero_upper > 0)
    {
      System.out.println("ROOT-FINDER CANNOT FIND VALUE FOR THETA_CPP.");
      return 0.0;
    }

    // Iterate the bisection method until desired tolerance is achieved:
    do
    {
      middle = (upperBound + lowerBound)/2.0;
      zero_middle = computeZero(middle, isRolling_R, isRolling_F, deltaTime);
      if(zero_middle==0.0) return middle; // very tiny chance that this will occur. Take this out for efficiency if needed.
      if(zero_lower*zero_middle<0) upperBound = middle;
      else lowerBound = middle;
    }
    while((upperBound-lowerBound)>errorTolerance);

    //System.out.println("theta_Cpp = " + (upperBound-lowerBound)/2.0);
    return (upperBound-lowerBound)/2.0;
  }



  /** Computes the angular acceleration of the rear wheel */
  private double getTheta_Rpp(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    double theta_Rpp;

    if(isRolling_R)
    {
      //System.out.println("X_Cpp = " + getX_Cpp(theta_Cpp,isRolling_R, isRolling_F));
      theta_Rpp = (getX_Rpp_rel(theta_Cpp) + getX_Cpp(theta_Cpp,isRolling_R, isRolling_F, deltaTime))/CarConstants.R;
    }
    else
    {
      theta_Rpp = (getTorque(theta_Cpp, isRolling_R, isRolling_F, deltaTime)-R*mu*(k*(R-getY_R()) - h*getY_Rp()))/I_zzR;
    }
    return theta_Rpp;
  }

  /** Computes the angular acceleration of the front wheel */
  private double getTheta_Fpp(double theta_Cpp, boolean isRolling_R, boolean isRolling_F, long deltaTime)
  {
    double theta_Fpp;
    if(isRolling_F)
    {
      theta_Fpp = (getX_Fpp_rel(theta_Cpp) + getX_Cpp(theta_Cpp,isRolling_R, isRolling_F, deltaTime))/CarConstants.R;
    }
    else
    {
      theta_Fpp = -(R*mu*(k*(R-getY_F()) - h*getY_Fp()))/I_zzF;
    }

    // If the car is in Drive, let's assume that the wheels cannot spin in reverse. Likewise, if the car is in
    // Reverse, its wheels cannot spin forward
    /*double deltaTimeInSeconds = (double)deltaTime/1000.0;
    if(getGear()==Gear.DRIVE && w_R+theta_Fpp*deltaTimeInSeconds<0) theta_Fpp = -w_R/deltaTimeInSeconds;
    else if(getGear()==Gear.REVERSE && w_R+theta_Fpp*deltaTimeInSeconds>0) theta_Fpp = -w_R/deltaTimeInSeconds;*/

    return theta_Fpp;
  }

  /**
   * updates the car's position and velocity for the next timestep
   * @param deltaTime The amount of time (in milliseconds) that has passed since the last timestep.
   * @return The distance traveled by the car during this timestep (in meters).
   */
  public double step(long deltaTime)
  {
    double deltaTimeInSeconds = ((double)deltaTime/1000.0);
    boolean isRolling_R;
    boolean isRolling_F;

    // Determine whether the rear wheel is sliding or in rolling contact:
    double w_roll = getX_Rp()/R;
    if(abs(getX_Rp()) > 0.1) isRolling_R = (w_roll-w_R)/w_roll < slipTolerance;
    else isRolling_R = abs(v_xC) < 0.1; // use this test instead when w_roll is small, to prevent division by 0.

    // Determine whether the front wheel is sliding or in rolling contact:
    w_roll = getX_Fp()/R;
    if(abs(getX_Fp()) > 0.1) isRolling_F = (w_roll-w_F)/w_roll < slipTolerance;
    else isRolling_F = abs(v_xC) < 0.1; // use this test instead when w_roll is small, to prevent division by 0.

    if(!isRolling_F) System.out.println("the front wheel is slipping");
    if(!isRolling_R) System.out.println("the rear wheel is slipping");

    // Compute theta_Cpp (the angular acceleration of the car frame) using a root-finding method:
    double theta_Cpp = getTheta_Cpp(isRolling_R,isRolling_F, deltaTime);

    // If this value is tiny, just assume it's zero (for stability reasons)
    if(theta_Cpp < 0.001) theta_Cpp = 0;

    // Compute values for next timestep (car frame)
    double v_xC_next = v_xC + getX_Cpp(theta_Cpp, isRolling_R, isRolling_F, deltaTime)*deltaTimeInSeconds;
    double x_C_next = x_C + v_xC*deltaTimeInSeconds;
    double v_yC_next = v_yC + getY_Cpp(theta_Cpp)*deltaTimeInSeconds;
    double y_C_next = y_C + v_yC*deltaTimeInSeconds;
    double w_C_next = w_C + theta_Cpp*deltaTimeInSeconds;
    double theta_C_next = theta_C + w_C*deltaTimeInSeconds;

    // Compute values for next timestep (wheels)
    double w_R_next = w_R + getTheta_Rpp(theta_Cpp,isRolling_R,isRolling_F,deltaTime)*deltaTimeInSeconds;
    double w_F_next = w_F + getTheta_Fpp(theta_Cpp,isRolling_R,isRolling_F,deltaTime)*deltaTimeInSeconds;
    double theta_R_next = theta_R + w_R*deltaTimeInSeconds;
    double theta_F_next = theta_F + w_F*deltaTimeInSeconds;

    // Get the return value ready:
    double deltaX = x_C_next - x_C;

    /* Sanity Checks */
    // The back wheel cannot spin backwards if the car is in Drive:
    if(w_R_next<0 && getGear()==Gear.DRIVE) w_R_next = 0.0;
    // The back wheel cannot spin forwards if the car is in Reverse:
    if(w_R_next>0 && getGear()==Gear.REVERSE) w_R_next = 0.0;
    // The back wheel cannot spin at all if the car is in Park:
    if(abs(w_R_next)>0 && getGear()==Gear.PARK) w_R_next = 0.0;
    // If both wheels are in rolling contact and the back wheel is no longer spinning due to one of the above modifications, then the car frame should not be moving at all in the next frame. The front wheel shouldn't spin, either:
    if(isRolling_R && isRolling_F && w_R_next == 0)
    {
      v_xC_next = 0.0;
      w_F_next = 0.0;
    }

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

    // Update bookkeeping variables
    if(getBrakeTorque() != 0.0) CarVariables.incrementStopDistance(deltaX);

    //printCarStats();
    return deltaX;
  }

  /** Prints key variables, For debugging purposes */
  public void printCarStats()
  {
    System.out.println("CARFRAME");
    System.out.println("x_C = " + x_C);
    System.out.println("y_C = " + y_C);
    System.out.println("v_xC = " + v_xC);
    System.out.println("v_yC = " + v_yC);
    System.out.println("theta_C = " + theta_C);
    System.out.println("w_C = " + w_C);
    System.out.println("REARWHEEL");
    System.out.println("x_R = " + getX_R());
    System.out.println("y_R = " + getY_R());
    System.out.println("theta_R = " + theta_R);
    System.out.println("w_R = " + w_R);
    System.out.println("FRONTWHEEL");
    System.out.println("x_F = " + getX_F());
    System.out.println("y_F = " + getY_F());
    System.out.println("theta_F = " + theta_F);
    System.out.println("w_F = " + w_F);
    System.out.println("*********timestep**********");
  }
}
