
public class Interfaces
{
  private int topSpeed;
  private Gear gear;
  private int currentSpeed;
  private int acceleration;
  private int brakingPower;
  private boolean braking;

  public Interfaces(int topSpeed)
  {
    this.topSpeed = topSpeed;
  }

  public void setTopSpeed(int topSpeed)
  {
    this.topSpeed = topSpeed;
  }
  public int getTopSpeed()
  {
    return topSpeed;
  }

  public void setGear(Gear gear)
  {
    this.gear = gear;
  }
  public Gear getGear()
  {
    return gear;
  }

  public void setCurrentSpeed(int newSpeed)
  {
    if(newSpeed > topSpeed)
    {
      this.currentSpeed = topSpeed;
    }
    else
    {
      this.currentSpeed = newSpeed;
    }
  }
  public int getCurrentSpeed()
  {
    return currentSpeed;
  }

  public void setAcceleration(int acceleration)
  {
    this.acceleration = acceleration;
  }
  public int getAcceleration()
  {
    return acceleration;
  }

  public void setBrakingPower(int brakingPower)
  {
    this.brakingPower = brakingPower;
  }
  public int getBrakingPower()
  {
    return brakingPower;
  }

  public void setBraking(boolean braking)
  {
    this.braking = braking;
    if(braking == true)
    {
      acceleration = 0;
    }
  }
  public boolean getBraking()
  {
    return braking;
  }
}