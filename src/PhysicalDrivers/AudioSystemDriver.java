package PhysicalDrivers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class AudioSystemDriver {
  private MediaPlayer mediaPlayer;

  public void playSound()
  {
    mediaPlayer.play();
  }
  public AudioSystemDriver()
  {
    URL resource = getClass().getResource("/Resources/Bell.wav");
    Media clip = new Media(resource.toString());
    mediaPlayer = new MediaPlayer(clip);
  }
}
