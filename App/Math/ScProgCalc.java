/***************************************************************************\
* Program: ScProgCalc.java                                                  *
*  Author: Kevin Simonson                                                   *
* Written: 16 Apr 2014                                                      *
*                                                                           *
*      Simulates a programmable calculator, with up to 100 {BigDecimal}s of *
* storage and up to 1000 programmable characters.  Enter "h" at the command *
* prompt to get a description of each of the non-numeric commands.          *
\***************************************************************************/

import java.math.BigInteger;
import java.math.BigDecimal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;

public class ScProgCalc
{
  private static final PrintStream SY_O = System.out;
  private static final PrintStream SY_E = System.err;
  private static final BigDecimal  NG_1 = BigDecimal.ONE.negate();

  // Numeric-oriented modes, storing whether the user has started entering a
  // number, and whether the user is before or after a decimal point.
  enum Status
  { ERROR, ILLGL_CHRCTR, NON_NUMERIC, BEFORE_DECIMAL, AFTER_DECIMAL;

    boolean lessThan ( Status right)
    {
      return ordinal() < right.ordinal();
    }
  }

  // Modes of operation, i.e. entering a program into memory, manual, stepping,
  // or running.
  enum Mode
  { PROGRAM_ENTRY, MANUAL, MANUAL_WAS_ST, STEPPING, RUNNING;

    boolean lessThan ( Mode right)
    {
      return ordinal() < right.ordinal();
    }
  }

       Scanner scnnr;
        BdMath bdm;
           int scale;
        Status status;
          Mode mprStatus;
    BigDecimal tenPower;
    BigDecimal yReg;
    BigDecimal xReg;
    BigInteger memoryLength;
  BigDecimal[] memory;
    BigInteger programLength;
        char[] program;
           int prgrmCntr;
        String cmmnds;
           int cmmndsLngth;
           int cmmndNmbr;

  // Constructor <ScProgCalc()> initializes scanner object, numeric-oriented
  // status to non-numeric, mode of operation to manual, two registers and all
  // of memory to zero, and program memory to all underscores (which are illegal
  // values and can't be executed).

  private ScProgCalc ( int sc
                     , int mmrySze
                     , int prgrmTens)
  {
    scnnr         = new Scanner( System.in);
    scale         = sc;
    bdm           = new BdMath( scale);
    status        = Status.NON_NUMERIC;
    mprStatus     = Mode.MANUAL;
    yReg          = BigDecimal.ZERO;
    xReg          = BigDecimal.ZERO;
    memoryLength  = BigInteger.valueOf( mmrySze);
    memory        = new BigDecimal[ mmrySze];
    int prgLngth  = 10 * prgrmTens;
    programLength = BigInteger.valueOf( prgLngth);
    program       = new char[ prgLngth];
    for (int mmry = 0; mmry < mmrySze; mmry++)
    { memory[ mmry] = BigDecimal.ZERO;
    }
    for (int chrctr = 0; chrctr < program.length; chrctr++)
    { program[ chrctr] = '_';
    }
    prgrmCntr = 0;
  }

  // Method <printHelp()> prints a summary of the commands and what each command
  // does.

  static private void printHelp ()
  {
    SY_O.println();
    SY_O.println(   "    e   [Clear all memory including program counter.]");
    SY_O.println(   "    P   x <- PI");
    SY_O.println(   "    E   x <- E");
    SY_O.println(   "    b   x <- [x]");
    SY_O.println(   "    p   [x] <- y");
    SY_O.println(   "    x   x <-> y");
    SY_O.println(   "    n   x <- -x");
    SY_O.println(   "    d   x <- floor( x)");
    SY_O.println(   "    u   x <- ceil( x)");
    SY_O.println(   "    +   y <- y + x");
    SY_O.println(   "    -   y <- y - x");
    SY_O.println(   "    *   y <- y * x");
    SY_O.println(   "    /   y <- y / x");
    SY_O.println(   "    %   y <- y % x");
    SY_O.println(   "    ^   y <- y ^ x");
    SY_O.println(   "    l   y <- log_x( y)");
    SY_O.println(   "    s   x <- sin( x)");
    SY_O.println(   "    c   x <- cos( x)");
    SY_O.println(   "    t   x <- tan( x)");
    SY_O.println(   "    S   x <- arcsin( x)");
    SY_O.println(   "    C   x <- arccos( x)");
    SY_O.println(   "    T   x <- arctan( x)");
    SY_O.println(   "    =   y <- y == x");
    SY_O.println(   "    <   y <- y <  x");
    SY_O.println(   "    N   x <- NOT x");
    SY_O.println(   "    A   y <- y AND x");
    SY_O.println(   "    O   y <- y OR  x");
    SY_O.println(   "    J   pc <- pc + y IF x");
    SY_O.println(   "    G   pc <= y      IF x");
    SY_O.println(   "    R   [Toggle run.]");
    SY_O.println(   "    I   [Single step.]");
    SY_O.println(   "    M   [Toggle mode.]");
    SY_O.print  (   "r\"fn\"   [Read x decimals into memory sta");
    SY_O.println(             "rting at y from \"<fn>.Dat\".]");
    SY_O.print  (   "w\"fn\"   [Write x decimals starting at y ");
    SY_O.println(             "to \"<fn>.Dat\".]");
    SY_O.println( "L\"fn\"   [Load program from \"<fn>.Pgm\".]");
    SY_O.println( "V\"fn\"   [Save program to \"<fn>.Pgm\".]");
    SY_O.println(   "    q   [Exit program.]");
  }

