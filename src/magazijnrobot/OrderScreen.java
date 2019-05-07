package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.awt.GridBagConstraints.*;

public class OrderScreen extends JFrame implements MouseListener, ActionListener {
//    private static OrderScreen thisJFrame = new OrderScreen();
    private Object[][] allOrders;
    private String[] columnNames = {"Ordernummer","Aantal","Gewicht","Klantnummer","Status"};
    private JTable jTable;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ScreenManager screenManager;

    //buttons for swithing screens.
    private JButton robotScreen = new JButton("Robot overzicht");
    private JButton orderScreen = new JButton("Order overzicht");
    private JButton inventoryScreen = new JButton("Voorraad overzicht");


    public OrderScreen() {
        createScreen();
        System.out.println("OrderScreen ready!");

    }

    private void createScreen() {
        fillAllOrders();
        setTitle("Order overzicht");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the frame completely fullscreen.
        setUndecorated(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setMinimumSize(new Dimension(screenSize.width, 35));
        buttonPanel.setMaximumSize(new Dimension(screenSize.width, 35));
        buttonPanel.setPreferredSize(new Dimension(screenSize.width, 35));

        buttonPanel.add(robotScreen);
        buttonPanel.add(orderScreen);
        buttonPanel.add(inventoryScreen);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = HORIZONTAL;
        c.anchor = NORTH;
        add(buttonPanel, c);

        c.fill = BOTH;
        c.insets = new Insets(-350, 0, 0, 0);
        c.gridy = 1;


        jTable = new JTable(allOrders, columnNames);
        jTable.setMinimumSize(new Dimension(screenSize.width, screenSize.height - 35));
        jTable.setMaximumSize(new Dimension(screenSize.width, screenSize.height - 35));
        jTable.setPreferredSize(new Dimension(screenSize.width, screenSize.height - 35));
        jTable.addMouseListener(this);
        add(jTable, c);

        JScrollPane sp = new JScrollPane(jTable);

        add(sp, c);

        robotScreen.addActionListener(this);
        orderScreen.addActionListener(this);
        inventoryScreen.addActionListener(this);

    }

    private void fillAllOrders() {
        //get results from database.
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT o.OrderId, SUM(ol.quantity), SUM(ol.quantity * s.TypicalWeightPerUnit), o.CustomerId, o.status FROM orders o JOIN orderlines ol ON o.OrderId = ol.OrderId JOIN stockitems s ON ol.StockItemID = s.StockItemID GROUP BY o.OrderId;");

        //int for measuring the amount of rows in the resultset.
        int amountOfRows = 0;

        //add results to two-dimensional array.
        try{
            if (rs != null){
                rs.last();
                amountOfRows = rs.getRow();
                rs.first();
            }

            //initiating two-dimensional array with correct amount of rows.
            //the amount of rows is dependant on the amount of results returned from the database.
            allOrders = new Object[amountOfRows][5];

            //adding results form resultset to two-dimensional array for JTable.
            for (int i = 0; i < amountOfRows - 1; i++) {
                allOrders[i][0] = rs.getString("o.OrderId");
                allOrders[i][1] = rs.getString("SUM(ol.quantity)");
                allOrders[i][2] = rs.getString("SUM(ol.quantity * s.TypicalWeightPerUnit)");
                allOrders[i][3] = rs.getString("o.CustomerId");
                allOrders[i][4] = rs.getString("o.status");
                rs.next();
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        } finally {
            dbConn.killStatement();
        }
    }

    public void refreshAllOrders() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = jTable.getSelectedRow();
        int orderId;
        try {
            orderId = Integer.parseInt((String)jTable.getValueAt(row,0));
        } catch (ClassCastException ex) {
            orderId = 0;
        }
        //create dialog
        OrderDialog dialog = new OrderDialog(this,orderId);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == robotScreen) {
            screenManager.buttonPressed("RobotScreen");
        } else if (e.getSource() == orderScreen) {
            screenManager.buttonPressed("OrderScreen");
        } else if (e.getSource() == inventoryScreen) {
            screenManager.buttonPressed("InventoryScreen");
        }
    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }


}



