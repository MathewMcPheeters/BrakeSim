import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

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
  private Timeline timeline;
  private Double pressure = 0.0;

  public void disengageBrakes()
  {
  }

  public void engageBrakes()
  {
    if (!braking)
    {
      braking = true;
      double currentVelocity = Car.getXVelocity();
      if (currentVelocity <= 10)
      {
        pressure = 5000.;
      }
      else if (currentVelocity > 10 && currentVelocity <= 20)
      {
        pressure = 10000.;
      }
      else if (currentVelocity > 20 && currentVelocity <= 30)
      {
        pressure = 18000.;
      }
      else if (currentVelocity > 30 && currentVelocity <= 40)
      {
        pressure = 23000.;
      }
      else if (currentVelocity > 40 && currentVelocity <= 50)
      {
        pressure = 26000.;
      }
      else if (currentVelocity > 50.0 && currentVelocity <= 60)
      {
        pressure = 36000.;
      }
      else if (currentVelocity > 60.0 && currentVelocity <= 80)
      {
        pressure = 38000.;
      }
      else if (currentVelocity > 80.0)
      {
        pressure = 44000.;
      }
      BrakeTimer timer = new BrakeTimer(pressure);
      timer.runBrakeTask();
    }
  }

  private class BrakeTimer extends Timer
  {
    private double pressure;
    public BrakeTimer(Double pressure)
    {
      this.pressure = pressure;
    }

    public void runBrakeTask()
    {
      scheduleAtFixedRate(
      new TimerTask()
      {
        @Override
        public void run()
        {
          if(Car.getXVelocity()<= 0)
          {
            Car.setBrakeTorque(0);
            pressure = 0;
            braking = false;
            cancel();
          }
          Car.stopping_distance+=Math.abs(Car.getXVelocity());
          double currentTorque = Car.getBrakeTorque();
          Car.setBrakeTorque(currentTorque + pressure);
          System.out.println("Applying Pressure");
          System.out.println(Car.getXVelocity());
        }
      },0,1000);
    }
  }
}

