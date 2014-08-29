public class Number
{
  double real;

  public Number ( double re)
  {
    real = re;
  }

  Number add ( Number operand)
  {
    return new Number( real + operand.real);
  }

  Number multiply ( Number operand)
  {
    return new Number( real * operand.real);
  }
}
