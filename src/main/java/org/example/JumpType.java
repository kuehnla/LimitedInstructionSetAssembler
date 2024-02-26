package org.example;

/*
 * A class representing a J-TYPE instruction.
 */
public class JumpType extends AbstractInstruction {
  String instrIndex;
  public JumpType() {
    op = "000010";
  }

  /*
   * Converts jump instruction and index into hexadecimal machine code.
   */
  @Override
  public void toMachine(String[] argz) {
    setInstrIndex(argz);

    StringBuilder sb = new StringBuilder();
    StringBuilder sbInd = new StringBuilder();

    sbInd.append(instrIndex);
    sb.append(op);

    while (sbInd.length() < 26) sbInd.append(0);                // add needed leading 0's
    while (sbInd.length() > 26) sbInd.deleteCharAt(0);    // remove extra leading 0's

    sb.append(sbInd);                                           // form the instruction's machine code in binary
    word = binToDec(sb.toString());                             // converts binary -> decimal -> hexadecimal
    word = decToHex(word);
  }

  /*
   * Removes extra characters and converts the instruction index from hex to binary.
   */
  private void setInstrIndex(String[] argz) {
    instrIndex = hexToBin(argz[1].split("x")[1].split("#")[0]);
  }
}
