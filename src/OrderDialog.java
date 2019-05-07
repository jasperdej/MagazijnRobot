import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private double totalWeight = 0;
    private Object[][] allOrderLines;
    private String[] columnNames = {"Item Id:","Aantal:","Gewicht:"};

    public OrderDialog(JFrame jFrame, int orderId) {
        super(jFrame);
        this.orderId = orderId;
        fillOrderDetails(orderId);
        setSize(450,400);
        setTitle("Order overzicht extra informatie");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel jlOrderId = new JLabel("Ordernummer " + orderId);
        jlOrderId.setHorizontalAlignment(JLabel.CENTER);
        jlOrderId.setFont(new Font("Ariel", Font.BOLD, 40));

        JTable jTable = new JTable(allOrderLines,columnNames);
        JScrollPane sp = new JScrollPane(jTable);

        totalWeight = getTotalWeight();
        JLabel jlWeight = new JLabel("Totaal gewicht: " + totalWeight);
        jlWeight.setHorizontalAlignment(JLabel.CENTER);
        jlWeight.setVerticalAlignment(JLabel.BOTTOM);

        add(jlOrderId,BorderLayout.PAGE_START);
        add(sp,BorderLayout.CENTER);
        add(jlWeight,BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void fillOrderDetails(int orderId){
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
                allOrderLines[i][0] = rs.getString("ol.StockItemID");
                allOrderLines[i][1] = rs.getString("ol.Quantity");
                allOrderLines[i][2] = rs.getString("si.TypicalWeightPerUnit");
                rs.next();
            }

        } catch (SQLException sqle) {
            System.out.println(sqle);
        } finally {
            dbConn.killStatement();
        }
    }

    public double getTotalWeight(){
        double weight;
        double count;
        for(int y = 0; y < allOrderLines.length; y++){
            try {
                weight = Double.parseDouble((String)allOrderLines[y][2]);
            } catch (ClassCastException ex){
                weight = 0.0;
            }
            try {
                count = Double.parseDouble((String)allOrderLines[y][1]);
            } catch (ClassCastException ex) {
                count = 0.0;
            }
            totalWeight += weight * count;
        }
        return totalWeight;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
