/**
 * Created by Sergio Sahid Martínez García on 6/8/17.
 *
 * This program is a standalone utility; that is, it does not return any numbers. Instead, it prints directly to console
 * step by step to help users visualize and understand this variation of Booth's Method. It is geared towards students
 * of mathematics, computer architecture, computer science, etc.
 * Algorithm:
 * Multiplies two unsigned binary numbers using one variation of Booth's Method (add and shift).
 * Addition is between a partial product P and two predetermined values A and S.
 * It is based on the following algorithm: https://en.wikipedia.org/wiki/Booth%27s_multiplication_algorithm
 *
 *  1.1-Let x and y represent the number of bits in multiplicand and multiplier respectively.
 *  1.2-Determine values of A, S and P. The number of bits for each value is equal to (x+y)*2+1 (double than Wikipedia's)
 *  1.3-For A: Place multiplicand in the middle of A, fill remaining leftmost and rightmost bits with zeros.
 *  1.4-For S: Obtain multiplicand's 2s complement. Place this value in the middle of S.
 *          Fill leftmost and rightmost bits with zeros.
 *  1.5-For P: Place multiplier into P starting at bit 1 (second right most). Fill the leftmost bits and rightmost bit
 *      with zeros; rightmost bit is needed to validate conditions in the following step.
 *
 *  2-Check the two rightmost bits of P and compare to the following conditions:
 *      -If 00, do nothing. Use the current value of P for the next step.
 *      -If 10, add P+S.
 *      -If 01, add P+A.
 *      -If 11, do nothing. Use the current value of P for the next step.
 *  3-Shift rightward the value obtained in step 2 by one bit. This is the new value of P. The most significant bit(MSB)
 *    that comes in from the left when shifting must be the same as its neighbor (the previous MSB).
 *  4-Repeat steps 2 and 3 a total of x+y times.
 *  5-The product is the last value of P without the least significant bit. (In this program, the result is the right
 *    half of the last P value without its least significant bit. (Run the program to better visualize this).
 */
import java.util.Scanner;

public class BoothMethodMultiplication {
    private int bitsProduct;                // number of bits needed to store the result/product.
    private int multiplicand;               // multiplicand
    private int multiplier;                 // multiplier
    private int totalBits;                  // number of bits per value.
    private int halfBit;                    // middle bit of the new values. Position of multiplicand and its 2s comple-
                                            // ment inside A and S respectively.


    /**
     * Constructor. Initializes variables.
     * @param multiplicand Binary number represented by a String.
     * @param multiplier   Binaty number represented by a String.
     * TODO: validate input.
     * TODO: remove leading zeros in user's input.
     */
    BoothMethodMultiplication(String multiplicand, String multiplier){

        int nBitsMultiplicand = multiplicand.length();     // number of bits in user's multiplicand.
        int nBitsMultiplier = multiplier.length();         // number of bits in user's multiplier.
        bitsProduct = nBitsMultiplier + nBitsMultiplicand; // bits in product (x+y).
        this.multiplicand = Integer.parseInt(multiplicand, 2); // extract binary from String.
        this.multiplier   = Integer.parseInt(multiplier,   2); // extract binary from String.
        totalBits = bitsProduct * 2 + 1;                   // total bits of each value. Double the bits in product +  1
        halfBit = bitsProduct + 1;                         // half of total bits + the rightmost bit for comparisons.
    }

    /**
     * Pre-recursive method. Builds values A, S, and P, prints the beginning of table to console, and calls recursive
     * method.
     */
    void multiply(){
        int A = multiplicand << (halfBit);                  // shift/place multiplicand to A's half bit.
        int S = ~multiplicand + 1;                          // obtain multiplicand's 2s complement. (flip and add 1)
        S = S << (halfBit);                                 // then shift it to S's half bit.
        int P = multiplier << 1;                            // place multiplier at bit 1 inside P(bit 0 is for comparing)

        System.out.println("    A: " + formatAndPrint(A)); // print A to console. (see method for details)
        System.out.println("    S: " + formatAndPrint(S));
        System.out.println("    P: " + formatAndPrint(P));
        boothRecursivo(bitsProduct, P, A, S);               // begin recursive multiplication.

    }

