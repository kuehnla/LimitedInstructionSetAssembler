package org.example;

import java.io.*;
import java.util.regex.*;

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
    while (br.ready()) {
      if (br.readLine().equals(".data"))
        data(br, path);
    }
  }

  private static void data(BufferedReader br, String path) throws IOException {

    // TODO: needs to store addresses of data

    String outName = fileName(path) + ".data";
    path = basePath(path) + outName;
    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
    StringBuilder sb = new StringBuilder();
    int j = 0;
    while (br.ready()) {
      br.mark(1);
      String line = br.readLine();

      if (line.equals(".text") || line.equals(".data")) {
        br.reset();
        break;
      }

      if (line.isEmpty()) continue;

      Pattern p = Pattern.compile("\"([^\"]*)\"");
      Matcher m = p.matcher(line);
      while (m.find()) {
        line = m.group(1);
      }

      System.out.println(line);

      // Integer.toHexString(Integer.parseInt(dec)
      for (int i = 0; i < line.length(); ++i, ++j) {
        if (j > 3) {
          j = 0;
          bw.write(sb.toString());
          bw.newLine();
          bw.flush();
          System.out.println("WRITING: " + sb.toString());
          sb.delete(0, sb.length());
        }

        sb.insert(0, Integer.toHexString(line.charAt(i)));
      }

      if (j > 3) {
        j = 1;
        bw.write(sb.toString());
        bw.newLine();
        bw.flush();
        System.out.println("WRITING: " + sb.toString());
        sb.delete(0, sb.length());
        sb.insert(0, "00");
      } else {
        sb.insert(0, "00");
        ++j;
      }
    }

    if (!sb.isEmpty()) {
      while (sb.length() < 8)
        sb.insert(0, 0);

      bw.write(sb.toString());
      bw.newLine();
      bw.flush();
    }
    bw.close();

  }

  private static String basePath(String path) {
    int lastSlashIndex = path.lastIndexOf('/');
    if (lastSlashIndex != path.length() - 1) {
      return path.substring(0, lastSlashIndex + 1);
    }

    for (int i = lastSlashIndex - 1; i > 0; --i) {
      if (path.charAt(i) == '/')
        return path.substring(0, i + 1);
    }

    return null;
  }

  private static String fileName(String path) {
    int lastSlashIndex = path.lastIndexOf('/');
    if (lastSlashIndex == path.length() - 1) {
      int i = lastSlashIndex - 1;
      for (; i > 0; --i) {
        if (path.charAt(i) == '/') break;
      }
      return path.substring(i + 1, lastSlashIndex).split("\\.")[0];
    }

    return path.substring(lastSlashIndex + 1).split("\\.")[0];
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