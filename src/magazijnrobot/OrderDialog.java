package magazijnrobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderDialog extends JDialog implements ActionListener {
    private Order order;

    public OrderDialog(JFrame jFrame, Order order) {
        super(jFrame);
        this.order = order;
    }

    public int dbGetItemQuantity(){
        return 10;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
