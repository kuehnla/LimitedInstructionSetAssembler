package org.example;

public class RegisterType extends AbstractInstruction {
  private final static String ZEROS = "00000";
  public RegisterType(String funct) {
    this.funct = funct;
    op = "000000";
  }
  @Override
  public void toMachine(String[] argz) {
    rd = argz[1];
    rs = argz[2];
    rt = argz[3];

  }
}
