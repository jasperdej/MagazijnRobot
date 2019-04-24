package magazijnrobot;

import arduino.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class ArduinoConn {
//    static Arduino arduinoPick;
//    static Arduino arduinoInpak;
//    static JFrame frame = new JFrame("An Led Controller");
//    static JButton btnOnPick = new JButton("ON");
//    static JButton btnOnInpak = new JButton("ON");
//    static JButton btnOffPick = new JButton("OFF");
//    static JButton btnOffInpak = new JButton("OFF");
//    static JLabel led1 = new JLabel("Led 1 is:");
//    static JLabel led2 = new JLabel("Led 2 is:");
//    static JButton btnRefresh;
//    static JButton btnRefresh2;
//
//
//
//
//    public static void main(String[] args) {
//        setUpGUI(); // refer to this function only if you have knowledge of JAVA swing classes and GUI elements.
//
//        frame.setResizable(false);
//
//
//        btnOnPick.addActionListener(new ActionListener(){
//            @Override public void actionPerformed(ActionEvent e) {
//                arduinoPick.serialWrite("aan");
//
//            }
//        });
//
//        btnOnInpak.addActionListener(new ActionListener(){
//            @Override public void actionPerformed(ActionEvent e) {
//                arduinoInpak.serialWrite("aan");
//                String status = arduinoInpak.serialRead();
//                System.out.println(status);
//                System.out.println("test aan");
//                led2.setText("Led 2 is: " + status);
//
//
//            }
//        });
//
//        btnOffPick.addActionListener(new ActionListener(){
//            @Override public void actionPerformed(ActionEvent e) {
//                arduinoPick.serialWrite("uit");
//            }
//        });
//
//        btnOffInpak.addActionListener(new ActionListener(){
//            @Override public void actionPerformed(ActionEvent e) {
//                arduinoInpak.serialWrite("uit");
//
//
//                String status = arduinoInpak.serialRead();
//                System.out.println(status);
//                System.out.println("test uit");
//                led2.setText("Led 2 is: " + status);
//            }
//        });
//
//    }
//
//    public static void populateMenu(){ //gets the list of available ports and fills the dropdown menu
//        final PortDropdownMenu portList = new PortDropdownMenu();
//        final PortDropdownMenu portList2 = new PortDropdownMenu();
//        portList.refreshMenu();
//        portList2.refreshMenu();
//        final JButton connectButton = new JButton("Connect");
//        final JButton connectButton2 = new JButton("Connect");
//
//
//        ImageIcon refresh = new ImageIcon("/Users/HirdayGupta/Documents/workspace/Java-Arduino-Communication-Library/src/examples/refresh.png");
//        btnRefresh = new JButton(refresh);
//        btnRefresh2 = new JButton(refresh);
//
//        JPanel topPanel = new JPanel();
//
//        btnRefresh.addActionListener(new ActionListener(){
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                portList.refreshMenu();
//
//            }
//        });
//        topPanel.add(led1);
//        topPanel.add(led2);
//        topPanel.add(portList);
//        topPanel.add(portList2);
//        topPanel.add(btnRefresh);
//        topPanel.add(btnRefresh2);
//        topPanel.add(connectButton);
//        topPanel.add(connectButton2);
//        // populate the drop-down box
//
//        connectButton.addActionListener(new ActionListener(){
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(connectButton.getText().equals("Connect")){
//                    arduinoPick = new Arduino("COM11",9600);
//                    if(arduinoPick.openConnection()){
//                        connectButton.setText("Disconnect");
//                        portList.setEnabled(false);
//                        btnOnPick.setEnabled(true);
//                        btnOffPick.setEnabled(true);
//                        btnRefresh.setEnabled(false);
//                        frame.pack();
//
//
//                    }
//                }
//                else {
//                    arduinoPick.closeConnection();
//                    connectButton.setText("Connect");;
//                    portList.setEnabled(true);
//                    btnOnPick.setEnabled(false);
//                    btnRefresh.setEnabled(true);
//                    btnOffPick.setEnabled(false);
//                }
//            }
//
//        });
//
//        connectButton2.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent ex) {
//                if(connectButton2.getText().equals("Connect")){
//                    arduinoInpak = new Arduino(portList2.getSelectedItem().toString(),9600);
//                    if(arduinoInpak.openConnection()){
//                        connectButton2.setText("Disconnect");
//                        portList2.setEnabled(false);
//                        btnOnInpak.setEnabled(true);
//                        btnOffInpak.setEnabled(true);
//                        btnRefresh2.setEnabled(false);
//                        frame.pack();
//
//                    }
//                }
//                else {
//                    arduinoInpak.closeConnection();
//                    connectButton2.setText("Connect");;
//                    portList2.setEnabled(true);
//                    btnOnInpak.setEnabled(false);
//                    btnRefresh2.setEnabled(true);
//                    btnOffInpak.setEnabled(false);
//                }
//            }
//
//        });
//        //topPanel.setBackground(Color.BLUE);
//        frame.add(topPanel, BorderLayout.NORTH);
//    }
//
//    public static void setUpGUI(){
//        frame.setSize(600, 600);
//        frame.setBackground(Color.black);
//        frame.setForeground(Color.black);
//        //frame.setPreferredSize(new Dimension(600,600));
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new BorderLayout());
//        btnOnPick.setForeground(Color.GREEN);
//        btnOnPick.setEnabled(false);
//        btnOffPick.setForeground(Color.RED);
//        btnOffPick.setEnabled(false);
//
//        btnOnInpak.setForeground(Color.GREEN);
//        btnOnInpak.setEnabled(false);
//        btnOffInpak.setForeground(Color.RED);
//        btnOffInpak.setEnabled(false);
//        JPanel pane = new JPanel();
//        //pane.setBackground(Color.blue);
//        pane.add(btnOnPick);
//        pane.add(btnOffPick);
//
//        pane.add(btnOnInpak);
//        pane.add(btnOffInpak);
//        frame.add(pane, BorderLayout.CENTER);
//        populateMenu();
//        frame.pack();
//        frame.getContentPane();
//        frame.setVisible(true);
//
//    }
//}


    static Arduino arduinoPickRobot;
    static Arduino arduinoInpakRobot;


    public static void arduinoConnectPickRobot() {

        arduinoPickRobot = new Arduino("COM11", 9600);

    }

    public static void main(String[] args) {
        ArduinoConn.arduinoConnectPickRobot();
        if (arduinoPickRobot.openConnection()) {
            try {Thread.sleep(1500);} catch(Exception e){}
                System.out.println("test");
                arduinoPickRobot.serialWrite("aan");
            }
        }
    }

