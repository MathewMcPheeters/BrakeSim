package VirtualDevices;

import Car.Gear;
import PhysicalDrivers.VehicleElectronicsDriver;

public class VehicleElectronics
{
  private VehicleElectronicsDriver driver;
  public VehicleElectronics(VehicleElectronicsDriver driver)
  {
    this.driver = driver;
  }
  public double getCurrentSpeed()
    {
        return driver.getCurrentSpeed();
    }
  public Gear getGear(){return driver.getGear();}
}
