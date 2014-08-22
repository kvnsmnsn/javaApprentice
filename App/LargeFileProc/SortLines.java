import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class SortLines
{
  private static final PrintStream SY_O = System.out;

       int numberOfLines;
  String[] lines;

  private SortLines ( int nl)
  {
    numberOfLines = nl;
    lines         = new String[ numberOfLines];
  }

  private void readIn ( String sourceName)
                      throws FileNotFoundException
  {
    Scanner source = new Scanner( new File( sourceName));
    int index;
    for (index = 0; index < numberOfLines; index++)
    { lines[ index] = source.nextLine();
    }
    source.close();
  }

  private void quickSort ( int low
                         , int high)
  {
    if (low < high)
    { String swappee;
      int mid      = low + high >> 1;
      String pivot = lines[ mid];
      int left     = low;
      int right    = high;
      lines[ mid]  = lines[ low];
      for (; ; )
      { while (++left < right && lines[ left].compareTo( pivot)  <= 0);
        while (left < --right && pivot.compareTo( lines[ right]) <= 0);
        if (left < right)
        { swappee       = lines[ left];
          lines[ left]  = lines[ right];
          lines[ right] = swappee;
        }
        else
        { lines[ low]  = lines[ --left];
          lines[ left] = pivot;
          quickSort( low, left);
          quickSort( left + 1, high);
          return;
        }
      }
    }
  }

  private boolean isSorted ()
  {
    if (0 < numberOfLines)
    { int index = 1;
      String before = lines[ 0];
      String after;
      for (; ; )
      { if (numberOfLines <= index)
        { return true;
        }
        after = lines[ index++];
        if (0 < before.compareTo( after))
        { return false;
        }
        before = after;
      }
    }
    else
    { return true;
    }
  }

  private void writeOut ( String destinationName)
                        throws IOException
  {
    PrintWriter destination
      = new PrintWriter( new BufferedWriter( new FileWriter( destinationName)));
    int index;
    for (index = 0; index < numberOfLines; index++)
    { destination.println( lines[ index]);
    }
    destination.close();
  }

  public static void main ( String[] arguments)
  {
    if (arguments.length == 3)
    { try
      { int numberOfLines    = Integer.parseInt( arguments[ 0]);
        SortLines lineSorter = new SortLines( numberOfLines);
        long start           = System.currentTimeMillis();
        lineSorter.readIn( arguments[ 1]);
        long beforeSort      = System.currentTimeMillis();
        lineSorter.quickSort( 0, numberOfLines);
        long afterSort       = System.currentTimeMillis();
        if (lineSorter.isSorted())
        { long afterVerified = System.currentTimeMillis();
          lineSorter.writeOut( arguments[ 2]);
          long end           = System.currentTimeMillis();
          SY_O.println
            (   "Read in " + numberOfLines + " lines in " + (beforeSort - start)
              + " ms, sorted them in " + (afterSort - beforeSort)
              + " ms, verified they were");
          SY_O.println
            (   "sorted in " + (afterVerified - afterSort)
              + " ms, and wrote the sorted lines to file \"" + arguments[ 2]
              + "\" in " + (end - afterVerified) + " ms.");
        }
        else
        { SY_O.println( "The lines were not successfully sorted!");
        }
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
      SY_O.println( "  java SortLines <#-lines> <source> <destination>");
    }
  }
}
