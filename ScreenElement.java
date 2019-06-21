// =========================\\
// Class ScreenElement.java \\
// Diederik van Linden      \\
// TI1A                     \\
// 08/03/2019               \\
//==========================\\


import java.awt.*;


public abstract class ScreenElement extends ATMElement{

    String name;
    Point pos;

    ScreenElement(String name, Point pos){
        super(name);
        this.name = name;
        this.pos = pos;

    }
    abstract void setContainer(Container container);

}
