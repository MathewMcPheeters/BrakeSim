package VirtualDevices;

import PhysicalDrivers.LEDNotificationDriver;

/**
 * James Perry
 * CS 460
 * This class contains the LED virtual device. It can be used to set the color
 * of the LED light on the Emergency Brake Button by calling light();
 *
 */
public class LEDNotification
{
  LEDNotificationDriver ledDriver;
  public LEDNotification(LEDNotificationDriver ledDriver)
  {
    this.ledDriver = ledDriver;
  }
  public void light(LEDNotificationDriver.Color color)
  {
    ledDriver.light(color);
  }

}
