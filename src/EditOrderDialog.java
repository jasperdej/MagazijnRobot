import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditOrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private int customerId;
    private boolean orderExists = true;
    private int rows = 6;
    private JLabel jlTitelNew, jlTitelEdit, jlCustomerId, jlArticleId, jlArticleAmount;
    private JComboBox<Integer> jcbCustomerId;
    private JPanel panel, topPanel, middlePanel, topMiddlePanel, bottomMiddlePanel, bottomPanel, buttonPanel;
    private GridLayout grBottomMiddlePanel = new GridLayout(rows,1);
    private JButton jbAddArticle, jbBevestigen, jbAnnuleren;
    private ArrayList<OrderLinePanel> panels;
    private Integer[] customers;
    private int[][] originalOrderLines;
    private JScrollPane sp;
    private int maxOrderLineId;
    private boolean isOk;

    //constructor for editing order
    public EditOrderDialog(JFrame jFrame, int orderId){
        super(jFrame,true);
        this.orderId = orderId;
        setExistingOrderFromDb();
        setLists();
        createDialog();
    }

    //constructor for creating order
    public EditOrderDialog(JFrame jFrame){
        super(jFrame,true);
        orderExists = false;
        setNewOrderIdFromDb();
        setLists();
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
        middlePanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new GridLayout(0,2));
        bottomMiddlePanel = new JPanel();
        bottomMiddlePanel.setLayout(grBottomMiddlePanel);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(0,10));
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
            jcbCustomerId.setSelectedIndex(customerId-1);
            jcbCustomerId.setEnabled(false);
        }
        topMiddlePanel.add(jcbCustomerId);

        jlArticleId = new JLabel("Artikelen: ");
        topMiddlePanel.add(jlArticleId);

        //add orderLines to panel
        showPanels();

        sp = new JScrollPane(bottomMiddlePanel);

        middlePanel.add(topMiddlePanel,BorderLayout.PAGE_START);
        middlePanel.add(sp,BorderLayout.CENTER);

        //add Buttons
        jbAddArticle = new JButton("Artikel toevoegen");
        jbAddArticle.addActionListener(this);
        bottomPanel.add(jbAddArticle, BorderLayout.LINE_END);

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

    //initializing lists
    public void setLists(){
        setCustomerList();
        setArticleList();
        setMaxOrderLineId();
        panels = new ArrayList<>();
        if(orderExists){
            getOrderLinesFromDb();
        } else {
            panels.add(new OrderLinePanel(this));
        }
    }

    //shows panels from the panels arraylist
    public void showPanels(){
        for(int i = 0; i < panels.size(); i++){
            bottomMiddlePanel.add(panels.get(i).getJpOrderLine());
        }
    }

    //adds a new panel to the screen
    public void addPanel(){
        panels.add(new OrderLinePanel(this));
        setRows();
        bottomMiddlePanel.add(panels.get(panels.size()-1).getJpOrderLine());
        revalidate();
        repaint();
    }

    //removes a panel from the screen
    public void removePanel(int index){
        bottomMiddlePanel.remove(panels.get(index).getJpOrderLine());
        panels.remove(index);
        setRows();
        revalidate();
        repaint();
    }

    //adds a new order to db
    public void addToDb(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            addToDb();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("INSERT INTO Orders (OrderID, CustomerID, SalespersonPersonID, ContactPersonID, OrderDate, ExpectedDeliveryDate, IsUndersupplyBackordered, LastEditedBy, LastEditedWhen, Status) VALUES (" + orderId + ", " + jcbCustomerId.getSelectedItem() + ", 1, 1, '" + getDate() + "', '" + getDate() + "', 0, 1, '" + getDateTime() + "', 'wachten op actie')");

        for(int i = 0; i < panels.size(); i++){
            addRecordToDb(dbConn,i);
        }

        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
        isOk = true;
    }

    //checks which orderlines need to be added, removed and updated in db
    public void editDb(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            editDb();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        for(int i = 0; i < originalOrderLines.length; i++) {
            boolean found = false;
            for (int j = 0; j < panels.size(); j++) {
                if (panels.get(j).getJcbOrderLine().getSelectedIndex() == originalOrderLines[i][1]) {
                    found = true;
                    panels.get(j).setOriginalOrderLine(originalOrderLines[i][0]);
                    if (Integer.parseInt(panels.get(j).getJtfOrderLine().getText()) != originalOrderLines[i][2]) {
                        updateRecordInDb(dbConn,j);
                    }
                }
            }
            if (!found) {
                deleteRecordFromDb(dbConn,i);
            }
        }
        for(int i = 0; i < panels.size(); i++){
            if(panels.get(i).getOriginalOrderLine() == -1){
                addRecordToDb(dbConn,i);
            }
        }
        dbConn.killStatement();
        Start.dbDoneLoading = true;
        isOk = true;
    }

    //updates a record in db
    public void updateRecordInDb(DbConn dbConn, int panelIndex){
        dbConn.updateDb("UPDATE OrderLines SET Quantity = " + Integer.parseInt(panels.get(panelIndex).getJtfOrderLine().getText()) + " WHERE OrderLineID = " + panels.get(panelIndex).getOriginalOrderLine());
    }

    //adds a record to db
    public void addRecordToDb(DbConn dbConn, int panelIndex){
        maxOrderLineId++;
        dbConn.updateDb("INSERT INTO OrderLines (OrderLineID, OrderID, StockItemID, Description, PackageTypeID, Quantity, TaxRate, PickedQuantity, LastEditedBy, LastEditedWhen) VALUES (" + maxOrderLineId + ", " + orderId + ", " + panels.get(panelIndex).getJcbOrderLine().getSelectedIndex() + ", '-', 1, " + Integer.parseInt(panels.get(panelIndex).getJtfOrderLine().getText()) + ", 15.0, 0, 1,'" + getDateTime() + "')");
    }

    //deletes a record from db
    public void deleteRecordFromDb(DbConn dbConn, int originalIndex){
        dbConn.updateDb("DELETE FROM OrderLines WHERE OrderLineID = " + originalOrderLines[originalIndex][0]);
    }

    //retrieves customerID from db when editing an order
    public void setExistingOrderFromDb(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setExistingOrderFromDb();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT CustomerID FROM Orders WHERE OrderID = " + orderId);

        try{
            rs.first();
            customerId = rs.getInt("CustomerID");
        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in EditOrderDialog.java in methode setExistingOrderFromDb");

        } finally {
            dbConn.killStatement();
            Start.dbDoneLoading = true;
        }
    }

    //sets a new orderID when creating an order
    public void setNewOrderIdFromDb(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setNewOrderIdFromDb();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(OrderID) FROM Orders;");

        try{
            rs.first();
            orderId = rs.getInt("MAX(OrderID)") + 1;
        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in EditOrderDialog.java in methode setNewOrderIdFromDb");
        } finally {
            dbConn.killStatement();
            Start.dbDoneLoading = true;
        }
    }

    //retrieves a new orderLineID when adding orderlines to db
    public void setMaxOrderLineId(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setMaxOrderLineId();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(OrderLineID) FROM OrderLines");

        try{
            rs.first();
            maxOrderLineId = rs.getInt("MAX(OrderLineId)");
        }catch(SQLException sqle){
            System.out.println("Er is een SQL fout opgetreden in EditOrderDialog.java in methode getMaxOrderLineId");
            maxOrderLineId = -1;
        }finally{
            dbConn.killStatement();
            Start.dbDoneLoading = true;
        }
    }

    //retrieves orderlines from db when editing an order
    public void getOrderLinesFromDb(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            getOrderLinesFromDb();
        } else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT ol.OrderLineID, ol.StockItemID, si.StockItemName, ol.Quantity FROM OrderLines ol JOIN StockItems si ON ol.StockItemID = si.StockItemID WHERE OrderID = " + orderId);

        int amountOfRows = 0;

        try{
            rs.last();
            amountOfRows = rs.getRow();
            rs.first();

            originalOrderLines = new int[amountOfRows][3];

            for(int i = 0; i < amountOfRows; i++){
                panels.add(new OrderLinePanel(this,rs.getInt("ol.StockItemID"),rs.getInt("ol.Quantity")));
                originalOrderLines[i][0] = rs.getInt("ol.OrderLineID");
                originalOrderLines[i][1] = rs.getInt("ol.StockItemID");
                originalOrderLines[i][2] = rs.getInt("ol.Quantity");
                rs.next();
            }
            setRows();
            } catch(SQLException sqle){
            System.out.println("Er is een SQL fout opgetreden in EditOrderDialog.java in methode getOrderLinesFromDb");
        } finally {
            dbConn.killStatement();
            Start.dbDoneLoading = true;
        }
    }

    //checks if an article someone is trying to select isn't in the order already
    public void checkIfSelected(int index){
        for(int i = 0; i < panels.size(); i++){
            if(i != index && panels.get(i).getJcbOrderLine().getSelectedIndex() == panels.get(index).getJcbOrderLine().getSelectedIndex()){
                panels.get(index).getJcbOrderLine().setSelectedIndex(0);
            }
        }
    }

    //retrieves a list with all articles for the combobox
    public void setArticleList() {
        OrderLinePanel.fillArticles();
    }

    //retrieves a list with all customers for the combobox
    public void setCustomerList(){
        if (!Start.dbDoneLoading){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setCustomerList();
        } else {
            Start.dbDoneLoading = false;
        }
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
            System.out.println("Er is een SQL fout opgetreden in EditOrderDialog.java in methode setCustomerList");
        } finally {
            dbConn.killStatement();
            Start.dbDoneLoading = true;
        }
    }

    //calculates amount of orderlines, the gridlayout needs a minimum of 6 lines to not mess up, so will change to 6 if lesser than 6
    public void setRows(){
        if(panels.size() > 6){
            rows = panels.size();
        } else {
            rows = 6;
        }
        grBottomMiddlePanel.setRows(rows);
    }

    //checks if an order has been edited when trying to save changes
    public boolean orderEdited(){
        System.out.println(panels.size());
        System.out.println(originalOrderLines.length);
        if(panels.size() != originalOrderLines.length){
            return true;
        }
        for(int j = 0; j < panels.size(); j++){
            System.out.println("panel " + j);
            boolean found = false;
            for(int i = 0; i < originalOrderLines.length; i++){
                if(panels.get(j).getJcbOrderLine().getSelectedIndex() == originalOrderLines[i][1]){
                    System.out.println("check1");
                    if(Integer.parseInt(panels.get(j).getJtfOrderLine().getText()) == originalOrderLines[i][2]){
                        System.out.println("check2");
                        found = true;
                    }
                }
            }
            System.out.println(found);
            if(found == false){
                System.out.println("check3");
                return true;
            }
        }
        return false;
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

    public boolean getIsOk(){
        return isOk;
    }

    public void actionPerformed(ActionEvent e){
        //"Annuleren" button
        if (e.getSource() == jbAnnuleren) {
            dispose();
        //"Bevestigen" button
        } else if (e.getSource() == jbBevestigen) {
            boolean correctInput = true;
            //checks if an order has at least 1 orderline
            if(panels.size() == 0){
                correctInput = false;
            } else {
                //checks if input is valid
                for(OrderLinePanel o : panels){
                    try{
                        int x = Integer.parseInt(o.getJtfOrderLine().getText());
                        if(x <= 0 || o.getJcbOrderLine().getSelectedIndex() == 0){
                            correctInput = false;
                        }
                    } catch (NumberFormatException nfe){
                        correctInput = false;
                    }
                }
            }
            if(!correctInput){
                JOptionPane.showMessageDialog(this,"Ongeldige invoer.");
            } else {
                if (orderExists == true) {
                    if(orderEdited()){
                        int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u deze gegevens wilt wijzigen?", "Wijzigen gegevens", JOptionPane.YES_NO_OPTION);
                        if (keuze == JOptionPane.YES_OPTION) {
                            editDb();
                            setVisible(false);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,"Geen wijzigingen gedetecteerd.");
                    }
                } else {
                    int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u deze order wilt toevoegen?", "Toevoegen order", JOptionPane.YES_NO_OPTION);
                    if (keuze == JOptionPane.YES_OPTION) {
                        addToDb();
                        setVisible(false);
                    }
                }
            }
        //adds a new orderline
        } else if(e.getSource() == jbAddArticle){
            addPanel();
        //removes an orderline
        } else {
            for(int i = 0; i < panels.size(); i++){
                if(e.getSource() == panels.get(i).getJbOrderLine()){
                    removePanel(i);
                } else if(e.getSource() == panels.get(i).getJcbOrderLine()){
                    checkIfSelected(i);
                }
            }
        }
    }
}