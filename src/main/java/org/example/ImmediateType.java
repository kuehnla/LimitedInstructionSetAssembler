package org.example;



// args can be decimal or hexadecimal
public class ImmediateType extends AbstractInstruction {
  public ImmediateType(String op) {
    this.op = op;
  }

  @Override
  public void toMachine(String[] argz) {
    rt = decToBin(registers(argFinder(argz, 1)),5);
    rs = decToBin(registers(argFinder(argz, 2)), 5);
    immediate = argFinder(argz, 3);
    immediate = (immediate.charAt(0) == '-') ? twosComplement(immediate) : decOrHex(immediate);
    StringBuilder sb = new StringBuilder(op);
    sb.append(rs);
    sb.append(rt);
    sb.append(immediate);
    word = binToDec(sb.toString());
    word = decToHex(word);
  }

  private String decOrHex(String dig) {
    return (dig.contains("x")) ? hexToBin(dig.split("x")[1], 16) : decToBin(dig, 16);
  }

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
