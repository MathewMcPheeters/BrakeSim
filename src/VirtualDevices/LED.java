package VirtualDevices;

import PhysicalDrivers.LEDDriver;

/**
 * James Perry
 * CS 460
 * This class contains the LED virtual device. It can be used to set the color
 * of the LED light on the Emergency Brake Button by calling light();
 *
 */
public class LED
{
  LEDDriver ledDriver;
  public LED(LEDDriver ledDriver)
  {
    this.ledDriver = ledDriver;
  }
  public void light(LEDDriver.Color color)
  {
    ledDriver.light(color);
  }

}
