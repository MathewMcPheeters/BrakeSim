import javafx.beans.property.SimpleBooleanProperty;

import java.util.Timer;
import java.util.TimerTask;

/**
 * James Perry
 * CS 351
 * This class contains the Emergency Braking System logic. It runs on it's own
 * thread, and gets signalled to engage the braking mechanism by Dashboard.java
 */
public class EBS extends Thread
{
    private boolean terminate = false;
    private boolean running = true;
    private boolean braking = false;
    private volatile SimpleBooleanProperty trigger;
    private BrakeTimer timer;
    public void engageBrakes()
    {
        trigger.setValue(true);
    }
    public void disengageBrakes()
    {
        trigger.setValue(false);
        if(timer != null) timer.cancel();
    }
    public void terminate()
    {
        terminate = true;
    }
    public void setRunning(boolean run)
    {
        running = run;
    }

    public EBS()
    {
        trigger = new SimpleBooleanProperty(false);
    }
    @Override
    public void run()
    {
        while(!terminate)
        {
            while(running)
            {
                if(trigger.getValue() == true)
                {
                    if(!braking)
                    {
                        braking = true;
                        double currentVelocity = Car.getXVelocity();
                        double pressure = 0;
                        if(currentVelocity <= 10)
                        {
                            pressure = 5000;
                        }
                        else if(currentVelocity > 10 && currentVelocity <= 20)
                        {
                            pressure = 10000;
                        }
                        else if(currentVelocity > 20 && currentVelocity <= 30)
                        {
                            pressure = 18000;
                        }
                        else if(currentVelocity > 30 && currentVelocity <= 40)
                        {
                            pressure = 23000;
                        }
                        else if(currentVelocity > 40 && currentVelocity <= 50)
                        {
                            pressure = 26000;
                        }
                        else if(currentVelocity > 50.0 && currentVelocity <= 60)
                        {
                            pressure = 36000;
                        }
                        else if(currentVelocity > 60.0 && currentVelocity <= 80)
                        {
                            pressure = 38000;
                        }
                        else if(currentVelocity > 80.0)
                        {
                            pressure = 44000;
                        }
                        timer = new BrakeTimer(pressure);
                        timer.runBrakeTask();
                        trigger.setValue(false);
                    }
                }
            }
            if(timer != null) timer.cancel();
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        if(timer != null) timer.cancel();
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
            scheduleAtFixedRate(new TimerTask()
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
                    double currentTorque = Car.getBrakeTorque();
                    Car.setBrakeTorque(currentTorque + pressure);
                }
            },0,1000);
        }
    }
}
