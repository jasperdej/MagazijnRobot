import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class EditOrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private boolean orderExists = true;
    private int rows = 7;
    private JLabel jlTitelNew, jlTitelEdit, jlCustomerId, jlArticleId, jlArticleAmount;
    private JComboBox<Integer> jcbCustomerId;
    private JPanel panel, topPanel, middlePanel, topMiddlePanel, bottomMiddlePanel, jpOrderLine, jpOrderLineLeft, jpOrderLineRight, bottomPanel, buttonPanel;
    private GridLayout grBottomMiddlePanel = new GridLayout(rows,1);
    private JButton jbAddArticle, jbBevestigen, jbAnnuleren;
    private String customerId;
    private ArrayList<JTextField> jtfOrderLines;
    private ArrayList<JComboBox> jcbOrderLines;
    private ArrayList<JButton> jbOrderLines;
    private ArrayList<JPanel> panels;
    private int[] originalOrderLines, originalQuantities;
    private Integer[] customers;
    private String[] articles;
    private JScrollPane sp;

    public EditOrderDialog(JFrame jFrame, int orderId){
        super(jFrame,true);
        this.orderId = orderId;
        setExistingOrderFromDb();
        setArticleList();
        setCustomerList();
        setArrayLists();
        createDialog();
    }

    public EditOrderDialog(JFrame jFrame){
        super(jFrame,true);
        orderExists = false;
        setNewOrderIdFromDb();
        setArticleList();
        setCustomerList();
        setArrayLists();
        createDialog();
    }

    public void createDialog(){
        setSize(800,400);
        setResizable(false);
        setTitle("Order ID: " + orderId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        //initialize all panels
        panel = (JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());
        topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new GridLayout(0,2));
        bottomMiddlePanel = new JPanel();
        bottomMiddlePanel.setLayout(grBottomMiddlePanel);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        //set topPanel
        jlTitelNew = new JLabel("Order aanmaken");
        jlTitelNew.setFont(new Font("Arial",Font.BOLD,30));
        jlTitelEdit = new JLabel("Order Gegevens wijzigen");
        jlTitelEdit.setFont(new Font("Arial",Font.BOLD,30));
        if(orderExists){
            topPanel.add(jlTitelEdit);
        } else {
            topPanel.add(jlTitelNew);
        }

        //setInput fields
        jlCustomerId = new JLabel("Klant ID: ");
        topMiddlePanel.add(jlCustomerId);
        jcbCustomerId = new JComboBox<>(customers);
        if(orderExists){
            jcbCustomerId.setEnabled(false);
        }
        topMiddlePanel.add(jcbCustomerId);

        jlArticleId = new JLabel("Artikel ID: ");
        topMiddlePanel.add(jlArticleId);
        jlArticleAmount = new JLabel("Aantal: ");
        topMiddlePanel.add(jlArticleAmount);

        //add orderLines to panel
        fillPanel();

        sp = new JScrollPane(bottomMiddlePanel);

        middlePanel.add(topMiddlePanel,BorderLayout.PAGE_START);
        middlePanel.add(sp,BorderLayout.CENTER);

        //add Buttons
        jbAddArticle = new JButton("Artikel toevoegen");
        jbAddArticle.addActionListener(this);
        bottomPanel.add(jbAddArticle, BorderLayout.PAGE_START);

        jbBevestigen = new JButton("Bevestigen");
        jbBevestigen.addActionListener(this);
        buttonPanel.add(jbBevestigen);

        jbAnnuleren = new JButton("Annuleren");
        jbAnnuleren.addActionListener(this);
        buttonPanel.add(jbAnnuleren);

        bottomPanel.add(buttonPanel, BorderLayout.PAGE_END);

        //add Panels to frame
        add(topPanel, BorderLayout.PAGE_START);
        add(middlePanel);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void setArrayLists(){
        jcbOrderLines = new ArrayList<>();
        jtfOrderLines = new ArrayList<>();
        jbOrderLines = new ArrayList<>();
        panels = new ArrayList<>();
        if(orderExists){
            getOrderLinesFromDb();
        } else {
            jcbOrderLines.add(new JComboBox<>(articles));
            jtfOrderLines.add(new JTextField(3));
            jbOrderLines.add(new JButton("Verwijder"));
        }
    }

    public void fillPanel(){
        panels.clear();
        for(int i = 0; i < jtfOrderLines.size(); i++){
            jpOrderLine = new JPanel();
            jpOrderLine.setLayout(new BorderLayout());
            jpOrderLineRight = new JPanel();
            jpOrderLineRight.setLayout(new BorderLayout());

            jpOrderLineRight.add(jtfOrderLines.get(i),BorderLayout.CENTER);
            jpOrderLineRight.add(jbOrderLines.get(i),BorderLayout.LINE_END);

            jpOrderLine.add(jcbOrderLines.get(i),BorderLayout.CENTER);
            jpOrderLine.add(jpOrderLineRight,BorderLayout.LINE_END);
            panels.add(jpOrderLine);
            bottomMiddlePanel.add(panels.get(i));
        }
        //System.out.println(jcbOrderLines.size() + " " + jtfOrderLines.size() + " " + jbOrderLines.size());
    }

    public void addToPanel(){
        jcbOrderLines.add(new JComboBox<>(articles));
        jcbOrderLines.get(jcbOrderLines.size() - 1).addActionListener(this);
        jtfOrderLines.add(new JTextField("0",3));
        jbOrderLines.add(new JButton("Verwijder"));
        jbOrderLines.get(jbOrderLines.size() - 1).addActionListener(this);

        setRows();

        jpOrderLine = new JPanel();
        jpOrderLine.setLayout(new BorderLayout());
        jpOrderLineRight = new JPanel();
        jpOrderLineRight.setLayout(new BorderLayout());

        jpOrderLineRight.add(jtfOrderLines.get(jbOrderLines.size() - 1),BorderLayout.CENTER);
        jpOrderLineRight.add(jbOrderLines.get(jbOrderLines.size() - 1),BorderLayout.LINE_END);

        jpOrderLine.add(jcbOrderLines.get(jbOrderLines.size() - 1),BorderLayout.CENTER);
        jpOrderLine.add(jpOrderLineRight,BorderLayout.LINE_END);
        panels.add(jpOrderLine);
        bottomMiddlePanel.add(panels.get(panels.size() - 1));

        revalidate();
        repaint();
        //System.out.println(jcbOrderLines.size() + " " + jtfOrderLines.size() + " " + jbOrderLines.size());
    }

    public void removeFromPanel(int i){
        bottomMiddlePanel.remove(panels.get(i));
        jcbOrderLines.remove(i);
        jtfOrderLines.remove(i);
        jbOrderLines.remove(i);
        panels.remove(i);
        setRows();
        revalidate();
        repaint();
        //System.out.println(jcbOrderLines.size() + " " + jtfOrderLines.size() + " " + jbOrderLines.size());
    }

    public void addToDb(){
        int orderLineId = getMaxOrderLineId();
        if (orderLineId == 0){
            JOptionPane.showMessageDialog(this,"Error, order niet toegevoegd.");
        } else {
            DbConn dbConn = new DbConn();
            DbConn.dbConnect();
            dbConn.updateDb("INSERT INTO Orders (OrderID, CustomerID, SalespersonPersonID, ContactPersonID, OrderDate, ExpectedDeliveryDate, IsUndersupplyBackordered, LastEditedBy, LastEditedWhen, Status) VALUES (" + orderId + ", " + jcbCustomerId.getSelectedItem() + ", 1, 1, '" + getDate() + "', '" + getDate() + "', 0, 1, '" + getDateTime() + "', 'wachten op actie')");

            for(int i = 0; i < jcbOrderLines.size(); i++){
                orderLineId++;
                dbConn.updateDb("INSERT INTO OrderLines (OrderLineID, OrderID, StockItemID, Description, PackageTypeID, Quantity, TaxRate, PickedQuantity, LastEditedBy, LastEditedWhen) VALUES (" + orderLineId + ", " + orderId + ", " + jcbOrderLines.get(i).getSelectedIndex() + ", '-', 1," + Integer.parseInt(jtfOrderLines.get(i).getText()) + ", 15.0, 0, 1, '" + getDateTime() + "')");
                orderLineId++;
            }
            dbConn.killStatement();
            DbConn.dbKill();
        }
    }

    public void editDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE Orders SET CustomerID = " + customerId + " WHERE OrderID = " + orderId);
        dbConn.killStatement();
        DbConn.dbKill();
    }

    public void setExistingOrderFromDb(){
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

    public void setNewOrderIdFromDb(){
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

    public int getMaxOrderLineId(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(OrderLineID) FROM OrderLines");

        try{
            rs.first();

            return rs.getInt("MAX(OrderLineId)");
        }catch(SQLException sqle){
            System.out.println(sqle);
            return 0;
        }finally{
            dbConn.killStatement();
        }
    }

    public void getOrderLinesFromDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT ol.StockItemID, si.StockItemName, ol.Quantity FROM OrderLines ol JOIN StockItems si ON ol.StockItemID = si.StockItemID WHERE OrderID = " + orderId);

        int amountOfRows = 0;

        try{
            rs.last();
            amountOfRows = rs.getRow();
            rs.first();

            originalOrderLines = new int[amountOfRows];
            originalQuantities = new int[amountOfRows];

            for(int i = 0; i < amountOfRows; i++){
                originalOrderLines[i] = rs.getInt("ol.StockItemID");
                originalQuantities[i] = rs.getInt("ol.Quantity");
                jcbOrderLines.add(new JComboBox<>(articles));
                jcbOrderLines.get(i).setSelectedIndex(rs.getInt("ol.StockItemID"));
                jcbOrderLines.get(i).addActionListener(this);
                jtfOrderLines.add(new JTextField(rs.getString("ol.Quantity"),3));
                jbOrderLines.add(new JButton("Verwijder"));
                jbOrderLines.get(i).addActionListener(this);
                rs.next();
            }
            setRows();
            //System.out.println(jcbOrderLines.size() + " " + jtfOrderLines.size() + " " + jbOrderLines.size());
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
            amountOfRows = rs.getRow() + 1;
            rs.first();

            articles = new String[amountOfRows];
            articles[0] = "Kies een artikel";

            for(int i = 1; i < amountOfRows; i++){
                articles[i] = rs.getInt("StockItemID") + "  " + rs.getString("StockItemName");
                rs.next();
            }
        } catch(SQLException sqle){
            System.out.println(sqle);
        }
    }

    public void setCustomerList(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT UserID FROM Users ORDER BY UserID");

        int amountOfRows = 0;

        try{
            rs.last();
            amountOfRows = rs.getRow();
            rs.first();

            customers = new Integer[amountOfRows];

            for(int i = 0; i < amountOfRows; i++){
                customers[i] = rs.getInt("UserID");
                rs.next();
            }
        } catch(SQLException sqle){
            System.out.println(sqle);
        }
    }

    public void setRows(){
        if(jbOrderLines.size() > 7){
            rows = jbOrderLines.size();
        } else {
            rows = 7;
        }
        grBottomMiddlePanel.setRows(rows);
    }

    public String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource() == jbAnnuleren) {
            dispose();
        } else if (e.getSource() == jbBevestigen) {
            boolean correctInput = true;
            for(JTextField jtf : jtfOrderLines){
                try{
                    int x = Integer.parseInt(jtf.getText());
                    if(x <= 0){
                        correctInput = false;
                    }
                } catch (NumberFormatException nfe){
                    correctInput = false;
                }
            }
            for(JComboBox jcb : jcbOrderLines){
                if(jcb.getSelectedIndex() == 0){
                    correctInput = false;
                }
            }
            if(!correctInput){
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
        } else if(e.getSource() == jbAddArticle){
            addToPanel();
        } else {
            int deleteSourceButton = -1;
            for(int i = 0; i < jbOrderLines.size(); i++){
                if(e.getSource() == jbOrderLines.get(i)){
                    deleteSourceButton = i;
                }
            }
            if(deleteSourceButton != -1){
                removeFromPanel(deleteSourceButton);
            }
        }
        for(JComboBox jcb : jcbOrderLines){
            System.out.println(jcb.getSelectedIndex());
        }
        getDate();
    }
}