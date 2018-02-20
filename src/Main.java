import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
/**
 * This class is the entry point for the BrakeSim application. It should be
 * called with no arguments.
 */
public class Main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        BorderPane root = new BorderPane();
        root.setPrefSize(900,500);

        SimulationArea simulationArea = new SimulationArea();

        SimulationWorker simulationWorker = new SimulationWorker();
        simulationWorker.setSimulationArea(simulationArea); //Set the simulation area for the worker

        Dashboard dashboard = new Dashboard(10,simulationWorker);
        dashboard.setAlignment(Pos.CENTER);
        dashboard.setPadding(new Insets(10,10,10,10));


        root.setCenter(simulationArea);
        root.setLeft(dashboard);

        Scene scene = new Scene(root);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent event)
            {
          //      simulationWorker.setRunning(false);
            //    simulationWorker.terminate();
              //  while(simulationWorker.isAlive()){}
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}