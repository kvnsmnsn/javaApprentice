public class InsertionSort
{
  private int[] numbers;

  public InsertionSort ( int[] ns)
  {
    numbers = ns;
  }

  public void sort ()
  {
    int limit = numbers.length;
    int outer;
    int inner;
    int nxInner;
    int nxValue;
    int insertee;
    for (outer = 1; outer < limit; outer++)
    { insertee = numbers[ outer];
      inner    = outer;
      while (0 < inner && insertee < (nxValue = numbers[ nxInner = inner - 1]))
      { numbers[ inner] = nxValue;
        inner           = nxInner;
      }
      numbers[ inner] = insertee;
    }
  }

  public String toString ()
  {
    String result     = "(numbers: (";
    int numbersLength = numbers.length;
    int index;
    for (index = 0; index < numbersLength; index++)
    { result += (0 < index ? ", " : "") + index + ": " + numbers[ index];
    }
    return result + "))";
  }
}
