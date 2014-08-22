import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Bad
{
  private static final PrintStream SY_O = System.out;

  private static void backupSources (   File source
                                    , String stem
                                    ,    int suffix)
  {
    File dstntn = new File( stem + ".b" + suffix);
    if (source.exists())
    { backupSources( dstntn, stem, suffix + 1);
    }
    source.renameTo( dstntn);
  }

  public static void main ( String[] arguments)
  {
    int argsLength = arguments.length;
    if (0 < argsLength)
    { int arg = 0;
      try
      { boolean stripping  = false;
        boolean adding     = false;
        int maxWidth       = 79;
        char commentSuffix = '\\';
        String argmnt;
        while (++arg < argsLength)
        { argmnt = arguments[ arg];
          if      (argmnt.equals( "s"))
          { stripping = true;
          }
          else if (argmnt.equals( "a"))
          { adding = true;
          }
          else if (argmnt.startsWith( "w"))
          { maxWidth = Integer.parseInt( argmnt.substring( 1));
          }
          else if (argmnt.startsWith( "c") && argmnt.length() == 2)
          { commentSuffix = argmnt.charAt( 1);
          }
          else
          { SY_O.println( "Arguments \"" + argmnt + "\" illegal, so ignored.");
          }
        }
        if (stripping || adding)
        { String comment = " //" + commentSuffix + ' ';
          String dstName = arguments[ 0];
          File dstntn    = new File( dstName);
          File source    = new File( dstName + ".b1");
          backupSources( source, dstName, 2);
          dstntn.renameTo( source);
          Scanner fromDisk = new Scanner( source);
          PrintWriter toDisk
            = new PrintWriter( new BufferedWriter( new FileWriter( dstntn)));
          int lineCount = 0;
          String spaces = " ";
          String line;
          while (fromDisk.hasNextLine())
          { line = fromDisk.nextLine();
            lineCount++;
            if (stripping)
            { int nonNumeric;
              char ch;
              for ( nonNumeric = line.length() - 1
                  ; 0 <= nonNumeric && '0' <= (ch = line.charAt( nonNumeric))
                                    && ch <= '9'
                  ; nonNumeric--);
              int low = nonNumeric - 4;
              if (  0 <= low
                 && line.substring( low, nonNumeric + 1).equals( comment))
              { int nonSpace;
                for ( nonSpace = low - 1
                    ; 0 <= nonSpace && line.charAt( nonSpace) == ' '
                    ; nonSpace--);
                line = line.substring( 0, nonSpace + 1);
              }
            }
            if (adding)
            { int lastNonSpace;
              String number = "" + lineCount;
              for ( lastNonSpace = line.length() - 1
                  ; 0 <= lastNonSpace && line.charAt( lastNonSpace) == ' '
                  ; lastNonSpace--);
              int addedLength = lastNonSpace + number.length() + 6;
              if (addedLength <= maxWidth)
              { int padding = maxWidth - addedLength;
                while (spaces.length() < padding)
                { spaces += spaces;
                }
                line
                  =   line.substring( 0, lastNonSpace + 1)
                    + spaces.substring( 0, padding) + comment + number;
              }
            }
            toDisk.println( line);
          }
          fromDisk.close();
          toDisk.close();
          SY_O.println( "Processed " + lineCount + " lines.");
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
      SY_O.println
        ( "  java Bad <file-name> [s] [a] [w<maxWidth>] [c<cmmnt-sffx>]");
    }
  }
}
