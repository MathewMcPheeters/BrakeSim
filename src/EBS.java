import javafx.beans.property.SimpleBooleanProperty;

public class EBS extends Thread
{
    private boolean terminate = false;
    private boolean running = false;
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
                  double currentVelocity = Car.getXVelocity();
                  //Braking policy goes here
                  if(currentVelocity <= 10.0)
                  {

                  }
                  else if(currentVelocity > 10.0 && currentVelocity <= 20)
                  {

                  }
                  else if(currentVelocity > 20.0 && currentVelocity <= 30)
                  {

                  }
                  else if(currentVelocity > 30.0 && currentVelocity <= 40)
                  {

                  }
                  else if(currentVelocity > 40.0 && currentVelocity <= 50)
                  {

                  }
                  else if(currentVelocity > 50.0 && currentVelocity <= 60)
                  {

                  }
                  else if(currentVelocity > 60.0 && currentVelocity <= 80)
                  {

                  }
                  else if(currentVelocity > 80.0)
                  {

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
}
