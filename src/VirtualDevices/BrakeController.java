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

  public void applyForce(double pressure)
  {
    brakeControllerDriver.applyForce(pressure);
    double force = brakeControllerDriver.currentForceApplied();
    force = force * 10000;
    if( force == 1.0)
    {
      this.brakeStatus = BrakeStatus.FULLY_ENGAGED;
      System.out.println("Brake Controller state change: FULLY_ENGAGED");
    }
  }

  public BrakeStatus getBrakeStatus()
  {
    return brakeStatus;
  }
}
