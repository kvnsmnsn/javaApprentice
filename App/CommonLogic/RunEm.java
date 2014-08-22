import java.io.PrintStream;

public class RunEm
{
  private static final PrintStream SY_O = System.out;

  public static void main ( String[] arguments)
  {
    int argsLength = arguments.length;
    if (2 <= argsLength)
    { int arg        = 1;
      String label   = arguments[ 0];
      double ratio   = Double.parseDouble( arguments[ arg]);
      int[] unsorted = new int[ argsLength - 2];
      int index      = 2;
      try
      { while (++arg < argsLength)
        { unsorted[ arg - 2] = Integer.parseInt( arguments[ arg]);
        }
        ComposeLabel composeLabel = new ComposeLabel( label, unsorted);
        InheritLabel inheritLabel = new InheritLabel( label, unsorted);
        StaticLabel  staticLabel  = new StaticLabel( label, unsorted);
        composeLabel.sortIndices();
        inheritLabel.sort();
        staticLabel.sortIndices();
        SY_O.println( "composeLabel: " + composeLabel);
        SY_O.println( "inheritLabel: " + inheritLabel);
        SY_O.println( "staticLabel:  " + staticLabel);
        ComposeRatio composeRatio = new ComposeRatio( ratio, unsorted);
        InheritRatio inheritRatio = new InheritRatio( ratio, unsorted);
        StaticRatio  staticRatio  = new StaticRatio( ratio, unsorted);
        composeRatio.sortIndices();
        inheritRatio.sort();
        staticLabel.sortIndices();
        SY_O.println( "composeRatio: " + composeRatio);
        SY_O.println( "inheritRatio: " + inheritRatio);
        SY_O.println( "staticRatio:  " + staticRatio);
      }
      catch (NumberFormatException nfExc)
      { SY_O.println
          ( "Unable to convert argument \"" + arguments[ index]
                                            + "\" to an number!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java <label> <number>*");
    }
  }
}