  private void display ( BigDecimal number)
  {
    int count;
    int decPoint;
    String toDisplay = number.toPlainString();
    if (75 < toDisplay.length())
    { while (35 < (decPoint = toDisplay.indexOf( '.')))
      { SY_O.println( toDisplay.substring( 0, 35));
        SY_O.print( "    ");
        toDisplay = toDisplay.substring( 35);
      }
      if (75 < toDisplay.length())
      { int limit = 74 - decPoint;
        SY_O.println( toDisplay.substring( 0, 75));
        toDisplay = toDisplay.substring( 75);
        for (; ; )
        { for (count = -5; count < decPoint; count++)
          { SY_O.print( ' ');
          }
          if (toDisplay.length() <= limit)
          { break;
          }
          SY_O.println( toDisplay.substring( 0, limit));
          toDisplay = toDisplay.substring( limit);
        }
      }
    }
    SY_O.println( toDisplay);
  }

  // Method <display()> prints the contents of the existing program, followed by
  // the program counter, followed by the contents of memory, followed by the y
  // and x register; and then prompts the user for input, which it stores in va-
  // riable <cmmnds>.

  private void display ()
  {
    int mmry;
    int cmmnd;
    int ten;
    int space;
    if (0 < program.length)
    { for (cmmnd = 0; cmmnd < program.length; cmmnd++)
      { if (cmmnd % 10 == 0)
        { ten = cmmnd / 10;
          if (ten == prgrmCntr / 10)
          { SY_O.println();
            for ( space = 4 + prgrmCntr % 10; 0 < space; space--)
            { SY_O.print( ' ');
            }
            SY_O.print( 'v');
          }
          SY_O.println();
          if (cmmnd < 100)
          { SY_O.print( " ");
          }
          SY_O.print( ten + ": ");
        }
        SY_O.print( program[ cmmnd]);
      }
      SY_O.println();
      SY_O.println();
      SY_O.println( "pc: " + prgrmCntr);
      SY_O.println();
    }
    else
    { SY_O.println();
    }
    if (0 < memory.length)
    { for (mmry = 0; mmry < memory.length; mmry++)
      { if (mmry < 10)
        { SY_O.print( " ");
        }
        SY_O.print( mmry + ": ");
        display( memory[ mmry]);
      }
      SY_O.println();
    }
    SY_O.print( " y: ");
    display( yReg);
    SY_O.print( " x: ");
    display( xReg);
    SY_O.println();
    SY_O.print( "> ");
    cmmnds      = scnnr.nextLine();
    cmmndsLngth = cmmnds.length();
    cmmndNmbr   = 0;
  }

  // Method <inputOutput()> processes the read, write, load, and save commands,
  // beginning with the reading of the quote surrounded string that is the left
  // stem of the name of the file.  Data files (accessed by the "r" and "w" com-
  // mands) append a ".Dat" to the stem, and program files (accessed by the "L"
  // and "V" commands) append a ".Pgm" to the stem.

