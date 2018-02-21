import Car.CarVariables;
import Car.Gear;
import javafx.animation.Timeline;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

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
      double currentVelocity = CarVariables.getV_xC();
      if (abs(currentVelocity) <= 10)
      {
        pressure = 5000.;
      }
      else if (abs(currentVelocity) > 10 && abs(currentVelocity) <= 20)
      {
          pressure = 10000.;
      }
      else if (abs(currentVelocity) > 20 && abs(currentVelocity) <= 30)
      {
          pressure = 18000.;
      }
      else if (abs(currentVelocity) > 30 && abs(currentVelocity) <= 40)
      {
        pressure = 23000.;
      }
      else if (abs(currentVelocity) > 40 && abs(currentVelocity) <= 50)
      {
        pressure = 26000.;
      }
      else if (abs(currentVelocity) > 50.0 && abs(currentVelocity) <= 60)
      {
        pressure = 36000.;
      }
      else if (abs(currentVelocity) > 60.0 && abs(currentVelocity) <= 80)
      {
        pressure = 38000.;
      }
      else if (abs(currentVelocity) > 80.0)
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
          if(abs(CarVariables.getV_xC())<= 0)
          {
            //CarVariables.setBrakeTorque(0);
            pressure = 0;
            braking = false;
            cancel();
          }
          else{
            double currentTorque = CarVariables.getBrakeTorque();
            if(CarVariables.getGear()== Gear.DRIVE) CarVariables.setBrakeTorque(currentTorque + pressure);
            else if(CarVariables.getGear()== Gear.REVERSE) CarVariables.setBrakeTorque(currentTorque - pressure);
            System.out.println("Applying Pressure");
            System.out.println(CarVariables.getV_xC());
          }
        }
      },0,1000);
    }
  }
}

