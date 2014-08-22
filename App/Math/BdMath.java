/**************************************************************************\
* Program: BdMath.java                                                     *
*  Author: Kevin Simonson                                                  *
* Written: 16 Apr 2014                                                     *
*                                                                          *
*      Provides trigonometric, logarithmic, and exponentiation support for *
* {BigDecimal}s.                                                           *
\**************************************************************************/

import java.math.BigInteger;
import java.math.BigDecimal;

public class BdMath
{
  private static final BigInteger I_TWO  = BigInteger.valueOf( 2);
  private static final BigDecimal D_TWO  = new BigDecimal( I_TWO);
  private static final BigDecimal D_FOUR = new BigDecimal( 4.0);
  private static final BigDecimal D_MN_1 = new BigDecimal( -1.0);

  private class ExponentPower
  {
    private BigInteger exponent;
    private BigDecimal power;

    private ExponentPower ( BigInteger ex
                          , BigDecimal po)
    {
      exponent = ex;
      power    = po;
    }
  }

  private        int scale;
  private BigDecimal epsilon;
  private BigDecimal pi;
  private BigDecimal halfPi;
  private BigDecimal sqrtHalf;

  public BdMath ( int sc)
  {
    scale = sc;
    epsilon
      = BigDecimal
          .ONE
          .divide( BigDecimal.TEN.pow( scale), scale, BigDecimal.ROUND_HALF_UP);
    BigDecimal half = new BigDecimal( 0.5);
    sqrtHalf        = squareRoot( half);
    halfPi          = new BigDecimal( 3.0).multiply( arcSinSmall( half));
    pi              = D_TWO.multiply( halfPi);
  }

  private BigDecimal squareRoot ( BigDecimal square)
  {
    if (BigDecimal.ZERO.compareTo( square) < 1)
    { BigDecimal nxtSrchr;
      BigDecimal searcher;
      for ( searcher = BigDecimal.ONE
          ;   0
            < searcher
                .subtract
                   ( nxtSrchr
                   = square.divide( searcher, scale, BigDecimal.ROUND_HALF_UP)
                           .add( searcher)
                           .divide( D_TWO, scale, BigDecimal.ROUND_HALF_UP))
                   .abs().compareTo( epsilon)
          ; searcher = nxtSrchr);
      return nxtSrchr;
    }
    else
    { throw
        new IllegalArgumentException
          ( "Can't take square root of a negative number!");
    }
  }

  private BigDecimal pow ( BigDecimal base
                         , BigInteger exponent)
  {
    BigInteger[] pair;
    BigDecimal product = BigDecimal.ONE;
    while (BigInteger.ZERO.compareTo( exponent) < 0)
    { pair = exponent.divideAndRemainder( I_TWO);
      if (pair[ 1].compareTo( BigInteger.ONE) == 0)
      { product
          = product.multiply( base)
                   .divide( BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP);
      }
      base
        = base.multiply( base)
              .divide( BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP);
      exponent = pair[ 0];
    }
    return product;
  }

  private ExponentPower log ( BigDecimal base
                            , BigDecimal power
                            , BigInteger exponent)
  {
    if (base.compareTo( power) < 1)
    { ExponentPower result
        = log( base.multiply( base), power, exponent.add( exponent));
      BigDecimal newPower = base.multiply( result.power);
      return
          newPower.compareTo( power) < 1
        ? new ExponentPower( result.exponent.add( exponent), newPower)
        : result;
    }
    else
    { return new ExponentPower( BigInteger.ZERO, BigDecimal.ONE);
    }
  }

  private BigDecimal trigCalc ( BigDecimal angle
                              , BigDecimal firstPower
                              , BigDecimal firstTerm)
  {
    BigDecimal delta;
    BigDecimal angSqrd   = angle.multiply( angle);
    BigDecimal sum       = BigDecimal.ZERO;
    BigDecimal power     = firstPower;
    BigDecimal factorial = BigDecimal.ONE;
    BigDecimal sign      = BigDecimal.ONE;
    BigDecimal term      = firstTerm;
    for (; ; )
    { delta = power.divide( factorial, scale, BigDecimal.ROUND_HALF_UP);
      if (delta.compareTo( epsilon) < 0)
      { return sum;
      }
      sum   = sum.add( delta.multiply( sign));
      power = power.multiply( angSqrd);
      factorial
        = factorial.multiply( term = term.add( BigDecimal.ONE))
                   .multiply( term = term.add( BigDecimal.ONE));
      sign = sign.negate();
    }
  }

