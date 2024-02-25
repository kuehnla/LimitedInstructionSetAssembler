package org.example;

public interface Instruction {
  String getWord();
  String getOp();
  String getFunct();
  String getRS();
  String getRT();
  String getRD();
  String getShamt();
  String toMachine();
  void setRS(String[] args);
  void setRT(String[] args);
  void setRD(String[] args);
  void setShamt(String[] args);
  String binToDec(String bin);
  String decToHex(String dec);
}
