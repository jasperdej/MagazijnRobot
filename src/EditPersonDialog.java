import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class EditPersonDialog extends JDialog implements ActionListener {

    private int personId;
    private boolean personExists = true;
    private JPanel panel, topPanel, middlePanel, topMiddlePanel, bottomPanel, buttonPanel;
    private JLabel jlTitelNew, jlTitelEdit, jlVoornaam, jlTussenvoegsel, jlAchternaam, jlEmailadres, jlAdres, jlWoonplaats, jlPostcode, jlVerplicht;
    private JTextField jtfVoornaam, jtfTussenvoegsel, jtfAchternaam, jtfEmailadres, jtfAdres, jtfWoonplaats,jtfPostcode;
    private JButton jbBevestigen, jbAnnuleren;
    private String voornaam, tussenvoegsel, achternaam, emailadres, adres, woonplaats, postcode;

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
        setResizable(false);
        setTitle("Persoon ID: " + personId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        panel = (JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        middlePanel = new JPanel();
        middlePanel.setLayout(new BorderLayout());

        topMiddlePanel = new JPanel();
        topMiddlePanel.setLayout(new GridLayout(0,2,0,5));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
      
        jlTitelNew = new JLabel("Persoon aanmaken");
        jlTitelNew.setFont(new Font("Arial",Font.BOLD,30));
        jlTitelEdit = new JLabel("Persoonsgegevens wijzigen");
        jlTitelEdit.setFont(new Font("Arial",Font.BOLD,30));
        if(personExists){
          topPanel.add(jlTitelEdit);
        } else {
          topPanel.add(jlTitelNew);
        }

        jlVoornaam = new JLabel("Voornaam: *");
        topMiddlePanel.add(jlVoornaam);
        jtfVoornaam = new JTextField(voornaam,10);
        topMiddlePanel.add(jtfVoornaam);

        jlTussenvoegsel = new JLabel("Tussenvoegsel: ");
        topMiddlePanel.add(jlTussenvoegsel);
        if(tussenvoegsel != null){
            jtfTussenvoegsel = new JTextField(tussenvoegsel,25);
        } else {
            jtfTussenvoegsel = new JTextField(25);
        }
        topMiddlePanel.add(jtfTussenvoegsel);

        jlAchternaam = new JLabel("Achternaam: * ");
        topMiddlePanel.add(jlAchternaam);
        jtfAchternaam = new JTextField(achternaam,25);
        topMiddlePanel.add(jtfAchternaam);

        jlEmailadres = new JLabel("Email adres: ");
        topMiddlePanel.add(jlEmailadres);
        if(emailadres != null) {
            jtfEmailadres = new JTextField(emailadres, 25);
        } else {
            jtfEmailadres = new JTextField(25);
        }
        topMiddlePanel.add(jtfEmailadres);

        jlAdres = new JLabel("Adres: * ");
        topMiddlePanel.add(jlAdres);
        jtfAdres = new JTextField(adres,25);
        topMiddlePanel.add(jtfAdres);

        jlWoonplaats = new JLabel("Woonplaats: * ");
        topMiddlePanel.add(jlWoonplaats);
        jtfWoonplaats = new JTextField(woonplaats,25);
        topMiddlePanel.add(jtfWoonplaats);

        jlPostcode = new JLabel("Postcode: * ");
        topMiddlePanel.add(jlPostcode);
        jtfPostcode = new JTextField(postcode,25);
        topMiddlePanel.add(jtfPostcode);

        middlePanel.add(topMiddlePanel,BorderLayout.PAGE_START);

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
        dbConn.updateDb("INSERT INTO Users (UserID, UserFirstName, UserPrefix, UserLastName, UserEmail, UserAddress, UserResidence, UserPostalCode) VALUES (" + personId + ", '" + voornaam + "', " + tussenvoegsel + ", '" + achternaam + "', " + emailadres + ", '" + adres + "', '" + woonplaats + "', '" + postcode + "')");
        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
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
        dbConn.updateDb("UPDATE Users SET UserFirstName = '" + voornaam + "', UserPrefix = " + tussenvoegsel + ", UserLastName = '" + achternaam + "', UserEmail = " + emailadres + ", UserAddress = '" + adres + "', UserResidence = '" + woonplaats + "', UserPostalCode = '" + postcode + "' WHERE UserID = " + personId);
        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
    }

    public void setPerson(){
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setPerson();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT UserFirstName, UserPrefix, UserLastName, UserEmail, UserAddress, UserResidence, UserPostalCode FROM Users WHERE UserID = " + personId);

        try{
            rs.first();

            voornaam = rs.getString("UserFirstName");
            if(rs.getString("UserPrefix")!= null){
                tussenvoegsel = rs.getString("UserPrefix");
            } else {
                tussenvoegsel = null;
            }
            achternaam = rs.getString("UserLastName");
            if(rs.getString("UserEmail") != null){
                emailadres = rs.getString("UserEmail");
            } else {
                emailadres = null;
            }
            adres = rs.getString("Useraddress");
            woonplaats = rs.getString("UserResidence");
            postcode = rs.getString("UserPostalcode");


        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in EditPersonDialog.java in methode setPerson");
        } finally {
            dbConn.killStatement();
            DbConn.dbKill();
            Start.dbDoneLoading = true;
        }
    }

    public void setNewPersonId(){
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            setNewPersonId();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT MAX(UserID) FROM Users;");


        try{
            rs.first();
            personId = rs.getInt("MAX(UserID)") + 1;
        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in EditPersonDialog.java in methode setNewPersonId");
        } finally {
            dbConn.killStatement();
            DbConn.dbKill();
            Start.dbDoneLoading = true;
        }
    }

    public void actionPerformed(ActionEvent e){

        if (e.getSource() == jbAnnuleren) {
            dispose();
        } else if (e.getSource() == jbBevestigen) {
            voornaam = jtfVoornaam.getText();
            if(!jtfTussenvoegsel.getText().equals("")){
                tussenvoegsel = "'" + jtfTussenvoegsel.getText() + "'";
            } else {
                tussenvoegsel = null;
            }
            achternaam = jtfAchternaam.getText();
            if(!jtfEmailadres.getText().equals("")){
                emailadres = "'" + jtfEmailadres.getText() + "'";
            } else {
                emailadres = null;
            }
            adres = jtfAdres.getText();
            woonplaats = jtfWoonplaats.getText();
            postcode = jtfPostcode.getText();

            if(voornaam.equals("") || achternaam.equals("") || adres.equals("") || woonplaats.equals("") || postcode.equals("")){
                JOptionPane.showMessageDialog(this,"Niet alle verplichte velden zijn ingevuld.");
            } else {
                if (personExists == true) {
                    int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u deze gegevens wilt wijzigen?", "Wijzigen gegevens", JOptionPane.YES_NO_OPTION);
                    if (keuze == JOptionPane.YES_OPTION) {
                        editDb();
                        dispose();
                    }
                } else {
                    int keuze = JOptionPane.showConfirmDialog(this, "Weet u zeker dat u deze persoon wilt toevoegen?", "Toevoegen persoon", JOptionPane.YES_NO_OPTION);
                    if (keuze == JOptionPane.YES_OPTION) {
                        addToDb();
                        dispose();
                    }
                }
            }

        }

    }
}
