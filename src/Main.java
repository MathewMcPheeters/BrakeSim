import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Label;

public class Main extends Application
{
  private void initializeDashboard(VBox dashboard)
  {
    HBox speed = new HBox(10);
    Label speedLabel = new Label("Speed:");
    TextField speedInput = new TextField();
    speed.getChildren().addAll(speedLabel,speedInput);
    dashboard.getChildren().add(speed);
  }

  @Override
  public void start(Stage primaryStage)
  {
    BorderPane root = new BorderPane();
    root.setPrefSize(800,500);
    VBox dashboard = new VBox();
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
