package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.awt.GridBagConstraints.LINE_END;
import static java.awt.GridBagConstraints.LINE_START;

public class OrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private double totalWeight = 0;
    private Object[][] allOrderLines;
    private String[] columnNames = {"Item Id:","Aantal:","Gewicht:"};

    public OrderDialog(JFrame jFrame, int orderId) {
        super(jFrame);
        this.orderId = orderId;
        fillOrderDetails(orderId);
        setSize(400,400);
        setTitle("Order overzicht extra informatie");
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

//        JLabel jlOrderId = new JLabel("Ordernummer " + orderId);
//        jlOrderId.setHorizontalAlignment(JLabel.CENTER);

//        JPanel jpOrderDetails = new JPanel();
//        jpOrderDetails.setLayout(new GridLayout(0,3));
//
//        JPanel jpTotalWeight = new JPanel();
//        jpTotalWeight.setLayout(new GridLayout(1,2));
//
//        for(int a = 0; a < 3; a++) {
//            String item = columnNames[a];
//            jpOrderDetails.add(new JLabel(item));
//        }

//        String item;
//        for(int y = 0; y < allOrderLines.length; y++){
//            for(int x = 0; x < 3; x++) {
//                try {
//                    item = (String) allOrderLines[y][x];
//                } catch (ClassCastException ex) {
//                    item = "Onbekend";
//                }
//
//                if (x == 2) {
//                    double weight = Double.parseDouble(item);
//                    totalWeight += weight;
//                    jpOrderDetails.add(new JLabel(item + " kg"));
//                } else {
//                    jpOrderDetails.add(new JLabel(item));
//                }
//            }
//        }

//        jpTotalWeight.add(new JLabel("Totaal gewicht:"));
//        jpTotalWeight.add(new JLabel(totalWeight + " kg"));
//
//        add(jlOrderId);
//        add(jpOrderDetails);
//        add(new JScrollPane(jpOrderDetails));
//        add(jpTotalWeight);

//        JPanel jpOrderDetails = new JPanel();
//        jpOrderDetails.setLayout(new GridBagLayout());
//        add(jpOrderDetails);

        GridBagConstraints c_START = new GridBagConstraints();
        c_START.gridx = 0;
        c_START.gridy = 1;
        c_START.anchor = LINE_START;

        GridBagConstraints c_MIDDLE = new GridBagConstraints();
        c_MIDDLE.gridx = c_START.gridx + 1;
        c_MIDDLE.gridy = c_START.gridy;
        c_MIDDLE.anchor = LINE_START;

        GridBagConstraints c_END = new GridBagConstraints();
        c_END.gridx = c_START.gridx + 2;
        c_END.gridy = c_START.gridy;
        c_END.anchor = LINE_START;

        String item;

        for (int y = 0; y < allOrderLines.length; y++){
            item = (String)allOrderLines[y][0];
            add(new JLabel(item),c_START);
            item = (String)allOrderLines[y][1];
            add(new JLabel(item),c_MIDDLE);
            item = allOrderLines[y][2] + " kg";
            add(new JLabel(item),c_END);
            c_START.gridy++;
            c_MIDDLE.gridy = c_START.gridy;
            c_END.gridy = c_START.gridy;
        }

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

    public int dbGetItemQuantity(){
        return 10;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
