import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class LstRv
{
  private static final PrintStream SY_O = System.out;

  public static void main ( String[] arguments)
  {
    if (arguments.length == 2)
    { try
      { Scanner source = new Scanner( new File( arguments[ 0]));
        PrintWriter dstntn
          = new PrintWriter
              ( new BufferedWriter( new FileWriter( arguments[ 1])));
        List< String> lines = new ArrayList< String>();
        while (source.hasNextLine())
        { lines.add( source.nextLine());
        }
        source.close();
        int index;
        int numberOfLines = lines.size();
        for (index = numberOfLines - 1; 0 <= index; index--)
        { dstntn.println( lines.get( index));
        }
        dstntn.close();
        SY_O.println
          ( "Read in " + numberOfLines + " lines from \"" + arguments[ 0]
                       + "\", reversed");
        SY_O.println
          ( "the order, and wrote them to \"" + arguments[ 1] + "\".");
      }
      catch (FileNotFoundException fnfExc)
      { SY_O.println
          ( "Unable to open file \"" + arguments[ 0] + "\" for input!");
      }
      catch (IOException ioExc)
      { SY_O.println
          ( "Unable to open file \"" + arguments[ 1] + "\" for output!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java LstRv <source <destination>");
    }
  }
}
