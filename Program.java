// =====================\\
// Class Program.java   \\
// Diederik van Linden  \\
// TI1A                 \\
// 08/03/2019           \\
//======================\\

/* De hoofdclass van het programma.
 * hier worden de nodige objecten aangemaakt */

public class Program{

    public static void main(String[] args) {

        //Bank ING = new Bank();         // maakt bank aan.
        ATM ATM1 = new ATM();       // maakt ATM aan.


        ATM1.doTransaction();

    }
}