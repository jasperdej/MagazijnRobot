import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.awt.GridBagConstraints.*;

public class InventoryScreen extends JFrame implements ActionListener {
    private Object[][] allArticles;
    private String[] columnNames = {"Naam", "ItemId", "Gewicht", "Aantal", "Gereserveerd"};
    private JTable jTable;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private ScreenManager screenManager;

    //buttons for swithing screens.

    private JButton robotScreen = new JButton("Robot overzicht");
    private JButton orderScreen = new JButton("Order overzicht");
    private JButton inventoryScreen = new JButton("Voorraad overzicht");

    public InventoryScreen() {
        createScreen();
        System.out.println("InventoryScreen ready!");
    }

    private void createScreen() {
        //fills array with articles from database.
        fillAllArticles();

        //gridbaglayout for buttons and JTable.
        setLayout(new GridBagLayout());

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the frame completely fullscreen.
        setUndecorated(true);

        //buttonpanel for buttons. is set to top of screen.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setMinimumSize(new Dimension(screenSize.width, 35));
        buttonPanel.setMaximumSize(new Dimension(screenSize.width, 35));
        buttonPanel.setPreferredSize(new Dimension(screenSize.width, 35));

        //adds buttons to panel.
        buttonPanel.add(robotScreen);
        buttonPanel.add(orderScreen);
        buttonPanel.add(inventoryScreen);

        //gridbagconstraints place buttons on top left of screen.
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


        //JTable with results from database.
        jTable = new JTable(allArticles, columnNames);
        jTable.setMinimumSize(new Dimension(screenSize.width, screenSize.height - 35));
        jTable.setMaximumSize(new Dimension(screenSize.width, screenSize.height - 35));
        jTable.setPreferredSize(new Dimension(screenSize.width, screenSize.height - 35));

        //adds JTable to screen.
        add(jTable, c);

        //scrollpane adds scroll functionality to jtable. adds scrollpane with jtable to screen.
        JScrollPane sp = new JScrollPane(jTable);
        add(sp, c);

        //adds actionlisteners for buttons.
        robotScreen.addActionListener(this);
        orderScreen.addActionListener(this);
        inventoryScreen.addActionListener(this);
    }


    //put all articles from database in allArticles.
    private void fillAllArticles() {
        //get results from database. resultset contains all results from query.
        DbConn dbConn = new DbConn();
        dbConn.dbConnect(); //"select si.StockItemName, si.StockItemID, si.TypicalWeightPerUnit, (SELECT sum(QuantityOnHand) FROM stockitemholdings sih WHERE sih.StockItemID = si.StockItemID), sum(ol.Quantity) from stockitems si join Orderlines ol on ol.Stockitemid = si.Stockitemid  group by ol.Stockitemid;"
        ResultSet rs = dbConn.getResultSetFromDb("select si.StockItemName, si.StockItemID, si.TypicalWeightPerUnit, (SELECT sum(QuantityOnHand) FROM stockitemholdings sih WHERE sih.StockItemID = si.StockItemID), sum(ol.Quantity) from stockitems si join Orderlines ol on ol.Stockitemid = si.Stockitemid  group by ol.Stockitemid;");

        //int for measuring the amount of rows in the resultset.
        int amountOfRows = 0;

        try{
            if (rs != null) {
                rs.last();
                amountOfRows = rs.getRow();
                rs.first();
            }

            //initiating two-dimensional array with correct amount of rows.
            //the amount of rows is dependant on the amount of results returned from the database.
            allArticles = new Object[amountOfRows][5];

            //adding results from resultset to two-dimensional array for JTable.
            for (int i = 0; i < amountOfRows; i++) {
                allArticles[i][0] = rs.getString("si.StockItemName");
                allArticles[i][1] = rs.getInt("si.StockItemID");
                allArticles[i][2] = rs.getDouble("si.TypicalWeightPerUnit");
                allArticles[i][3] = rs.getInt("(SELECT sum(QuantityOnHand) FROM stockitemholdings sih WHERE sih.StockItemID = si.StockItemID)");
                allArticles[i][4] = rs.getInt("sum(ol.Quantity)");
                rs.next();
            }

        } catch (SQLException sqle) {
            System.out.println(sqle);
        } finally {
            dbConn.killStatement();
        }

    }

    //gets results from database and updates current values on screen.
    public void refreshInventoryScreen() {
        fillAllArticles();
        jTable = new JTable(allArticles, columnNames);
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //buttons for switching between screens.
        if (e.getSource() == robotScreen) {
            System.out.println("robot");
            screenManager.buttonPressed("RobotScreen");
        } else if (e.getSource() == orderScreen) {
            screenManager.buttonPressed("OrderScreen");
        } else if (e.getSource() == inventoryScreen) {
            screenManager.buttonPressed("InventoryScreen");
        }
    }
}
