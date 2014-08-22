import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class RecRev
{
  private static final PrintStream SY_O = System.out;

  private     Scanner source;
  private PrintWriter dstntn;

  private RecRev ( String srName
                 , String dsName)
                 throws FileNotFoundException, IOException
  {
    source = new Scanner( new File( srName));
    dstntn = new PrintWriter( new BufferedWriter( new FileWriter( dsName)));
  }

  private void writeReversed ()
  {
    if (source.hasNextLine())
    { String line = source.nextLine();
      writeReversed();
      dstntn.println( line);
    }
  }

  private void closeBoth ()
  {
    source.close();
    dstntn.close();
  }

  public static void main ( String[] arguments)
  {
    if (arguments.length == 2)
    { try
      { RecRev recRev = new RecRev( arguments[ 0], arguments[ 1]);
        recRev.writeReversed();
        recRev.closeBoth();
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
      SY_O.println( "  java RecRev <source> <dstntn>");
    }
  }
}
