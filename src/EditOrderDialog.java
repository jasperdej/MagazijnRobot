import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ScrollPaneConstants;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class EditOrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private boolean orderExists = true;
    private JLabel jlTitelNew, jlTitelEdit, jlCustomerId, jlVerplicht, jlArticleId, jlArticleAmount;
    private JTextField jtfCustomerId;
    private JButton jbAddArticle, jbBevestigen, jbAnnuleren;
    private String customerId;
    private JTextField[] jtfOrderLines;
    private JComboBox[] jcOrderLines;
    private JButton[] jbOrderLines;
    private String[][] orderLines;
    private String[] articles;
    int rows = 7;

    public EditOrderDialog(JFrame jFrame, int orderId){
        super(jFrame,true);
        this.orderId = orderId;
        setOrder();
        setArticleList();
        fillOrderLines();
        createDialog();
    }

    public EditOrderDialog(JFrame jFrame){
        super(jFrame,true);
        setNewOrderId();
        setArticleList();
        jcOrderLines = new JComboBox[1];
        jcOrderLines[0] = new JComboBox<>(articles);
        jtfOrderLines = new JTextField[1];
        jtfOrderLines[0] = new JTextField(3);
        jbOrderLines = new JButton[1];
        jbOrderLines[0] = new JButton("Verwijder");

        orderExists = false;
        createDialog();
    }

    public void createDialog(){

        setSize(800,400);
        setResizable(false);
        setTitle("Order ID: " + orderId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panel = (JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        JPanel topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new GridLayout(0,2));

        JPanel bottomMiddlePanel = new JPanel();
        bottomMiddlePanel.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        jlTitelNew = new JLabel("Order aanmaken");
        jlTitelNew.setFont(new Font("Arial",Font.BOLD,30));
        jlTitelEdit = new JLabel("Order Gegevens wijzigen");
        jlTitelEdit.setFont(new Font("Arial",Font.BOLD,30));
        if(orderExists){
            topPanel.add(jlTitelEdit);
        } else {
            topPanel.add(jlTitelNew);
        }

        jlCustomerId = new JLabel("Klant ID: *");
        topMiddlePanel.add(jlCustomerId);
        jtfCustomerId = new JTextField(customerId,10);
        topMiddlePanel.add(jtfCustomerId);

        jlArticleId = new JLabel("Artikel ID: ");
        topMiddlePanel.add(jlArticleId);
        jlArticleAmount = new JLabel("Aantal: ");
        topMiddlePanel.add(jlArticleAmount);

        JPanel jpOrderLinesLeft = new JPanel();
        jpOrderLinesLeft.setLayout(new GridLayout(rows,1));
        JPanel jpOrderLinesRight = new JPanel();
        jpOrderLinesRight.setLayout(new GridLayout(rows,1));

        for(int i = 0; i < jtfOrderLines.length; i++){
            if(orderExists){
                jcOrderLines[i].setSelectedIndex(Integer.parseInt(orderLines[i][0])-1);
            }
            jpOrderLinesLeft.add(jcOrderLines[i]);
            JPanel jpOrderLinesRightLine = new JPanel();
            jpOrderLinesRightLine.setLayout(new BorderLayout());
            jpOrderLinesRightLine.add(jtfOrderLines[i],BorderLayout.CENTER);
            jpOrderLinesRightLine.add(jbOrderLines[i],BorderLayout.LINE_END);
            jpOrderLinesRight.add(jpOrderLinesRightLine);

        }
        bottomMiddlePanel.add(jpOrderLinesLeft,BorderLayout.CENTER);
        bottomMiddlePanel.add(jpOrderLinesRight,BorderLayout.LINE_END);

        JScrollPane sp = new JScrollPane(bottomMiddlePanel);

        middlePanel.add(topMiddlePanel,BorderLayout.PAGE_START);
        middlePanel.add(sp,BorderLayout.CENTER);

        jlVerplicht = new JLabel("Velden met een sterretje* zijn verplicht");
        jlVerplicht.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        bottomPanel.add(jlVerplicht, BorderLayout.PAGE_START);

        jbBevestigen = new JButton("Bevestigen");
        jbBevestigen.addActionListener(this);
        buttonPanel.add(jbBevestigen);

        jbAnnuleren = new JButton("Annuleren");
        jbAnnuleren.addActionListener(this);
        buttonPanel.add(jbAnnuleren);

        bottomPanel.add(buttonPanel, BorderLayout.PAGE_END);

        add(topPanel, BorderLayout.PAGE_START);
        add(middlePanel);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void addToDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        //dbConn.updateDb("INSERT INTO Users (UserID, UserFirstName, UserPrefix, UserLastName, UserEmail, UserAddress, UserResidence, UserPostalCode) VALUES (" + orderId + ", '" + voornaam + "', " + tussenvoegsel + ", '" + achternaam + "', " + emailadres + ", '" + adres + "', '" + woonplaats + "', '" + postcode + "')");
        dbConn.killStatement();
        DbConn.dbKill();
    }

    public void editDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE Orders SET CustomerID = " + customerId + " WHERE OrderID = " + orderId);
        dbConn.killStatement();
        DbConn.dbKill();
    }

    public void setOrder(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT CustomerID FROM Orders WHERE OrderID = " + orderId);

        try{
            rs.first();

            customerId = rs.getString("CustomerID");
        } catch (SQLException sqle) {
            System.out.println(sqle);

        } finally {
            dbConn.killStatement();
        }
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

    public void fillOrderLines(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT ol.StockItemID, si.StockItemName, ol.Quantity FROM OrderLines ol JOIN StockItems si ON ol.StockItemID = si.StockItemID WHERE OrderID = " + orderId);

        int amountOfRows = 0;

        try{
            rs.last();
            amountOfRows = rs.getRow();
            if (amountOfRows > 7) {
                rows = amountOfRows;
            }
            rs.first();

            jtfOrderLines = new JTextField[amountOfRows];
            orderLines = new String[amountOfRows][3];
            jcOrderLines = new JComboBox[amountOfRows];
            jbOrderLines = new JButton[amountOfRows];

            for(int i = 0; i < amountOfRows; i++){
                orderLines[i][0] = rs.getString("ol.StockItemID");
                orderLines[i][1] = rs.getString("si.StockItemName");
                orderLines[i][2] = rs.getString("ol.Quantity");
                jcOrderLines[i] = new JComboBox<>(articles);
                jtfOrderLines[i] = new JTextField(orderLines[i][2],3);
                jbOrderLines[i] = new JButton("Verwijder");

                rs.next();
            }
        } catch(SQLException sqle){
            System.out.println(sqle);
        } finally {
            dbConn.killStatement();
        }
    }

    public void setArticleList(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT StockItemID, StockItemName FROM StockItems ORDER BY StockItemID");

        int amountOfRows = 0;

        try{
            rs.last();
            amountOfRows = rs.getRow();
            rs.first();

            articles = new String[amountOfRows];
            for(int i = 0; i < amountOfRows; i++){
                articles[i] = rs.getInt("StockItemID") + "  " + rs.getString("StockItemName");
                rs.next();
            }
        } catch(SQLException sqle){
            System.out.println(sqle);
        }
    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource() == jbAnnuleren) {
            dispose();
        } else if (e.getSource() == jbBevestigen) {
            customerId = jtfCustomerId.getText();
            if(customerId.equals("")){
                JOptionPane.showMessageDialog(this,"Niet alle verplichte velden zijn ingevuld.");
            } else {
                if (orderExists == true) {
                    int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u deze gegevens wilt wijzigen?", "Wijzigen gegevens", JOptionPane.YES_NO_OPTION);
                    if (keuze == JOptionPane.YES_OPTION) {
                        editDb();
                        dispose();
                    }
                } else {
                    int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u deze order wilt toevoegen?", "Toevoegen order", JOptionPane.YES_NO_OPTION);
                    if (keuze == JOptionPane.YES_OPTION) {
                        addToDb();
                        dispose();
                    }
                }
            }

        }

    }
}
