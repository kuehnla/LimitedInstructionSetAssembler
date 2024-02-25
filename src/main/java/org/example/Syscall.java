package org.example;

public class Syscall extends AbstractInstruction {
  public String code;
  public Syscall() {
    op = "000000";
    funct = "001100";
    code = "00000000000000000000";
  }

  @Override
  public void toMachine(String[] argz) {
    StringBuilder s = new StringBuilder();
    s.append(op);
    s.append(code);
    s.append(funct);
    word = s.toString();
    word = binToDec(word);
    word = decToHex(word);
  }
}
