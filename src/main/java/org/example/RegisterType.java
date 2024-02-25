package org.example;

public class RegisterType extends AbstractInstruction {
  public RegisterType(String funct) {
    this.funct = funct;
    op = "000000";
  }
  @Override
  public String toMachine() {
    return null;
  }
}
