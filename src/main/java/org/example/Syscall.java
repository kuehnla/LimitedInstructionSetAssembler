package org.example;

public class Syscall extends AbstractInstruction {
  public String code;
  public Syscall() {
    op = "000010";
    funct = "001100";
    code = "00000000000000000000";
  }

  @Override
  public String toMachine() {
    StringBuilder s = new StringBuilder();
    s.append(op);
    s.append(code);
    s.append(funct);
    return s.toString();
  }
}
