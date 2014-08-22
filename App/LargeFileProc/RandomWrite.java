import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;

public class RandomWrite
{
  private static final PrintStream SY_O = System.out;

  public static void main ( String[] arguments)
  {
    if (arguments.length == 3)
    { int arg = 0;
      try
      { int numberRows    = Integer.parseInt( arguments[   arg]);
        int numberColumns = Integer.parseInt( arguments[ ++arg]);
        PrintWriter dstntn
          = new PrintWriter
              ( new BufferedWriter( new FileWriter( arguments[ 2])));
        int row;
        int column;
        for (row = 0; row < numberRows; row++)
        { for (column = 0; column < numberColumns; column++)
          { dstntn.print( (char) ('a' + 26 * Math.random()));
          }
          dstntn.println();
        }
        dstntn.close();
        SY_O.println
          ( "Wrote " + numberRows + " rows of " + numberColumns
                     + " each of random characters.");
      }
      catch (NumberFormatException nfExc)
      { SY_O.println
          ( "Unable to convert argument \"" + arguments[ arg]
                                            + "\" to an integer!");
      }
      catch (IOException ioExc)
      { SY_O.println
          ( " Unable to open file \"" + arguments[ 2] + "\" for output!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java RandomWrite <#-rows> <#-columns> <out-file>");
    }
  }
}
