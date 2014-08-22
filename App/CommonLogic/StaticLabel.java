public class StaticLabel
{
  String label;
   int[] numbers;

  public StaticLabel ( String lb
                     ,  int[] ns)
  {
    label   = lb;
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
    return "(label: \"" + label + "\", numbers: (" + nsRep + "))";
  }
}
