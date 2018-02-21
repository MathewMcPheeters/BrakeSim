import javafx.animation.AnimationTimer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * James Perry
 * CS 460
 * This class contains the Emergency Braking System logic. It runs on it's own
 * thread, and gets signalled to engage the braking mechanism by Dashboard.java
 */
public class EBS
{
  private boolean braking = false;
  private BrakeTimer timer;

  public void disengageBrakes()
  {

  }

  public void engageBrakes()
  {
    if (!braking)
    {
      braking = true;
      double currentVelocity = Car.getXVelocity();
      double pressure = 0;
      if (currentVelocity <= 10)
      {
        pressure = 5000;
      }
      else if (currentVelocity > 10 && currentVelocity <= 20)
      {
        pressure = 10000;
      }
      else if (currentVelocity > 20 && currentVelocity <= 30)
      {
        pressure = 18000;
      }
      else if (currentVelocity > 30 && currentVelocity <= 40)
      {
        pressure = 23000;
      }
      else if (currentVelocity > 40 && currentVelocity <= 50)
      {
        pressure = 26000;
      }
      else if (currentVelocity > 50.0 && currentVelocity <= 60)
      {
        pressure = 36000;
      }
      else if (currentVelocity > 60.0 && currentVelocity <= 80)
      {
        pressure = 38000;
      }
      else if (currentVelocity > 80.0)
      {
        pressure = 44000;
      }
      timer = new BrakeTimer(pressure);
      //timer.runBrakeTask();
    }
  }
  //Need to delete this
  private class BrakeTimer extends AnimationTimer
  {

    private double pressure;
    private boolean signFlip = false;
    private long start;
    public BrakeTimer(Double pressure)
        {
            this.pressure = pressure;
        }

    @Override
    public void handle(long now)
    {

    }
    /**
    public void runBrakeTask()
    {
      scheduleAtFixedRate(
      new TimerTask()
            {
                @Override
                public void run()
                {
      if(Car.getXVelocity()== 0)
      {
        Car.setBrakeTorque(0);
        pressure = 0;
        braking = false;
        cancel();
      }
      if(Car.getXVelocity()< 0 && signFlip == false)
      {
        pressure *= -1;
        signFlip = true;
      }
      double currentTorque = Car.getBrakeTorque();
      Car.setBrakeTorque(currentTorque + pressure);
            }
            },0,1000);
      }
    }
     **/
  }
}
