package org.example;

/*
 * A class representing a syscall instruction.
 */
public class Syscall extends AbstractInstruction {
  public Syscall() {
  }

  /*
   * Converts syscall instruction into hexadecimal machine code.
   */
  @Override
  public void toMachine(String[] argz) {
    word = decToHex(binToDec(word = "00000000000000000000000000001100"));
  }
}
