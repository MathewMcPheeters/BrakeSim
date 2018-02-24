package VirtualDevices;

import Car.CarVariables;

public class ECU
{
  public double getSpeed()
  {
    return CarVariables.getV_xC();
  }
}
