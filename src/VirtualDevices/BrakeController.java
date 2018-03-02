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
  }

  public void applyForce(double pressure)
  {
    brakeControllerDriver.applyForce(pressure);
  }

}
