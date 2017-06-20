# Discrete Math 
Aid utilities for studying discrete mathematics.

These are stand alone utilities, meaning they all have main methods. Simply copy the code and run it in your IDE or directly
from the console. 

To run these programs from console do the following: 
  - Open a terminal.
  - Change directory to where your file is located. 
  - javac filename.java
  - java filename.
 
 # Booth's Multiplication
 
 Prints all operations directly to console in the form of a table to help students visualize binary numbers and facilitate 
 the understanding of the concept. The algorithm for Booth's Method is located at the top of the file within a comment. 
 
 Example: 
 Enter multiplicand: 
 101010
 Enter multiplier: 
 1011
     A: 0000101010 0000000000 0
     S: 1111010110 0000000000 0
     P: 0000000000 0000001011 0
   P+S: 1111010110 0000001011 0
 >>1 P: 1111101011 0000000101 1
 >>1 P: 1111110101 1000000010 1
   P+A: 0000011111 1000000010 1
 >>1 P: 0000001111 1100000001 0
   P+S: 1111100101 1100000001 0
 >>1 P: 1111110010 1110000000 1
   P+A: 0000011100 1110000000 1
 >>1 P: 0000001110 0111000000 0
 >>1 P: 0000000111 0011100000 0
 >>1 P: 0000000011 1001110000 0
 >>1 P: 0000000001 1100111000 0
 >>1 P: 0000000000 1110011100 0
 >>1 P: 0000000000 0111001110 0

 
 If you have any questions or requests, feel free to ask.
