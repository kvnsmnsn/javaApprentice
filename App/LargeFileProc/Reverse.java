import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Reverse
{
  private static final PrintStream SY_O = System.out;

  public static void main ( String[] arguments)
  {
    if (arguments.length == 3)
    { try
      { int numberOfLines = Integer.parseInt( arguments[ 0]);
        String[] lines    = new String[ numberOfLines];
        Scanner source    = new Scanner( new File( arguments[ 1]));
        PrintWriter dstntn
          = new PrintWriter
              ( new BufferedWriter( new FileWriter( arguments[ 2])));
        int index;
        for (index = 0; index < numberOfLines; index++)
        { lines[ index] = source.nextLine();
        }
        source.close();
        for (index = numberOfLines - 1; 0 <= index; index--)
        { dstntn.println( lines[ index]);
        }
        dstntn.close();
        SY_O.println
          ( "Read in " + numberOfLines + " lines from \"" + arguments[ 1]
                       + "\", reversed");
        SY_O.println
          ( "the order, and wrote them to \"" + arguments[ 2] + "\".");
      }
      catch (NumberFormatException nfExc)
      { SY_O.println
          ( "Unable to convert argument \"" + arguments[ 0]
                                            + "\" to an integer!");
      }
      catch (FileNotFoundException fnfExc)
      { SY_O.println
          ( "Unable to open file \"" + arguments[ 1] + "\" for input!");
      }
      catch (IOException ioExc)
      { SY_O.println
          ( "Unable to open file \"" + arguments[ 2] + "\" for output!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java Reverse <#-lines> <source> <destination>");
    }
  }
}
