import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InvalidPropertiesFormatException;

import static java.awt.GridBagConstraints.*;

public class RobotScreen extends JFrame implements ActionListener {
    //screenmanager is responsible for showing the right screen.
    private ScreenManager screenManager;
    private Main main;

    private boolean isOpOn = false;
    private boolean isIpOn = false;

    //panels for screenlayout.
    private JPanel labelPanel = new JPanel();

    //JPanel to which all labels are added. this panel fills the upperscreen en devides it into two columns.
    private JPanel topPanel = new JPanel();

    //contains buttonns for switching between screens.
    private JPanel buttonPanel = new JPanel();

    //local variables for all labels and drawing of warehouse.
    private RobotDraw robotDraw;
    private JLabel[][] allLabels = new JLabel[4][5];
    private JLabel[] opLabels2;
    private JLabel[] ipLabels2;

    //buttons for swithing screens.
    private JButton robotScreen = new JButton("Robot overzicht"){
        protected void paintComponent(Graphics g){
            setContentAreaFilled(false);
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0),
                    new Color(141, 177, 216),
                    new Point(0, getHeight()/3),
                    new Color(230, 230, 230)));
            g2.fillRect(0, 0, getWidth(), getHeight()/3);
            g2.setPaint(new GradientPaint(
                    new Point(0, getHeight()/3),
                    new Color(230, 230, 230),
                    new Point(0, getHeight()),
                    new Color(141, 177, 216)));
            g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }
    };
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

    //labels for variables orderpick robot.
    private JLabel opFiller, opStatus2, opOrderNr2, opTotalAmountOfArticles2,opAmountOfArticlesPicked2,opCoordinate2;

    //buttons for OrderPick robot.
    private JButton opOnOffButton = new JButton("OFF");
    private JButton opResetButton = new JButton("Reset");

    //labels for displaying Inpak robot information description.
    private JLabel ipName = new JLabel("Inpak robot");
    private JLabel ipStatus1 = new JLabel("Status: ");
    private JLabel ipOrderNr1 = new JLabel("Ordernummer: ");
    private JLabel ipTotalArticlesInOrder1 = new JLabel("Totaal producten: ");
    private JLabel ipAmountOfArticlesPacked1 = new JLabel("Ingepakte producten:     ");
    private JLabel ipBinId1 = new JLabel("Bin: ");
    private JLabel[] ipLabels1 = new JLabel[]{ipName, ipStatus1, ipOrderNr1, ipTotalArticlesInOrder1, ipAmountOfArticlesPacked1, ipBinId1};

    //labels for variabels Inpak robot.
    private JLabel ipFiller, ipStatus2, ipOrderNr2, ipTotalArticlesInOrder2, ipAmountOfArticlesPacked2, ipBinId2;

    //buttons for Inpak robot.
    private JButton ipOnOffButton = new JButton("OFF");
    private JButton ipResetButton = new JButton("Reset");

    //constructor initiates robotdraw and screenmanager.
    public RobotScreen (ScreenManager screenManager, RobotDraw robotDraw, Main main) {
        this.robotDraw = robotDraw;
        this.screenManager = screenManager;
        this.main = main;
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
        labelPanel.setLayout(new GridLayout(6,2,25,0));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0,15,0,15));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //adds buttons to buttonpanel.
        buttonPanel.add(robotScreen);
        buttonPanel.add(orderScreen);
        buttonPanel.add(inventoryScreen);

        //adds labels/buttons for OP and IP status
        for (int i = 0; i < opLabels1.length; i++) {
            JPanel opLabelPanel = new JPanel();
            opLabelPanel.setLayout(new BorderLayout());
            JPanel ipLabelPanel = new JPanel();
            ipLabelPanel.setLayout(new BorderLayout());

            if(i == 0){
                JPanel opButtonPanel = new JPanel();
                opButtonPanel.setLayout(new GridLayout(1,2));
                opButtonPanel.setBorder(BorderFactory.createEmptyBorder(15,0,5,0));
                JPanel ipButtonPanel = new JPanel();
                ipButtonPanel.setLayout(new GridLayout(1,2));
                ipButtonPanel.setBorder(BorderFactory.createEmptyBorder(15,0,5,0));

                opButtonPanel.add(opOnOffButton);
                opButtonPanel.add(opResetButton);
                ipButtonPanel.add(ipOnOffButton);
                ipButtonPanel.add(ipResetButton);

                opLabelPanel.add(opLabels1[i],BorderLayout.LINE_START);
                opLabelPanel.add(opButtonPanel, BorderLayout.LINE_END);
                ipLabelPanel.add(ipLabels1[i], BorderLayout.LINE_START);
                ipLabelPanel.add(ipButtonPanel,BorderLayout.LINE_END);
            } else {
                opLabelPanel.add(opLabels1[i],BorderLayout.LINE_START);
                opLabelPanel.add(opLabels2[i],BorderLayout.LINE_END);
                ipLabelPanel.add(ipLabels1[i],BorderLayout.LINE_START);
                ipLabelPanel.add(ipLabels2[i],BorderLayout.LINE_END);
            }
            labelPanel.add(opLabelPanel);
            labelPanel.add(ipLabelPanel);
        }

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the screen truly fullscreen. close button does not appear.
        setUndecorated(true);

        //gridlayout for splitting the screen horizontally.
        setLayout(new GridLayout(2 , 1));

        //gridlayout for splitting the screen vertically.
        topPanel.setLayout(new BorderLayout());

        //adds both information JPanels to topPanel.
        topPanel.add(buttonPanel, BorderLayout.PAGE_START);
