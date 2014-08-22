public class InheritLabel extends InsertionSort
{
  private String label;

  public InheritLabel ( String lb
                      ,  int[] nms)
  {
    super( nms);
    label = lb;
  }

  public String toString ()
  {
    return "(label: \"" + label + "\", " + super.toString().substring( 1);
  }
}