    /**
     * Recursive method. Compares two rightmost bits of P with 4 conditions and applies proper action onto it (Step 2),
     * then shifts P to the right by one bit (Step 3), prints each operation to console, and finally calls itself again
     * with one less operation and a new P value.
     *
     * @param numberOfOperations The number of operations needed to obtain a product. *See algorithm.
     * @param newPValue   new value of P.
     * @param a Value of A. Does not change.
     * @param s Value of S. Does not change
     */
    private void boothRecursivo(int numberOfOperations, int newPValue, int a, int s){
        if (numberOfOperations <= 0)                // base case or stopping point.
            return;

        // Step 2
        switch (twoRightmostBitsOf(newPValue)){     // get last two bits of P. *See method for details.
            case 0b00:                              // condition 00 (0b identifies what succeeds it as a binary number)
                break;                              // do nothing.
            case 0b01:                              // case 01
                newPValue += a;
                System.out.println("  P+A: " + formatAndPrint(newPValue));
                break;
            case 0b10:                              // case 10
                newPValue += s;
                System.out.println("  P+S: " + formatAndPrint(newPValue));
                break;
            case 0b11:                              // case 11
                break;                              // do nothing.
            default:
                System.out.println("Found an error");
        }

        newPValue = newPValue >> 1;                 // shift to the right by one bit (Step 3)

        /* This next section is needed to overcome an issue (at least for our purposes) with primitive variables of type
         * int. An int has 32 bits, but our binary numbers, most likely, are not that big. When a number is shifted to
         * the right, the new most significant bit (MSB) will be a copy of the bit to its right (previous MSB);
         * this is done automatically by the machine, but our number (most likely) has less than 32 bits; therefore, we
         * must copy that value manually.
         */
        int penultimateMostSignificantBit = (newPValue >> (totalBits -2) & 1); // isolate second to last bit
        int mostSignificantBit    = (newPValue >> (totalBits -1) & 1);         // isolate new most significant bit
        if(penultimateMostSignificantBit != mostSignificantBit)                // if they are not the same,
            newPValue ^= 1 << (totalBits -1);                                  // flip most significant bit using XOR ^


        System.out.println(">>1 P: " + formatAndPrint(newPValue));             // print new value of P
        // Repeat steps 2 and 3 on new P.
        boothRecursivo(numberOfOperations-1, newPValue, a, s);
    }

    /**
     * Takes a value stored in an int variable (32bits) and sets its leftmost bits to zero (bits more significant than
     * the values' most significant bit.) For example, if our values are 12 bits long, this method will zero bits 12th
     * to 32nd. This step is needed to print the value properly. Integer.toBinaryString() takes an int and turns it into
     * a binary String beginning the leading one (which could be more significant than needed) while ignoring leading
     * zeros. Therefore, it is imperative to set the value's unnecessary bits to zero.
     * @param bits value to be printed.
     * @return int with unnecessary bits set to zero.
     */
    private int cleanBits(int bits) {
        return bits & ((1 << (totalBits))-1); // Shift 1 to the left of value's MSB, subtract 1 to turn it into all 1s,
                                              // then bitwise AND.
    }

    /**
     * Set all bits to zero but the 2 LSBs.
     * For more info: https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/Bitwise_Operators
     * @param p current P value
     * @return int with two LSBs left untouched.
     */
    private int twoRightmostBitsOf(int p) {
        return p & 0b11;
    }

    /**
     * Turn value into a String, format in the form of a table (with spaces in key spots) and return for printing.
     * @param value value to be printed
     * @return formatted String
     */
    private String formatAndPrint(int value) {
        String s = String.format("%"+(totalBits)+"s", Integer.toBinaryString(cleanBits(value))).replace(" ", "0");
        return String.format("%s %s %s", s.substring(0, bitsProduct), s.substring(bitsProduct, totalBits -1),
                s.charAt((totalBits -1)));
    }

    /**
     * Main method.
     */
    public static void main(String[] args){

        String multiplicand;
        String multiplier;
        Scanner input = new Scanner(System.in);
        System.out.println("This program multiplies 2 unsigned binary numbers using one variation of Booth's Method.");
        System.out.println("Enter multiplicand: ");
        multiplicand = input.nextLine();
        System.out.println("Enter multiplier: ");
        multiplier = input.nextLine();

        BoothMethodMultiplication boothMethodMultiplication = new BoothMethodMultiplication(multiplicand, multiplier);
        boothMethodMultiplication.multiply();

    }


}
