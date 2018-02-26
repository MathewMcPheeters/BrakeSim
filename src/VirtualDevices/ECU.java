package VirtualDevices;

import Car.CarConstants;
import Car.CarVariables;
import Car.Gear;

public class ECU
{
  public double getCurrentSpeed()
  {
    return CarVariables.getV_xC();
  }
  public double getMaxSpeed()
  {
    return CarConstants.getMaxSpeed();
  }

  public boolean isGearInPark()
  {
    if(CarVariables.getGear() == Gear.PARK) return true;
    else return false;
  }
}
