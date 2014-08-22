public class ComposeLabel
{
  private        String label;
  private InsertionSort indices;

  public ComposeLabel ( String lb
                      ,  int[] nms)
  {
    label   = lb;
    indices = new InsertionSort( nms);
  }

  public void sortIndices ()
  {
    indices.sort();
  }

  public String toString ()
  {
    return "(label: \"" + label + "\", indices: " + indices + ')';
  }
}
