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
    private final Image blue = new Image( "Resources/BlueButton.png" );
    private final Image blank = new Image("Resources/cat.jpg");

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
    public void update()
    {
        LEDNotificationDriver.Color setting = led.color;

        if(setting == LEDNotificationDriver.Color.BLUE)
        {
            buttonColorState.setImage( blue );
        }
    }
}
