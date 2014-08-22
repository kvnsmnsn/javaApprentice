package def;

import java.io.PrintStream;
import abc.AbcOne;

public class DefOne
{
  private static final PrintStream SY_O = System.out;

  public static void directs ()
  {
    AbcOne abcOne = new AbcOne( 23, 84, 62, 64);
    // SY_O.println( "abcOne.def: " + abcOne.def);
    // SY_O.println( "abcOne.ghi: " + abcOne.ghi);
    // SY_O.println( "abcOne.jkl: " + abcOne.jkl);
    SY_O.println( "abcOne.mno: " + abcOne.mno);
  }

  public static void indirects ()
  {
    AbcOne abcOne = new AbcOne( 33, 83, 27, 95);
    SY_O.println( "abcOne.getDef(): " + abcOne.getDef());
    // SY_O.println( "abcOne.getGhi(): " + abcOne.getGhi());
    // SY_O.println( "abcOne.getJkl(): " + abcOne.getJkl());
    // SY_O.println( "abcOne.getMno(): " + abcOne.getMno());
  }

  public static void main ( String[] arguments)
  {
    directs();
    indirects();
  }
}
