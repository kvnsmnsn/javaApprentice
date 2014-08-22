public class StaticRatio
{
  double ratio;
   int[] numbers;

  public StaticRatio ( double rt
                     ,  int[] ns)
  {
    ratio   = rt;
    numbers = ns;
  }

  public void sortIndices ()
  {
    StaticSort.stInSort( numbers);
  }

  public String toString ()
  {
    String nsRep      = "";
    int numberIndices = numbers.length;
    int index;
    for (index = 0; index < numberIndices; index++)
    { nsRep += (0 < index ? ", " : "") + index + ": " + numbers[ index];
    }
    return "(ratio: " + ratio + ", numbers: (" + nsRep + "))";
  }
}
