public class DouIntStrLon extends IntStrLon
{
  double dou;

  public DouIntStrLon (   long lo
                      , String st
                      ,    int ig
                      , double db)
  {
    super( lo, st, ig);
    dou = db;
  }

  public DouIntStrLon (   long lo
                      ,    int ig
                      , double db)
  {
    this( lo, "", ig, db);
  }

  public DouIntStrLon (   long lo
                      , double db)
  {
    this( lo, 0, db);
  }

  public DouIntStrLon ( double db)
  {
    this( 0L, db);
  }
}
