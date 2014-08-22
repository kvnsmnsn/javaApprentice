import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class CaPerms
{
  private static final PrintStream SY_O = System.out;

  private List< String> stringList;
  private           int limit;
  private        char[] toPermute;

  private CaPerms ( String seed)
  {
    limit      = seed.length() - 1;
    stringList = new ArrayList< String>();
    toPermute  = seed.toCharArray();
  }

  private void storePermutes ( int ix)
  { if (ix < limit)
    { int count;
      char swappee;
      int next = ix + 1;
      storePermutes( next);
      for (count = next; count <= limit; count++)
      { swappee           = toPermute[ count];
        toPermute[ count] = toPermute[ ix];
        toPermute[ ix]    = swappee;
        storePermutes( next);
      }
      swappee = toPermute[ ix];
      count   = ix;
      while (count < limit)
      { toPermute[ count] = toPermute[ ++count];
      }
      toPermute[ limit] = swappee;
    }
    else
    { stringList.add( new String( toPermute));
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

  private int listSize ()
  {
    int size   = stringList.size();
    stringList = null;
    return size;
  }

  public static void main ( String[] arguments)
  {
    if (0 < arguments.length)
    { CaPerms perms = new CaPerms( arguments[ 0]);
      long start    = System.currentTimeMillis();
      try
      { perms.storePermutes( 0);
        SY_O.println
          ( "Permutations succeeded in " + (System.currentTimeMillis() - start)
                                         + " ms.");
        if (1 < arguments.length)
        { perms.verbose();
        }
      }
      catch (OutOfMemoryError oomErr)
      { int size = perms.listSize();
        SY_O.println
          ( "Ran out of memory after " + (System.currentTimeMillis() - start)
                                       + " ms after inserting "
                                       + size + " strings.");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java CaPerms <seed> [v]");
    }
  }
}
