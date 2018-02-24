package VirtualDevices;

public class Light
{
  public enum State {ON,OFF};
  private State state;
  public void setState(State state)
  {
    this.state = state;
  }
}