  private void inputOutput ( char cmmnd)
  {
    Scanner scnnr;
    PrintWriter prntWrtr;
    String fileName = "";
    String dtaLne;
    int quote;
    int lowDbl      = 0;
    int nmbrDbls    = 0;
    // Check to see if the file-stem name begins with a quote.
    if (mprStatus == Mode.MANUAL && cmmndNmbr < cmmndsLngth
                                 && cmmnds.charAt( cmmndNmbr) == '\"')
    { // Increment variable <quote> until it either points to the end of
      // <cmmnds> or the second quote, whichever it finds first.
      for ( quote = cmmndNmbr + 1
          ; quote < cmmndsLngth && cmmnds.charAt( quote) != '\"'
          ; quote++);
      // If <quote> points at the second quote, process the command.
      if (quote < cmmndsLngth)
      { // If <cmmnd> is upper case, then the filename has a ".Pgm" suffix, and
        // the command is either load or save; otherwise the filename has a
        // ".Dat" suffix, and the command is either read or write; in the latter
        // case determine the range of doubles read in from disk or written to
        // disk.
        if (cmmnd < 'Z')
        { fileName = cmmnds.substring( cmmndNmbr + 1, quote) + ".Pgm";
        }
        else
        { if (  BigDecimal.ZERO.compareTo( yReg) < 1
             && BigDecimal.ZERO.compareTo( xReg) < 1
             && yReg.add( xReg).compareTo( new BigDecimal( memory.length)) < 1
             &&    xReg.compareTo( new BigDecimal( nmbrDbls = xReg.intValue()))
                == 0
             && yReg.compareTo( new BigDecimal( lowDbl = yReg.intValue())) == 0)
          { fileName = cmmnds.substring( cmmndNmbr + 1, quote) + ".Dat";
          }
          else
          { status = Status.ERROR;
          }
        }
        // Open the files, do the data transfer, close the files, and set the
        // next command to one character after the second quote.
        if (status == Status.NON_NUMERIC)
        { try
          { if (cmmnd == 'r' || cmmnd == 'L')
            { scnnr = new Scanner( new File( fileName));
              if (cmmnd == 'r')
              { for (int dbl = 0; dbl < nmbrDbls; dbl++)
                { dtaLne = scnnr.nextLine();
                  memory[ lowDbl + dbl] = new BigDecimal( dtaLne);
                }
              }
              else
              { dtaLne = scnnr.nextLine();
                int prgrmLngth = dtaLne.length();
                if (program.length < prgrmLngth)
                { prgrmLngth = program.length;
                }
                for (int index = 0; index < prgrmLngth; index++)
                { program[ index] = dtaLne.charAt( index);
                }
              }
              scnnr.close();
            }
            else
            { prntWrtr
                = new PrintWriter
                    ( new BufferedWriter( new FileWriter( fileName)));
              if (cmmnd == 'w')
              { for (int dbl = 0; dbl < nmbrDbls; dbl++)
                { prntWrtr.println( memory[ lowDbl + dbl].toPlainString());
                }
              }
              else
              { prntWrtr.println( String.valueOf( program));
              }
              prntWrtr.close();
            }
          }
          catch (Exception excptn)
          { status = Status.ERROR;
          }
          cmmndNmbr = quote + 1;
          return;
        }
      }
    }
    status = Status.ERROR;
  }

  // Method <nonNumericQuit()> processes one entered command, and returns whe-
  // ther the user has entered a desire to quit.

