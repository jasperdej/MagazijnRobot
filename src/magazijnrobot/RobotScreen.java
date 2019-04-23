package magazijnrobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RobotScreen extends JFrame implements ActionListener {
    private OrderPick orderPickRobot;
    private JLabel opCoordinaat;
    private JLabel opOrderNr;
    private JLabel opStatus;
    private JLabel opTotalAmountOfArticles;
    private JLabel opAmountOfArticlesPicked;
    private JButton opOnOffButton;
    private JButton opResetButton;

    private Inpak inpakRobot;
    private JLabel ipStatus;
    private JLabel ipOrderNr;
    private JLabel ipTotalArticlesInOrder;
    private JLabel ipAmountOfArticlesPacked;
    private JLabel ipBinPercentageFilled; //laten we dit hier printen? en dan x3?
    private JLabel ipBinId;
    private JButton ipOnOffButton;
    private JButton ipResetButton;


    public RobotScreen () {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void updateOp() {

    }

    public void updateIp(){

    }
}
