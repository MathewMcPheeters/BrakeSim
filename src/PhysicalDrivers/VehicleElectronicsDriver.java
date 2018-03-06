package PhysicalDrivers;

import Car.CarVariables;
import Car.Gear;

public class VehicleElectronicsDriver
{
    public double getCurrentSpeed()
    {
        return CarVariables.getV_xC();
    }
    public Gear getGear()
    {
        return CarVariables.getGear();
    }
}
