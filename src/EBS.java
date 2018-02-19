import javafx.beans.property.SimpleBooleanProperty;
import java.util.Timer;
import java.util.TimerTask;

public class EBS extends Thread
{
    private boolean terminate = false;
    private boolean running = true;
    private boolean braking = false;
    private volatile SimpleBooleanProperty trigger;
    public void triggerBrake()
    {

      if(trigger.getValue() == false)
      {
          trigger.setValue(true);
      }
      trigger.setValue(true);
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
        trigger.setValue(false);
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
                    System.out.println("Thread is running.....");
                    if(!braking)
                    {
                        braking = true;
                        System.out.println("Braking mechanism is engaging");
                        double currentVelocity = Car.getXVelocity();
                        double pressure = 0;
                        //Braking policy goes here
                        if(currentVelocity <= 0.1)
                        {
                            pressure = 5.0;
                        }
                        else if(currentVelocity > 0.1 && currentVelocity <= 0.2)
                        {
                            pressure = 7.0;
                        }
                        else if(currentVelocity > 20.0 && currentVelocity <= 30)
                        {
                            pressure = 9.0;
                        }
                        else if(currentVelocity > 30.0 && currentVelocity <= 40)
                        {
                            pressure = 11.0;
                        }
                        else if(currentVelocity > 40.0 && currentVelocity <= 50)
                        {
                            pressure = 13.0;
                        }
                        else if(currentVelocity > 50.0 && currentVelocity <= 60)
                        {
                            pressure = 16.0;
                        }
                        else if(currentVelocity > 60.0 && currentVelocity <= 80)
                        {
                            pressure = 20.0;
                        }
                        else if(currentVelocity > 80.0)
                        {
                            pressure = 23.0;
                        }
                        BrakeTimer timer = new BrakeTimer(pressure);
                        timer.runBrakeTask();
                        trigger.setValue(false);
                    }
                }
                else
                {
                 // System.out.println("Brake Trigger is False");
                }
            }
            try
            {
                Thread.sleep(500);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
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
            scheduleAtFixedRate(new TimerTask()
            {
                @Override
                public void run()
                {
                    System.out.println("Car X Velocity: "+Car.getXVelocity());

                    if(Car.getXVelocity()<= 0)
                    {
                      Car.setTorque(0);
                      pressure = 0;
                      cancel();
                    }

                    double currentTorque = Car.getTorque();
                    System.out.println("CURRENT TORQUE: "+currentTorque);
                    Car.setTorque(currentTorque + pressure);
                }
            },0,1000);
        }
    }
}
