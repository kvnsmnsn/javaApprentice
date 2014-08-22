public class ComposeRatio
{
  private        double ratio;
  private InsertionSort indices;

  public ComposeRatio ( double rt
                      ,  int[] nms)
  {
    ratio   = rt;
    indices = new InsertionSort( nms);
  }

  public void sortIndices ()
  {
    indices.sort();
  }

  public String toString ()
  {
    return "(ratio: " + ratio + ", indices: " + indices + ')';
  }
}
