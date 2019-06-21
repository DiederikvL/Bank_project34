// ========================\\
// Class ScreenButton.java \\
// Diederik van Linden     \\
// TI1A                    \\
// 08/03/2019              \\
//=========================\\

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ScreenButton extends ScreenElement implements InputDevice, ActionListener {

    Button button;
    boolean inputAvailable = false;

    ScreenButton(String name, Point pos){

        super(name, pos);

        button = new Button(name);
        button.setBounds(pos.x, pos.y, 10 + 15 * name.length(), 25);
        button.addActionListener(this);
    }

    @Override
    void setContainer(Container container) {
        container.add(button);
    }

    @Override
    public String getInput() {

        if(inputAvailable){
            inputAvailable = false;
            return this.name;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        inputAvailable = true;

    }
}

