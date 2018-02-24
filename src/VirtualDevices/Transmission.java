package VirtualDevices;
public class Transmission
{
  public enum Gear
  {
    PARK,REVERSE,NEUTRAL,DRIVE
  }
  private Gear gear = Gear.PARK;
  public Gear getGear()
  {
    return gear;
  }

}
