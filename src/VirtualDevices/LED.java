package VirtualDevices;
/**
 * James Perry
 * CS 460
 * This class contains the LED virtual device. It can be used to set the color
 * of the LED light on the Emergency Brake Button.
 *
 */
public class LED
{
  public enum Color
  {
    RED,
    ORANGE,
    BLUE
  };
  private Color color;
  public void setState(Color color)
  {
    this.color = color;
  }
}
