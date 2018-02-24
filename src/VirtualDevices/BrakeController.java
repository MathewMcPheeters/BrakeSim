package VirtualDevices;

import Car.CarVariables;

public class BrakeController
{
  public void setBrakePressure(double pressure)
  {
    CarVariables.setBrakeTorque(pressure);
  }
  public double getBrakePressure()
  {
    return CarVariables.getBrakeTorque();
  }
}
