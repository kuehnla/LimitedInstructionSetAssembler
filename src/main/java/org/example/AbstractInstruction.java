package org.example;

public abstract class AbstractInstruction implements Instruction {
  String word, op, rs, rt, rd, shamt, funct;

  @Override
  public String getWord() {
    return word;
  }

  @Override
  public String getOp() {
    return op;
  }

  @Override
  public String getFunct() {
    return funct;
  }

  @Override
  public String getRS() {
    return rs;
  }

  @Override
  public String getRT() {
    return rt;
  }

  @Override
  public String getRD() {
    return rd;
  }

  @Override
  public String getShamt() {
    return shamt;
  }

  @Override
  public String toMachine() {
    return null;
  }

  @Override
  public void setRS(String[] args) {return;}

  @Override
  public void setRT(String[] args) {return;}

  @Override
  public void setRD(String[] args) {return;}

  @Override
  public void setShamt(String[] args) {return;}

  @Override
  public String binToDec(String bin) {
    int dec = 0;
    for (int i = bin.length() - 1; i >= 0; --i) {
      dec += (int) ((bin.charAt(i)-'0') * Math.pow(2, i));
    }
    return String.valueOf(dec);
  }

  @Override
  public String decToHex(String dec) {

  }
}
