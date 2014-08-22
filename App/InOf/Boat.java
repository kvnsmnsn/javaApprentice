public class Boat extends Vehicle
{
  int numberLifeboats;

  public Boat (    int nw
              , double mv
              ,    int nlb)
  {
    super( nw, mv);
    numberLifeboats = nlb;
  }
}
