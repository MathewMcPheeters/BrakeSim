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

    public EBS()
    {
        trigger.setValue(true);
    }
    @Override
    public void run()
    {
        while(!terminate)
        {
            while(running)
            {

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
