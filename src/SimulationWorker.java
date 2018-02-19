
/**
 * This thread needs to control the position of the car
 */
public class SimulationWorker extends Thread
{
  int speed = 5; //TODO: temporary value. Speed/Acceleration updates from Car will be needed for animation timing
  private int simulationTimeElapsed = 0;
  private int tickInterval = 16; //Tick every second
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
  @Override
  public void run()
  {
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

  private void tick(){

    long currentTime = System.currentTimeMillis();
    long deltaTime = currentTime - lastTick;

    if(deltaTime >= tickInterval){
      //simulationArea.updateAnimations();
      lastTick = currentTime;
      simulationTimeElapsed ++;
      System.out.println("simulationTimeElapsed= " + simulationTimeElapsed);
      simulationArea.carVis.update();
      Car.runTests();
      simulationArea.update();
    }

    //Start animations at the beginning of the simulation
    if(simulationTimeElapsed == 0){
    }
  }

  public void setSimulationArea(SimulationArea area){
    simulationArea = area;
  }

  public SimulationArea getSimulationArea(){
        return simulationArea;
  }
}
