package VirtualDevices;
/**
 * James Perry
 * CS 460
 * This class contains the Brake Button virtual device. It can be used to press and depress the emergency brake
 * button, by calling the setPosition method. It can also be used to obtain the current position of the button
 * by calling getPosition()
 */
public class BrakeButton
{
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
