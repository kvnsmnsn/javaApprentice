import java.io.PrintStream;

public class ArithStack
{
  private static final PrintStream SY_O = System.out;

  Expression[] stack;
           int current;

  private interface Expression
  {
    double valueOf ();
  }

  private class Number implements Expression
  {
    double value;

    private Number ( String rep)
    {
      value = Double.parseDouble( rep);
    }

    public double valueOf ()
    {
      current++;
      return value;
    }
  }

  private class Plus implements Expression
  {
    public double valueOf ()
    {
      return stack[ ++current].valueOf() + stack[ current].valueOf();
    }
  }

  private class Minus implements Expression
  {
    public double valueOf ()
    {
      return stack[ ++current].valueOf() - stack[ current].valueOf();
    }
  }

  private class Times implements Expression
  {
    public double valueOf ()
    {
      return stack[ ++current].valueOf() * stack[ current].valueOf();
    }
  }

  private class Over implements Expression
  {
    public double valueOf ()
    {
      return stack[ ++current].valueOf() / stack[ current].valueOf();
    }
  }

  private class Power implements Expression
  {
    public double valueOf ()
    {
      return
        Math.pow( stack[ ++current].valueOf(), stack[ current].valueOf());
    }
  }

  private class Log implements Expression
  {
    public double valueOf ()
    {
      double base  = stack[ ++current].valueOf();
      double power = stack[   current].valueOf();
      return Math.log( power) / Math.log( base);
    }
  }

  private class Sine implements Expression
  {
    public double valueOf ()
    {
      return Math.sin( stack[ ++current].valueOf());
    }
  }

  private class Cosine implements Expression
  {
    public double valueOf ()
    {
      return Math.cos( stack[ ++current].valueOf());
    }
  }

  private class Tangent implements Expression
  {
    public double valueOf ()
    {
      return Math.tan( stack[ ++current].valueOf());
    }
  }

  private class ArcSine implements Expression
  {
    public double valueOf ()
    {
      return Math.asin( stack[ ++current].valueOf());
    }
  }

  private class ArcCos implements Expression
  {
    public double valueOf ()
    {
      return Math.acos( stack[ ++current].valueOf());
    }
  }

  private class ArcTan implements Expression
  {
    public double valueOf ()
    {
      return Math.atan( stack[ ++current].valueOf());
    }
  }

  private Expression singleChar ( char initial)
  {
    switch (initial)
    { case '+'
    : return new Plus();
      case '-'
    : return new Minus();
      case 'x'
    : return new Times();
      case '/'
    : return new Over();
      case '!'
    : return new Power();
      default
    : throw new IllegalArgumentException( "Illegal single character op-code");
    }
  }

  private ArithStack (      int argsLength
                     , String[] args)
  {
    stack = new Expression[ argsLength];
    int argLength;
    int index;
    String arg;
    char initial;
    for (index = 0; index < argsLength; index++)
    { arg       = args[ index];
      argLength = arg.length();
      if (argLength == 0)
      { throw new IllegalArgumentException( "Illegal empty op-code");
      }
      if      ( '0' <= (initial = arg.charAt( 0)) && initial <= '9'
              || 1 < argLength && initial == '-')
      { stack[ index] = new Number( arg);
      }
      else if (argLength == 1)
      { stack[ index] = singleChar( initial);
      }
      else if (arg.equals( "log"))
      { stack[ index] = new Log();
      }
      else if (arg.equals( "sin"))
      { stack[ index] = new Sine();
      }
      else if (arg.equals( "cos"))
      { stack[ index] = new Cosine();
      }
      else if (arg.equals( "tan"))
      { stack[ index] = new Tangent();
      }
      else if (arg.equals( "asn"))
      { stack[ index] = new ArcSine();
      }
      else if (arg.equals( "acs"))
      { stack[ index] = new ArcCos();
      }
      else if (arg.equals( "atn"))
      { stack[ index] = new ArcTan();
      }
      else
      { throw new IllegalArgumentException ( "Illegal multi-character op-code");
      }
    }
    current = 0;
  }

  public static void main ( String[] arguments)
  {
    int argsLength = arguments.length;
    if (0 < argsLength)
    { try
      { ArithStack arSt = new ArithStack( argsLength, arguments);
        double result   = arSt.stack[ 0].valueOf();
        if (arSt.current == argsLength)
        { SY_O.println( "Result is " + result + '.');
        }
        else
        { SY_O.println( "Part of the stack not executed!");
        }
      }
      catch (NumberFormatException nfExc)
      { SY_O.println( "Couldn't convert stack element to a double!");
      }
      catch (IllegalArgumentException iaExc)
      { SY_O.println( iaExc.getMessage() + '!');
      }
      catch (ArrayIndexOutOfBoundsException aioobExc)
      { SY_O.println( "Ran off edge of stack!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java ArithStack <prefix expression>");
    }
  }
}
