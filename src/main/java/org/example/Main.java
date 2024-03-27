package org.example;

import java.io.*;

public class Main {
  public static void main(String[] args) {
    if (!args[0].contains("asm")) {
      String[] argz = args[0].split(" ");
      Instruction in = singleInstruction(argz);
      in.toMachine(argz);
      System.out.print(in.getWord() + "\n");
    } else {
      try {
        assemblyFile(args[0]);
      } catch (IOException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private static AbstractInstruction singleInstruction(String[] argz) {
    String op = null;
    for (String s : argz) {
      if (s == null || s.isEmpty()) continue;
      op = s;
      break;
    }
    assert op != null;
    op = op.split("#")[0];
    return initOp(op);
  }

  private static void assemblyFile(String path) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(path));
    System.out.println(br.readLine());
  }

  private static AbstractInstruction initOp(String op) {
    return switch (op) {
      // R-TYPES:
      case "add" -> new RegisterType("100000");
      case "and" -> new RegisterType("100100");
      case "or" -> new RegisterType("100101");
      case "slt" -> new RegisterType("101010");
      case "sub" -> new RegisterType("100010");
      // I-TYPES:
      case "addiu" -> new ImmediateType("001001");
      case "andi" -> new ImmediateType("001100");
      case "beq" -> new ImmediateType("000100");
      case "bne" -> new ImmediateType("000101");
      case "lui" -> new ImmediateType("001111");
      case "lw" -> new ImmediateType("100011");
      case "ori" -> new ImmediateType("001101");
      case "sw" -> new ImmediateType("101011");
      // J-TYPE:
      case "j" -> new JumpType();
      // SYSCALL:
      case "syscall" -> new Syscall();
      default -> null;
      // TODO : li la blt
    };
  }
}