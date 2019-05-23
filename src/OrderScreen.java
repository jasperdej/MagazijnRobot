import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;

public class OrderScreen extends JFrame implements MouseListener, ActionListener {

    private JPanel headerPanel, buttonPanel1, buttonPanel2;

    //variables for JTable.
    private Object[][] allOrders;
    private String[] columnNames = {"OrderID","Aantal","Gewicht (in kg)","Klantnummer","Status"};
    private JTable jTable;

    //variables for correctly displaying information on screen.
    private ScreenManager screenManager;

    private JScrollPane sp;
    private EditOrderDialog createOrderDialog, editOrderDialog;
    private boolean isEdited = false;

    //buttons for switching between screens.
    private JButton robotScreen = new JButton("Robot overzicht");
    private JButton orderScreen = new JButton("Order overzicht"){
        //sets colors for buttons.
        protected void paintComponent(Graphics g){
            setContentAreaFilled(false);
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0),
                    new Color(141, 177, 216),
                    new Point(0, getHeight()/3),
                    new Color(230, 230, 230)));
            g2.fillRect(0, 0, getWidth(), getHeight()/3);
            g2.setPaint(new GradientPaint(
                    new Point(0, getHeight()/3),
                    new Color(230, 230, 230),
                    new Point(0, getHeight()),
                    new Color(141, 177, 216)));
            g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }
    };
    private JButton inventoryScreen = new JButton("Voorraad overzicht");

    private JButton addOrder = new JButton("Order toevoegen");
    private JButton editOrder = new JButton("Order bewerken");
    private JButton addPersoon = new JButton("Persoon toevoegen");
    private JButton editPersoon = new JButton("Persoon bewerken");

    //creates screen. setVisible is false. prints Orderscreen Ready! when screenbuilding is complete. might take some time because of database.
    public OrderScreen() {
        createScreen();
        System.out.println("OrderScreen ready!");
    }

    //creates screen.
    private void createScreen() {
        //gets information from database and adds them to two-dimensional array.
        fillAllOrders();
        setTitle("Order overzicht");

        //gridbaglayout for correctly displaying buttons and jtable.
        setLayout(new BorderLayout());

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the frame completely fullscreen.
        setUndecorated(true);

        //buttonpanel to which buttons are added. is set to display at top of screen.
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 2));
        buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

        //adds buttons to buttonpanel.
        buttonPanel1.add(robotScreen);
        buttonPanel1.add(orderScreen);
        buttonPanel1.add(inventoryScreen);

        buttonPanel2.add(addPersoon);
        buttonPanel2.add(editPersoon);
        buttonPanel2.add(addOrder);
        buttonPanel2.add(editOrder);

        headerPanel.add(buttonPanel1);
        headerPanel.add(buttonPanel2);

        add(headerPanel,BorderLayout.PAGE_START);

        //jtable contains results from database.
        jTable = new JTable(allOrders, columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //adds mouselistener for opening extra information dialog.
        jTable.addMouseListener(this);
        jTable.getTableHeader().setReorderingAllowed(false);

        //jtable is added to scrollpane. scrollpane is responsible for being able to scroll trough jtable.
        sp = new JScrollPane(jTable);

        //adds scrollpane.
        add(sp);

        //adds actionlistener for buttons.
        robotScreen.addActionListener(this);
        orderScreen.addActionListener(this);
        inventoryScreen.addActionListener(this);
        addOrder.addActionListener(this);
        editOrder.addActionListener(this);
        addPersoon.addActionListener(this);
        editPersoon.addActionListener(this);

    }

    //fills two-dimensional array with results from database.
    private void fillAllOrders() {
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            fillAllOrders();
        } else {
            Start.dbDoneLoading = false;
        }

        //get results from database.
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT o.OrderId, SUM(ol.quantity), SUM(ol.quantity * s.TypicalWeightPerUnit), o.CustomerId, o.status FROM orders o JOIN orderlines ol ON o.OrderId = ol.OrderId JOIN stockitems s ON ol.StockItemID = s.StockItemID GROUP BY o.OrderId;");

        //int for measuring the amount of rows in the resultset.
        int amountOfRows = 0;

        //add results to two-dimensional array.
        try {
            rs.last();
            amountOfRows = rs.getRow();
            rs.first();

            //initiating two-dimensional array with correct amount of rows.
            //the amount of rows is dependant on the amount of results returned from the database.
            allOrders = new Object[amountOfRows][5];

            //adding results form resultset to two-dimensional array for JTable.
            for (int i = 0; i < amountOfRows; i++) {
                allOrders[i][0] = rs.getString("o.OrderId");
                allOrders[i][1] = rs.getString("SUM(ol.quantity)");
                allOrders[i][2] = rs.getString("SUM(ol.quantity * s.TypicalWeightPerUnit)");
                allOrders[i][3] = rs.getString("o.CustomerId");
                allOrders[i][4] = rs.getString("o.status");
                rs.next();
            }
        } catch (Exception e) {
            Start.dbDoneLoading = true;
            fillAllOrders();
        } finally {
            dbConn.killStatement();
            DbConn.dbKill();
            Start.dbDoneLoading = true;
        }
    }

    //gets results from database and updates current values on screen.
    public void refreshAllOrders() {
        remove(sp);
        editTable();
        add(sp);
        this.revalidate();
        this.repaint();
    }

    //refreshing of orderscreen.
    public void editTable(){
        fillAllOrders();
        jTable = new JTable(allOrders, columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        //adds mouselistener for opening extra information dialog.
        jTable.addMouseListener(this);
        jTable.getTableHeader().setReorderingAllowed(false);

        sp = new JScrollPane(jTable);

        isEdited = false;
    }

    //would have been used for updating screens after finishing order.
    public void setIsEdited(boolean value){
        isEdited = value;
    }

    public boolean getIsEdited(){
        return isEdited;
    }

    //mouseClicked opens dialog after user clicked on jtable row.
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println(e.getSource());
        System.out.println(jTable);
        if(e.getSource() == jTable){
            //gets row clicked.
            int row = jTable.getSelectedRow();
            System.out.println(row);
            int orderId;

            //gets orderId of row clicked.
            try {
                orderId = Integer.parseInt((String)jTable.getValueAt(row,0));
                //creates dialog
                new OrderDialog(this,orderId);
            } catch (ClassCastException cce) {
                System.out.println("Er is een ClassCastException opgetreden in OrderScreen.java in methode mouseClicked");
            }
        }
    }

    //checks if Order exists in database.
    public boolean checkID(String query, String id){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb(query + id);

        try {
            if(rs.next()){
                return true;
            }
        } catch (Exception e) {
            System.out.println("Er is een fout opgetreden in OrderScreen.java in methode checkID");
        } finally {
            dbConn.killStatement();
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //buttons for switching between screens.
        if (e.getSource() == robotScreen) {
            screenManager.buttonPressed("RobotScreen");
        } else if (e.getSource() == orderScreen) {
            screenManager.buttonPressed("OrderScreen");
        } else if (e.getSource() == inventoryScreen) {
            screenManager.buttonPressed("InventoryScreen");
        }
        else if (e.getSource() == addOrder) {
            createOrderDialog = new EditOrderDialog(this);
            if(createOrderDialog.getIsOk()){
                isEdited = true;
            }
        } else if (e.getSource() == editOrder) {
            String orderid = JOptionPane.showInputDialog(this,"Voer order nummer in: ");
            if(orderid != null){
                if (checkID("SELECT OrderID FROM Orders WHERE Status = 'wachten op actie' AND OrderID = ", orderid)) {
                    editOrderDialog = new EditOrderDialog(this, Integer.parseInt(orderid));
                    if(editOrderDialog.getIsOk()){
                        isEdited = true;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Ongeldige invoer.");
                }
            }
        } else if (e.getSource() == addPersoon) {
            new EditPersonDialog(this);
        } else if (e.getSource() == editPersoon) {
            String persoonid = JOptionPane.showInputDialog(this, "Voer klant nummer in: ");
            if(persoonid != null) {
                if (checkID("SELECT UserID FROM USERS WHERE UserID = ", persoonid)) {
                    new EditPersonDialog(this, Integer.parseInt(persoonid));
                } else {
                    JOptionPane.showMessageDialog(this, "Ongeldige invoer.");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e){

    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }


}



