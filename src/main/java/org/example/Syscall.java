package org.example;

public class Syscall extends AbstractInstruction {
  public Syscall() {
    op = "000010";
    funct = "001100";
  }

  @Override
  public String toMachine() {

  }
}
