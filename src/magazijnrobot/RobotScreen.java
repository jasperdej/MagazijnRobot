package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RobotScreen extends JFrame implements ActionListener {
    private OrderPick orderPickRobot;
    private JLabel opCoordinaat;
    private JLabel opOrderNr = new JLabel("20");
    private JLabel opStatus = new JLabel("status op");
    private JLabel opTotalAmountOfArticles;
    private JLabel opAmountOfArticlesPicked;
    private JButton opOnOffButton;
    private JButton opResetButton;

    private Inpak inpakRobot;
    private JLabel ipStatus = new JLabel("status ip");
    private JLabel ipOrderNr = new JLabel("30");
    private JLabel ipTotalArticlesInOrder;
    private JLabel ipAmountOfArticlesPacked;
    private JLabel ipBinPercentageFilled; //laten we dit hier printen? en dan x3?
    private JLabel ipBinId;
    private JButton ipOnOffButton;
    private JButton ipResetButton;


    public RobotScreen () {
        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLayout(new GridLayout(2, 16));

        add(new JLabel("Status: "));
        add(opStatus);

        add(new JLabel("Status"));
        add(ipStatus);

        add(new JLabel("Ordernummer"));
        add(opOrderNr);

        add(new JLabel("Ordernummer"));
        add(ipOrderNr);



        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void updateOp() {

    }

    public void updateIp(){

    }
}
