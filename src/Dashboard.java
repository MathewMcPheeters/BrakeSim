import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Everything to do with the dashboard is now defined in here. Note that this class extends VBox, so we can add
 * an instance of Dashboard to a Scene just as we would add a Vbox.
 */
public class Dashboard extends VBox {

    public Dashboard(double spacing, SimulationWorker simulationWorker){
        super(spacing);
        HBox speed = new HBox(5);
        Label speedLabel = new Label("Speed:");
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
                int speed = Integer.parseInt(speedText);
                System.out.println(speed);
              }
              catch(NumberFormatException e)
              {
                System.out.println("Invalid input speed");
              }
            }
        });
        speed.getChildren().addAll(speedLabel,speedInput,enterSpeed);

        HBox acceleration = new HBox(5);
        Label accelerationLabel = new Label("Acceleration:");
        TextField accelerationInput = new TextField();
        accelerationInput.setPrefColumnCount(5);
        Button enterAcceleration = new Button("Enter");
        acceleration.getChildren().addAll(accelerationLabel,accelerationInput,enterAcceleration);

        HBox gear = new HBox(10);
        Label gearLabel = new Label("Gear:");
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton park = new RadioButton("Park");
        RadioButton reverse = new RadioButton("Reverse");
        RadioButton neutral = new RadioButton("Neutral");
        RadioButton drive = new RadioButton("Drive");
        park.setToggleGroup(toggleGroup);
        reverse.setToggleGroup(toggleGroup);
        neutral.setToggleGroup(toggleGroup);
        drive.setToggleGroup(toggleGroup);
        gear.getChildren().addAll(gearLabel,park,reverse,neutral,drive);

        HBox simulationControl = new HBox(10);
        simulationControl.setAlignment(Pos.CENTER_LEFT);
        Button start = new Button("Start");
        start.setOnAction((event -> {
            System.out.println("start button pressed!");
        }));
        Button stop = new Button("Stop");
        Button reset = new Button("Reset");
        simulationControl.getChildren().addAll(start,stop,reset);
        getChildren().addAll(speed,acceleration,gear,simulationControl);
    }
}
