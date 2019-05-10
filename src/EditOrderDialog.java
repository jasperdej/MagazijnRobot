import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditOrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private boolean orderExists = true;
    private JButton jbBevestigen, jbAnnuleren;

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
        setSize(480,400);
        setTitle("Order " + orderId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(10,2));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        //JLabel test = new JLabel("ID = " + orderId + ", exists = " + orderExists);
        //add(test);

        if(orderExists){
            topPanel.add(new JLabel("Wijzig bestelling."));
        } else {
            topPanel.add(new JLabel("Maak een nieuwe bestelling aan."));
        }

        jbBevestigen = new JButton("Bevestigen");
        jbBevestigen.addActionListener(this);
        bottomPanel.add(jbBevestigen, BorderLayout.LINE_END);

        jbAnnuleren = new JButton("Annuleren");
        jbAnnuleren.addActionListener(this);
        bottomPanel.add(jbAnnuleren, BorderLayout.LINE_START);

        JLabel jlVerplicht = new JLabel("Velden met een sterretje* zijn verplicht");
        jlVerplicht.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(jlVerplicht, BorderLayout.PAGE_START);

        add(topPanel, BorderLayout.PAGE_START);
        add(middlePanel);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void addToDb(){
        //Bestelling toevoegen aan database
    }

    public void editDb(){
        //Bestelling wijzigen in database
    }

    public void setOrder(){

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
