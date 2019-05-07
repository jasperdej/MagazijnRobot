import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.GridBagConstraints.*;

public class RobotScreen extends JFrame implements ActionListener {
    //screenmanager is responsible for showing the right screen.
    private ScreenManager screenManager;

    //panels for screenlayout.
    private JPanel opLabelPanel = new JPanel();
    private JPanel ipLabelPanel = new JPanel();

    //JPanel to which all labels are added. this panel fills the upperscreen en devides it into two columns.
    private JPanel topPanel = new JPanel();

    //devides buttons from labels.
    private JPanel opDevider = new JPanel();

    //contains buttonns for switching between screens.
    private JPanel buttonPanel = new JPanel();

    //local variables for all labels and drawing of warehouse.
    private RobotDraw robotDraw;
    private JLabel[][] allLabels = new JLabel[4][5];
    private JLabel[] opLabels2;
    private JLabel[] ipLabels2;

    //buttons for swithing screens.
    private JButton robotScreen = new JButton("Robot overzicht");
    private JButton orderScreen = new JButton("Order overzicht");
    private JButton inventoryScreen = new JButton("Voorraad overzicht");

    //labels for displaying OrderPick robot information description.
    private JLabel opName = new JLabel("OrderPick robot");
    private JLabel opStatus1 = new JLabel("Status: ");
    private JLabel opOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel opTotalAmountOfArticles1 = new JLabel("Totaal producten: ");
    private JLabel opAmountOfArticlesPicked1= new JLabel("Opgehaalde producten: ");
    private JLabel opCoordinate1 = new JLabel("Coördinaten: ");
    private JLabel[] opLabels1 = new JLabel[]{opName, opStatus1, opOrderNr1, opTotalAmountOfArticles1, opAmountOfArticlesPicked1, opCoordinate1};

    //buttons for OrderPick robot.
    private JButton opOnOffButton = new JButton("ON/OFF");
    private JButton opResetButton = new JButton("Reset");

    //labels for displaying Inpak robot information description.
    private JLabel ipName = new JLabel("Inpak robot");
    private JLabel ipStatus1 = new JLabel("Status: ");
    private JLabel ipOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel ipTotalArticlesInOrder1 = new JLabel("Totaal producten: ");
    private JLabel ipAmountOfArticlesPacked1 = new JLabel("Ingepakte producten:     ");
    private JLabel ipBinId1 = new JLabel("Bin: ");
    private JLabel[] ipLabels1 = new JLabel[]{ipName, ipStatus1, ipOrderNr1, ipTotalArticlesInOrder1, ipAmountOfArticlesPacked1, ipBinId1};

    //buttons for Inpak robot.
    private JButton ipOnOffButton = new JButton("ON/OFF");
    private JButton ipResetButton = new JButton("Reset");

    //constructor initiates robotdraw and screenmanager.
    public RobotScreen (ScreenManager screenManager, RobotDraw robotDraw) {
        this.robotDraw = robotDraw;
        this.screenManager = screenManager;
    }

