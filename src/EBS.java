import javafx.beans.property.SimpleBooleanProperty;
import java.util.Timer;
import java.util.TimerTask;

public class EBS extends Thread
{
    private boolean terminate = false;
    private boolean running = true;
    private boolean braking = false;
    private SimpleBooleanProperty trigger;
    public void triggerBrake()
    {
      trigger.set(true);
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
                if(trigger.get())
                {
                    if(!braking)
                    {
                        braking = true;
                        System.out.println("Braking mechanism is engaging");
                        double currentVelocity = Car.getXVelocity();
                        double pressure = 0;
                        //Braking policy goes here
                        if(currentVelocity <= 10.0)
                        {
                            pressure = 5.0;
                        }
                        else if(currentVelocity > 10.0 && currentVelocity <= 20)
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