  private boolean nonNumericQuit ( char cmmnd)
  {
    int mmry;
    BigInteger index;
    int quote;
    BigDecimal swappee;
    switch (cmmnd)
    { case 'h'
    : printHelp();
    break;
      // The "e" command sets to zero each of the two registers, the program
      // counter, and each memory location; it does not affect the stored pro-
      // gram.
      case 'e'
    : xReg      = BigDecimal.ZERO;
      yReg      = BigDecimal.ZERO;
      prgrmCntr = 0;
      for (mmry = 0; mmry < memory.length; mmry++)
      { memory[ mmry] = BigDecimal.ZERO;
      }
    break;
      // The "P" and "E" commands enter into the x register the values of pi and
      // the base of the natural logarithms, respectively.
      case 'P'
    : xReg = bdm.getPi();
    break;
      case 'E'
    : xReg = bdm.getE();
    break;
      // The "b" command brings to the x register the memory location designated
      // by the current contenst of the x register; the "p" command puts the
      // contents of the y register into the memory location designated by the x
      // register; the "x" command switches the contents of the x register and
      // the contents of the y register.
      case 'b'
    : index = xReg.toBigInteger();
      if (  xReg.compareTo( new BigDecimal( index)) == 0
         && BigInteger.ZERO.compareTo( index) < 1
         && index.compareTo( memoryLength) < 0)
      { xReg = memory[ index.intValue()];
      }
      else
      { status = Status.ERROR;
      }
    break;
      case 'p'
    : index = xReg.toBigInteger();
      if (  xReg.compareTo( new BigDecimal( index)) == 0
         && BigInteger.ZERO.compareTo( index) < 1
         && index.compareTo( memoryLength) < 0)
      { memory[ index.intValue()] = yReg;
      }
      else
      { status = Status.ERROR;
      }
    break;
      case 'x'
    : swappee = xReg;
      xReg    = yReg;
      yReg    = swappee;
      // Arithmetical operators, including modula, exponentiation, and logarithm
      // operators.
    break;
      case 'n'
    : xReg = xReg.negate();
    break;
      case 'd'
    : swappee = new BigDecimal( xReg.toBigInteger());
      xReg
        =      xReg.compareTo( swappee) == 0
            || BigDecimal.ZERO.compareTo( xReg) < 0
          ? swappee
          : swappee.subtract( BigDecimal.ONE);
    break;
      case 'u'
    : swappee = new BigDecimal( xReg.toBigInteger());
      xReg
        =      xReg.compareTo( swappee) == 0
            || xReg.compareTo( BigDecimal.ZERO) < 0
          ? swappee
          : swappee.add( BigDecimal.ONE);
    break;
      case '+'
    : yReg = yReg.add( xReg);
    break;
      case '-'
    : yReg = yReg.subtract( xReg);
    break;
      case '*'
    : yReg
        = yReg.multiply( xReg)
              .divide( BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP);
    break;
      case '/'
    : if (xReg.compareTo( BigDecimal.ZERO) != 0)
      { yReg = yReg.divide( xReg, scale, BigDecimal.ROUND_HALF_UP);
      }
      else
      { status = Status.ERROR;
      }
    break;
      case '%'
    : if (  xReg.compareTo( BigDecimal.ZERO) != 0
         && xReg.compareTo( new BigDecimal( xReg.toBigInteger())) == 0
         && yReg.compareTo( new BigDecimal( yReg.toBigInteger())) == 0)
      { yReg = yReg.remainder( xReg);
      }
      else
      { status = Status.ERROR;
      }
    break;
      case '^'
    : if (  (  yReg.compareTo( BigDecimal.ZERO) != 0
            || BigDecimal.ZERO.compareTo( xReg) < 1)
         && (  BigDecimal.ZERO.compareTo( yReg) < 1
            || xReg.compareTo( new BigDecimal( xReg.toBigInteger())) == 0))
      { yReg = bdm.pow( yReg, xReg);
      }
      else
      { status = Status.ERROR;
      }
    break;
      case 'l'
    : if (  BigDecimal.ZERO.compareTo( xReg) < 0
         && BigDecimal.ZERO.compareTo( yReg) < 0
         && BigDecimal.ONE.compareTo( xReg) != 0)
      { yReg = bdm.log( xReg, yReg);
      }
      else
      { status = Status.ERROR;
      }
    break;
      // Trigonometric operators, taking the contents of the x register as the
      // measure of an angle in radians, and replacing it with the trigonometric
      // ratio, in the case of the first three commands, and vice versa in the
      // case of the last three.
      case 's'
    : xReg = bdm.sin( xReg);
    break;
      case 'c'
    : xReg = bdm.cos( xReg);
    break;
      case 't'
    : try
      { xReg = bdm.tan( xReg);
      }
      catch (ArithmeticException aExc)
      { status = Status.ERROR;
      }
    break;
      case 'S'
    : if (NG_1.compareTo( xReg) < 1 && xReg.compareTo( BigDecimal.ONE) < 1)
      { xReg = bdm.arcSin( xReg);
      }
      else
      { status = Status.ERROR;
      }
    break;
      case 'C'
    : if (NG_1.compareTo( xReg) < 1 && xReg.compareTo( BigDecimal.ONE) < 1)
      { xReg = bdm.arcCos( xReg);
      }
      else
      { status = Status.ERROR;
      }
    break;
      case 'T'
    : xReg = bdm.arcTan( xReg);
    break;
      // Relational operators; "=" sets the y register to one (for true) if the
      // the y register equals the x register, and sets it to zero (for false)
      // otherwise; "<" sets the y register to one if the y register is less
      // than the x register, and sets it to zero otherwise.
      case '='
    : yReg = yReg.compareTo( xReg) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
    break;
      case '<'
    : yReg = yReg.compareTo( xReg) <  0 ? BigDecimal.ONE : BigDecimal.ZERO;
    break;
      // Logical operators; false is represented by zero and true by one.
      case 'N'
    : if      (xReg.compareTo( BigDecimal.ZERO) == 0)
      { xReg = BigDecimal.ONE;
      }
      else if (xReg.compareTo( BigDecimal.ONE) == 0)
      { xReg = BigDecimal.ZERO;
      }
      else
      { status = Status.ERROR;
      }
    break;
      case 'A'
    : case 'O'
    : if      (  (  xReg.compareTo( BigDecimal.ZERO) != 0
                 && xReg.compareTo( BigDecimal.ONE ) != 0)
              || (  yReg.compareTo( BigDecimal.ZERO) != 0
                 && yReg.compareTo( BigDecimal.ONE ) != 0))
      { status = Status.ERROR;
      }
      else if (cmmnd == 'A')
      { yReg
          =      xReg.compareTo( BigDecimal.ONE) == 0
              && yReg.compareTo( BigDecimal.ONE) == 0
            ? BigDecimal.ONE
            : BigDecimal.ZERO;
      }
      else
      { yReg
          =      xReg.compareTo( BigDecimal.ONE) == 0
              || yReg.compareTo( BigDecimal.ONE) == 0
            ? BigDecimal.ONE
            : BigDecimal.ZERO;
      }
    break;
      // Control commands.
      case 'J'
    : case 'G'
    : index = yReg.toBigInteger();
      if      (yReg.compareTo( new BigDecimal( index)) != 0)
      { status = Status.ERROR;
      }
      else if (xReg.compareTo( BigDecimal.ONE) == 0)
      { if (cmmnd == 'J')
        { index = index.add( BigInteger.valueOf( prgrmCntr));
        }
        if (  BigInteger.ZERO.compareTo( index) < 1
           && index.compareTo( programLength) < 0)
        { prgrmCntr = index.intValue();
        }
        else
        { status = Status.ERROR;
        }
      }
      else if (xReg.compareTo( BigDecimal.ZERO) != 0)
      { status = Status.ERROR;
      }
    break;
      case 'M'
    : if (mprStatus.lessThan( Mode.STEPPING))
      { mprStatus = Mode.PROGRAM_ENTRY;
      }
      else
      { status = Status.ERROR;
      }
    break;
      // File input/output commands.
      case 'r'
    : case 'w'
    : case 'L'
    : case 'V'
    : inputOutput( cmmnd);
    break;
      // Command "q" exits the program.
      case 'q'
    : return true;
      default
    : status = Status.ILLGL_CHRCTR;
    }
    return false;
  }

