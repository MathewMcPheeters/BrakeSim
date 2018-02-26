package VirtualDevices;

import Car.CarConstants;
import Car.CarVariables;
/**
 * James Perry
 * CS 460
 * This class provides the Brake Controller virtual device. It can be used to apply force to, and obtain
 * the force currently applied to the emergency brake.
 * It is instantiated by Main.java and passed to EBS.java
 */
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
