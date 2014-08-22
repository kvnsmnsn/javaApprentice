package org.familysearch.util;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;
import java.math.BigInteger;

public class FacTest {

  Fac fac;

  @BeforeMethod
  public void setupClass ()
  {
    fac = new Fac();
  }

  @Test
  public void testZero ()
  {
    Assert.assertEquals( fac.factorial( 0), BigInteger.ONE, "Calculation of 0! failed!");
  }

  @Test
  public void testOne ()
  {
    Assert.assertEquals( fac.factorial( 1), BigInteger.ONE, "Calculation of 1! failed!");
  }

  @Test
  public void testTwo ()
  {
    Assert.assertEquals( fac.factorial( 2), BigInteger.valueOf( 2), "Calculation of 2! failed!");
  }

  @Test
  public void testThree ()
  {
    Assert.assertEquals( fac.factorial( 3), BigInteger.valueOf( 6), "Calculation of 3! failed!");
  }

  @Test
  public void testTen ()
  {
    Assert
      .assertEquals
         ( fac.factorial( 10), BigInteger.valueOf( 3628800), "Calculation of 10! failed!");
  }

  @Test
  public void testFifty ()
  {
    Assert
      .assertEquals
         ( fac.factorial( 50)
         , new BigInteger( "30414093201713378043612608166064768844377641568960512000000000000")
         , "Calculation of 50! failed!");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNegative ()
  {
    fac.factorial( -1);
  }
}
