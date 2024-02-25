package org.example;

public class ImmediateType extends AbstractInstruction {
  public ImmediateType(String op) {
    this.op = op;
  }

  @Override
  public String toMachine() {
    return null;
  }
}
