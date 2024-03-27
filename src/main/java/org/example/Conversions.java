package org.example;

public class Conversions {

  /*
   * Converts a binary number to decimal.
   */
  public static String binToDec(String bin) {
    return String.valueOf(Integer.parseInt(bin, 2));
  }

  /*
   * Converts a decimal integer to hexadecimal.
   */
  public static String decToHex(String dec) {
    StringBuilder sb = new StringBuilder(Integer.toHexString(Integer.parseInt(dec)));
    while (sb.length() < 8) {
      sb.insert(0, 0);
    }
    return sb.toString();
  }

  /*
   * Converts a decimal integer to a bit string of specified length.
   */
  public static String decToBin(String dec, int bits) {
    StringBuilder sb = new StringBuilder(Integer.toBinaryString(Integer.parseInt(dec)));
    while (sb.length() < bits) {
      sb.insert(0, 0);
    }
    return sb.toString();
  }
}
