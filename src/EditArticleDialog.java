import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditArticleDialog extends JDialog implements ActionListener {

    private int articleId;
    private boolean articleExists = true;
    private JButton jbBevestigen, jbAnnuleren;
    private JTextField jtfProductNaam, jtfProductAantal, jtfProductPrijs, jtfProductGewicht, jtfProductBeschrijving;
    private String productnaam, productid, productaantal, productprijs, productgewicht, productbeschrijving;

    public EditArticleDialog(JFrame jFrame, int articleId){
        super(jFrame,true);
        this.articleId = articleId;
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
        jtfProductAantal = new JTextField(productaantal, 25);
        middlePanel.add(jtfProductAantal);

        JLabel jlProductPrijs = new JLabel("Product prijs: *");
        middlePanel.add(jlProductPrijs);
        jtfProductPrijs = new JTextField(productprijs,25);
        middlePanel.add(jtfProductPrijs);

        JLabel jlProductGewicht = new JLabel("Product gewicht: *");
        middlePanel.add(jlProductGewicht);
        jtfProductGewicht = new JTextField(productgewicht);
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
       // dbConn.updateDb("INSERT INTO StockItems (UserID, UserFirstName, UserPrefix, UserLastName, UserEmail, UserAddress, UserResidence, UserPostalCode) VALUES (" + personId + ", '" + voornaam + "', " + tussenvoegsel + ", '" + achternaam + "', " + emailadres + ", '" + adres + "', '" + woonplaats + "', '" + postcode + "')");

    }

    public void editDb(){

    }

    public void setArticle(){

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

    public void actionPerformed(ActionEvent e){

    }
}
