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
    immediate = decOrHex(argFinder(argz, 3));
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
}
