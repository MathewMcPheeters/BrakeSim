package VirtualDevices;

import PhysicalDrivers.AlarmNotificationDriver;
import javafx.scene.media.AudioClip;

public class AlarmNotification
{
  private AlarmNotificationDriver alarmNotificationDriver;

  public AlarmNotification(AlarmNotificationDriver alarmNotificationDriver)
  {
    this.alarmNotificationDriver = alarmNotificationDriver;
  }

  public void playSound(AudioClip clip)
  {
      alarmNotificationDriver.playSound(clip);
  }
}
