package org.example;

public abstract class AbstractInstruction implements Instruction {
  String word, op, rs, rt, rd, shamt, funct;

  @Override
  public String getWord() {
    return word;
  }

  @Override
  public String getOp() {
    return op;
  }

  @Override
  public String getFunct() {
    return funct;
  }

  @Override
  public String getRS() {
    return rs;
  }

  @Override
  public String getRT() {
    return rt;
  }

  @Override
  public String getRD() {
    return rd;
  }

  @Override
  public String getShamt() {
    return shamt;
  }

  @Override
  public void setRS(String[] argz) {return;}

  @Override
  public void setRT(String[] argz) {return;}

  @Override
  public void setRD(String[] argz) {return;}

  @Override
  public void setShamt(String[] argz) {return;}

  @Override
  public void toMachine(String[] argz) {
    return;
  }

  @Override
  public String hexToBin(String hex) {
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
    return hex;
  }

  @Override
  public String binToDec(String bin) {
    int dec = 0;
    for (int i = bin.length() - 1, j = 0; i >= 0; --i, ++j) {
      dec += (int) ((bin.charAt(i)-'0') * Math.pow(2, j));
    }
    return String.valueOf(dec);
  }

  @Override
  public String decToHex(String dec) {
    int decimal = Integer.parseInt(dec);
    StringBuilder hex = new StringBuilder();
    do {
      hex.append(hexHelper(decimal % 16));
      decimal /= 16;
    } while (decimal > 0);

    while (hex.toString().length() < 8) {
      hex.append(0);
    }

    return hex.reverse().toString();
  }

  @Override
  public String decToBin(String dec, int bits) {
    StringBuilder sb = new StringBuilder();
    int val = Integer.parseInt(dec);
    do {
      sb.insert(0, (val % 2 == 0 ? 0 : 1));
      val /= 2;
    } while (val > 0);

    while (sb.length() < 5) sb.insert(0, 0);

    return sb.toString();
  }

  @Override
  public String hexHelper(Object digit) {
    if (digit instanceof Integer) {
      Integer dig = (Integer) digit;
      return switch (dig) {
        case 10 -> "a";
        case 11 -> "b";
        case 12 -> "c";
        case 13 -> "d";
        case 14 -> "e";
        case 15 -> "f";
        default -> String.valueOf(dig);
      };
    }

    String hexDig = (String) digit;
    return switch (hexDig) {
      case "a" -> "10";
      case "b" -> "11";
      case "c" -> "12";
      case "d" -> "13";
      case "e" -> "14";
      case "f" -> "15";
      default -> hexDig;
    };
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
      arg = arg.split(",")[0];
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return arg;
  }
}