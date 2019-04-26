package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryScreen extends JFrame {
    //private Object[][] allArticles = new Article[2][3];
    private Object[][] allArticles;
    private String[] columnNames = {"Naam", "ItemId", "Gewicht", "Aantal", "Gereserveerd"};
    private int amountOfArticles;
    JTable jTable;

    public InventoryScreen () {
        fillAllArticles();
        setTitle("Voorraad overzicht");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the frame completely fullscreen.
        //setUndecorated(true);


        jTable = new JTable(allArticles, columnNames);
        //ik weet niet wat dit doet, voorbeeld van internet overgenomen.
        //jTable.setBounds(30, 40, 200, 300);

        JScrollPane sp = new JScrollPane(jTable);
        add(sp);

        setVisible(true);
        refreshInventoryScreen();
    }

    //put all articles from database in allArticles.
    private void fillAllArticles() {
        //get results from database.
        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("select si.StockItemName, si.StockItemID, si.TypicalWeightPerUnit, (SELECT sum(QuantityOnHand) FROM stockitemholdings sih WHERE sih.StockItemID = si.StockItemID), sum(ol.Quantity) from stockitems si left join Orderlines ol on ol.Stockitemid = si.Stockitemid where si.StockItemID < 6 group by ol.Stockitemid;");

        //int for measuring the amount of rows in the resultset.
        int amountOfRows = 0;

        //add results to two-dimensional array;
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
            for (int i = 0, y = 1; i < amountOfRows; i++) {
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

    public void refreshInventoryScreen() {
        fillAllArticles();
        jTable = new JTable(allArticles, columnNames);
    }
}
