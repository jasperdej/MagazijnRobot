import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderLinePanel {
    private EditOrderDialog dialog;
    private JTextField jtfOrderLine;
    private JComboBox jcbOrderLine;
    private JButton jbOrderLine;
    private int originalOrderLine = -1;
    private static String[] articles;
    private JPanel jpOrderLine, jpOrderLineRight;

    public OrderLinePanel(EditOrderDialog dialog, int articleId, int quantity){
        this.dialog = dialog;
        jtfOrderLine = new JTextField(String.valueOf(quantity),3);
        jcbOrderLine = new JComboBox(articles);
        jcbOrderLine.setSelectedIndex(articleId);
        jcbOrderLine.addActionListener(dialog);
        jbOrderLine = new JButton("Verwijder");
        jbOrderLine.addActionListener(dialog);
        createPanel();
    }

    public OrderLinePanel(EditOrderDialog dialog){
        this.dialog = dialog;
        jtfOrderLine = new JTextField(3);
        jcbOrderLine = new JComboBox(articles);
        jbOrderLine = new JButton("Verwijder");
        jbOrderLine.addActionListener(dialog);
        createPanel();
    }

    public void createPanel(){
        jpOrderLine = new JPanel();
        jpOrderLine.setLayout(new BorderLayout());
        jpOrderLineRight = new JPanel();
        jpOrderLineRight.setLayout(new BorderLayout());

        jpOrderLineRight.add(jtfOrderLine,BorderLayout.CENTER);
        jpOrderLineRight.add(jbOrderLine,BorderLayout.LINE_END);

        jpOrderLine.add(jcbOrderLine,BorderLayout.CENTER);
        jpOrderLine.add(jpOrderLineRight,BorderLayout.LINE_END);
    }

    public static void fillArticles(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            fillArticles();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT StockItemID, StockItemName FROM StockItems ORDER BY StockItemID");

        int amountOfRows = 0;

        try{
            rs.last();
            amountOfRows = rs.getRow() + 1;
            rs.first();

            articles = new String[amountOfRows];
            articles[0] = "Kies een artikel";

            for(int i = 1; i < amountOfRows; i++){
                articles[i] = rs.getInt("StockItemID") + "  " + rs.getString("StockItemName");
                rs.next();
            }
        } catch(SQLException sqle){
            System.out.println("Er is een SQL fout opgetreden in OrderLinePanel.java in methode fillArticles");
        } finally {
            dbConn.killStatement();
        }
        Start.dbDoneLoading = true;
    }

    public JPanel getJpOrderLine() {
        return jpOrderLine;
    }

    public JTextField getJtfOrderLine() {
        return jtfOrderLine;
    }

    public JComboBox getJcbOrderLine() {
        return jcbOrderLine;
    }

    public JButton getJbOrderLine() {
        return jbOrderLine;
    }

    public void setOriginalOrderLine(int originalOrderLine) {
        this.originalOrderLine = originalOrderLine;
    }

    public int getOriginalOrderLine() {
        return originalOrderLine;
    }

}
