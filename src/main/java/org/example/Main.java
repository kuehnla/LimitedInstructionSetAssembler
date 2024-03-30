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
      String line = br.readLine();
      if (line.equals(".data"))
        dataAddrs = data(br, path);

      if (line.equals(".text")) {
        text(br, path, dataAddrs);
      }
    }
  }

  private static void text(BufferedReader br, String path, Map<String, String> dataAddrs) throws IOException {
    String outName = fileName(path) + ".text";
    path = basePath(path) + outName;
    BufferedWriter bw = new BufferedWriter(new FileWriter(path));
    StringBuilder sb = new StringBuilder();
    Map<String, String> labelAddrs = new HashMap<>();
    String addr = "00400000";

    while (br.ready()) {
      br.mark(1);
      String line = br.readLine();
      String checkLine = line;
      checkLine = checkLine.trim();

      if (checkLine.isEmpty() || line.contains("#"))
        continue;

      if (line.contains(":")) {
        labelAddrs.put(line, addr);
        continue;
      }


      PseudoInstruction psIn = null;
      if (line.contains("li") || line.contains("la") || line.contains("blt")) {
        psIn = initPseudo(line, addr, br);
        if (psIn.type.equals("li")) {
          psIn.toMachine(null);
          bw.write(psIn.instructions[0]);
          bw.newLine();
          bw.flush();
          addr = Integer.toHexString((Integer.parseInt(addr, 16) + 4));
        } else if (psIn.type.equals("la")) {
          String[] label = {dataAddrs.get(psIn.in[2])};
          psIn.toMachine(label);
          for (int i = 0; i < psIn.instructions.length; ++i) {
            if (psIn.instructions[i] == null) continue;
            bw.write(psIn.instructions[i]);
            bw.newLine();
            bw.flush();
            addr = Integer.toHexString((Integer.parseInt(addr, 16) + 4));
          }
        } else if (psIn.type.equals("blt")) {
          psIn.toMachine(null);
          for (int i = 0; i < psIn.instructions.length; ++i) {
            bw.write(psIn.instructions[i]);
            bw.newLine();
            bw.flush();
            addr = Integer.toHexString((Integer.parseInt(addr, 16) + 4));
          }
        }
      } else {
        line = line.replaceAll("\\t", "");

        String[] argz = line.split(" ");
        Instruction in = initOp(line.split(" ")[0]);

        if (argz[0].equals("beq")) {
          argz = beq(argz, br);
        } else if (argz[0].equals("j")) {
          argz = j(addr, argz, br);
        }

        in.toMachine(argz);
        bw.write(in.getWord());
        bw.newLine();
        bw.flush();
        addr = Integer.toHexString((Integer.parseInt(addr, 16) + 4));
      }
    }

    bw.close();
  }

  private static String[] beq(String[] instr, BufferedReader br) throws IOException {
    br.mark(1000);
    int offset = 0;

    while (br.ready()) {
      String line = br.readLine();
      line = line.trim();

      if (line.isEmpty() || line.contains("#")|| (line.contains(":") && !line.contains(instr[3]))) continue;
      if (line.contains(instr[3])) {
        break;
      }
      ++offset;

      if (line.contains("la") || line.contains("blt")) ++offset;
    }

    StringBuilder sb = new StringBuilder(Integer.toHexString(offset)); // KEEP EYE ON THIS LINE
    sb.insert(0, "0x");

    instr[3] = sb.toString();
    br.reset();
    return instr;
  }

  private static String[] j(String addr, String[] instr, BufferedReader br) throws IOException {
    br.mark(1000);

    while (br.ready()) {
      String line = br.readLine();
      line = line.trim();
      if (line.isEmpty() || line.contains("#") || (line.contains(":") && !line.contains(instr[1]))) continue;
      addr = Integer.toHexString((Integer.parseInt(addr, 16) + 4));
      if (line.contains(instr[1])) break;

      if (line.contains("la") || line.contains("blt"))
        addr = Integer.toHexString((Integer.parseInt(addr, 16) + 4));

    }

    addr = Integer.toHexString(Integer.parseInt(addr, 16) / 4);

    StringBuilder sb = new StringBuilder(addr);
    sb.insert(0, "0x");
    instr[1] = sb.toString();
    br.reset();

    return instr;
  }

  private static PseudoInstruction initPseudo(String line, String addr, BufferedReader br) {
    int i = 0;
    for (; i < line.length(); ++i)
      if (line.charAt(i) != 9 || line.charAt(i) != 32) {
        line = line.substring(i + 1);
        break;
      }

    String[] instr = line.split(" ");

    return new PseudoInstruction(instr, addr, br);
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
    };
  }
}