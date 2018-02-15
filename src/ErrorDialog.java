import javafx.scene.control.Alert;

public class ErrorDialog extends Alert
{
  public ErrorDialog(Alert.AlertType type,String message)
  {
    super(type);
    setContentText(message);
    showAndWait();
  }
}
