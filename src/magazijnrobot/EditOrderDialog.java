package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditOrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private boolean orderExists = true;

    public EditOrderDialog(JFrame jFrame, int orderId){
        super(jFrame,true);
        this.orderId = orderId;
        createDialog();
    }

    public EditOrderDialog(JFrame jFrame){
        super(jFrame,true);
        setNewOrderId();
        orderExists = false;
        createDialog();
    }

    public void createDialog(){
        setSize(450,400);
        setTitle("Order " + orderId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel test = new JLabel("ID = " + orderId + ", exists = " + orderExists);
        add(test);

        setVisible(true);
    }

    public void setNewOrderId(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(OrderID) FROM Orders;");


        try{
            rs.first();
            orderId = rs.getInt("MAX(OrderID)") + 1;
        } catch (SQLException sqle) {
            System.out.println(sqle);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        } finally {
            dbConn.killStatement();
        }
    }

    public void actionPerformed(ActionEvent e){

    }
}
