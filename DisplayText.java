// ========================\\
// Class DisplayText.java  \\
// Diederik van Linden     \\
// TI1A                    \\
// 08/03/2019              \\
//=========================\\

import java.awt.*;




public class DisplayText extends ScreenElement implements OutputDevice{

    private Label label;

    DisplayText(String name, Point pos){

        super(name, pos);
        label = new Label();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 30));
        label.setBounds(pos.x, pos.y, 400, 35);

    }

    @Override
    void setContainer(Container container) {
        container.add(label);
    }

    @Override
    public void giveOutput(String name) {
        label.setText(name);
    }
}
