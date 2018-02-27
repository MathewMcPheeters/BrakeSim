package VirtualDevices;

import PhysicalDrivers.BrakeButtonDriver;
import PhysicalDrivers.BrakeButtonDriver.Position;

/**
 * James Perry
 * CS 460
 * This class contains the Brake Button virtual device. It can be used to press and depress the emergency brake
 * button, by calling the setPosition method. It can also be used to obtain the current position of the button
 * by calling getPosition()
 */
public class BrakeButton
{
  public BrakeButtonDriver brakeButtonDriver;

  public BrakeButton(BrakeButtonDriver brakeButtonDriver)
  {
    this.brakeButtonDriver = brakeButtonDriver;
  }

  public void setPosition(Position position)
  {
    brakeButtonDriver.setPosition(position);
  }
  public Position getPosition()
  {
    return brakeButtonDriver.getPosition();
  }

}
