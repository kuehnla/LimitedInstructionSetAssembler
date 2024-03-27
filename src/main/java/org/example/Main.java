package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
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
    Map<String, String> dataAddrs = null;
    while (br.ready()) {
      if (br.readLine().equals(".data"))
        dataAddrs = data(br, path);
    }
    System.out.println(dataAddrs.entrySet());

  }

  private static Map<String, String> data(BufferedReader br, String path) throws IOException {
    String outName = fileName(path) + ".data";
    path = basePath(path) + outName;
    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
    StringBuilder sb = new StringBuilder();
    Map<String, String> dataAddrs = new HashMap<>();
    int j = 0;
    String addr = "10010000";

    while (br.ready()) {
      br.mark(1);
      String line = br.readLine();

      if (line.isEmpty()) {
        continue;
      } else if (!line.contains(".asciiz") && !line.contains(".word")) {
        br.reset();
        break;
      }

      dataAddrs.put(line.substring(0, line.indexOf(':')).split("\\t")[1], addr);

      if (line.contains(".word")) {
        if (!sb.isEmpty()) {
          while (sb.length() < 8)
            sb.insert(0, 0);

          bw.write(sb.toString());
          bw.newLine();
          bw.flush();
          sb.delete(0, sb.length());
        }
        addr = wordType(addr, line, bw);
        continue;
      }

      Pattern p = Pattern.compile("\"([^\"]*)\"");
      Matcher m = p.matcher(line);
      while (m.find()) {
        line = m.group(1);
      }

      addr = Integer.toHexString(Integer.parseInt(addr, 16) + line.length() + 1);

      for (int i = 0; i < line.length(); ++i, ++j) {
        if (j > 3) {
          j = 0;
          bw.write(sb.toString());
          bw.newLine();
          bw.flush();
          sb.delete(0, sb.length());
        }

        sb.insert(0, Integer.toHexString(line.charAt(i)));
      }

      if (j > 3) {
        j = 1;
        bw.write(sb.toString());
        bw.newLine();
        bw.flush();
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

    return dataAddrs;
  }

  private static String wordType(String addr, String line, BufferedWriter bw) throws IOException {
    if (line.contains("0x"))
      bw.write(line.split("0x")[1]);
    else
      bw.write(Conversions.decToHex(line.replaceAll("[^0-9]", "")));
    bw.flush();

    addr = Integer.toHexString(Integer.parseInt(addr, 16) + 4);

    return addr;
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