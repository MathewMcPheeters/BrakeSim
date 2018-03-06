package VirtualDevices;

import PhysicalDrivers.BrakeControllerDriver;

/**
 * James Perry
 * CS 460
 * This class provides the Brake Controller virtual device. It can be used to apply force to, and obtain
 * the force currently applied to the emergency brake.
 * It is instantiated by Main.java and passed to EBS.java
 */
public class BrakeController
{
  BrakeControllerDriver brakeControllerDriver;
  public enum BrakeStatus
  {
    FULLY_ENGAGED, NOT_FULLY_ENGAGED, DISENGAGED
  }
  private BrakeStatus brakeStatus;

  public BrakeController(BrakeControllerDriver brakeControllerDriver)
  {
    this.brakeControllerDriver = brakeControllerDriver;
    brakeStatus = BrakeStatus.DISENGAGED;
  }

  /**
   * Original code by Team 07
   * Updated by Mat M., Team 09
   * @param pressure double value indicating the brake pressure to be applied
   * Modifications: Brake Controller now updates the brakeStatus variable based
   *                 on the brake force being applied
   */
  public void applyForce(double pressure)
  {
    brakeControllerDriver.applyForce(pressure);
    double force = brakeControllerDriver.currentForceApplied();
    force = force * 10000;
    if( force == 1.0)
    {
      this.brakeStatus = BrakeStatus.FULLY_ENGAGED;
    }
    else if( force == 0.0)
    {
      this.brakeStatus = BrakeStatus.DISENGAGED;
    }
    else
    {
      this.brakeStatus = BrakeStatus.NOT_FULLY_ENGAGED;
    }
  }

  public BrakeStatus getBrakeStatus()
  {
    return brakeStatus;
  }
}
