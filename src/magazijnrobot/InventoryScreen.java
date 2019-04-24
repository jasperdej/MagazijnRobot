package magazijnrobot;

import javax.swing.*;
import java.awt.*;

public class InventoryScreen extends JFrame {
    //private Object[][] allArticles = new Article[2][3];
    private String[][] allArticles;
    private String[] columnNames = {"Naam", "ItemId", "Gewicht", "Aantal", "Gereserveerd"};
    private int amountOfArticles;
    JTable jTable;

    public InventoryScreen () {
        fillAllArticle();
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
    }

    //put all articles from database in allArticles.
    private void fillAllArticle() {
        //gegevens uit de database halen
        DbConn dbConn = new DbConn();
        dbConn.getDb("select StockItemName, StockItemID, TypicalWeightPerUnit, ")
    }
}
