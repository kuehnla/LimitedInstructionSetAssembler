package org.example;

public interface Instruction {
  String getWord();
  String getOp();
  String getFunct();
  String getRS();
  String getRT();
  String getRD();
  String getShamt();

  void setRS(String[] argz);
  void setRT(String[] argz);
  void setRD(String[] argz);
  void setShamt(String[] argz);
  String binToDec(String bin);
  String decToHex(String dec);
  String hexHelper(Object hexDig);
  void toMachine(String[] argz);
  String hexToBin(String hex);

  String registers(String reg);
}