    public void createScreen() {
        //creates information labels for both robots and adds all JLabels to allLabels. allLabels[0] and allLabels[2] are information description. 1 and 3 are actual information.
        allLabels[0] = opLabels1;
        allLabels[2] = ipLabels1;

        //sets all JLabels to Ariel and size 50/35. 50 for titles, 35 for information description and information.
        for (int i = 0; i < allLabels.length; i++) {
            for (int n = 0; n < allLabels[i].length; n++) {
                if (i == 0 && n == 0 || i == 2 && n == 0){
                    allLabels[i][n].setFont(new Font("Ariel", Font.BOLD, 50));
                } else {
                    allLabels[i][n].setFont(new Font("Ariel", Font.PLAIN, 35));
                }
            }
        }

        //gridbaglayout for displaying labels Correctly.
        opLabelPanel.setLayout(new GridBagLayout());
        ipLabelPanel.setLayout(new GridBagLayout());
        buttonPanel.setLayout(new GridBagLayout());



        //gridbagconstraints for Displaying labels correctly and within their padding.
        GridBagConstraints c_START = new GridBagConstraints();
        c_START.gridx = 0;
        c_START.gridy = 1;
        c_START.anchor = LINE_START;
        c_START.insets = new Insets(0, 0, 0, 100);

        GridBagConstraints c_END = new GridBagConstraints();
        c_END.gridx = c_START.gridx + 1;
        c_END.gridy = c_START.gridy;
        c_END.anchor = LINE_END;

        GridBagConstraints c_Buttons = new GridBagConstraints();
        c_Buttons.gridx = 0;
        c_Buttons.gridy = 0;
        c_Buttons.anchor = NORTHWEST;
        c_Buttons.insets = new Insets(0, 0, 40, 0);

        buttonPanel.add(robotScreen, c_Buttons);
        c_Buttons.gridx++;
        buttonPanel.add(orderScreen, c_Buttons);
        c_Buttons.gridx++;
        buttonPanel.add(inventoryScreen, c_Buttons);

        //filler. ain't beautiful, but it works...
//        buttonPanel.add(new JLabel("                                                          "), c_Buttons);


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

        //makes the screen truly fullscreen. close button does not appear.
        setUndecorated(true);

        //gridlayout for splitting the screen horizontally.
        setLayout(new GridLayout(2 , 1));

        //gridlayout for splitting the screen vertically.
        topPanel.setLayout(new GridLayout(1, 2));

        //adds both information JPanels to topPanel.
        topPanel.add(opDevider);
        opDevider.add(buttonPanel, BorderLayout.PAGE_START);
        opDevider.add(opLabelPanel, BorderLayout.CENTER);
        topPanel.add(ipLabelPanel);

        //add topPanel to screen. fills the upper part of the screen.
        add(topPanel);

        //add robotDraw to screen. fills the bottom part of the screen.
        add(robotDraw);

        //adds actionlistener for all buttons.
        opOnOffButton.addActionListener(this);
        opResetButton.addActionListener(this);
        ipOnOffButton.addActionListener(this);
        ipResetButton.addActionListener(this);

        robotScreen.addActionListener(this);
        orderScreen.addActionListener(this);
        inventoryScreen.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == robotScreen) {
            screenManager.buttonPressed("RobotScreen");
        } else if (e.getSource() == orderScreen) {
            screenManager.buttonPressed("OrderScreen");
        } else if (e.getSource() == inventoryScreen) {
            screenManager.buttonPressed("InventoryScreen");
        } else if (e.getSource() == opOnOffButton) {

        } else if (e.getSource() == opResetButton) {

        } else if (e.getSource() == ipOnOffButton) {

        } else if (e.getSource() == ipResetButton) {

        }
    }

    //updates information on OrderPick panel. information comes from Arduino/Java.
    public void updateOp(OrderPick orderPickRobot) {
        JLabel opFiller = new JLabel("");
        JLabel opStatus2 = new JLabel(orderPickRobot.status);
        JLabel opOrderNr2 = new JLabel(Integer.toString(orderPickRobot.getOrder().getOrderNr()));
        JLabel opTotalAmountOfArticles2 = new JLabel(Integer.toString(orderPickRobot.getOrder().getAmountOfArticles()));
        JLabel opAmountOfArticlesPicked2 = new JLabel(Integer.toString(orderPickRobot.getAmountOfArticlesPicked()));
        JLabel opCoordinate2 = new JLabel(orderPickRobot.getCurrentLocation().toString());

        //adds labels to opLabels which makes it easier to display.
        opLabels2 = new JLabel[]{opFiller, opStatus2, opOrderNr2, opTotalAmountOfArticles2, opAmountOfArticlesPicked2, opCoordinate2};
        //adds opLabels to allLabels.
        allLabels[1] = opLabels2;
    }

    //updates information on inpak panel. information comes from Arduino/Java.
    public void updateIp(Inpak inpakRobot){
        JLabel ipFiller = new JLabel("");
        JLabel ipStatus2 = new JLabel(inpakRobot.status);
        JLabel ipOrderNr2 = new JLabel(Integer.toString(inpakRobot.order.getOrderNr()));
        JLabel ipTotalArticlesInOrder2 = new JLabel(Integer.toString(inpakRobot.order.getAmountOfArticles()));
        JLabel ipAmountOfArticlesPacked2 = new JLabel(Integer.toString(inpakRobot.getAmountOfArticlesPacked()));
        JLabel ipBinId2 = new JLabel(Integer.toString(inpakRobot.getCurrentBin().getBinNumber()));

        //adds labels to opLabels which makes it easier to display.
        ipLabels2 = new JLabel[]{ipFiller, ipStatus2, ipOrderNr2, ipTotalArticlesInOrder2,ipAmountOfArticlesPacked2, ipBinId2};
        //adds opLabels to allLabels.
        allLabels[3] = ipLabels2;
    }
}
