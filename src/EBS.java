import PhysicalDrivers.BrakeButtonDriver;
import VirtualDevices.*;

/**
 * James Perry
 * CS 460
 * This class contains a simple Emergency Braking System.
 */
public class EBS
{
  private boolean braking = false;
  private Double pressure = 0.0;

  // Virtual drivers:
  private AlarmNotification alarmNotification;
  private BrakeButton brakeButton;
  private BrakeController brakeController;
  private VehicleElectronics vehicleElectronics;
  private LEDNotification ledNotification;

  public EBS(BrakeButton brakeButton, BrakeController brakeController, VehicleElectronics vehicleElectronics,
             AlarmNotification alarmNotification, LEDNotification ledNotification)
  {
    this.brakeButton = brakeButton;
    this.brakeController = brakeController;
    this.vehicleElectronics = vehicleElectronics;
    this.alarmNotification = alarmNotification;
    this.ledNotification = ledNotification;
  }

  public void update()
  {
    if(brakeButton.getPosition() == BrakeButtonDriver.Position.DOWN)
    {
      if(!braking)
      {
        braking = true;
        double absVelocity = Math.abs(vehicleElectronics.getCurrentSpeed());
        if (absVelocity <= 10)
        {
          pressure = 5000.;
        }
        else if (absVelocity > 10 && absVelocity <= 20)
        {
          pressure = 10000.;
        }
        else if (absVelocity > 20 && absVelocity <= 30)
        {
          pressure = 18000.;
        }
        else if (absVelocity > 30 && absVelocity <= 40)
        {
          pressure = 23000.;
        }
        else if (absVelocity > 40 && absVelocity <= 50)
        {
          pressure = 26000.;
        }
        else if (absVelocity > 50.0 && absVelocity <= 60)
        {
          pressure = 36000.;
        }
        else if (absVelocity > 60.0 && absVelocity <= 80)
        {
          pressure = 38000.;
        }
        else if (absVelocity > 80.0)
        {
          pressure = 44000.;
        }
        brakeController.applyForce(pressure);
        braking = false;
        brakeButton.setPosition(BrakeButtonDriver.Position.UP);
      }
    }
  }
}

