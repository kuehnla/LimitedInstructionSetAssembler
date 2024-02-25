package org.example;

public class RegisterType extends AbstractInstruction {
  public RegisterType(String funct) {
    this.funct = funct;
    op = "000000";
  }
  @Override
  public void toMachine(String[] argz) {
  }
}
