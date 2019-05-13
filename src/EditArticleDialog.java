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

    private int articleId;
    private boolean articleExists = true;
    private JButton jbBevestigen, jbAnnuleren;
    private JLabel jlTitelNew, jlTitelEdit, jlProductNaam, jlProductAantal, jlProductPrijs, jlProductGewicht, jlProductBeschrijving, jlVerplicht;
    private JTextField jtfProductNaam, jtfProductAantal, jtfProductPrijs, jtfProductGewicht;
    private JTextArea jtaProductBeschrijving;
    private String productnaam, productbeschrijving, productprijs, productgewicht, productaantal;
    private boolean valseInvoer = false;

    public EditArticleDialog(JFrame jFrame, int articleId){
        super(jFrame,true);
        this.articleId = articleId;
        setArticle();
        createDialog();
    }

    public EditArticleDialog(JFrame jFrame){
        super(jFrame,true);
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

        JPanel panel =(JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        JPanel topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new GridLayout(0,2,0,5));

        JPanel bottomMiddlePanel = new JPanel();
        bottomMiddlePanel.setLayout(new GridLayout(1,1));
        bottomMiddlePanel.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
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
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("INSERT INTO StockItems (StockItemID , StockItemName, QuantityPerOuter, UnitPrice, TypicalWeightPerUnit, SearchDetails, SupplierID, UnitPackageID, OuterPackageID, LeadTimeDays, IsChillerStock, TaxRate, LastEditedBy, ValidFrom, ValidTo) VALUES (" + articleId + ", '" + productnaam + "', " + Integer.parseInt(productaantal) + ", " + Double.parseDouble(productprijs) + ", " + Double.parseDouble(productgewicht) + ", '" + productbeschrijving + "', 1, 1, 1, 14, 0, 15, 1, '" + getDate() + "' , '9999-12-31 23:59:59' )");
    }

    public void editDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE StockItems SET StockItemName = '" + productnaam + "', QuantityPerOuter = " + Integer.parseInt(productaantal) + ", UnitPrice = " + Double.parseDouble(productprijs) + ", TypicalWeightPerUnit = " + Double.parseDouble(productgewicht) + ", SearchDetails = '" + productbeschrijving + "' WHERE StockItemID = " + articleId);
    }

    public void setArticle(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT StockItemName, QuantityPerOuter, UnitPrice, TypicalWeightPerUnit, SearchDetails FROM StockItems WHERE StockItemID = " + articleId);
        System.out.println(rs);

        try {
            rs.first();

            productnaam = rs.getString("StockItemName");
            productaantal = rs.getString("QuantityPerOuter");
            productprijs = rs.getString("UnitPrice");
            productgewicht = rs.getString("TypicalWeightPerUnit");
            productbeschrijving = rs.getString("SearchDetails");

        } catch (SQLException e){
            System.out.println(e);
        } finally {
            dbConn.killStatement();
        }
    }


    public void setNewArticleId(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(StockItemID) FROM StockItems;");


        try{
            rs.first();
            articleId = rs.getInt("MAX(StockItemID)") + 1;
        } catch (SQLException sqle) {
            System.out.println(sqle);
        } catch (NumberFormatException nfe) {
            System.out.println(nfe);
        } finally {
            dbConn.killStatement();
        }
    }

    public String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean isInt(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public boolean isDouble(String input){
        try{
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public void actionPerformed(ActionEvent e){
        valseInvoer = false;
        if(e.getSource() == jbAnnuleren) {
            dispose();
        } else if (e.getSource() == jbBevestigen) {
            productnaam = jtfProductNaam.getText();
            productbeschrijving = jtaProductBeschrijving.getText();
            productaantal = jtfProductAantal.getText();
            productprijs = jtfProductPrijs.getText();
            productgewicht = jtfProductGewicht.getText();

            if (productnaam.equals("") || productbeschrijving.equals("") || productaantal.equals("") || productprijs.equals("") || productgewicht.equals("")) {
                JOptionPane.showMessageDialog(this, "Niet alle verplichte velden zijn ingevuld.");
            } else {
                if(!isInt(productaantal) || !isDouble(productprijs) || !isDouble(productgewicht)){
                    JOptionPane.showMessageDialog(this,"Ongeldige invoer.");
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