  // Method <execute()> takes care of the logic of the operational mode and
  // calls <nonNumericQuit()> to execute commands.

  private boolean execute ()
  {
    int space;
    BigDecimal value;
    char cmmnd;
    // Either loop to the end of the command in manual mode, or execute one com-
    // mand in the program in stepping mode, or execute until run is toggled in
    // running mode.
    while (  Status.ILLGL_CHRCTR.lessThan( status)
          && (  Mode.MANUAL_WAS_ST.lessThan( mprStatus)
             || cmmndNmbr < cmmndsLngth))
    { cmmnd
        =   Mode.MANUAL_WAS_ST.lessThan( mprStatus)
          ? program[ prgrmCntr++]
          : cmmnds.charAt( cmmndNmbr++);
      // In stepping mode, execute just one command and then return in two steps
      // to manual mode.
      if (mprStatus == Mode.STEPPING)
      { mprStatus = Mode.MANUAL_WAS_ST;
      }
      // In program entry mode, just copy the command to the program.
      if      (mprStatus == Mode.PROGRAM_ENTRY)
      { if      (cmmnd == 'M')
        { mprStatus = Mode.MANUAL;
        }
        else if (0 <= prgrmCntr && prgrmCntr < program.length)
        { program[ prgrmCntr++] = cmmnd;
        }
        else
        { status = Status.ERROR;
        }
      }
      else if ('0' <= cmmnd && cmmnd <= '9')
      { value = new BigDecimal( cmmnd - '0');
        // If a numeric command didn't precede this one, start the number from
        // scratch; otherwise if the decimal point has been reached, add another
        // decimal digit after the point; otherwise add a digit before the
        // point.
        switch (status)
        { case NON_NUMERIC
        : xReg   = value;
          status = Status.BEFORE_DECIMAL;
        break;
          case BEFORE_DECIMAL
        : xReg = BigDecimal.TEN.multiply( xReg).add( value);
        break;
          case AFTER_DECIMAL
        : xReg
            = xReg
                .add( value.divide( tenPower, scale, BigDecimal.ROUND_HALF_UP));
          tenPower = tenPower.multiply( BigDecimal.TEN);
        }
      }
      // Command "R" toggles the running status.
      else if (cmmnd == 'R')
      { mprStatus
          = Mode.MANUAL.lessThan( mprStatus) ? Mode.MANUAL : Mode.RUNNING;
      }
      // Command "I" steps through one command in the program.
      else if (cmmnd == 'I')
      { if (mprStatus == Mode.MANUAL)
        { mprStatus = Mode.STEPPING;
        }
        else
        { status = Status.ERROR;
        }
      }
      else if (cmmnd != '.')
      { status = Status.NON_NUMERIC;
        if (nonNumericQuit( cmmnd))
        { return false;
        }
      }
      else if (status.lessThan( Status.AFTER_DECIMAL))
      { if (status == Status.NON_NUMERIC)
        { xReg = BigDecimal.ZERO;
        }
        status   = Status.AFTER_DECIMAL;
        tenPower = BigDecimal.TEN;
      }
      else
      { status = Status.ERROR;
      }
      // Check to see if the program counter is within the bounds of the pro-
      // gram.
      if (program.length <= prgrmCntr && Mode.MANUAL.lessThan( mprStatus))
      { status = Status.ERROR;
      }
      // Second step of transitioning to manual status from stepping status.
      if (mprStatus == Mode.MANUAL_WAS_ST)
      { mprStatus = Mode.MANUAL;
      }
    }
    // If an error occurred, print a vertical pipe to indicate which command
    // caused the error.
    if (status.lessThan( Status.NON_NUMERIC))
    { if (mprStatus.lessThan( Mode.STEPPING))
      { for (space = -1; space < cmmndNmbr; space++)
        { SY_O.print( " ");
        }
        SY_O.println( "|");
      }
      if (status == Status.ILLGL_CHRCTR)
      { SY_O.println( "Illegal character--must be one of");
        SY_O.println
          ( "  \"0123456789.hePEbpxndu+-*/%^lsctSCT=<NAOJRIMrwLVq\"!");
      }
      else
      { SY_O.println( "Error encountered!");
      }
      status    = Status.NON_NUMERIC;
      mprStatus = Mode.MANUAL;
    }
    return true;
  }

