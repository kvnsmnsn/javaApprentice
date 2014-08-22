import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class Perms
{
  private static final PrintStream SY_O = System.out;

  private List< String> stringList;
  private           int limit;

  private Perms ( int sl)
  {
    limit      = sl - 1;
    stringList = new ArrayList< String>();
  }

  private void storePermutes (    int ix
                             , String toPermute)
  { if (ix < limit)
    { int count;
      int next = ix + 1;
      storePermutes( next, toPermute);
      for (count = next; count <= limit; count++)
      { storePermutes
          ( next
          , toPermute.substring( 0, ix) + toPermute.charAt( count)
                                        + toPermute.substring( ix, count)
                                        + toPermute.substring( count + 1));
      }
    }
    else
    { stringList.add( toPermute);
    }
  }

  private void verbose ()
  {
    int count;
    int listLength = stringList.size();
    for (count = 0; count < listLength; count++)
    { SY_O.println( count + ": " + stringList.get( count));
    }
  }

  public static void main ( String[] arguments)
  {
    if (0 < arguments.length)
    { String seed = arguments[ 0];
      Perms perms = new Perms( seed.length());
      long start  = System.currentTimeMillis();
      try
      { perms.storePermutes( 0, seed);
        SY_O.println
          ( "Permutations succeeded in " + (System.currentTimeMillis() - start)
                                         + " ms.");
        if (1 < arguments.length)
        { perms.verbose();
        }
      }
      catch (OutOfMemoryError oomErr)
      { SY_O.println
          ( "Ran out of memory after " + (System.currentTimeMillis() - start)
                                       + " ms after inserting "
                                       + perms.stringList.size() + " strings.");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java Perms <seed> [v]");
    }
  }
}
