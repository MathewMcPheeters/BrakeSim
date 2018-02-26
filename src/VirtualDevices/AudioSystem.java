package VirtualDevices;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
/**
 * James Perry
 * CS 460
 * This class contains the Audio System virtual device. It can be used to play a bell sound
 * through the Car audio system by calling errorSound().
 */
public class AudioSystem
{
  private MediaPlayer mediaPlayer;
  public void errorSound()
  {
    mediaPlayer.play();
  }
  public AudioSystem()
  {
    URL resource = getClass().getResource("/Resources/Bell.wav");
    Media clip = new Media(resource.toString());
    mediaPlayer = new MediaPlayer(clip);
  }
}
