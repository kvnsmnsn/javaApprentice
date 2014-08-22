public class StaticSort
{
  public static void stInSort ( int[] nmbrs)
  {
    int limit = nmbrs.length;
    int outer;
    int inner;
    int nxInner;
    int nxValue;
    int insertee;
    for (outer = 1; outer < limit; outer++)
    { insertee = nmbrs[ outer];
      inner    = outer;
      while (0 < inner && insertee < (nxValue = nmbrs[ nxInner = inner - 1]))
      { nmbrs[ inner] = nxValue;
        inner         = nxInner;
      }
      nmbrs[ inner] = insertee;
    }
  }
}
