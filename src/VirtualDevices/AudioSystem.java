package VirtualDevices;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

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
