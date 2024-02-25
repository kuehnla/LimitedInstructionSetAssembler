package org.example;

public class Main {
  public static void main(String[] args) {
    String[] argz = args[0].split(" ");
    Instruction in = initOp(argz[0]);
    in.toMachine(argz);
    System.out.print(in.getWord() + "\n");
  }

  private static AbstractInstruction initOp(String op) {
    return switch (op) {
      //R-TYPES:
      case "add" -> new RegisterType("100000");
      case "and" -> new RegisterType("100100");
      case "or" -> new RegisterType("100101");
      case "slt" -> new RegisterType("101010");
      case "sub" -> new RegisterType("100010");
      //I-TYPES:
      case "addiu" -> new ImmediateType("001001");
      case "andi" -> new ImmediateType("001100");
      case "beq" -> new ImmediateType("000100");
      case "bne" -> new ImmediateType("000101");
      case "lui" -> new ImmediateType("001111");
      case "lw" -> new ImmediateType("100011");
      case "ori" -> new ImmediateType("001101");
      case "sw" -> new ImmediateType("101011");
      //J-TYPE:
      case "j" -> new JumpType();
      //SYSCALL:
      case "syscall" -> new Syscall();
      default -> null;
        /*
         * R-TYPE:
         * add 000000, 100000
         * and 000000, 100100
         * or 000000, 100101
         * slt 000000, 101010
         * sub 000000, 100010
         * I-TYPE:
         * addiu 001001
         * andi 001100
         * beq 000100
         * bne 000101
         * lui 001111
         * lw 100011
         * ori 001101
         * sw 101011
         * J-TYPE:
         * j 000010
         * SYSCALL:
         * sycall 000000, 001100
         */
    };
  }
}