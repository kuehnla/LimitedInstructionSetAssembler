Continuation of this project --> https://github.com/Dennis-Smith414/MIPS-RISA-Simulator

A simple Java program that converts a limited set of MIPS assembly instructions into hexadecimal machine code with the goal of furthering understanding of the relationship between 
symbolic representations of code and machine code.

Program can be used to generate .text and .data memory dumps from a .asm file.

Included instructions:

    add
    addiu
    and
    andi
    beq
    bne
    j
    lui
    lw
    or
    ori
    slt
    sub
    sw
    syscall

    Pseudoinstructions:
    blt
    la
    li

Usage: 

$> java -jar LimitedAssembler.jar "add $t2, $s6, $s4"
02d45020

$> java -jar LimitedAssembler.jar "syscall"
0000000c

$> java -jar LimitedAssembler.jar "J 0x00000c5"
0x080000c5

*requires appropriate escape keys preceding $

$> java -jar "path/AssemblyFile.asm"
outputs .data and .text files (hexadecimal format)