//        topPanel.add(opLabelPanel, BorderLayout.LINE_START);
//        topPanel.add(ipLabelPanel, BorderLayout.LINE_END);
        topPanel.add(labelPanel,BorderLayout.CENTER);

        //add topPanel to screen. fills the upper part of the screen.
        add(topPanel);

        //add robotDraw to screen. fills the bottom part of the screen.
        add(robotDraw);
//        add(conveyorBeltAnimation);


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
            if (isOpOn) {
                opOnOffButton.setText("ON");
                ipOnOffButton.setText("ON");
                main.setPaused(true);
            } else {
                opOnOffButton.setText("OFF");
                ipOnOffButton.setText("OFF");
                main.setPaused(false);
                main.setReset(false);
            }
            isOpOn = !isOpOn;
        } else if (e.getSource() == opResetButton) {
            main.getOrderPick().sendToArduino("reset");
            main.getInpak().sendToArduino("reset");
            main.setReset(true);
            opOnOffButton.setText("ON");
            ipOnOffButton.setText("ON");
            main.setPaused(true);
        } else if (e.getSource() == ipOnOffButton) {
            if (isIpOn) {
                opOnOffButton.setText("ON");
                ipOnOffButton.setText("ON");
                main.setPaused(true);
            } else {
                opOnOffButton.setText("OFF");
                ipOnOffButton.setText("OFF");
                main.setPaused(false);
                main.setReset(false);
            }
            isIpOn = !isIpOn;
        } else if (e.getSource() == ipResetButton) {
            main.getInpak().sendToArduino("reset");
            main.getOrderPick().sendToArduino("reset");
            main.setReset(true);
            opOnOffButton.setText("ON");
            ipOnOffButton.setText("ON");
            main.setPaused(true);
        }
    }

    //updates information on OrderPick panel. information comes from Arduino/Java.
    public void setUpOp(OrderPick orderPickRobot) {
        opFiller = new JLabel("");
        opStatus2 = new JLabel(orderPickRobot.status);
        opOrderNr2 = new JLabel(Integer.toString(main.getCurrentOrder().getOrderNr()));
        opTotalAmountOfArticles2 = new JLabel(Integer.toString(main.getCurrentOrder().getAmountOfArticles()));
        opAmountOfArticlesPicked2 = new JLabel(Integer.toString(orderPickRobot.getAmountOfArticlesPicked()));
        opCoordinate2 = new JLabel(orderPickRobot.getCurrentLocation().toString());

        //adds labels to opLabels which makes it easier to display.
        opLabels2 = new JLabel[]{opFiller, opStatus2, opOrderNr2, opTotalAmountOfArticles2, opAmountOfArticlesPicked2, opCoordinate2};
        //adds opLabels to allLabels.
        allLabels[1] = opLabels2;
    }

    //updates information on inpak panel. information comes from Arduino/Java.
    public void setUpIp(Inpak inpakRobot){
        ipFiller = new JLabel("");
        ipStatus2 = new JLabel(inpakRobot.status);
        ipOrderNr2 = new JLabel(Integer.toString(main.getCurrentOrder().getOrderNr()));
        ipTotalArticlesInOrder2 = new JLabel(Integer.toString(main.getCurrentOrder().getAmountOfArticles()));
        ipAmountOfArticlesPacked2 = new JLabel(Integer.toString(inpakRobot.getAmountOfArticlesPacked()));
        ipBinId2 = new JLabel(Integer.toString(1));

        //adds labels to opLabels which makes it easier to display.
        ipLabels2 = new JLabel[]{ipFiller, ipStatus2, ipOrderNr2, ipTotalArticlesInOrder2,ipAmountOfArticlesPacked2, ipBinId2};
        //adds opLabels to allLabels.
        allLabels[3] = ipLabels2;
    }

    public void updateOp(OrderPick orderPickRobot) {
        opStatus2.setText(orderPickRobot.getStatus());
        opOrderNr2.setText(Integer.toString(main.getCurrentOrder().getOrderNr()));
        opTotalAmountOfArticles2.setText(Integer.toString(main.getCurrentOrder().getAmountOfArticles()));
        opAmountOfArticlesPicked2.setText(Integer.toString(orderPickRobot.getAmountOfArticlesPicked()));
        opCoordinate2.setText(orderPickRobot.getCurrentLocation().toString());
    }

    public void updateIp(Inpak inpakRobot) {
        ipStatus2.setText(inpakRobot.getStatus());
        ipOrderNr2.setText(Integer.toString(main.getCurrentOrder().getOrderNr()));
        ipTotalArticlesInOrder2.setText(Integer.toString(main.getCurrentOrder().getAmountOfArticles()));
        ipAmountOfArticlesPacked2.setText(Integer.toString(inpakRobot.getAmountOfArticlesPacked()));
        ipBinId2.setText(Integer.toString(inpakRobot.getCurrentBin()));
    }

    public JFrame getJframe() {
        return this;
    }
}
