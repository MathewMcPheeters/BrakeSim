/*******************************************************************************
 * Class: EBS
 * Author: Mathew McPheeters, T09
 * Usage:
 *         Class is coded to test functionality of all simulator functions
 *         EBS walks through functionality in POST then interacts with
 *         the environment.
 ******************************************************************************/
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
     * citation for URL code
     * http://www.java2s.com/Code/Java/JavaFX/wavfileplayer.htm
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

    /**
     * This method is a self test of components
     */
    private void initialize()
    {
        System.out.println("Initial self test in progress...");

        // play the warning tone to make sure it works
        System.out.println(("Playing audio tone..."));
        alarmNotification.playSound( clip );

        //change LED colors
        System.out.println("Changing colors of LED indicator to assure it works...");
        ledNotification.light( LEDNotificationDriver.Color.RED);
        ledNotification.light( LEDNotificationDriver.Color.ORANGE);
        ledNotification.light( LEDNotificationDriver.Color.BLUE);

    }
    public void update()
    {
        this.initialize();
    }
}
