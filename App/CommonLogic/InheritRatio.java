public class InheritRatio extends InsertionSort
{
  private double ratio;

  public InheritRatio ( double rt
                      ,  int[] nms)
  {
    super( nms);
    ratio = rt;
  }

  public String toString ()
  {
    return "(ratio: " + ratio + ", " + super.toString().substring( 1);
  }
}
