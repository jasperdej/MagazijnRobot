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
    private JTextField jtfProductNaam, jtfProductAantal, jtfProductPrijs, jtfProductGewicht, jtfProductBeschrijving;
    private String productnaam, productbeschrijving;
    private double productprijs, productgewicht;
    private int productaantal;
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
        setTitle("Artikel " + articleId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(0,2));
        middlePanel.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        if(articleExists){
            topPanel.add(new JLabel("Wijzig product."));
        } else {
            topPanel.add(new JLabel("Maak een nieuw product."));
        }

        JLabel jlProductNaam = new JLabel("Product naam: *");
        middlePanel.add(jlProductNaam);
        jtfProductNaam = new JTextField(productnaam, 25);
        middlePanel.add(jtfProductNaam);

        JLabel jlProductAantal = new JLabel("Product aantal: *");
        middlePanel.add(jlProductAantal);
        jtfProductAantal = new JTextField(String.valueOf(productaantal), 25);
        middlePanel.add(jtfProductAantal);

        JLabel jlProductPrijs = new JLabel("Product prijs: *");
        middlePanel.add(jlProductPrijs);
        jtfProductPrijs = new JTextField(String.valueOf(productprijs),25);
        middlePanel.add(jtfProductPrijs);

        JLabel jlProductGewicht = new JLabel("Product gewicht: *");
        middlePanel.add(jlProductGewicht);
        jtfProductGewicht = new JTextField(String.valueOf(productgewicht), 25);
        middlePanel.add(jtfProductGewicht);

        JLabel jlProductBeschrijving = new JLabel("Product beschrijving: *");
        middlePanel.add(jlProductBeschrijving);
        jtfProductBeschrijving = new JTextField(productbeschrijving);
        middlePanel.add(jtfProductBeschrijving);

        JLabel jlVerplicht = new JLabel("Velden met een sterretje* zijn verplicht");
        jlVerplicht.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(jlVerplicht, BorderLayout.PAGE_START);

        jbBevestigen = new JButton("Bevestigen");
        jbBevestigen.addActionListener(this);
        bottomPanel.add(jbBevestigen, BorderLayout.LINE_END);

        jbAnnuleren = new JButton("Annuleren");
        jbAnnuleren.addActionListener(this);
        bottomPanel.add(jbAnnuleren, BorderLayout.LINE_START);

        add(topPanel, BorderLayout.PAGE_START);
        add(middlePanel);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void addToDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("INSERT INTO StockItems (StockItemID , StockItemName, QuantityPerOuter, UnitPrice, TypicalWeightPerUnit, SearchDetails, SupplierID, UnitPackageID, OuterPackageID, LeadTimeDays, IsChillerStock, TaxRate, LastEditedBy, ValidFrom, ValidTo) VALUES (" + articleId + ", '" + productnaam + "', '" + productaantal + "', " + productprijs + ", " + productgewicht + ", '" + productbeschrijving + "', 1, 1, 1, 14, 0, 15, 1, '" + getDate() + "' , '9999-12-31 23:59:59' )");

    }

    public void editDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE StockItems SET StockItemName = '" + productnaam + "', QuantityPerOuter = " + productaantal + ", UnitPrice = " + productprijs + ", TypicalWeightPerUnit = " + productgewicht + ", SearchDetails = '" + productbeschrijving + "' WHERE StockItemID = " + articleId);
    }

    public void setArticle(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT StockItemName, QuantityPerOuter, UnitPrice, TypicalWeightPerUnit, SearchDetails FROM StockItems WHERE StockItemID = " + articleId);
        System.out.println(rs);

        try {
            rs.first();

            productnaam = rs.getString("StockItemName");
            productaantal = rs.getInt("QuantityPerOuter");
            productprijs = rs.getDouble("UnitPrice");
            productgewicht = rs.getDouble("TypicalWeightPerUnit");
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

    public void actionPerformed(ActionEvent e){
        valseInvoer = false;
        if(e.getSource() == jbAnnuleren) {
            dispose();
        } else if (e.getSource() == jbBevestigen) {
            productnaam = jtfProductNaam.getText();
            productbeschrijving = jtfProductBeschrijving.getText();
            try {
                productaantal = Integer.parseInt(jtfProductAantal.getText());
                productprijs = Double.parseDouble(jtfProductPrijs.getText());
                productgewicht = Double.parseDouble(jtfProductGewicht.getText());
            } catch (NumberFormatException nfe){
                JOptionPane.showMessageDialog(this,"Ongeldige invoer.");
                valseInvoer = true;
            }


            // int productaantal, double productprijs en double productgewicht mogen niet null zijn
            // nog even naar kijken Wietske? :)?



            if (!valseInvoer) {
                if (productnaam.equals("") || productbeschrijving.equals("") || productaantal == 0 || productprijs == 0 || productgewicht == 0) {
                    JOptionPane.showMessageDialog(this, "Velden met een sterretje* moet u invullen.");
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
