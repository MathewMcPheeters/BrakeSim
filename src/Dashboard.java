import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
    Label speedDisplay = new Label("Current Speed: "+ Car.getXVelocity());
    speedDisplay.setAlignment(Pos.CENTER_LEFT);
    Car.getVelocityChange().addListener(new ChangeListener<Boolean>()
    {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
      {
        speedDisplay.setText("Current Speed: "+ Car.getXVelocity());
        Car.getVelocityChange().set(false);
      }
    });
    Label accelerationDisplay = new Label("Current Acceleration: "+Car.getAcceleration());
    accelerationDisplay.setAlignment(Pos.CENTER_LEFT);
    Car.getAccelerationChange().addListener(new ChangeListener<Boolean>()
    {
      @Override
      public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
      {
        accelerationDisplay.setText("Current Acceleration: "+ Car.getAcceleration());
        Car.getVelocityChange().set(false);
      }
    });
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
          //simulationWorker.getSimulationArea().setSpeed(speed);
          System.out.println("Speed is " + speed);
        }
        catch(NumberFormatException e)
        {
          System.out.println("Invalid input speed");
        }
      }
    });
    Label milesPerHour = new Label("mi/hr");
    speed.setAlignment(Pos.CENTER);
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
    acceleration.setAlignment(Pos.CENTER);
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
        if(Car.getGear() == Gear.NEUTRAL || Car.getGear() == Gear.PARK)
        {
          String message = "Error: Acceleration and/or speed cannot be nonzero if "+
                           "starting gear is Park or Neutral";
          new ErrorDialog(Alert.AlertType.ERROR,message);
          return;
        }
        if (!simulationWorker.isAlive())
        {
          simulationWorker.start();
        }
        else
        {
          simulationWorker.setRunning(true);
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
      }
    }));
    Button reset = new Button("Reset");
    reset.setOnAction((event) ->
    {
      if(simulationWorker.isAlive())
      {
        simulationWorker.setRunning(false);
        simulationWorker.terminate();
      }
      Car.setX_C(0);
      Car.setY_C(0);
    });
    Button brake = new Button("Brake");
    brake.setOnAction((event) ->
    {
      if(simulationWorker.isAlive())
      {
        Car.getBrakeSystem().triggerBrake();
      }
      else
      {
        String message = "Error: Simulation is not running";
        new ErrorDialog(Alert.AlertType.ERROR,message);
      }
    });
    simulationControl.getChildren().addAll(start,stop,reset,brake);
    simulationControl.setAlignment(Pos.CENTER);
    getChildren().addAll(speedDisplay,accelerationDisplay,speed,acceleration,gear,simulationControl);
  }
}
