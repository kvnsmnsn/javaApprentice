package org.familysearch.util;

import java.math.BigInteger;
import org.familysearch.logging.api.LogFactory;
import org.familysearch.logging.api.Logger;

public class Fac
{
  private static final Logger LOGGER = LogFactory.getLog( Fac.class);

  public BigInteger factorial ( int factor)
  {
    if (0 <= factor)
    { int count;
      LOGGER.error( "Initializing {product} to 1.");
      BigInteger product = BigInteger.ONE;
      LOGGER.warn( "After product initialization.");
      for (count = factor; 1 < count; count--) {
        product = product.multiply(BigInteger.valueOf(count));
      }
      LOGGER.error( "Finished calculating {product}.");
      return product;
    }
    else {
      LOGGER.error( "Argument was negative!");
      throw new IllegalArgumentException("Factorial argument can't be negative!");
    }
  }
}
