package PhysicalDrivers;

import javafx.scene.media.AudioClip;

public class AlarmNotificationDriver
{
  public AlarmNotificationDriver()
  {

  }

  public void playSound(AudioClip clip)
  {
    clip.play();
  }
}
