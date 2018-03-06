/*******************************************************************************
 * Class: ButtonUI
 * Author: Mathew McPheeters, T09
 * Usage:
 *         Class displays the button's state in the user interfacs
 ******************************************************************************/
import PhysicalDrivers.LEDNotificationDriver;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class ButtonUI extends VBox
{
    public ImageView buttonColorState;
    public Label buttonStateLabel;
    private LEDNotificationDriver led;

    //final values that hold the images to display
    private final Image blue = new Image( "Resources/BlueButton.png" );
    private final Image blank = new Image("Resources/cat.png");
    private final Image red = new Image("Resources/RedButton.png");
    private final Image orange = new Image("Resources/OrangeButton.png");

    /**
     * Default constructor
     * @param l the class managing the LED color state
     */
    public ButtonUI( LEDNotificationDriver l)
    {
        this.setPadding(new Insets(50, 50, 50, 50));
        this.led = l;
        this.buttonColorState = new ImageView();
        buttonColorState.setFitWidth(80);
        buttonColorState.setFitHeight(80);
        buttonColorState.setImage(blank);
        this.buttonStateLabel = new Label("Button LED State");
        this.getChildren().addAll(buttonStateLabel, buttonColorState);
    }

    /**
     * @method update
     * @usage checks the current state of the LED color and send the button
     * image on the display
     */
    public void update()
    {
        LEDNotificationDriver.Color setting = led.color;

        if(setting == LEDNotificationDriver.Color.BLUE)
        {
            buttonColorState.setImage( blue );
        }
        else if( setting == LEDNotificationDriver.Color.RED)
        {
            buttonColorState.setImage( red );
        }
        else if( setting == LEDNotificationDriver.Color.ORANGE)
        {
            buttonColorState.setImage( orange );
        }
        else
        {
            System.out.println("Reaching else");
            buttonColorState.setImage( blank );
        }
    }
}