  private BigDecimal arcSinSmall ( BigDecimal ratio)
  {
    BigDecimal ratioSquared    = ratio.multiply( ratio);
    BigDecimal factorial       = BigDecimal.ONE;
    BigDecimal doubleFactorial = BigDecimal.ONE;
    BigDecimal fourPower       = BigDecimal.ONE;
    BigDecimal ratioPower      = ratio;
    BigDecimal count           = BigDecimal.ZERO;
    BigDecimal sum             = BigDecimal.ZERO;
    BigDecimal delta;
    BigDecimal doubleCount;
    for (; ; )
    { delta
        = doubleFactorial
            .multiply( ratioPower)
            .divide
               ( fourPower
                   .multiply( factorial).multiply( factorial)
                   .multiply( D_TWO.multiply( count).add( BigDecimal.ONE))
               , scale, BigDecimal.ROUND_HALF_UP);
      if (delta.compareTo( epsilon) < 1)
      { return sum;
      }
      sum         = sum.add( delta);
      count       = count.add( BigDecimal.ONE);
      factorial   = factorial.multiply( count);
      doubleCount = count.add( count);
      doubleFactorial
        = doubleFactorial.multiply( doubleCount.subtract( BigDecimal.ONE))
                         .multiply( doubleCount);
      fourPower  = fourPower.multiply( D_FOUR);
      ratioPower = ratioPower.multiply( ratioSquared);
    }
  }

  private BigDecimal arcCosPos ( BigDecimal avRatio)
  {
    return
        avRatio.compareTo( sqrtHalf) < 1
      ? halfPi.subtract( arcSinSmall( avRatio))
      : arcSinSmall
          ( squareRoot( BigDecimal.ONE.subtract( avRatio.multiply( avRatio))));
  }

  public BigDecimal getPi ()
  {
    return pi;
  }

  public BigDecimal getE ()
  {
    BigDecimal sum    = BigDecimal.ZERO;
    BigDecimal recFac = BigDecimal.ONE;
    BigDecimal count  = BigDecimal.ONE;
    while (epsilon.compareTo( recFac) < 1)
    { sum    = sum.add( recFac);
      recFac = recFac.divide( count, scale, BigDecimal.ROUND_HALF_UP);
      count  = count.add( BigDecimal.ONE);
    }
    return sum;
  }

  public BigDecimal sin ( BigDecimal angle)
  {
    return trigCalc( angle, angle, BigDecimal.ONE);
  }

  public BigDecimal cos ( BigDecimal angle)
  {
    return trigCalc( angle, BigDecimal.ONE, BigDecimal.ZERO);
  }

  public BigDecimal tan ( BigDecimal angle)
  {
    return sin( angle).divide( cos( angle), scale, BigDecimal.ROUND_HALF_UP);
  }

  public BigDecimal arcSin ( BigDecimal ratio)
  {
    if (ratio.compareTo( D_MN_1) < 0 || BigDecimal.ONE.compareTo( ratio) < 0)
    { throw new IllegalArgumentException( "Out of range for arcsin!");
    }
    boolean changed;
    BigDecimal avRatio;
    if (ratio.compareTo( BigDecimal.ZERO) < 0)
    { changed = true;
      avRatio = ratio.negate();
    }
    else
    { changed = false;
      avRatio = ratio;
    }
    BigDecimal intermediate
      =   avRatio.compareTo( sqrtHalf) < 1
        ? arcSinSmall( avRatio)
        : halfPi
            .subtract
               ( arcSinSmall
                   ( squareRoot
                       ( BigDecimal.ONE.subtract( ratio.multiply( ratio)))));
    return changed ? intermediate.negate() : intermediate;
  }

  public BigDecimal arcCos ( BigDecimal ratio)
  {
    if (ratio.compareTo( D_MN_1) < 0 || BigDecimal.ONE.compareTo( ratio) < 0)
    { throw new IllegalArgumentException( "Out of range for arccos!");
    }
    boolean changed;
    BigDecimal avRatio;
    if (ratio.compareTo( BigDecimal.ZERO) < 0)
    { changed = true;
      avRatio = ratio.negate();
    }
    else
    { changed = false;
      avRatio = ratio;
    }
    BigDecimal intermediate = arcCosPos( avRatio);
    return changed ? pi.subtract( intermediate) : intermediate;
  }