  // Method <main()> checks for errors in the input parameters, and if it finds
  // none calls methods <display()> and <execute()> repeatedly to run the calcu-
  // lator.

  public static void main ( String[] arguments)
  {
    if (arguments.length == 3)
    { int arg = 0;
      try
      { int scale     = Integer.parseInt( arguments[   arg]);
        int mmrySze   = Integer.parseInt( arguments[ ++arg]);
        int prgrmTens = Integer.parseInt( arguments[ ++arg]);
        if      (scale < 0)
        { SY_E.println( "Scale must be non-negative!");
        }
        else if (mmrySze < 0)
        { SY_E.println( "Memory size must be non-negative!");
        }
        else if (100 < mmrySze)
        { SY_E.println
            ( "Memory size must be less than or equal to one hundred!");
        }
        else if ( prgrmTens < 0)
        { SY_E.println( "Program size must be non-negative!");
        }
        else if (100 < prgrmTens)
        { SY_E.println
            ( "Program tens must be less than or equal to one hundred!");
        }
        else
        { ScProgCalc clcltr = new ScProgCalc( scale, mmrySze, prgrmTens);
          do
          { clcltr.display();
          }
          while (clcltr.execute());
        }
      }
      catch (NumberFormatException excptn)
      { SY_E.println
          ( "Couldn't convert string \"" + arguments[ arg]
                                         + "\" to an integer!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.println( "  java ScProgCalc <scale> <memory-size> <program-tens>");
    }
  }
}
