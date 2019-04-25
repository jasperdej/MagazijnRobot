package magazijnrobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderScreen extends JFrame implements ActionListener {
    private Object[][] allOrders;
    private String[] columnNames = {"Ordernummer","Aantal","Gewicht","Klantnummer","Status"};
    JTable jTable;

    public OrderScreen() {
        fillAllOrders();
        setTitle("Order overzicht");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        jTable = new JTable(allOrders, columnNames);

        JScrollPane sp = new JScrollPane(jTable);
        add(sp);

        setVisible(true);
    }

    private void fillAllOrders() {
        //get results from database.
        DbConn dbConn = new DbConn();
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
            for (int i = 0, y = 1; i < amountOfRows; i++) {
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

    public void printAllOrders() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}



