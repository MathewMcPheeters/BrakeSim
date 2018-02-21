import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application
{
  private final int window_width = 900;
  private final int window_height = 500;

  private Dashboard dashboard;
  private SimulationArea simulationArea;

  public static void main(String[] args) {launch(args); }
  @Override
  public void start(Stage stage)
  {
    // Define the root & add GUI (Dashboard) and visualization (SimulationArea).
    BorderPane root = new BorderPane();

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), ev -> update()));
    timeline.setCycleCount(Animation.INDEFINITE);

    this.dashboard = new Dashboard(10,timeline);
    dashboard.setAlignment(Pos.CENTER);
    dashboard.setPadding(new Insets(10,10,10,10));
    this.simulationArea = new SimulationArea();

    root.setPrefSize(window_width, window_height);
    root.setCenter(simulationArea);
    root.setLeft(dashboard);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public void update() {
    simulationArea.update();
  }
}
