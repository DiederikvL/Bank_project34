// ==========================\\
// Class ReceiptPrinter.java \\
// Diederik van Linden       \\
// TI1A                      \\
// 08/03/2019                \\
//===========================\\


public class ReceiptPrinter implements OutputDevice{
    ReceiptPrinter(String name){

        //super(name);
    }

    @Override                               // alles wat meegegeven is, wordt uitgeprint
    public void giveOutput(String name) {
        System.out.println(name);

    }
}
