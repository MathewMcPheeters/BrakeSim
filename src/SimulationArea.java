import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Simulation area will be defined in here. Note that this class extends Pane, so we can add
 * a SimulationArea to a Scene just as we would add a Pane.
 */
public class SimulationArea extends Pane {

    Image backgroundImageFile = new Image("Resources/testBackground.png");
    BackgroundImage backgroundImage = new BackgroundImage(backgroundImageFile, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

    Rectangle roadDash = new Rectangle(200,420,40,10);
    TranslateTransition dashTranslate = new TranslateTransition(Duration.millis(2000), roadDash);


    public SimulationArea(){

        this.setBackground(new Background(backgroundImage));
        this.roadDash.setFill(Color.WHITE);
        dashTranslate.setByX(-50);
        this.getChildren().add(roadDash);
        dashTranslate.play();

    }



}