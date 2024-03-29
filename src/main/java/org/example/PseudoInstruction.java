package org.example;

import java.io.BufferedReader;
import java.io.IOException;

public class PseudoInstruction extends AbstractInstruction {
  String[] instructions = new String[2];
  String addr;
  String[] in;
  String type;
  BufferedReader br;

  public PseudoInstruction(String[] in, String addr, BufferedReader br) {
    this.addr = addr;
    this.in = in;
    type = in[0];
    this.br = br;
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
      case ("blt") :
        try {
          branchLessThan();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        break;
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

  public void branchLessThan() throws IOException {
    // SLT
    StringBuilder sbSlt = new StringBuilder("000000");
    String funct = "00000101010";
    String rs = decToBin(registers(in[1].replaceAll(",", "")), 5);
    String rt = decToBin(registers(in[2].replaceAll(",", "")), 5);
    String rd = "00001";
    sbSlt.append(rs);
    sbSlt.append(rt);
    sbSlt.append(rd);
    sbSlt.append(funct);
    instructions[0] = binToHex(sbSlt.toString());

    // BNE
    StringBuilder sbBne = new StringBuilder("000101");
    sbBne.append(rd);
    sbBne.append(decToBin(registers("$zero"), 5));
    int offset = 0;
    String label = in[3];
    br.mark(1000);
    while (br.ready()) {
      String line = br.readLine();
      line = line.trim();

      if (line.isEmpty() || line.contains("#")|| (line.contains(":") && !line.contains(label))) continue;
      if (line.contains(label)) {
        break;
      }
      ++offset;

      if (line.contains("la") || line.contains("blt")) ++offset;
    }

    br.reset();
    sbBne.append(decToBin(String.valueOf(offset), 16));
    instructions[1] = binToHex(sbBne.toString());
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
