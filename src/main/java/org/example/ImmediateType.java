package org.example;

/*
 * A class representing an I-TYPE instruction.
 */
public class ImmediateType extends AbstractInstruction {
  String rt, rs, immediate, base, offset;
  public ImmediateType(String op) {
    this.op = op;
  }

  @Override
  public void toMachine(String[] argz) {
    // lw, sw, or lui
    if (op.equals("101011") || op.equals("100011")) {
      storeWordLoadWord(argz);
      return;
    } else if (op.equals("001111")) {
      loadUpperImmediate(argz);
      return;
    }
    // normal I-TYPES
    rt = decToBin(registers(argFinder(argz, 1)),5);
    rs = decToBin(registers(argFinder(argz, 2)), 5);
    immediate = argFinder(argz, 3);
    immediate = (immediate.charAt(0) == '-') ? twosComplement(immediate) : decOrHex(immediate);
    StringBuilder sb = new StringBuilder(op);
    sb.append(rs);
    sb.append(rt);
    sb.append(immediate);
    word = decToHex(binToDec(sb.toString()));
  }

  private void storeWordLoadWord(String[] argz) {

  }

  private void loadUpperImmediate(String[] argz) {
    rt = decToBin(registers(argFinder(argz, 1)), 5);
    immediate = argFinder(argz, 2);
    immediate = (immediate.charAt(0) == '-') ? twosComplement(immediate) : decOrHex(immediate);
    StringBuilder sb = new StringBuilder(op);
    sb.append("00000");
    sb.append(rt);
    sb.append(immediate);
    word = decToHex(binToDec(sb.toString()));
  }

  /*
   * Determines the appropriate conversion method to call based on given number.
   */
  private String decOrHex(String dig) {
    return (dig.contains("x")) ? hexToBin(dig.split("x")[1], 16) : decToBin(dig, 16);
  }

  /*
   * Converts a negative-signed binary digit to two's complement representation.
   */
  private String twosComplement(String dig) {
    dig = decToBin(dig.split("-")[1], 16);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < dig.length(); ++i) {
      sb.append((dig.charAt(i) == '0') ? 1 : 0);
    }

    String one = "1";
    int num0 = Integer.parseInt(sb.toString(), 2);
    int num1 = Integer.parseInt(one, 2);
    dig = Integer.toBinaryString(num0 + num1);

    return dig;
  }
}
