import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;

public class LiOfSt
{
  private static final PrintStream SY_O = System.out;

  List< String> stList;
        boolean testsPassed;

  private LiOfSt ()
  {
    stList      = new ArrayList< String>();
    testsPassed = true;
  }

  static private String random ( int size)
  {
    char[] chas = new char[ size];
    int index;
    for (index = 0; index < size; index++)
    { chas[ index] = (char) ('a' + 26.0 * Math.random());
    }
    return new String( chas);
  }

  private int findLimit ()
  {
    int power = 1;
    int total = 0;
    try
    { for (; ; )
      { stList.add( random( power));
        total  += power;
        power <<= 1;
      }
    }
    catch (OutOfMemoryError oomErr)
    {
    }
    power >>= 1;
    for (; ; )
    { try
      { stList.add( random( power));
        total += power;
      }
      catch (OutOfMemoryError oomErr)
      { power >>= 1;
        if (power < 1)
        { break;
        }
      }
    }
    return total;
  }

  private void listOfSize ( int size
                          , int limit)
  {
    int index;
    boolean succeeded;
    boolean good = true;
    int qtnt     = limit / size;
    int rmndr    = limit % size;
    int oneMore  = qtnt + 1;
    for (; ; )
    { stList = new ArrayList< String>();
      try
      { for (index = 0; index < size; index++)
        { stList.add( random( index < rmndr ? oneMore : qtnt));
        }
        succeeded = true;
      }
      catch (OutOfMemoryError oomErr)
      { succeeded = false;
      }
      stList = null;
      SY_O.println
        ( (succeeded ? "Able to" : "Couldn't") + " divide " + limit
                                               + " characters between " + size
                                               + " strings.");
      if (good && succeeded)
      { limit++;
        if (++rmndr == size)
        { qtnt++;
          rmndr = 0;
        }
        good = false;
      }
      else
      { if (good || succeeded)
        { testsPassed = false;
        }
        return;
      }
    }
  }

  public static void main ( String[] arguments)
  {
    LiOfSt los = new LiOfSt();
    int limit  = los.findLimit();
    los.stList = null;
    SY_O.println( "Limit is " + limit + " characters stored in lines.");
    int hyphen;
    int lsBegin;
    int lsEnd;
    int listSize;
    try
    { for (String arg : arguments)
      { hyphen = arg.indexOf( '-');
        if (hyphen < 0)
        { los.listOfSize( Integer.parseInt( arg), limit);
        }
        else
        { lsBegin = Integer.parseInt( arg.substring( 0, hyphen));
          lsEnd   = Integer.parseInt( arg.substring( hyphen + 1));
          for (listSize = lsBegin; listSize <= lsEnd; listSize++)
          { los.listOfSize( listSize, limit);
          }
        }
      }
      SY_O.println( "Tests " + (los.testsPassed ? "passed!" : "failed!"));
    }
    catch (NumberFormatException nfExc)
    { SY_O.println( "Couldn't convert numeric argument to an integer!");
    }
  }
}
