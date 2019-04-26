package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.LINE_END;
import static java.awt.GridBagConstraints.LINE_START;

public class RobotScreen extends JFrame implements ActionListener {
    JPanel opLabelPanel = new JPanel();
    JPanel ipLabelPanel = new JPanel();

    private Bin[] bins;
    private OrderPick orderPickRobot;
    private Inpak inpakRobot;
    private JLabel[][] allLabels = new JLabel[4][5];

    private JLabel opStatus1 = new JLabel("Status: ");
    private JLabel opOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel opTotalAmountOfArticles1 = new JLabel("Totaal producten: ");
    private JLabel opAmountOfArticlesPicked1= new JLabel("Opgehaalde producten: ");
    private JLabel opCoordinate1 = new JLabel("Coördinaten: ");
    private JLabel[] opLabels1 = new JLabel[]{opStatus1, opOrderNr1, opTotalAmountOfArticles1, opAmountOfArticlesPicked1, opCoordinate1};


    private JLabel opStatus2;
    private JLabel opOrderNr2;
    private JLabel opTotalAmountOfArticles2;
    private JLabel opAmountOfArticlesPicked2;
    private JLabel opCoordinate2;
    private JLabel[] opLabels2;

    private JButton opOnOffButton;
    private JButton opResetButton;

    private JLabel ipStatus1 = new JLabel("Status: ");
    private JLabel ipOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel ipTotalArticlesInOrder1 = new JLabel("Totaal producten: ");
    private JLabel ipAmountOfArticlesPacked1 = new JLabel("Ingepakte producten: ");
    private JLabel ipBinId1 = new JLabel("Bin: ");
    private JLabel[] ipLabels1 = new JLabel[]{ipStatus1, ipOrderNr1, ipTotalArticlesInOrder1, ipAmountOfArticlesPacked1, ipBinId1};


    private JLabel ipStatus2;
    private JLabel ipOrderNr2;
    private JLabel ipTotalArticlesInOrder2;
    private JLabel ipAmountOfArticlesPacked2;
    private JLabel ipBinId2;
    private JLabel ipBinPercentageFilled; //laten we dit hier printen? en dan x3?
    private JButton ipOnOffButton;
    private JButton ipResetButton;
    private JLabel[] ipLabels2;


    public RobotScreen (Bin bin1, Bin bin2, Bin bin3) {
        bins = new Bin[]{bin1, bin2, bin3};

        orderPickRobot = new OrderPick(new Order(15));
        inpakRobot = new Inpak(new Order(12), bin1, bin2, bin3);

        allLabels[0] = opLabels1;
        allLabels[2] = ipLabels1;
        updateOp();
        updateIp();

        for (int i = 0; i < allLabels.length; i++) {
            System.out.println(i);
            for (int n = 0; n < allLabels[i].length; n++) {
                System.out.println(n);
                allLabels[i][n].setFont(new Font("Ariel", Font.PLAIN, 35));
            }
        }

        opLabelPanel.setLayout(new GridBagLayout());
        ipLabelPanel.setLayout(new GridBagLayout());

        GridBagConstraints c_START = new GridBagConstraints();
        c_START.gridx = 0;
        c_START.gridy = 0;
        c_START.anchor = LINE_START;

        GridBagConstraints c_END = new GridBagConstraints();
        c_END.gridx = c_START.gridx + 1;
        c_END.gridy = c_START.gridy;
        c_END.anchor = LINE_END;

        for (int i = 0; i < opLabels1.length; i++) {
            opLabelPanel.add(opLabels1[i], c_START);
            opLabelPanel.add(opLabels2[i], c_END);
            c_START.gridy++;
            c_END.gridy = c_START.gridy;
        }

        c_START.gridx = 0;

        for (int i = 0; i < ipLabels1.length; i++) {
            ipLabelPanel.add(ipLabels1[i], c_START);
            ipLabelPanel.add(ipLabels2[i], c_END);
            c_START.gridy++;
            c_END.gridy = c_START.gridy;
        }


        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridLayout(1 , 2));

        add(opLabelPanel);
        add(ipLabelPanel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void updateOp() {
        JLabel opStatus2 = new JLabel(orderPickRobot.status);
        JLabel opOrderNr2 = new JLabel(Integer.toString(orderPickRobot.getOrder().getOrderNr()));
        JLabel opTotalAmountOfArticles2 = new JLabel(Integer.toString(orderPickRobot.getOrder().getAmountOfArticles()));
        JLabel opAmountOfArticlesPicked2 = new JLabel(Integer.toString(orderPickRobot.getAmountOfArticlesPicked()));
        JLabel opCoordinate2 = new JLabel(orderPickRobot.getCurrentLocation().toString());
        opLabels2 = new JLabel[]{opStatus2, opOrderNr2, opTotalAmountOfArticles2, opAmountOfArticlesPicked2, opCoordinate2};
        allLabels[1] = opLabels2;
        for (JLabel jl: opLabels2) {
            System.out.println(jl.getText());
        }
    }

    public void updateIp(){
        JLabel ipStatus2 = new JLabel(inpakRobot.status);
        JLabel ipOrderNr2 = new JLabel(Integer.toString(inpakRobot.order.getOrderNr()));
        JLabel ipTotalArticlesInOrder2 = new JLabel(Integer.toString(inpakRobot.order.getAmountOfArticles()));
        JLabel ipAmountOfArticlesPacked2 = new JLabel(Integer.toString(inpakRobot.getAmountOfArticlesPacked()));
        JLabel ipBinId2 = new JLabel(Integer.toString(inpakRobot.getCurrentBin().getBinNumber()));
        ipLabels2 = new JLabel[]{ipStatus2, ipOrderNr2, ipTotalArticlesInOrder2,ipAmountOfArticlesPacked2, ipBinId2};
        allLabels[3] = ipLabels2;
    }
}
