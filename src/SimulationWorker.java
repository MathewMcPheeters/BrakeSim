/**
 * This thread needs to control the position of the car
 */
public class SimulationWorker extends Thread
{
  private boolean terminate = false;
  private boolean running = true;
  public void setTerminate(boolean b)
  {
    terminate = b;
  }
  public void setRunning(boolean b)
  {
    running = b;
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
