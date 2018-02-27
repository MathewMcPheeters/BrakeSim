package PhysicalDrivers;

public class LEDDriver {

  Color color;

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
