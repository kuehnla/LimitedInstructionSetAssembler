package org.example;

/*
 * An abstract class representing an instruction. Implements methods that would be useful
 * for different types of instructions such as I-TYPE, R-TYPE, J-TYPE, and Syscall including
 * numerical conversions, register number lookups and argument parsing.
 */
public abstract class AbstractInstruction implements Instruction {
  String word, op;

  @Override
  public String getWord() {
    return word;
  }

  /*
   * Converts a hexadecimal number to binary.
   */
  @Override
  public String hexToBin(String hex, int bits) {
    hex = hex.replaceAll("0", "0000");
    hex = hex.replaceAll("1", "0001");
    hex = hex.replaceAll("2", "0010");
    hex = hex.replaceAll("3", "0011");
    hex = hex.replaceAll("4", "0100");
    hex = hex.replaceAll("5", "0101");
    hex = hex.replaceAll("6", "0110");
    hex = hex.replaceAll("7", "0111");
    hex = hex.replaceAll("8", "1000");
    hex = hex.replaceAll("9", "1001");
    hex = hex.replaceAll("a", "1010");
    hex = hex.replaceAll("b", "1011");
    hex = hex.replaceAll("c", "1100");
    hex = hex.replaceAll("d", "1101");
    hex = hex.replaceAll("e", "1110");
    hex = hex.replaceAll("f", "1111");
    StringBuilder sb = new StringBuilder(hex);
    while (sb.length() < bits) sb.insert(0, 0);
    hex = sb.toString();
    return hex;
  }

  /*
   * Converts a binary number to decimal.
   */
  @Override
  public String binToDec(String bin) {
    return String.valueOf(Integer.parseInt(bin, 2));
  }

  /*
   * Converts a decimal integer to hexadecimal.
   */
  @Override
  public String decToHex(String dec) {
    StringBuilder sb = new StringBuilder(Integer.toHexString(Integer.parseInt(dec)));
    while (sb.length() < 8) {
      sb.insert(0, 0);
    }
    return sb.toString();
  }

  /*
   * Converts a decimal integer to a bit string of specified length.
   */
  @Override
  public String decToBin(String dec, int bits) {
    StringBuilder sb = new StringBuilder(Integer.toBinaryString(Integer.parseInt(dec)));
    while (sb.length() < bits) {
      sb.insert(0, 0);
    }
    return sb.toString();
  }

  /*
   * Returns a string representing the decimal value of a register number.
   */
  @Override
  public String registers(String reg) {
    switch (reg) {
      case "$zero" : return "0";
      case "$at" : return "1";
      case "$gp" : return "28";
      case "$sp" : return "29";
      case "$fp" : return "30";
      case "$ra" : return "31";
      case "$t8" : return "24";
      case "$t9" : return "25";
      default : break;
    }

    char let = reg.charAt(1);
    int num = Character.getNumericValue(reg.charAt(2));
    StringBuilder numString = new StringBuilder();
    return switch (let) {
      case 'v' -> numString.append(2 + num).toString();
      case 'a' -> numString.append(4 + num).toString();
      case 't' -> numString.append(8 + num).toString();
      case 's' -> numString.append(16 + num).toString();
      case 'k' -> numString.append(26 + num).toString();
      default -> null;
    };
  }

  /*
   * Finds an argument from args based on the given index.
   * Ignores nulls or empty strings.
   */
  @Override
  public String argFinder(String[] argz, int index) {
    String arg = null;
    for (String s : argz) {
      if (s == null || s.isEmpty()) continue;
      if (index-- == 0) {
        arg = s;
        break;
      }
    }

    try {
      arg = arg.split(",")[0].split("#")[0];
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return arg;
  }
}