

//============================\\
// Class HardwareElement.java \\
// Diederik van Linden        \\
// TI1A                       \\
// 08/03/2019                 \\
//============================\\

public abstract class HardwareElement extends ATMElement {

    private boolean power = false;


    HardwareElement(String name){
        super(name);
    }

    void powerOn(){

        power = true;
    }

    void powerOff(){

        power = false;
    }


}