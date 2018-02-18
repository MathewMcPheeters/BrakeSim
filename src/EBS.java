import javafx.beans.property.SimpleBooleanProperty;

/**
 * Simple policy idea:
 * Apply varying degrees of braking, depending on velocity
 */
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
                  //Braking policy goes here
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
