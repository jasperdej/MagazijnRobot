import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class TestScreen extends JFrame implements ActionListener {

    private JButton jbCreateOrder;
    private JButton jbEditOrder;
    private JTextField jtfOrder;
    private JButton jbCreateArticle;
    private JButton jbEditArticle;
    private JTextField jtfArticle;
    private JButton jbCreatePerson;
    private JButton jbEditPerson;
    private JTextField jtfPerson;

    public TestScreen(){
        setTitle("Test screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new GridLayout(0,3));

        jtfArticle = new JTextField();
        add(jtfArticle);
        jbEditArticle = new JButton("Edit Article");
        jbEditArticle.addActionListener(this);
        add(jbEditArticle);
        jbCreateArticle = new JButton("Create Article");
        jbCreateArticle.addActionListener(this);
        add(jbCreateArticle);

        jtfOrder = new JTextField();
        add(jtfOrder);
        jbEditOrder = new JButton("Edit Order");
        jbEditOrder.addActionListener(this);
        add(jbEditOrder);
        jbCreateOrder = new JButton("Create Order");
        jbCreateOrder.addActionListener(this);
        add(jbCreateOrder);

        jtfPerson = new JTextField();
        add(jtfPerson);
        jbEditPerson = new JButton("Edit Person");
        jbEditPerson.addActionListener(this);
        add(jbEditPerson);
        jbCreatePerson = new JButton("Create Person");
        jbCreatePerson.addActionListener(this);
        add(jbCreatePerson);

        setVisible(true);
    }

    public boolean checkID(String query, String id){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb(query + id);

            try {
                if(rs.next()){
                     return true;
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                dbConn.killStatement();
            }
            return false;
    }

    public void actionPerformed(ActionEvent e){
        remove(jbCreatePerson);
        revalidate();
        repaint();
        if(e.getSource() == jbCreateArticle){
            //EditArticleDialog createArticleDialog = new EditArticleDialog(this);
        } else if (e.getSource() == jbEditArticle){
            if(checkID("SELECT StockItemID FROM StockItems WHERE StockItemID = ", jtfArticle.getText())) {
                //EditArticleDialog editArticleDialog = new EditArticleDialog(this, Integer.parseInt(jtfArticle.getText()));
            } else {
                JOptionPane.showMessageDialog(this,"Dit artikel bestaat niet.");
            }
        } else if (e.getSource() == jbCreateOrder) {
           // EditOrderDialog createOrderDialog = new EditOrderDialog(this,this);
        } else if (e.getSource() == jbEditOrder) {
            if(checkID("SELECT OrderID FROM Orders WHERE OrderID = ",jtfOrder.getText())) {
             //   EditOrderDialog editOrderDialog = new EditOrderDialog(this,this,Integer.parseInt(jtfOrder.getText()));
            } else {
                JOptionPane.showMessageDialog(this,"Deze order bestaat niet.");
            }
        } else if (e.getSource() == jbCreatePerson) {
            EditPersonDialog createPersonDialog = new EditPersonDialog(this);
        } else if (e.getSource() == jbEditPerson) {
            if(checkID("SELECT UserID FROM USERS WHERE UserID = ",jtfPerson.getText())) {
                EditPersonDialog editPersonDialog = new EditPersonDialog(this,Integer.parseInt(jtfPerson.getText()));
            } else {
                JOptionPane.showMessageDialog(this,"Deze klant bestaat niet.");
            }

        }
    }
}
