import javafx.animation.AnimationTimer;
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
    private Label speedDisplay;
    private Label accelerationDisplay;

    public Dashboard(double spacing, SimulationWorker simulationWorker)
    {
        super(spacing);

        speedDisplay = new Label("Current Speed: "+ Car.getXVelocity());
        speedDisplay.setAlignment(Pos.CENTER_LEFT);

        accelerationDisplay = new Label("Current Acceleration: "+Car.getCurrentXCAcceleration());
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
                    Car.setV_xC(speed);
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


        // Note: we *CAN* change the acceleration in the middle of the simulation.
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
                    Car.setAccelerationTorque(acceleration);
                }
                catch(NumberFormatException e)
                {
                    new ErrorDialog(Alert.AlertType.ERROR,"Invalid Input Acceleration");
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
            if(Car.getGear() == null)
            {
                new ErrorDialog(Alert.AlertType.ERROR,"Please Select a gear for the car");
                return;
            }
            if(Car.getAccelerationTorque() != 0 || Car.getXVelocity() != 0)
            {
                if(Car.getGear() == Gear.NEUTRAL || Car.getGear() == Gear.PARK)
                {
                    String message = "Error: Acceleration and/or speed cannot be nonzero if "+
                                     "starting gear is Park or Neutral";
                new ErrorDialog(Alert.AlertType.ERROR,message);
                return;
                }
                simulationWorker.resetLastTick(); // For smoothly re-starting a simulation that was stopped.
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
            }
            Car.getBrakeSystem().disengageBrakes();
            Car.resetVariables();
            Car.setVariablesRollingContact();
            simulationWorker.resetLastTick();

            accelerationInput.clear();
            speedInput.clear();
        });
        Button brake = new Button("Brake");
        brake.setOnAction((event) ->
        {
            if(simulationWorker.isAlive())
            {
                Car.engageBrakes();
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

        AnimationTimer animationTimer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        updateLabels();
      }
    };
        animationTimer.start();
  }

      /**
      * Called once per update by the SimulationWorker class
      */
      public void updateLabels()
      {
           speedDisplay.setText("Current Speed: "+ Car.getXVelocity());
           accelerationDisplay.setText("Current Acceleration: "+ Car.getCurrentXCAcceleration());
      }
}
