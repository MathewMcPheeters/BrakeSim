import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;

public class Dashboard extends VBox
{


  public Dashboard(int spacing)
  {
    super(spacing);

    Label speedDisplay = new Label("Current Speed: "+ Car.getXVelocity()+" m/s");
    speedDisplay.setAlignment(Pos.CENTER_LEFT);

    Label accelerationDisplay = new Label("Current Acceleration: "+Car.getCurrentXCAcceleration()+" m/s^2");
    accelerationDisplay.setAlignment(Pos.CENTER_LEFT);

    // Note: We currently cannot change the velocity in the middle of the simulation; The wheel rotations will be wrong.
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
          if(speed>=0)
          {
            Car.setV_xC(speed);
            speedDisplay.setText("Current Speed: "+ Car.getXVelocity()+" m/s");
          }
          else
          {
            String message = "Speed cannot be negative";
            new ErrorDialog(AlertType.ERROR,message);
          }
        }
        catch(NumberFormatException e)
        {
          new ErrorDialog(AlertType.ERROR,"Invalid input speed");
        }
      }
    });
    Label milesPerHour = new Label("m/s");
    speed.setAlignment(Pos.CENTER);
    speed.getChildren().addAll(speedLabel,speedInput,milesPerHour,enterSpeed);

    HBox acceleration = new HBox(5);
    Label accelerationLabel = new Label("Acceleration torque\n (from gas pedal):");
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
          if(acceleration>=0)
          {
            Car.setAccelerationTorque(acceleration);
            accelerationDisplay.setText("Current Acceleration: "+Car.getCurrentXCAcceleration()+" m/s^2");
          }
          else
          {
            String message = "Acceleration must be positive";
            new ErrorDialog(AlertType.ERROR,message);
          }
        }
        catch(NumberFormatException e)
        {
          new ErrorDialog(AlertType.ERROR,"Invalid Input Acceleration");
        }
      }
    });
    Label milesPerHourSquared = new Label("N-m");
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
      Main.timeline.play();
      if(Car.getGear() == null)
      {
        new ErrorDialog(AlertType.ERROR,"Please Select a gear for the car");
        return;
      }
      if(Car.getAccelerationTorque() == 0.0 && Car.getXVelocity() == 0.0)
      {
        if(Car.getGear() != Gear.NEUTRAL && Car.getGear() != Gear.PARK)
        {
          String message = "Error: Acceleration and/or speed must be nonzero if "+
                  "starting gear is Drive or Reverse";
          new ErrorDialog(AlertType.ERROR,message);
          return;
        }
        return;
      }
      if(Car.getAccelerationTorque() != 0 || Car.getXVelocity() != 0)
      {
        if(Car.getGear() == Gear.NEUTRAL || Car.getGear() == Gear.PARK)
        {
          String message = "Error: Acceleration and speed must be zero if "+
                  "starting gear is Park or Neutral";
          new ErrorDialog(AlertType.ERROR,message);
          return;
        }
      }
      else
      {
        String message = "Please enter an acceleration and/or velocity";
        new ErrorDialog(AlertType.ERROR,message);
      }
    }));


    Button stop = new Button("Stop");
    stop.setOnAction((event ->
    {
      Main.timeline.pause();
    }));
    Button reset = new Button("Reset");
    reset.setOnAction((event) ->
    {

    });
    Button brake = new Button("Brake");
    brake.setOnAction((event) ->
    {

    });
    simulationControl.getChildren().addAll(start,stop,reset,brake);
    simulationControl.setAlignment(Pos.CENTER);
    getChildren().addAll(speedDisplay,accelerationDisplay,speed,acceleration,gear,simulationControl);
  }


  public class ErrorDialog extends Alert
  {
    public ErrorDialog(Alert.AlertType type,String message)
    {
      super(type);
      setContentText(message);
      showAndWait();
    }
  }

}

