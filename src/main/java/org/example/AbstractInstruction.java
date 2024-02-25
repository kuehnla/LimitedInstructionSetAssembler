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
  public String hexHelper(Object digit) {
    if (digit instanceof Integer) {
      Integer dig = (Integer) digit;
      switch (dig) {
        case 10 -> {return "a";}
        case 11 -> {return "b";}
        case 12 -> {return "c";}
        case 13 -> {return "d";}
        case 14 -> {return "e";}
        case 15 -> {return "f";}
        default -> {return String.valueOf(dig);}
      }
    }

    String hexDig = (String) digit;
    switch (hexDig) {
      case "a" -> {return "10";}
      case "b" -> {return "11";}
      case "c" -> {return "12";}
      case "d" -> {return "13";}
      case "e" -> {return "14";}
      case "f" -> {return "15";}
      default -> {return hexDig;}
    }
  }
}
