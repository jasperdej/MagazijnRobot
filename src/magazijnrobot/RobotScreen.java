package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.*;

public class RobotScreen extends JFrame implements ActionListener {
    JPanel opLabelPanel = new JPanel();
    JPanel ipLabelPanel = new JPanel();
    JPanel topPanel = new JPanel();

    private RobotDraw robotDraw;
    private Bin[] bins;
    private OrderPick orderPickRobot;
    private Inpak inpakRobot;
    private JLabel[][] allLabels = new JLabel[4][5];
    private JLabel[] opLabels2;
    private JLabel[] ipLabels2;

    private JLabel opName = new JLabel("OrderPick robot");
    private JLabel opStatus1 = new JLabel("Status: ");
    private JLabel opOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel opTotalAmountOfArticles1 = new JLabel("Totaal producten: ");
    private JLabel opAmountOfArticlesPicked1= new JLabel("Opgehaalde producten: ");
    private JLabel opCoordinate1 = new JLabel("Coördinaten: ");
    private JLabel[] opLabels1 = new JLabel[]{opName, opStatus1, opOrderNr1, opTotalAmountOfArticles1, opAmountOfArticlesPicked1, opCoordinate1};

    private JButton opOnOffButton = new JButton("ON/OFF");
    private JButton opResetButton = new JButton("Reset");

//    private JLabel opStatus2;
//    private JLabel opOrderNr2;
//    private JLabel opTotalAmountOfArticles2;
//    private JLabel opAmountOfArticlesPicked2;
//    private JLabel opCoordinate2;


    private JLabel ipName = new JLabel("Inpak robot");
    private JLabel ipStatus1 = new JLabel("Status: ");
    private JLabel ipOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel ipTotalArticlesInOrder1 = new JLabel("Totaal producten: ");
    private JLabel ipAmountOfArticlesPacked1 = new JLabel("Ingepakte producten:     ");
    private JLabel ipBinId1 = new JLabel("Bin: ");
    private JLabel[] ipLabels1 = new JLabel[]{ipName, ipStatus1, ipOrderNr1, ipTotalArticlesInOrder1, ipAmountOfArticlesPacked1, ipBinId1};

    private JButton ipOnOffButton = new JButton("ON/OFF");
    private JButton ipResetButton = new JButton("Reset");

    //    private JLabel ipStatus2;
    //    private JLabel ipOrderNr2;
    //    private JLabel ipTotalArticlesInOrder2;
    //    private JLabel ipAmountOfArticlesPacked2;
    //    private JLabel ipBinId2;
    //    private JLabel ipBinPercentageFilled; //laten we dit hier printen? en dan x3?


    public RobotScreen (Bin bin1, Bin bin2, Bin bin3) {
        bins = new Bin[]{bin1, bin2, bin3};
        orderPickRobot = new OrderPick(new Order(15));
        inpakRobot = new Inpak(new Order(12), bin1, bin2, bin3);
        robotDraw = new RobotDraw(this, new OrderPick(new Order(15)), inpakRobot);

        allLabels[0] = opLabels1;
        allLabels[2] = ipLabels1;
        updateOp();
        updateIp();

        for (int i = 0; i < allLabels.length; i++) {
            for (int n = 0; n < allLabels[i].length; n++) {
                if (i == 0 && n == 0 || i == 2 && n == 0){
                    allLabels[i][n].setFont(new Font("Ariel", Font.BOLD, 50));
                } else {
                    allLabels[i][n].setFont(new Font("Ariel", Font.PLAIN, 35));
                }
            }
        }

        opLabelPanel.setLayout(new GridBagLayout());
        ipLabelPanel.setLayout(new GridBagLayout());

        GridBagConstraints c_START = new GridBagConstraints();
        c_START.gridx = 0;
        c_START.gridy = 1;
        c_START.anchor = LINE_START;

        GridBagConstraints c_END = new GridBagConstraints();
        c_END.gridx = c_START.gridx + 1;
        c_END.gridy = c_START.gridy;
        c_END.anchor = LINE_END;


        for (int i = 0; i < opLabels1.length; i++) {
            opLabelPanel.add(opLabels1[i], c_START);
            opLabelPanel.add(opLabels2[i], c_END);
            if (i == 0) {
                opLabelPanel.add(opOnOffButton, c_END);
                c_END.gridx++;
                opLabelPanel.add(opResetButton, c_END);
                c_END.gridx--;
            }
            c_START.gridy++;
            c_END.gridy = c_START.gridy;
        }

        c_START.gridx = 1;

        for (int i = 0; i < ipLabels1.length; i++) {
            ipLabelPanel.add(ipLabels1[i], c_START);
            ipLabelPanel.add(ipLabels2[i], c_END);
            if (i == 0) {
                ipLabelPanel.add(ipOnOffButton, c_END);
                c_END.gridx++;
                ipLabelPanel.add(ipResetButton, c_END);
                c_END.gridx--;
            }
            c_START.gridy++;
            c_END.gridy = c_START.gridy;
        }


        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
//        setUndecorated(true);//makes the screen truly fullscreen. close button does not appear.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //delete this line when done with project.

        setLayout(new GridLayout(2 , 1));

        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(opLabelPanel);
        topPanel.add(ipLabelPanel);

        add(topPanel);
        add(robotDraw);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void updateOp() {
        JLabel opFiller = new JLabel("");
        JLabel opStatus2 = new JLabel(orderPickRobot.status);
        JLabel opOrderNr2 = new JLabel(Integer.toString(orderPickRobot.getOrder().getOrderNr()));
        JLabel opTotalAmountOfArticles2 = new JLabel(Integer.toString(orderPickRobot.getOrder().getAmountOfArticles()));
        JLabel opAmountOfArticlesPicked2 = new JLabel(Integer.toString(orderPickRobot.getAmountOfArticlesPicked()));
        JLabel opCoordinate2 = new JLabel(orderPickRobot.getCurrentLocation().toString());
        opLabels2 = new JLabel[]{opFiller, opStatus2, opOrderNr2, opTotalAmountOfArticles2, opAmountOfArticlesPicked2, opCoordinate2};
        allLabels[1] = opLabels2;
    }

    public void updateIp(){
        JLabel ipFiller = new JLabel("");
        JLabel ipStatus2 = new JLabel(inpakRobot.status);
        JLabel ipOrderNr2 = new JLabel(Integer.toString(inpakRobot.order.getOrderNr()));
        JLabel ipTotalArticlesInOrder2 = new JLabel(Integer.toString(inpakRobot.order.getAmountOfArticles()));
        JLabel ipAmountOfArticlesPacked2 = new JLabel(Integer.toString(inpakRobot.getAmountOfArticlesPacked()));
        JLabel ipBinId2 = new JLabel(Integer.toString(inpakRobot.getCurrentBin().getBinNumber()));
        ipLabels2 = new JLabel[]{ipFiller, ipStatus2, ipOrderNr2, ipTotalArticlesInOrder2,ipAmountOfArticlesPacked2, ipBinId2};
        allLabels[3] = ipLabels2;
    }

    public RobotDraw getRobotDraw() {
        return this.robotDraw;
    }
}
