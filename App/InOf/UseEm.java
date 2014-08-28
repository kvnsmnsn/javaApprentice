import java.io.PrintStream;

public class UseEm
{
  private static final PrintStream SY_O = System.out;

  public static void main ( String[] arguments)
  {
    Vehicle veh = new Vehicle( 3, 5.0);
    Car car     = new Car( 4, 99.9, true);
    Boat bo     = new Boat( 0, 10.0, 5);
    SY_O.println( "(veh instanceof Car     ): " + (veh instanceof Car));
    SY_O.println( "(car instanceof Vehicle ): " + (car instanceof Vehicle));
    SY_O.println( "(veh instanceof Boat    ): " + (veh instanceof Boat));
    SY_O.println( "(bo  instanceof Vehicle ): " + (bo  instanceof Vehicle));
    //SY_O.println( "(bo  instanceof Car     ): " + (bo  instanceof Car));
    SY_O.println( "(veh instanceof Drivable): " + (veh instanceof Drivable));
    SY_O.println( "(car instanceof Drivable): " + (car instanceof Drivable));
    SY_O.println( "(bo  instanceof Drivable): " + (bo  instanceof Drivable));
    veh = car;
    SY_O.println( "(veh instanceof Car     ): " + (veh instanceof Car));
    car = (Car) veh;
  }
}
