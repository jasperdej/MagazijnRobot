import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class InventoryScreen extends JFrame implements ActionListener {
    private Object[][] allArticles;
    private String[] columnNames = {"Naam", "ItemID", "Gewicht (in kg)", "Aantal", "Gereserveerd"};
    private JTable jTable;
    private ScreenManager screenManager;

    private EditArticleDialog createArticleDialog, editArticleDialog;
    private JScrollPane sp;

    //buttons for swithing screens.
    private JButton robotScreen = new JButton("Robot overzicht");
    private JButton orderScreen = new JButton("Order overzicht");
    private JButton inventoryScreen = new JButton("Voorraad overzicht"){
        //sets button color.
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

    private JButton addArticle = new JButton("Artikel toevoegen");
    private JButton editArticle = new JButton("Artikel bewerken");

    private boolean isEdited = false;

    public InventoryScreen() {
        createScreen();
        System.out.println("InventoryScreen ready!");
    }

    //creates screen
    private void createScreen() {
        //fills array with articles from database.
        fillAllArticles();

        //gridbaglayout for buttons and JTable.
        setLayout(new BorderLayout());

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        //makes the frame completely fullscreen.
        setUndecorated(true);

        //buttonpanel for buttons. is set to top of screen.
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1,2));
        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

        //adds buttons to buttonpanel.
        buttonPanel1.add(robotScreen);
        buttonPanel1.add(orderScreen);
        buttonPanel1.add(inventoryScreen);

        buttonPanel2.add(addArticle);
        buttonPanel2.add(editArticle);

        headerPanel.add(buttonPanel1);
        headerPanel.add(buttonPanel2);

        add(headerPanel,BorderLayout.PAGE_START);

        //JTable with results from database.
        jTable = new JTable(allArticles, columnNames){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable.getTableHeader().setReorderingAllowed(false);

        //scrollpane adds scroll functionality to jtable. adds scrollpane with jtable to screen.
        sp = new JScrollPane(jTable);
        add(sp);

        //adds actionlisteners for buttons.
        robotScreen.addActionListener(this);
        orderScreen.addActionListener(this);
        inventoryScreen.addActionListener(this);
        addArticle.addActionListener(this);
        editArticle.addActionListener(this);

    }


    //put all articles from database in allArticles.
    private void fillAllArticles() {
        //get results from database. resultset contains all results from query.
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            fillAllArticles();
        }  else {
            Start.dbDoneLoading = false;
        }

        DbConn dbConn = new DbConn();
        dbConn.dbConnect(); //"select si.StockItemName, si.StockItemID, si.TypicalWeightPerUnit, (SELECT sum(QuantityOnHand) FROM stockitemholdings sih WHERE sih.StockItemID = si.StockItemID), sum(ol.Quantity) from stockitems si join Orderlines ol on ol.Stockitemid = si.Stockitemid  group by ol.Stockitemid;"


        ResultSet rs1 = dbConn.getResultSetFromDb("SELECT si.StockItemName, si.StockItemID, si.TypicalWeightPerUnit, SUM(sih.QuantityOnHand) FROM StockItems si JOIN StockItemHoldings sih ON si.StockItemID = sih.StockItemID GROUP BY StockItemID;");
        ResultSet rs2 = dbConn.getResultSetFromDb("SELECT Quantity, StockItemID FROM Orderlines ORDER BY StockItemID;");

        //int for measuring the amount of rows in the resultset.
        int amountOfRows = 0;

        try{
              rs1.last();
              amountOfRows = rs1.getRow();
              rs1.first();
              rs2.first();

            //initiating two-dimensional array with correct amount of rows.
            //the amount of rows is dependant on the amount of results returned from the database.
            allArticles = new Object[amountOfRows][5];

            //adding results from resultset to two-dimensional array for JTable.
            for (int i = 0; i < amountOfRows; i++) {
                allArticles[i][0] = rs1.getString("si.StockItemName");
                allArticles[i][1] = rs1.getInt("si.StockItemID");
                allArticles[i][2] = rs1.getDouble("si.TypicalWeightPerUnit");
                allArticles[i][3] = rs1.getInt("SUM(sih.QuantityOnHand)");
                int id = rs1.getInt("si.StockItemID");
                boolean foundRelevantRecords = false;
                int quantityOnHand = 0;
                while(!foundRelevantRecords && rs2.next()) {
                    if(rs2.getInt("StockItemID") < id){
//                        rs2.next();
                    } else if(rs2.getInt("StockItemID") == id){
                        quantityOnHand += rs2.getInt("Quantity");
//                        rs2.next();
                    } else {
                        foundRelevantRecords = true;
                    }
                }
                allArticles[i][4] = quantityOnHand;
                rs1.next();
            }

        } catch (Exception e) {
            Start.dbDoneLoading = true;
            fillAllArticles();
        } finally {
            dbConn.killStatement();
            Start.dbDoneLoading = true;
        }

    }

    //checks if article exists
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

    //gets results from database and updates current values on screen.
    public void refreshInventoryScreen() {
        remove(sp);
        editTable();
        add(sp);
        this.revalidate();
        this.repaint();
    }

    public void editTable(){
        fillAllArticles();
        jTable = new JTable(allArticles, columnNames){
            public boolean isCellEditable(int row, int column) {
                    return false;
                }
        };
        jTable.getTableHeader().setReorderingAllowed(false);
        sp = new JScrollPane(jTable);
        isEdited = false;
    }

    public void setIsEdited(boolean value){
        isEdited = value;
    }

    public boolean getIsEdited(){
        return isEdited;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //buttons for switching between screens.
        if (e.getSource() == robotScreen) {
            System.out.println("robot");
            screenManager.buttonPressed("RobotScreen");
        } else if (e.getSource() == orderScreen) {
            screenManager.buttonPressed("OrderScreen");
        } else if (e.getSource() == inventoryScreen) {
            screenManager.buttonPressed("InventoryScreen");

        } else if (e.getSource() == addArticle){
            createArticleDialog = new EditArticleDialog(this);
            if(createArticleDialog.getIsOk()){
                isEdited = true;
            }
        } else if (e.getSource() == editArticle){
            String artikelid = JOptionPane.showInputDialog(this,"Voer artikel nummer in: ");
            if(!artikelid.equals(null)) {
                if (checkID("SELECT StockItemID FROM StockItems WHERE StockItemID = ", artikelid)) {
                    editArticleDialog = new EditArticleDialog(this, Integer.parseInt(artikelid));
                    if(editArticleDialog.getIsOk()){
                        isEdited = true;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Ongeldige invoer.");
                }
            }
        }
    }
}
