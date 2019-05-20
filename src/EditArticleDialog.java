import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditArticleDialog extends JDialog implements ActionListener {

    private InventoryScreen screen;
    private int articleId;
    private boolean articleExists = true;
    private JPanel panel, topPanel, middlePanel, topMiddlePanel, locatiePanel, locatieXPanel, locatieYPanel, bottomMiddlePanel, bottomPanel, buttonPanel;
    private JButton jbBevestigen, jbAnnuleren;
    private JLabel jlTitelNew, jlTitelEdit, jlProductNaam, jlProductAantal, jlProductPrijs, jlProductGewicht, jlProductVoorraad, jlProductLocatie, jlProductLocatieX, jlProductLocatieY, jlProductBeschrijving, jlVerplicht;
    private JTextField jtfProductNaam, jtfProductAantal, jtfProductPrijs, jtfProductGewicht, jtfProductVoorraad, jtfProductLocatieX, jtfProductLocatieY;
    private JTextArea jtaProductBeschrijving;
    private String productnaam, productbeschrijving, productprijs, productgewicht, productaantal, productvoorraad, productlocatiex, productlocatiey;
    private boolean isOk = false;

    public EditArticleDialog(InventoryScreen screen, JFrame jFrame, int articleId){
        super(jFrame,true);
        this.screen = screen;
        this.articleId = articleId;
        setArticle();
        createDialog();
    }

    public EditArticleDialog(InventoryScreen screen, JFrame jFrame){
        super(jFrame,true);
        this.screen = screen;
        setNewArticleId();
        articleExists = false;
        createDialog();
    }

    public void createDialog(){
        setSize(480,400);
        setResizable(false);
        setTitle("Artikel ID: " + articleId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        panel =(JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new GridLayout(0,2,0,5));

        locatiePanel = new JPanel();
        locatiePanel.setLayout(new GridLayout(1,2,10,0));

        locatieXPanel = new JPanel();
        locatieXPanel.setLayout(new BorderLayout(10,0));
        locatieYPanel = new JPanel();
        locatieYPanel.setLayout(new BorderLayout(10,0));

        bottomMiddlePanel = new JPanel();
        bottomMiddlePanel.setLayout(new GridLayout(1,1));
        bottomMiddlePanel.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        jlTitelNew = new JLabel("Artikel aanmaken");
        jlTitelNew.setFont(new Font("Arial",Font.BOLD,30));
        jlTitelEdit = new JLabel("Artikel gegevens wijzigen");
        jlTitelEdit.setFont(new Font("Arial",Font.BOLD,30));

        if(articleExists){
            topPanel.add(jlTitelEdit);
        } else {
            topPanel.add(jlTitelNew);
        }

        jlProductNaam = new JLabel("Product naam: *");
        topMiddlePanel.add(jlProductNaam);
        jtfProductNaam = new JTextField(productnaam, 25);
        topMiddlePanel.add(jtfProductNaam);

        jlProductAantal = new JLabel("Aantal per bestelling: *");
        topMiddlePanel.add(jlProductAantal);
        jtfProductAantal = new JTextField(productaantal, 25);
        topMiddlePanel.add(jtfProductAantal);

        jlProductPrijs = new JLabel("Prijs: *");
        topMiddlePanel.add(jlProductPrijs);
        jtfProductPrijs = new JTextField(productprijs,25);
        topMiddlePanel.add(jtfProductPrijs);

        jlProductGewicht = new JLabel("Gewicht: *");
        topMiddlePanel.add(jlProductGewicht);
        jtfProductGewicht = new JTextField(productgewicht, 25);
        topMiddlePanel.add(jtfProductGewicht);

        jlProductVoorraad = new JLabel("Voorraad: *");
        topMiddlePanel.add(jlProductVoorraad);
        jtfProductVoorraad = new JTextField(productvoorraad,25);
        topMiddlePanel.add(jtfProductVoorraad);

        jlProductLocatie = new JLabel("Locatie: *");
        topMiddlePanel.add(jlProductLocatie);

        jlProductLocatieX = new JLabel("x: ");
        locatieXPanel.add(jlProductLocatieX, BorderLayout.LINE_START);
        jtfProductLocatieX = new JTextField(productlocatiex,1);
        locatieXPanel.add(jtfProductLocatieX, BorderLayout.CENTER);

        jlProductLocatieY = new JLabel("y: ");
        locatieYPanel.add(jlProductLocatieY, BorderLayout.LINE_START);
        jtfProductLocatieY = new JTextField(productlocatiey,1);
        locatieYPanel.add(jtfProductLocatieY, BorderLayout.CENTER);

        locatiePanel.add(locatieXPanel);
        locatiePanel.add(locatieYPanel);

        topMiddlePanel.add(locatiePanel);

        jlProductBeschrijving = new JLabel("Beschrijving: *");
        topMiddlePanel.add(jlProductBeschrijving);
        jtaProductBeschrijving = new JTextArea(productbeschrijving);
        bottomMiddlePanel.add(jtaProductBeschrijving);

        middlePanel.add(topMiddlePanel,BorderLayout.PAGE_START);
        middlePanel.add(bottomMiddlePanel);

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
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            addToDb();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("INSERT INTO StockItems (StockItemID , StockItemName, QuantityPerOuter, UnitPrice, TypicalWeightPerUnit, SearchDetails, SupplierID, UnitPackageID, OuterPackageID, LeadTimeDays, IsChillerStock, TaxRate, LastEditedBy, ValidFrom, ValidTo) VALUES (" + articleId + ", '" + productnaam + "', " + Integer.parseInt(productaantal) + ", " + Double.parseDouble(productprijs) + ", " + Double.parseDouble(productgewicht) + ", '" + productbeschrijving + "', 1, 1, 1, 14, 0, 15, 1, '" + getDate() + "' , '9999-12-31 23:59:59' )");
        dbConn.updateDb("INSERT INTO StockItemHoldings (StockItemID, QuantityOnHand, BinLocation, LastStocktakeQuantity, LastCostPrice, ReorderLevel, TargetStockLevel, LastEditedBy, LastEditedWhen) VALUES (" + articleId + ", " + Integer.parseInt(productvoorraad) + ", " + Integer.parseInt(productlocatiex + productlocatiey) + ", 1, " + Double.parseDouble(productprijs) + ", 1, 1, 1, '" + getDateTime() + "')");
        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
        isOk = true;
    }

    public void editDb(){
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            editDb();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE StockItems SET StockItemName = '" + productnaam + "', QuantityPerOuter = " + Integer.parseInt(productaantal) + ", UnitPrice = " + Double.parseDouble(productprijs) + ", TypicalWeightPerUnit = " + Double.parseDouble(productgewicht) + ", SearchDetails = '" + productbeschrijving + "' WHERE StockItemID = " + articleId);
        dbConn.updateDb("UPDATE StockItemHoldings SET QuantityOnHand = " + Integer.parseInt(productvoorraad) + ", BinLocation = " + Integer.parseInt(productlocatiex + productlocatiey) + " WHERE StockItemID = " + articleId);
        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
        isOk = true;
    }

    public void setArticle(){
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setArticle();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT si.StockItemName, si.QuantityPerOuter, si.UnitPrice, si.TypicalWeightPerUnit, sih.QuantityOnHand, sih.BinLocation, si.SearchDetails FROM StockItems si JOIN StockItemHoldings sih ON si.StockItemID = sih.StockItemID WHERE si.StockItemID = " + articleId);
        System.out.println(rs);

        try {
            rs.first();

            productnaam = rs.getString("si.StockItemName");
            productaantal = rs.getString("si.QuantityPerOuter");
            productprijs = rs.getString("si.UnitPrice");
            productgewicht = rs.getString("si.TypicalWeightPerUnit");
            productvoorraad = rs.getString("sih.QuantityOnHand");
            productlocatiex = String.valueOf(rs.getString("sih.BinLocation").charAt(0));
            productlocatiey = String.valueOf(rs.getString("sih.BinLocation").charAt(1));
            productbeschrijving = rs.getString("si.SearchDetails");

        } catch (SQLException sqle){
            System.out.println("Er is een SQL fout opgetreden in EditArticleDialog.java in methode setArticle");
        } finally {
            dbConn.killStatement();
            DbConn.dbKill();
            Start.dbDoneLoading = true;
        }
    }


    public void setNewArticleId(){
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setNewArticleId();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(StockItemID) FROM StockItems;");

        try{
            rs.first();
            articleId = rs.getInt("MAX(StockItemID)") + 1;
        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in EditArticleDialog.java in methode setNewArticleId");
        } finally {
            dbConn.killStatement();
            DbConn.dbKill();
            Start.dbDoneLoading = true;
        }
    }

    public String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean isInt(String input){
        try{
            int x = Integer.parseInt(input);
            if(x > 0){
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isDouble(String input){
        try{
            double x = Double.parseDouble(input);
            if(x > 0){
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public boolean getIsOk(){
        return isOk;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == jbAnnuleren) {
            dispose();
        } else if (e.getSource() == jbBevestigen) {
            productnaam = jtfProductNaam.getText();
            productbeschrijving = jtaProductBeschrijving.getText();
            productaantal = jtfProductAantal.getText();
            productprijs = jtfProductPrijs.getText();
            productgewicht = jtfProductGewicht.getText();
            productvoorraad = jtfProductVoorraad.getText();
            productlocatiex = jtfProductLocatieX.getText();
            productlocatiey = jtfProductLocatieY.getText();

            if (productnaam.equals("") || productbeschrijving.equals("") || productaantal.equals("") || productprijs.equals("") || productgewicht.equals("") || productvoorraad.equals("") || productlocatiex.equals("") || productlocatiey.equals("")) {
                JOptionPane.showMessageDialog(this, "Niet alle verplichte velden zijn ingevuld.");
            } else {
                if(!isInt(productaantal) || !isDouble(productprijs) || !isDouble(productgewicht) || !isInt(productvoorraad) || !isInt(productlocatiex) || !isInt(productlocatiey)){
                    if(productlocatiex.length() > 1 || productlocatiey.length() > 1){
                        JOptionPane.showMessageDialog(this,"Ongeldige invoer.");
                    }
                } else {
                    if (articleExists == true) {
                        int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u dit product wijzigen?", "Wijzigen product", JOptionPane.YES_NO_OPTION);
                        if (keuze == JOptionPane.YES_OPTION) {
                            editDb();
                            dispose();
                        }
                    } else {
                        int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u dit product wilt toevoegen?", "Toevoegen product", JOptionPane.YES_NO_OPTION);
                        if (keuze == JOptionPane.YES_OPTION) {
                            addToDb();
                            dispose();
                        }
                    }
                }
            }
        }
    }
}
