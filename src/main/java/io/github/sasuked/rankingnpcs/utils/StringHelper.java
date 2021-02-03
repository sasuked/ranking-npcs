package io.github.sasuked.rankingnpcs.utils;

import org.bukkit.material.MaterialData;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.google.common.base.Strings.repeat;

public class StringHelper {
  
  private static final DecimalFormat decimalFormat = new DecimalFormat("###,###");
  private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
  private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
  private static String[] formats = {"-", "-",
    "K", // Mil
    "M", // Milhão
    "B", // Bilhão
    "T", // Trilhão
    "Q", // Quadrilhão
    "QT", // Quintilhão
    "S", // Sextilhão
    "ST", // Setilhão
    "O", // Octilhão
    "N", // Nonilhão
    "D", // Decilhão
    "UD", // Undecilhão
    "DD", // Duodecilhão
    "TD", // Tredecilhão
    "QD", // Quadecilhão
    "QTD", // Quintedecilhão
    "SD", // Sextedecihão
    "STD", // Setedecilhão
    "OD", // Octedecilhão
    "ND", // Novendecilhão
    "V" // Vingintilhão
  };
  
  static {
    DECIMAL_FORMAT.setMaximumFractionDigits(2);
  }
  
  
  public static String charsFrom(String label, char start, char end) {
    int startIndex = label.indexOf(start);
    int endIndex = label.indexOf(end);
    
    if (startIndex == -1 || endIndex == -1) {
      return "";
    }
    
    return label.substring(startIndex, endIndex);
  }
  
  public static char[] charsFrom(String label, int index, int end) {
    if (index < 0) index = 0;
    
    int length = label.length();
    if (end > length) {
      end = length;
    }
    
    return label.substring(index, end).toCharArray();
  }
  
  public static String simpleFormat(double value) {
    return decimalFormat.format(value).replace(',', '.');
  }
  
  public static String format(double value) {
    if (value < 1_000_000D) {
      return decimalFormat.format(value).replace(',', '.');
    }
    
    try {
      String val = decimalFormat.format(value).replace(".", ",");
      int ii = val.indexOf(","), i = val.split(",").length;
      
      if (ii == -1) return val;
      
      return (val.substring(0, ii + 2) + formats[i]).replace(",0", "");
    } catch (Exception e) {
      String val = decimalFormat.format(value).replace(".", ",");
      int ii = val.indexOf(",");
      
      if (ii == -1) return val;
      
      String num = val.substring(0, 1);
      String finalVal = val.substring(1).replace(",", "");
      
      return num + "e" + finalVal.length();
    }
  }
  
  public static String formatPercentage(double value) {
    return DECIMAL_FORMAT.format(value);
  }
  
  public static String simpleBar(int current, int max) {
    return getProgressBar(
      current,
      max,
      5,
      '▋',
      "§a",
      "§8"
    );
  }
  
  public static String getProgressBar(
    int current,
    int max,
    int totalBars,
    char symbol,
    String completedColor,
    String notCompletedColor
  ) {
    float percent = (float) current / max;
    
    if (percent >= 100) {
      percent = 100;
    }
    
    int progressBars = (int) (totalBars * percent);
    if (progressBars >= totalBars) {
      progressBars = totalBars;
    }
    
    int restant = totalBars - progressBars;
    if (restant <= 0) restant = 0;
    
    return completedColor + repeat("" + symbol, progressBars)
      + notCompletedColor + repeat("" + symbol, restant);
  }
  
  public static MaterialData parseMaterialData(String string) {
    try {
      int id;
      int data = 0;
      
      if (string.contains(":")) {
        String[] split = string.split(":");
        
        id = Integer.parseInt(split[0]);
        data = Integer.parseInt(split[1]);
      } else {
        id = Integer.parseInt(string);
      }
      
      return new MaterialData(id, (byte) data);
    } catch (Exception e) {
      return null;
    }
  }
  
  public static String toString(MaterialData materialData) {
    return materialData.getItemTypeId() + ":" + (int) materialData.getData();
  }
  
  public static boolean isInt(String arg) {
    try {
      Integer.parseInt(arg);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
