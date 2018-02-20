
/**
 * This thread needs to control the position of the car
 */
public class SimulationWorker extends Thread
{
    private int simulationTimeElapsed = 0;
    private int tickInterval = 20; // 40 ==> 25 updates per second
    //private int physicsInterval = 5; // physics updates happen many times per animation update.
    private long lastTick = 0;
    private boolean terminate = false;
    private boolean running = true;
    public void terminate()
  {
    terminate = true;
  }
    public void setRunning(boolean b)
  {
    running = b;
  }
    private SimulationArea simulationArea = null;
    private Dashboard dashboard;
    public void resetLastTick(){
    lastTick = System.currentTimeMillis();
  }

    @Override
    public void run()
    {
        Car.setVariablesRollingContact();
        lastTick = System.currentTimeMillis();
        while(!terminate)
        {
            while(running)
            {
                tick();
            }
            try
            {
                Thread.sleep(50);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void tick()
    {

        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastTick;

        if(deltaTime >= tickInterval)
        {
            //simulationArea.updateAnimations();
            //System.out.println("simulationTimeElapsed= " + simulationTimeElapsed);

            // bookkeeping
            lastTick = currentTime;
            simulationTimeElapsed ++;

            // updates
            double deltaX = Car.step(deltaTime);
            simulationArea.carVis.update();
            simulationArea.update(deltaX);
        }

        //Start animations at the beginning of the simulation
        if(simulationTimeElapsed == 0)
        {
        }
    }

    public void setSimulationArea(SimulationArea area)
    {
        simulationArea = area;
        this.dashboard = dashboard;
    }

    public SimulationArea getSimulationArea(){
        return simulationArea;
  }
}
