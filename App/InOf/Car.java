import java.io.PrintStream;

public class Car extends Vehicle implements Drivable
{
  private static final PrintStream SY_O = System.out;

  boolean airConditioning;

  public Car (     int nw
             ,  double mv
             , boolean ac)
  {
    super( nw, mv);
    airConditioning = ac;
  }

  public void drive ( long when)
  {
    SY_O.println( "Driving at time " + when + '.');
  }

  public void drive ()
  {
    drive( 30);
  }
}
