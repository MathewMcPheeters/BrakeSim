package VirtualDevices;

import Car.CarConstants;
import Car.CarVariables;

public class BrakeController
{
  public void applyForce(double pressure)
  {
    CarVariables.setBrakeTorque(pressure);
  }
  public double currentForceApplied()
  {
    return CarVariables.getBrakeTorque()/ CarConstants.getMaxBrakeForce();
  }
}
