package org.example;

public class PseudoInstruction extends AbstractInstruction {
  String[] instructions = new String[2];
  String addr;
  String[] in;
  String type;

  public PseudoInstruction(String[] in, String addr) {
    this.addr = addr;
    this.in = in;
    type = in[0];
  }

  /*
   * Generates the corresponding MIPS instruction(s) from a pseudo-instruction and
   * converts into correct hexadecimal machine code.
   */
  @Override
  public void toMachine(String[] argz) {
    switch(type) {
      case ("li") : loadImmediate(); break;
      case ("la") : loadAddress(argz[0]); break;
      case ("blt") : branchLessThan(); break;
      default :
    }
  }

  public void loadImmediate() {
    StringBuilder sb = new StringBuilder("001001");
    String rs = "00000";
    String rt = decToBin(registers(in[1].replaceAll(",", "")), 5);
    String immediate = decToBin(in[2], 16);
    sb.append(rs);
    sb.append(rt);
    sb.append(immediate);

    instructions[0] = binToHex(sb.toString());
  }

  public String branchLessThan() {
    return null;
  }

  public void loadAddress(String labelAddr) {
    // LUI
    StringBuilder sbLui = new StringBuilder("00111100000");
    String rt = decToBin(registers("$at"), 5);
    String imm = hexToBin(labelAddr.substring(0, 4), 16);
    sbLui.append(rt);
    sbLui.append(imm);
    instructions[0] = Conversions.binToHex(sbLui.toString());

    // ORI
    StringBuilder sbOri = new StringBuilder("001101");
    sbOri.append(rt);
    sbOri.append(decToBin(registers(in[1].replaceAll(",", "")), 5));
    sbOri.append(hexToBin(labelAddr.substring(4 + 1), 16));
    instructions[1] = Conversions.binToHex(sbOri.toString());
  }
}
