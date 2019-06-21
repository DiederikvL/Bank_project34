// =====================\\
// Class ATMscreen.java \\
// Diederik van Linden  \\
// TI1A                 \\
// 08/03/2019           \\
//======================\\

/*Deze class is een subclass van de class Container.
 * Alle schermelementen van de ATM worden vanuit hier opgeroepen.
 * De layout wordt bepaald.
 * */

import java.awt.*;


public class ATMScreen extends Container {


    ATMScreen(){
        super();            // Super wordt aangevraagd.
        setLayout(null);
    }

    void add(ScreenElement name){
        name.setContainer(this);
    }

    void clear(){

        removeAll();
    }


    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRoundRect(747, 530, 35, 35, 10, 10);
        g.fillRect(777, 560, 5, 5);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("LK", 750, 550);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString("bank", 751, 560);
    }
}


