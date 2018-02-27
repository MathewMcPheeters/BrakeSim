package VirtualDevices;

import PhysicalDrivers.AudioSystemDriver;
/**
 * James Perry
 * CS 460
 * This class contains the Audio System virtual device. It can be used to play a bell sound
 * through the Car audio system by calling errorSound().
 */
public class AudioSystem
{
  private AudioSystemDriver audioSystemDriver;

  public AudioSystem(AudioSystemDriver audioSystemDriver)
  {
    this.audioSystemDriver = audioSystemDriver;
  }

  public void errorSound()
  {
    audioSystemDriver.playSound();
  }
}
