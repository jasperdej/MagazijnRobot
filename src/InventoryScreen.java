import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.awt.GridBagConstraints.*;

public class InventoryScreen extends JFrame implements ActionListener {
    private Object[][] allArticles;
    private String[] columnNames = {"Naam", "ItemID", "Gewicht (in kg)", "Aantal", "Gereserveerd"};
    private JTable jTable;
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
        setLayout(new BorderLayout());

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the frame completely fullscreen.
        setUndecorated(true);

        //buttonpanel for buttons. is set to top of screen.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        //adds buttons to panel.
        buttonPanel.add(robotScreen);
        buttonPanel.add(orderScreen);
        buttonPanel.add(inventoryScreen);

        add(buttonPanel,BorderLayout.PAGE_START);

        //JTable with results from database.
        jTable = new JTable(allArticles, columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //adds JTable to screen.
        add(jTable);

        //scrollpane adds scroll functionality to jtable. adds scrollpane with jtable to screen.
        JScrollPane sp = new JScrollPane(jTable);
        add(sp);

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


        ResultSet rs1 = dbConn.getResultSetFromDb("SELECT si.StockItemName, si.StockItemID, si.TypicalWeightPerUnit, SUM(sih.QuantityOnHand) FROM StockItems si JOIN StockItemHoldings sih ON si.StockItemID = sih.StockItemID GROUP BY StockItemID;");
        ResultSet rs2 = dbConn.getResultSetFromDb("SELECT Quantity, StockItemID FROM Orderlines ORDER BY StockItemID;");

        //int for measuring the amount of rows in the resultset.
        int amountOfRows = 0;

        try{
            if (rs1 != null) {
                rs1.last();
                amountOfRows = rs1.getRow();
                rs1.first();
            }
//            rs2.first();

            //initiating two-dimensional array with correct amount of rows.
            //the amount of rows is dependant on the amount of results returned from the database.
            allArticles = new Object[amountOfRows][5];

            //adding results from resultset to two-dimensional array for JTable.
            for (int i = 0; i < amountOfRows; i++) {
                allArticles[i][0] = rs1.getString("si.StockItemName");
                allArticles[i][1] = rs1.getInt("si.StockItemID");
                allArticles[i][2] = rs1.getDouble("si.TypicalWeightPerUnit");
                allArticles[i][3] = rs1.getInt("SUM(sih.QuantityOnHand)");
                int id = rs1.getInt("si.StockItemID");
                boolean foundRelevantRecords = false;
                int quantityOnHand = 0;
                while(!foundRelevantRecords && rs2.next()) {
                    if(rs2.getInt("StockItemID") < id){
//                        rs2.next();
                    } else if(rs2.getInt("StockItemID") == id){
                        quantityOnHand += rs2.getInt("Quantity");
//                        rs2.next();
                    } else {
                        foundRelevantRecords = true;
                    }
                }
                allArticles[i][4] = quantityOnHand;
                rs1.next();
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
