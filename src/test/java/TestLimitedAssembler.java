import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLimitedAssembler {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  //SYSCALL TESTS
  @Test
  public void test00() {
    Main.main(new String[]{"syscall"});
    assertEquals("0000000c\n", outContent.toString());
  }

  @Test
  public void test01() {
    Main.main(new String[]{"syscall     "});
    assertEquals("0000000c\n", outContent.toString());
  }

  @Test
  public void test02() {
    Main.main(new String[]{"       syscall"});
    assertEquals("0000000c\n", outContent.toString());
  }

  @Test
  public void test03() {
    Main.main(new String[]{"     syscall     "});
    assertEquals("0000000c\n", outContent.toString());
  }

  @Test
  public void test04() {
    Main.main(new String[]{"  syscall# Comment"});
    assertEquals("0000000c\n", outContent.toString());
  }


  //J-TYPE TESTS

  //j tests
  @Test
  public void test05() {
    Main.main(new String[]{"j 0xc5"});
    assertEquals("080000c5\n", outContent.toString());
  }

  @Test
  public void test06() {
    Main.main(new String[]{"j 0x48"});
    assertEquals("08000048\n", outContent.toString());
  }

  @Test
  public void test07() {
    Main.main(new String[]{"j 0x15"});
    assertEquals("08000015\n", outContent.toString());
  }

  @Test
  public void test08() {
    Main.main(new String[]{"j 0x53# Comment"});
    assertEquals("08000053\n", outContent.toString());
  }

  @Test
  public void test09() {
    Main.main(new String[]{"  j    0x2d      #Comment"});
    assertEquals("0800002d\n", outContent.toString());
  }


  //R-TYPE tests

  //Testing add
  @Test
  public void test10() {
    Main.main(new String[]{"add $t2, $s6, $s4"});
    assertEquals("02d45020\n", outContent.toString());
  }

  @Test
  public void test11() {
    Main.main(new String[]{"add $at, $a2, $s2"});
    assertEquals("00d20820\n", outContent.toString());
  }

  @Test
  public void test12() {
    Main.main(new String[]{"add $t7, $t6, $sp"});
    assertEquals("01dd7820\n", outContent.toString());
  }

  @Test
  public void test13() {
    Main.main(new String[]{"  add   $k0,  $at,  $s4"});
    assertEquals("0034d020\n", outContent.toString());
  }

  @Test
  public void test14() {
    Main.main(new String[]{"add $t3, $t2, $t0#Comment"});
    assertEquals("01485820\n", outContent.toString());
  }

  //tests of and
  @Test
  public void test15() {
    Main.main(new String[]{"add $t3, $t2, $t0#Comment"});
    assertEquals("01485820\n", outContent.toString());
  }
}
