package VirtualDevices;

public class Light
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
