package PhysicalDrivers;

public class LEDNotificationDriver
{
  public Color color;

  public void light(Color color)
  {
    this.color = color;
  }

  public enum Color
  {
    RED,
    ORANGE,
    BLUE,
    UNLIT
  }
}