  public BigDecimal arcTan ( BigDecimal ratio)
  {
    boolean changed;
    BigDecimal avRatio;
    if (ratio.compareTo( BigDecimal.ZERO) < 0)
    { changed = true;
      avRatio = ratio.negate();
    }
    else
    { changed = false;
      avRatio = ratio;
    }
    BigDecimal intermediate
      = arcCosPos
          ( squareRoot
              ( BigDecimal
                  .ONE
                  .divide( avRatio.multiply( avRatio).add( BigDecimal.ONE)
              , scale, BigDecimal.ROUND_HALF_UP)));
    return changed ? intermediate.negate() : intermediate;
  }

  public BigDecimal pow ( BigDecimal base
                        , BigDecimal exponent)
  {
    if (exponent.compareTo( BigDecimal.ZERO) < 0)
    { if (base.compareTo( BigDecimal.ZERO) == 0)
      { throw
          new IllegalArgumentException
            ( "Can't raise zero to a negative power!");
      }
      base     = BigDecimal.ONE.divide( base, scale, BigDecimal.ROUND_HALF_UP);
      exponent = exponent.negate();
    }
    BigInteger iExp        = exponent.toBigInteger();
    BigDecimal fExp        = exponent.subtract( new BigDecimal( iExp));
    BigDecimal product     = pow( base, iExp);
    if      (fExp.compareTo( BigDecimal.ZERO) == 0)
    { return product;
    }
    else if (BigDecimal.ZERO.compareTo( base) < 1)
    { while (epsilon.compareTo( base.subtract( BigDecimal.ONE).abs()) < 0)
      { base = squareRoot( base);
        fExp = fExp.multiply( D_TWO);
        iExp = fExp.toBigInteger();
        if (BigInteger.ONE.compareTo( iExp) <= 0)
        { product = product.multiply( base);
          fExp    = fExp.subtract( new BigDecimal( iExp));
        }
      }
      return product.divide( BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP);
    }
    else
    { throw
        new IllegalArgumentException
          ( "Can't raise a negative number to a non-integer power!");
    }
  }

  public BigDecimal log ( BigDecimal base
                        , BigDecimal power)
  {
    boolean negateResult = false;
    int baseToOne        = base.compareTo( BigDecimal.ONE);
    int powerToOne       = power.compareTo( BigDecimal.ONE);
    if (baseToOne == 0)
    { throw new IllegalArgumentException( "Base can't be one!");
    }
    if (base.compareTo( BigDecimal.ZERO) < 1)
    { throw new IllegalArgumentException( "Base must be positive!");
    }
    if (power.compareTo( BigDecimal.ZERO) < 1)
    { throw new IllegalArgumentException( "Power must be positive!");
    }
    if (baseToOne < 1)
    { base = BigDecimal.ONE.divide( base, scale, BigDecimal.ROUND_HALF_UP);
      negateResult = true;
    }
    if (powerToOne < 1)
    { power = BigDecimal.ONE.divide( power, scale, BigDecimal.ROUND_HALF_UP);
      negateResult = ! negateResult;
    }
    ExponentPower iPair = log( base, power, BigInteger.ONE);
    BigDecimal result   = new BigDecimal( iPair.exponent);
    BigDecimal fPower
      = power.divide( iPair.power, scale, BigDecimal.ROUND_HALF_UP);
    BigDecimal toAdd        = BigDecimal.ONE;
    BigDecimal toMultiplyBy = base;
    for (; ; )
    { toAdd = toAdd.divide( D_TWO, scale, BigDecimal.ROUND_HALF_UP);
      if (toAdd.compareTo( epsilon) < 1)
      { return negateResult ? result.negate() : result;
      }
      toMultiplyBy = squareRoot( toMultiplyBy);
      if (toMultiplyBy.compareTo( fPower) < 1)
      { result = result.add( toAdd);
        fPower = fPower.divide( toMultiplyBy, scale, BigDecimal.ROUND_HALF_UP);
      }
    }
  }
}
