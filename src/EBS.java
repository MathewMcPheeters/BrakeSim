/*******************************************************************************
 * Class: EBS
 * Author: Mathew McPheeters, T09
 * Usage:
 *         Class is coded to test functionality of all simulator functions
 *         EBS walks through functionality in POST then interacts with
 *         the environment.
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
     * Brake system state variables
     */
    private Gear gear;
    private double speed; // in meters / second
    private BrakeButtonDriver.Position pos;
    private BrakeController.BrakeStatus state;

    /*
     * Local variable holding audio clip to send to audio system
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

        this.initialize(); //run a self test to assure functionality of system
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
        ledNotification.light( LEDNotificationDriver.Color.RED );
        ledNotification.light( LEDNotificationDriver.Color.ORANGE );
        ledNotification.light( LEDNotificationDriver.Color.BLUE );

        //check the gear
        gear = vehicleElectronics.getGear();
        System.out.print("Gear = " + gear + "\n");

        //get the current speed
        speed = vehicleElectronics.getCurrentSpeed();
        System.out.print("Speed = "+ speed + "\n");


    }

    /**
     * Update the internal state varables by asking each external interface
     * for an updated value
     */
    private void updateVariables()
    {
        gear = vehicleElectronics.getGear();
        speed = vehicleElectronics.getCurrentSpeed();
        pos = brakeButton.getPosition();
        state = brakeController.getBrakeStatus();
    }
    public void update()
    {
        this.updateVariables();

        if( pos == BrakeButtonDriver.Position.DOWN )
        {
            /* release the brake */
            if( state == BrakeController.BrakeStatus.FULLY_ENGAGED )
            {
                brakeController.applyForce(0.0);
                ledNotification.light( LEDNotificationDriver.Color.BLUE );
            }

            else if( state == BrakeController.BrakeStatus.NOT_FULLY_ENGAGED )
            {
                while ( state != BrakeController.BrakeStatus.FULLY_ENGAGED )
                {
                    brakeController.applyForce( 0.5 );
                }
            }
            /* parking brake or emergency brake */
            else if( state == BrakeController.BrakeStatus.DISENGAGED )
            {
                if( gear == Gear.PARK ) //parking brake mode
                {
                    brakeController.applyForce(1.0);
                    ledNotification.light( LEDNotificationDriver.Color.RED );
                }
                else // emergency brake mode
                {

                }
            }
            else
            {
                System.out.println("ERROR: This shouldn't happen in EBS");
            }

            // when a down button is detected, reset the button position to up
            brakeButton.setPosition( BrakeButtonDriver.Position.UP);
        }
    }
}
