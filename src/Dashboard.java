import Car.CarPhysics;
import Car.CarVariables;
import PhysicalDrivers.BrakeButtonDriver;
import VirtualDevices.BrakeController;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import VirtualDevices.BrakeButton;
/**
 * This class is used for the user interface that allows setting the speed, acceleration, gear, and controlling the
 * animation.
 */
public class Dashboard extends VBox
{
  private Label speedDisplay;
  private Label accelerationDisplay;
  private Label stopDistanceDisplay;
  private EBS ebs;
  private CarPhysics carPhysics;

  public Dashboard(int spacing, Timeline timeLine, EBS ebs, CarPhysics carPhysics, BrakeButton brakeButton,
                   BrakeController brakeController)
  {
    super(spacing);

    this.ebs = ebs;
    this.carPhysics = carPhysics;

    speedDisplay = new Label("Current Speed: "+ CarVariables.getV_xC() +" m/s");
    speedDisplay.setAlignment(Pos.CENTER_LEFT);

    accelerationDisplay = new Label("Current Acceleration: 0.0 m/s^2");
    accelerationDisplay.setAlignment(Pos.CENTER_LEFT);

    stopDistanceDisplay = new Label("Stopping Distance: "+CarVariables.getStopDistance()+" m");
    stopDistanceDisplay.setAlignment(Pos.CENTER_LEFT);

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
        if(timeLine.getStatus() == Animation.Status.RUNNING)
        {
          String message = "Speed cannot be changed when simulator is running";
          new ErrorDialog(AlertType.ERROR,message);
          return;
        }
        String speedText = speedInput.getText();
        if(speedText.equals("")) return;
        try
        {
          double speed = Double.parseDouble(speedText);
          if(speed>=0)
          {
            CarVariables.setV_xC(speed);
            CarVariables.setBrakeTorque(0);
            speedDisplay.setText("Current Speed: "+ CarVariables.getV_xC()+" m/s");
          }
          else
          {
            String message = "Speed must be positive";
            new ErrorDialog(AlertType.ERROR,message);
            speedInput.clear();
          }
        }
        catch(NumberFormatException e)
        {
          new ErrorDialog(AlertType.ERROR,"Invalid input speed");
          speedInput.clear();
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
            CarVariables.setAccelerationTorque(acceleration);
            double currentThetaCpp = carPhysics.getTheta_Cpp(CarPhysics.WheelStatus.ROLLING, CarPhysics.WheelStatus.ROLLING, 16);
            double currentAcceleration = carPhysics.getX_Cpp(currentThetaCpp,CarPhysics.WheelStatus.ROLLING,CarPhysics.WheelStatus.ROLLING,16);
            CarVariables.setBrakeTorque(0.0); // Brake torque must be set to zero here because calling getX_Cpp can cause brakeTorque to be set to a nonzero value.
            accelerationDisplay.setText("Current Acceleration: "+Math.round(100*currentAcceleration)/100.0+" m/s^2");
          }
          else
          {
            String message = "Acceleration must be positive";
            new ErrorDialog(AlertType.ERROR,message);
            accelerationInput.clear();
          }
        }
        catch(NumberFormatException e)
        {
          new ErrorDialog(AlertType.ERROR,"Invalid Input Acceleration");
          accelerationInput.clear();
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
      CarVariables.setGear(Car.Gear.PARK);
    }));
    reverse.setToggleGroup(toggleGroup);
    reverse.setOnAction((event ->
    {
      CarVariables.setGear(Car.Gear.REVERSE);
    }));
    neutral.setToggleGroup(toggleGroup);
    neutral.setOnAction((event ->
    {
      CarVariables.setGear(Car.Gear.NEUTRAL);
    }));
    drive.setToggleGroup(toggleGroup);
    drive.setOnAction((event ->
    {
      CarVariables.setGear(Car.Gear.DRIVE);
    }));
    gear.getChildren().addAll(gearLabel,park,reverse,neutral,drive);

    HBox simulationControl = new HBox(10);
    simulationControl.setAlignment(Pos.CENTER_LEFT);
    Button start = new Button("Start");
    start.setOnAction((event ->
    {
      //timeLine.play();
      if(CarVariables.getGear() == null)
      {
        new ErrorDialog(AlertType.ERROR,"Please Select a gear for the car");
        return;
      }
      if(CarVariables.getGear() == Car.Gear.PARK || CarVariables.getGear() == Car.Gear.NEUTRAL)
      {
        if(CarVariables.getAccelerationTorque() != 0 || CarVariables.getV_xC() != 0)
        {
          String message = "Starting acceleration and speed must be zero if starting gear is "+
                  "Park or Neutral";
          new ErrorDialog(AlertType.ERROR,message);
          return;
        }
      }
      if(CarVariables.getGear() == Car.Gear.DRIVE || CarVariables.getGear() == Car.Gear.REVERSE)
      {
        if(CarVariables.getAccelerationTorque() == 0 && CarVariables.getV_xC() == 0)
        {
          String message = "Starting acceleration and speed cannot both be zero if starting gear is "+
                  "either Drive or Reverse";
          new ErrorDialog(AlertType.ERROR,message);
          return;
        }
      }
      if(timeLine.getStatus() == Animation.Status.PAUSED || timeLine.getStatus() == Animation.Status.STOPPED)
      {
        //Car.stopping_distance = 0;
        if(CarVariables.getGear() == Car.Gear.REVERSE)
        {
          System.out.println(CarVariables.getV_xC());
          if(CarVariables.getV_xC() > 0)
          {
            CarVariables.setV_xC(CarVariables.getV_xC()*-1);
          }
        }
        else if(CarVariables.getGear() == Car.Gear.DRIVE)
        {
          if(CarVariables.getV_xC() < 0)
          {
            CarVariables.setV_xC(CarVariables.getV_xC()*-1);
          }
        }
        CarVariables.setRollingContact();
        brakeButton.setPosition(BrakeButtonDriver.Position.UP);
        brakeController.applyForce(0);
        timeLine.play();
      }
      else
      {
        new ErrorDialog(AlertType.ERROR,"Simulation is already running");
      }
    }));

    Button stop = new Button("Stop");
    stop.setOnAction((event ->
    {
      timeLine.pause();
    }));
    Button reset = new Button("Reset");
    reset.setOnAction((event) ->
    {
      stop.fire();
      CarVariables.resetVariables();
      updateLabels();
    });
    Button brake = new Button("Brake");
    brake.setOnAction((event) ->
    {
      if(timeLine.getStatus() == Animation.Status.RUNNING)
      {
        //ebs.engageBrakes();
        if(brakeButton.getPosition() == BrakeButtonDriver.Position.UP)
        {
          brakeButton.setPosition(BrakeButtonDriver.Position.DOWN);
        }
      }
    });
    simulationControl.getChildren().addAll(start,stop,reset,brake);
    simulationControl.setAlignment(Pos.CENTER);
    getChildren().addAll(speedDisplay,accelerationDisplay, stopDistanceDisplay ,speed,acceleration,gear,simulationControl);
  }

  public void updateLabels()
  {
    speedDisplay.setText("Current Speed: "+ Math.round(100*CarVariables.getV_xC())/100.0+" m/s");
    stopDistanceDisplay.setText("Stopping Distance: " + Math.round(100*CarVariables.getStopDistance())/100.0+" m");
    double currentThetaCpp = carPhysics.getTheta_Cpp(CarPhysics.WheelStatus.ROLLING, CarPhysics.WheelStatus.ROLLING, 16);
    double currentAcceleration = carPhysics.getX_Cpp(currentThetaCpp,CarPhysics.WheelStatus.ROLLING,CarPhysics.WheelStatus.ROLLING,16);
    accelerationDisplay.setText("Current Acceleration: "+ Math.round(100*currentAcceleration)/100.0+" m/s^2");
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
