/*******************************************************************************
 * Class: EBS
 * Author: Mathew McPheeters, T09
 * Usage:
 *         This is a blank template class to get you started
 ******************************************************************************/
import Car.Gear;
import PhysicalDrivers.BrakeButtonDriver;
import PhysicalDrivers.LEDNotificationDriver;
import VirtualDevices.*;
import javafx.scene.media.AudioClip;
import java.net.URL;

public class EBS
{
    /* Local variables for holding references to external system */
    private AlarmNotification alarmNotification;
    private BrakeButton brakeButton;
    private BrakeController brakeController;
    private VehicleElectronics vehicleElectronics;
    private LEDNotification ledNotification;

    /*
     * How to load a custom audio file
     */
    final URL resource = getClass().getResource("Resources/Bell.wav");
    final AudioClip clip = new AudioClip(resource.toString());

    /*
     * Constructor for EBS
     * References to external vehicle environment passed here
     * Code taken from original T07 code
     */
    public EBS(BrakeButton brakeButton, BrakeController brakeController, VehicleElectronics vehicleElectronics, AlarmNotification alarmNotification, LEDNotification ledNotification)
    {
        this.brakeButton = brakeButton;
        this.brakeController = brakeController;
        this.vehicleElectronics = vehicleElectronics;
        this.alarmNotification = alarmNotification;
        this.ledNotification = ledNotification;
    }

    public void update()
    {
        // put your code here
    }
}
