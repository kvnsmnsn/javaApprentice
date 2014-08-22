import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class NumberLines
{
  private static final PrintStream SY_O = System.out;

  private boolean stripping;
  private boolean adding;
  private     int maxWidth;
  private  String comment;
  private  String dstName;
  private    File dstntn;
  private    File source;
  private  String spaces;

  private NumberLines ( boolean st
                      , boolean ad
                      ,     int mw
                      ,    char cs
                      ,  String dn)
  {
    stripping = st;
    adding    = ad;
    maxWidth  = mw;
    comment   = " //" + cs + ' ';
    dstName   = dn;
    dstntn    = new File( dstName);
    source    = backupSource();
    spaces    = " ";
  }

  private void backupSources ( File source
                             ,  int suffix)
  {
    File dstntn = new File( dstName + ".b" + suffix);
    if (source.exists())
    { backupSources( dstntn, suffix + 1);
    }
    source.renameTo( dstntn);
  }

  private File backupSource ()
  {
    File source = new File( dstName + ".b1");
    backupSources( source, 2);
    dstntn.renameTo( source);
    return source;
  }

  private String strip ( String sourceLine)
  {
    int nonNumeric;
    char ch;
    for ( nonNumeric = sourceLine.length() - 1
        ; 0 <= nonNumeric && '0' <= (ch = sourceLine.charAt( nonNumeric))
                          && ch <= '9'
        ; nonNumeric--);
    int low = nonNumeric - 4;
    if (0 <= low && sourceLine.substring( low, nonNumeric + 1).equals( comment))
    { int nonSpace;
      for ( nonSpace = low - 1
          ; 0 <= nonSpace && sourceLine.charAt( nonSpace) == ' '
          ; nonSpace--);
      return sourceLine.substring( 0, nonSpace + 1);
    }
    return sourceLine;
  }

  private String add ( String sourceLine
                     , String number)
  {
    int lastNonSpace;
    for ( lastNonSpace = sourceLine.length() - 1
        ; 0 <= lastNonSpace && sourceLine.charAt( lastNonSpace) == ' '
        ; lastNonSpace--);
    int addedLength = lastNonSpace + number.length() + 6;
    if (addedLength <= maxWidth)
    { int padding = maxWidth - addedLength;
      while (spaces.length() < padding)
      { spaces += spaces;
      }
      return
          sourceLine.substring( 0, lastNonSpace + 1)
        + spaces.substring( 0, padding) + comment + number;
    }
    else
    { return sourceLine;
    }
  }

  private int readFile ()
                       throws FileNotFoundException, IOException
  {
    Scanner fromDisk = new Scanner( source);
    PrintWriter toDisk
      = new PrintWriter( new BufferedWriter( new FileWriter( dstntn)));
    int lineCount = 0;
    String line;
    while (fromDisk.hasNextLine())
    { line = fromDisk.nextLine();
      lineCount++;
      if (stripping)
      { line = strip( line);
      }
      if (adding)
      { line = add( line, "" + lineCount);
      }
      toDisk.println( line);
    }
    fromDisk.close();
    toDisk.close();
    return lineCount;
  }

  public static void main ( String[] arguments)
  {
    int argsLength = arguments.length;
    if (0 < argsLength)
    { int arg = 0;
      try
      { boolean strip  = false;
        boolean add    = false;
        int mxWdth     = 79;
        char cmmntSffx = '\\';
        String argmnt;
        while (++arg < argsLength)
        { argmnt = arguments[ arg];
          if      (argmnt.equals( "s"))
          { strip = true;
          }
          else if (argmnt.equals( "a"))
          { add = true;
          }
          else if (argmnt.startsWith( "w"))
          { mxWdth = Integer.parseInt( argmnt.substring( 1));
          }
          else if (argmnt.startsWith( "c") && argmnt.length() == 2)
          { cmmntSffx = argmnt.charAt( 1);
          }
          else
          { SY_O.println( "Argument \"" + argmnt + "\" illegal, so ignored.");
          }
        }
        if (strip || add)
        { NumberLines nmbrLns
            = new NumberLines( strip, add, mxWdth, cmmntSffx, arguments[ 0]);
          SY_O.println( "Processed " + nmbrLns.readFile() + " lines.");
        }
        else
        { SY_O.print  ( "Without either an \"a\" or an \"s\" argument");
          SY_O.println(           ", there's nothing to do.");
        }
      }
      catch (NumberFormatException nfExc)
      { SY_O.println
          ( "Unable to convert argument \"" + arguments[ arg]
                                            + "\" to an integer!");
      }
      catch (FileNotFoundException fnfExc)
      { SY_O.println
          ( "Unable to open file \"" + arguments[ 0] + ".b1\" for input!");
      }
      catch (IOException ioExc)
      { SY_O.println
          ( "Unable to open file \"" + arguments[ 0] + "\" for output!");
      }
    }
    else
    { SY_O.println( "Usage is");
      SY_O.print  ( "  java NumberLines <file-name> [s] [a] [");
      SY_O.println(           "w<maxWidth>] [c<cmmnt-sffx>]");
    }
  }
}
