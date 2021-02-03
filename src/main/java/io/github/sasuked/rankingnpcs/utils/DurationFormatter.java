package io.github.sasuked.rankingnpcs.utils;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class DurationFormatter {
  
  private static final Map<TimeUnit, Integer> unitSizes = new LinkedHashMap<>();
  
  static {
    int ms = 1;
    int s = ms * 1000;
    int min = s * 60;
    int h = min * 60;
    int d = h * 24;
    unitSizes.put(TimeUnit.DAYS, d);
    unitSizes.put(TimeUnit.HOURS, h);
    unitSizes.put(TimeUnit.MINUTES, min);
    unitSizes.put(TimeUnit.SECONDS, s);
    unitSizes.put(TimeUnit.MILLISECONDS, ms);
  }
  
  private DurationFormatter() {
  }
  
  public static String format(Duration duration, String formatString, TimeUnit biggestUnit) {
    Iterator<TimeUnit> iterator = unitSizes.keySet().iterator();
    while (iterator.hasNext() && iterator.next() != biggestUnit) ;
    if (!iterator.hasNext()) {
      throw new IllegalArgumentException();
    }
    TimeUnit current = biggestUnit;
    long remainingMillis = duration.toMillis();
    String result = formatString;
    do {
      long unitSize = unitSizes.get(current);
      long amount = remainingMillis / unitSize;
      result = result.replace("{" + current.name().toLowerCase() + "}", String.valueOf(amount));
      remainingMillis %= unitSize;
      current = iterator.next();
    } while (iterator.hasNext());
    return result;
  }
  
}