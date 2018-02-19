import javafx.beans.property.SimpleBooleanProperty;
import java.util.Timer;
import java.util.TimerTask;

public class EBS extends Thread
{
    private boolean terminate = false;
    private boolean running = true;
    private boolean braking = false;
    private volatile SimpleBooleanProperty trigger = new SimpleBooleanProperty();
    public void engageBrakes()
    {
      trigger.setValue(true);
    }
    public void disengageBrakes()
    {
        trigger.setValue(false);
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
                            pressure = 0.05;
                        }
                        else if(currentVelocity > 0.1 && currentVelocity <= 0.2)
                        {
                            pressure = 0.07;
                        }
                        else if(currentVelocity > 0.2 && currentVelocity <= 0.3)
                        {
                            pressure = 0.09;
                        }
                        else if(currentVelocity > 0.3 && currentVelocity <= 0.4)
                        {
                            pressure = 0.1;
                        }
                        else if(currentVelocity > 0.4 && currentVelocity <= 0.5)
                        {
                            pressure = 0.13;
                        }
                        else if(currentVelocity > 50.0 && currentVelocity <= 60)
                        {
                            pressure = 0.16;
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
