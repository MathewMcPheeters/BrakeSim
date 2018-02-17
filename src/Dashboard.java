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
public class Dashboard extends VBox
{

  public Dashboard(double spacing, SimulationWorker simulationWorker)
  {
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
          double speed = Double.parseDouble(speedText);
          Car.setV_xC(speed);
          simulationWorker.getSimulationArea().setSpeed(speed);
          System.out.println("Speed is " + speed);
        }
        catch(NumberFormatException e)
        {
          System.out.println("Invalid input speed");
        }
      }
    });
    Label milesPerHour = new Label("mi/hr");
    speed.getChildren().addAll(speedLabel,speedInput,milesPerHour,enterSpeed);

    HBox acceleration = new HBox(5);
    Label accelerationLabel = new Label("Acceleration:");
    TextField accelerationInput = new TextField();
    accelerationInput.setPrefColumnCount(5);
    Button enterAcceleration = new Button("Enter");
    enterAcceleration.setOnMouseClicked(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent event)
      {
        String accelerationText = accelerationInput.getText();
        if(accelerationText.equals("")) return;
        try
        {
          double acceleration = Double.parseDouble(accelerationText);
          Car.setAcceleration(acceleration);
          System.out.println("Acceleration is: "+acceleration);
        }
        catch(NumberFormatException e)
        {
          System.out.println("Invalid input acceleration");
        }
      }
    });
    Label milesPerHourSquared = new Label("mi/hr^2");
    acceleration.getChildren().addAll(accelerationLabel,accelerationInput,milesPerHourSquared,enterAcceleration);

    HBox gear = new HBox(10);
    Label gearLabel = new Label("Gear:");
    ToggleGroup toggleGroup = new ToggleGroup();
    RadioButton park = new RadioButton("Park");
    RadioButton reverse = new RadioButton("Reverse");
    RadioButton neutral = new RadioButton("Neutral");
    RadioButton drive = new RadioButton("Drive");
    park.setToggleGroup(toggleGroup);
    park.setOnAction((event ->
    {
      Car.setGear(Gear.PARK);
    }));
    reverse.setToggleGroup(toggleGroup);
    reverse.setOnAction((event ->
    {
      Car.setGear(Gear.REVERSE);
    }));
    neutral.setToggleGroup(toggleGroup);
    neutral.setOnAction((event ->
    {
      Car.setGear(Gear.NEUTRAL);
    }));
    drive.setToggleGroup(toggleGroup);
    drive.setOnAction((event ->
    {
      Car.setGear(Gear.DRIVE);
    }));
    gear.getChildren().addAll(gearLabel,park,reverse,neutral,drive);

    HBox simulationControl = new HBox(10);
    simulationControl.setAlignment(Pos.CENTER_LEFT);
    Button start = new Button("Start");
    start.setOnAction((event ->
    {
      if(Car.getGear() == null)
      {
        new ErrorDialog(Alert.AlertType.ERROR,"Please Select a gear for the car");
        return;
      }
      if(Car.getAcceleration() != 0 || Car.getXVelocity() != 0)
      {
        if (!simulationWorker.isAlive())
        {
          simulationWorker.start();
        }
      }
      else
      {
        String message = "Please enter an acceleration and/or velocity";
        ErrorDialog error = new ErrorDialog(Alert.AlertType.ERROR,message);
      }
    }));
    Button stop = new Button("Stop");
    stop.setOnAction((event ->
    {
      if(simulationWorker.isAlive())
      {
        simulationWorker.setRunning(false);
        simulationWorker.setTerminate(true);
      }
    }));
    Button reset = new Button("Reset");
    reset.setOnAction((event) ->
    {
      if(simulationWorker.isAlive())
      {
        simulationWorker.setRunning(false);
        simulationWorker.setTerminate(true);
      }
      Car.setX_C(0);
      Car.setY_C(0);
    });
    simulationControl.getChildren().addAll(start,stop,reset);
    getChildren().addAll(speed,acceleration,gear,simulationControl);
  }
}
