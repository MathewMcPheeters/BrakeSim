/**
 * James Perry
 * CS 460
 * This class contains the Emergency Braking System logic. It runs on it's own
 * thread, and gets signalled to engage the braking mechanism by Dashboard.java
 */
public class EBS
{
  private boolean braking = false;
  private Double pressure = 0.0;

  public void disengageBrakes()
  {
    Car.setBrakeTorque(0);
  }

  public void engageBrakes()
  {
    if (!braking)
    {
      braking = true;
      double currentVelocity = Car.getXVelocity();
      if (currentVelocity <= 10)
      {
        pressure = 500.;
      }
      else if (currentVelocity > 10 && currentVelocity <= 20)
      {
        pressure = 1000.;
      }
      else if (currentVelocity > 20 && currentVelocity <= 30)
      {
        pressure = 1800.;
      }
      else if (currentVelocity > 30 && currentVelocity <= 40)
      {
        pressure = 2300.;
      }
      else if (currentVelocity > 40 && currentVelocity <= 50)
      {
        pressure = 2600.;
      }
      else if (currentVelocity > 50.0 && currentVelocity <= 60)
      {
        pressure = 3600.;
      }
      else if (currentVelocity > 60.0 && currentVelocity <= 80)
      {
        pressure = 3800.;
      }
      else if (currentVelocity > 80.0)
      {
        pressure = 4400.;
      }
      Car.setBrakeTorque(pressure);
    }
  }
}

