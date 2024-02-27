package org.example;
/*
 * An interface to allow for easily transferable common methods between
 * different instruction types.
 */
public interface Instruction {
  String getWord();
  void setRS(String[] argz);
  void setRT(String[] argz);
  void setRD(String[] argz);
  void setShamt(String[] argz);
  String binToDec(String bin);
  String decToHex(String dec);
  String decToBin(String dec, int bits);
  String hexHelper(Object hexDig);
  void toMachine(String[] argz);
  String hexToBin(String hex, int bits);

  String registers(String reg);
  String argFinder(String[] argz, int index);
}
