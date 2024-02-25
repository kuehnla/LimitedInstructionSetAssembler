package org.example;

public class JumpType extends AbstractInstruction {
  String instrIndex;
  public JumpType() {
    op = "000010";
  }
  @Override
  public void toMachine(String[] argz) {
    setInstrIndex(argz);
//    instrIndex = hexToBin(instrIndex);
    StringBuilder sb = new StringBuilder();
    sb.append(op);
    for (int i = 0; i < 26 - instrIndex.length(); ++i) sb.append(0);
    sb.append(instrIndex);
    word = binToDec(sb.toString());
    word = decToHex(word);
  }

  private void setInstrIndex(String[] argz) {
    instrIndex = hexToBin(argz[1].split("x")[1].split("#")[0]);
  }
}
