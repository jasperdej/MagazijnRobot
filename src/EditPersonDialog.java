import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditPersonDialog extends JDialog implements ActionListener {

    private int personId;
    private boolean personExists = true;
    private JTextField jtfVoornaam, jtfTussenvoegsel, jtfAchternaam, jtfEmailadres, jtfAdres, jtfWoonplaats,jtfPostcode;
    private JButton jbBevestigen, jbAnnuleren;
    private JPasswordField jpfWachtwoord;
    private String voornaam, tussenvoegsel, achternaam, emailadres, wachtwoord, adres, woonplaats, postcode;

    public EditPersonDialog(JFrame jFrame, int personId){
        super(jFrame,true);
        this.personId = personId;
        setPerson();
        createDialog();
    }

    public EditPersonDialog(JFrame jFrame){
        super(jFrame,true);
        setNewPersonId();
        personExists = false;
        createDialog();
    }

    public void createDialog(){
        setSize(480,400);
        setTitle("Persoon ID: " + personId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(10,2));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        topPanel.add(new JLabel("Maak een nieuw persoon aan."));

        JLabel jlVoornaam = new JLabel("Voornaam: *");
        jlVoornaam.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlVoornaam);
        jtfVoornaam = new JTextField(voornaam,25);
        middlePanel.add(jtfVoornaam);

        JLabel jlTussenvoegsel = new JLabel("Tussenvoegsel: ");
        jlTussenvoegsel.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlTussenvoegsel);
        jtfTussenvoegsel = new JTextField(tussenvoegsel,25);
        middlePanel.add(jtfTussenvoegsel);

        JLabel jlAchternaam = new JLabel("Achternaam: * ");
        jlAchternaam.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlAchternaam);
        jtfAchternaam = new JTextField(achternaam,25);
        middlePanel.add(jtfAchternaam);

        JLabel jlEmailadres = new JLabel("Email adres: * ");
        jlEmailadres.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlEmailadres);
        jtfEmailadres = new JTextField(emailadres,25);
        middlePanel.add(jtfEmailadres);

        JLabel jlWachtwoord = new JLabel("Wachtwoord: * ");
        jlWachtwoord.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlWachtwoord);
        jpfWachtwoord = new JPasswordField(25);
        middlePanel.add(jpfWachtwoord);

        JLabel jlAdres = new JLabel("Adres: * ");
        jlAdres.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlAdres);
        jtfAdres = new JTextField(adres,25);
        middlePanel.add(jtfAdres);

        JLabel jlWoonplaats = new JLabel("Woonplaats: * ");
        jlWoonplaats.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlWoonplaats);
        jtfWoonplaats = new JTextField(woonplaats,25);
        middlePanel.add(jtfWoonplaats);

        JLabel jlPostcode = new JLabel("Postcode: * ");
        jlPostcode.setBorder(BorderFactory.createEmptyBorder(0,50,0,0));
        middlePanel.add(jlPostcode);
        jtfPostcode = new JTextField(postcode,25);
        middlePanel.add(jtfPostcode);

        JLabel jlVerplicht = new JLabel("Velden met een sterretje* zijn verplicht");
        jlVerplicht.setHorizontalAlignment(JLabel.CENTER);
        bottomPanel.add(jlVerplicht, BorderLayout.PAGE_START);

        jbBevestigen = new JButton("Bevestigen");
        jbBevestigen.addActionListener(this);
        bottomPanel.add(jbBevestigen, BorderLayout.PAGE_END);

        jbAnnuleren = new JButton("Annuleren");
        jbAnnuleren.addActionListener(this);
        bottomPanel.add(jbAnnuleren, BorderLayout.PAGE_END);

        add(topPanel, BorderLayout.PAGE_START);
        add(middlePanel);
        add(bottomPanel, BorderLayout.PAGE_END);

        setVisible(true);
    }

    public void  editPerson(){

    }

    public void addPerson(){

    }

    public void setPerson(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT UserFirstName, UserPrefix, UserLastName, UserEmail, UserPassword, UserAddress, UserResidence, UserPostalCode FROM Users WHERE UserID = " + personId);

        try{
            rs.first();

            voornaam = rs.getString("UserFirstName");
            //System.out.println(voornaam);
            if(rs.getString("UserPrefix")!= null){
                tussenvoegsel = rs.getString("UserPrefix");
            } else {
                tussenvoegsel = "";
            }
            achternaam = rs.getString("UserLastName");
            if(rs.getString("UserEmail") != null){
                emailadres = rs.getString("UserEmail");
            } else {
                emailadres = "";
            }
            if(rs.getString("UserPassword") != null){
                wachtwoord = rs.getString("UserPassword");
            } else {
                wachtwoord = "";
            }
            adres = rs.getString("Useraddress");
            woonplaats = rs.getString("UserResidence");
            postcode = rs.getString("UserPostalcode");


        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            dbConn.killStatement();
        }
    }







    public void setNewPersonId(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(UserID) FROM Users;");


        try{
            rs.first();
            personId = rs.getInt("MAX(UserID)") + 1;
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
