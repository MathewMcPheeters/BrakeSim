import Car.CarVariables;
import VirtualDevices.BrakeButton;

/**
 * James Perry
 * CS 460
 * This class contains the Emergency Braking System logic. It runs on it's own
 * thread, and gets signalled to engage the braking mechanism by Dashboard.java
 *
 * /Update every 60th of a second.
 * /Transmission,speed,brake controller,light,
 * /Team two is doing the review.
 * /Team 8- sleep schedule, pick up code when it is done
 */
public class EBS
{
  private boolean braking = false;
  private Double pressure = 0.0;
  private BrakeButton brakeButton;

  public EBS(BrakeButton brakeButton)
  {
    this.brakeButton = brakeButton;
  }

  public void update()
  {
    if(brakeButton.getPosition() == BrakeButton.Position.DOWN)
    {
      if(!braking)
      {
        braking = true;
        double absVelocity = Math.abs(CarVariables.getV_xC());
        if (absVelocity <= 10)
        {
          pressure = 5000.;
        }
        else if (absVelocity > 10 && absVelocity <= 20)
        {
          pressure = 10000.;
        }
        else if (absVelocity > 20 && absVelocity <= 30)
        {
          pressure = 18000.;
        }
        else if (absVelocity > 30 && absVelocity <= 40)
        {
          pressure = 23000.;
        }
        else if (absVelocity > 40 && absVelocity <= 50)
        {
          pressure = 26000.;
        }
        else if (absVelocity > 50.0 && absVelocity <= 60)
        {
          pressure = 36000.;
        }
        else if (absVelocity > 60.0 && absVelocity <= 80)
        {
          pressure = 38000.;
        }
        else if (absVelocity > 80.0)
        {
          pressure = 44000.;
        }
        CarVariables.setBrakeTorque(pressure);
        braking = false;
        brakeButton.setPosition(BrakeButton.Position.UP);
      }
    }
  }
}

