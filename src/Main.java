import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application
{
  private void initializeDashboard(VBox dashboard)
  {
    HBox speed = new HBox(10);
    Label speedLabel = new Label("Speed:");
    TextField speedInput = new TextField();
    Button enterSpeed = new Button("Enter");
    speed.getChildren().addAll(speedLabel,speedInput,enterSpeed);

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
    Button stop = new Button("Stop");
    Button reset = new Button("Reset");
    simulationControl.getChildren().addAll(start,stop,reset);
    dashboard.getChildren().addAll(speed,gear,simulationControl);
  }

  @Override
  public void start(Stage primaryStage)
  {
    BorderPane root = new BorderPane();
    root.setPrefSize(900,500);
    VBox dashboard = new VBox(10);
    initializeDashboard(dashboard);
    dashboard.setAlignment(Pos.CENTER);
    dashboard.setPadding(new Insets(10,10,10,10));
    VBox simulationArea = new VBox();
    root.setLeft(dashboard);
    root.setCenter(simulationArea);
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}
