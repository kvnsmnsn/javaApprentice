import java.io.PrintStream;

public class PassBy
{
  private static final PrintStream SY_O = System.out;

  private static class OneInt
  {
    int justOne;

    private OneInt ( int ig)
    {
      justOne = ig;
    }
  }

  private int demo (          int byValue
                   ,       OneInt byReference
                   , final double finalValue)
  {
    byReference.justOne +=  5;
    byValue             += 10;
    // finalValue          += 1.0;
    return (int) (byValue + byReference.justOne + finalValue);
  }

  public static void main ( String[] arguments)
  {
    if (arguments.length == 3)
    { PassBy passBy = new PassBy();
      int arg       = 0;
      try
      { int bv     = Integer.parseInt( arguments[ arg]);
        OneInt br  = new OneInt( Integer.parseInt( arguments[ ++arg]));
        double fv  = Double.parseDouble( arguments[ ++arg]);
        SY_O.println
          ( "Before call, bv: " + bv + ", br: (justOne: " + br.justOne
                                + "), fv: " + fv + '.');
        int result = passBy.demo( bv, br, fv);
        SY_O.println( "Result is " + result + '.');
        SY_O.println
          ( "After call, bv: " + bv + ", br: (justOne: " + br.justOne
                               + "), fv: " + fv + '.');
      }
      catch (NumberFormatException nfExc)
      { SY_O.println( "Couldn't convert argument \"" + arg + "\" to a number!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java <integer> <integer> <double>");
    }
  }
}
