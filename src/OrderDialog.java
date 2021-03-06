import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDialog extends JDialog {

    private int orderId;
    private BigDecimal totalWeight = new BigDecimal(0);
    private Object[][] allOrderLines;
    private String[] columnNames = {"Item Id","Aantal","Gewicht (in kg)"};

    public OrderDialog(JFrame jFrame, int orderId) {
        super(jFrame,true);
        this.orderId = orderId;
        fillOrderDetails();
        setSize(450,400);
        setTitle("Order overzicht extra informatie");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel jlOrderId = new JLabel("Ordernummer " + orderId);
        jlOrderId.setHorizontalAlignment(JLabel.CENTER);
        jlOrderId.setFont(new Font("Ariel", Font.BOLD, 40));

        JTable jTable = new JTable(allOrderLines,columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane sp = new JScrollPane(jTable);

        totalWeight = getTotalWeight();
        JLabel jlWeight = new JLabel("Totaal gewicht: " + totalWeight + " kg");
        jlWeight.setHorizontalAlignment(JLabel.CENTER);
        jlWeight.setVerticalAlignment(JLabel.BOTTOM);

        add(jlOrderId,BorderLayout.PAGE_START);
        add(sp,BorderLayout.CENTER);
        add(jlWeight,BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void fillOrderDetails(){
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            fillOrderDetails();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT ol.StockItemId, ol.Quantity, si.TypicalWeightPerUnit FROM OrderLines ol JOIN StockItems si ON ol.StockItemID = si.StockItemID WHERE ol.OrderID = " + orderId);

        int amountOfRows = 0;

        try{
            if(rs != null){
                rs.last();
                amountOfRows = rs.getRow();
                rs.first();
            }

            allOrderLines = new Object[amountOfRows][3];

            for(int i = 0; i < amountOfRows; i++){
                allOrderLines[i][0] = rs.getInt("ol.StockItemID");
                allOrderLines[i][1] = rs.getInt("ol.Quantity");
                allOrderLines[i][2] = rs.getBigDecimal("si.TypicalWeightPerUnit");
                rs.next();
            }

        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in OrderDialog.java in methode fillOrderDetails");
        } finally {
            dbConn.killStatement();
            DbConn.dbKill();
            Start.dbDoneLoading = true;
        }
    }

    //returns total weight of order.
    public BigDecimal getTotalWeight(){
        BigDecimal weight;
        BigDecimal count;
        for(int y = 0; y < allOrderLines.length; y++){
            try {
                weight = (BigDecimal)allOrderLines[y][2];
            } catch (ClassCastException cce){
                weight = new BigDecimal(0);
            }
            try {
                count = new BigDecimal((int)allOrderLines[y][1]);
            } catch (ClassCastException cce) {
                count = new BigDecimal(0);
            }
            totalWeight = totalWeight.add(count.multiply(weight));
        }
        return totalWeight;
    }
}
