import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Dashboard extends VBox
{
  public Dashboard(double spacing)
  {
    super(spacing);

    HBox speed = new HBox(5);
    Label speedLabel = new Label("Initial Speed:");
    TextField speedInput = new TextField();
    speedInput.setPrefColumnCount(5);
    Button enterSpeed = new Button("Enter");
    enterSpeed.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        String speedText = speedInput.getText();
        if(speedText.equals("")) return;
        try
        {
          double speed = Double.parseDouble(speedText);
          System.out.println("hit the button for speed");
        }
        catch(NumberFormatException e)
        {
          System.out.println("Invalid input speed");
        }
      }
    });
    Label milesPerHour = new Label("m/s");
    speed.setAlignment(Pos.CENTER);
    speed.getChildren().addAll(speedLabel,speedInput,milesPerHour,enterSpeed);

    getChildren().addAll(speed);


  }
}
