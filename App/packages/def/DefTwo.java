package def;

import java.io.PrintStream;
import abc.AbcOne;

public class DefTwo extends AbcOne
{
  private static final PrintStream SY_O = System.out;

  private int pqr;

  public DefTwo ( int de
                , int gh
                , int jk
                , int mn
                , int pq)
  {
    super( de, gh, jk, mn);
    pqr = pq;
  }

  public int getGhi ()
  {
    return super.getGhi();
  }

  public int getJkl ()
  {
    return jkl;
  }

  public int getMno ()
  {
    return mno;
  }

  public int getPqr ()
  {
    return pqr;
  }

  public static void main ( String[] arguments)
  {
    DefTwo defTwo = new DefTwo( 2, 88, 41, 97, 16);
    SY_O.println( "defTwo.getDef(): " + defTwo.getDef());
    SY_O.println( "defTwo.getGhi(): " + defTwo.getGhi());
    SY_O.println( "defTwo.getJkl(): " + defTwo.getJkl());
    SY_O.println( "defTwo.getMno(): " + defTwo.getMno());
    SY_O.println( "defTwo.getPqr(): " + defTwo.getPqr());
  }
}
