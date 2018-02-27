package PhysicalDrivers;

public class BrakeButtonDriver {
  private Position position = Position.UP;
  public void setPosition(Position position)
    {
        this.position = position;
    }

  public Position getPosition()
    {
        return position;
    }

  public enum Position
  {
    UP, DOWN
  }
}
