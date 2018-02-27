package VirtualDevices;

import PhysicalDrivers.ECUDriver;

public class ECU
{
  private ECUDriver ecuDriver;
  public ECU(ECUDriver ecuDriver)
  {
    this.ecuDriver = ecuDriver;
  }

  public double getCurrentSpeed()
  {
    return ecuDriver.getCurrentSpeed();
  }
  public double getMaxSpeed()
  {
    return ecuDriver.getMaxSpeed();
  }
  public boolean isGearInPark()
  {
    return ecuDriver.isGearInPark();
  }
}
