import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

public class EditOrderDialog extends JDialog implements ActionListener {

    private int orderId;
    private boolean orderExists = true;
    private JTextField jtfVoornaam, jtfTussenvoegsel, jtfAchternaam, jtfEmailadres, jtfAdres, jtfWoonplaats,jtfPostcode;
    private JButton jbBevestigen, jbAnnuleren;
    private String voornaam, tussenvoegsel, achternaam, emailadres, adres, woonplaats, postcode;

    public EditOrderDialog(JFrame jFrame, int orderId){
        super(jFrame,true);
        this.orderId = orderId;
        setOrder();
        createDialog();
    }

    public EditOrderDialog(JFrame jFrame){
        super(jFrame,true);
        setNewOrderId();
        orderExists = false;
        createDialog();
    }

    public void createDialog(){
        setSize(480,400);
        setResizable(false);
        setTitle("Order ID: " + orderId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel panel = (JPanel)this.getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(10,2));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));

        JLabel jlTitelNew = new JLabel("Order aanmaken");
        jlTitelNew.setFont(new Font("Arial",Font.BOLD,30));
        JLabel jlTitelEdit = new JLabel("Order Gegevens wijzigen");
        jlTitelEdit.setFont(new Font("Arial",Font.BOLD,30));
        if(orderExists){
            topPanel.add(jlTitelEdit);
        } else {
            topPanel.add(jlTitelNew);
        }

        JLabel jlVoornaam = new JLabel("Voornaam: *");
        middlePanel.add(jlVoornaam);
        jtfVoornaam = new JTextField(voornaam,10);
        middlePanel.add(jtfVoornaam);

        JLabel jlTussenvoegsel = new JLabel("Tussenvoegsel: ");
        middlePanel.add(jlTussenvoegsel);
        if(tussenvoegsel != null){
            jtfTussenvoegsel = new JTextField(tussenvoegsel,25);
        } else {
            jtfTussenvoegsel = new JTextField(25);
        }
        middlePanel.add(jtfTussenvoegsel);

        JLabel jlAchternaam = new JLabel("Achternaam: * ");
        middlePanel.add(jlAchternaam);
        jtfAchternaam = new JTextField(achternaam,25);
        middlePanel.add(jtfAchternaam);

        JLabel jlEmailadres = new JLabel("Email adres: ");
        middlePanel.add(jlEmailadres);
        if(emailadres != null) {
            jtfEmailadres = new JTextField(emailadres, 25);
        } else {
            jtfEmailadres = new JTextField(25);
        }
        middlePanel.add(jtfEmailadres);

        JLabel jlAdres = new JLabel("Adres: * ");
        middlePanel.add(jlAdres);
        jtfAdres = new JTextField(adres,25);
        middlePanel.add(jtfAdres);

        JLabel jlWoonplaats = new JLabel("Woonplaats: * ");
        middlePanel.add(jlWoonplaats);
        jtfWoonplaats = new JTextField(woonplaats,25);
        middlePanel.add(jtfWoonplaats);

        JLabel jlPostcode = new JLabel("Postcode: * ");
        middlePanel.add(jlPostcode);
        jtfPostcode = new JTextField(postcode,25);
        middlePanel.add(jtfPostcode);

        JLabel jlVerplicht = new JLabel("Velden met een sterretje* zijn verplicht");
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
        dbConn.updateDb("INSERT INTO Users (UserID, UserFirstName, UserPrefix, UserLastName, UserEmail, UserAddress, UserResidence, UserPostalCode) VALUES (" + orderId + ", '" + voornaam + "', " + tussenvoegsel + ", '" + achternaam + "', " + emailadres + ", '" + adres + "', '" + woonplaats + "', '" + postcode + "')");
    }

    public void editDb(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE Users SET UserFirstName = '" + voornaam + "', UserPrefix = " + tussenvoegsel + ", UserLastName = '" + achternaam + "', UserEmail = " + emailadres + ", UserAddress = '" + adres + "', UserResidence = '" + woonplaats + "', UserPostalCode = '" + postcode + "' WHERE UserID = " + orderId);
    }

    public void setOrder(){
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("SELECT UserFirstName, UserPrefix, UserLastName, UserEmail, UserAddress, UserResidence, UserPostalCode FROM Users WHERE UserID = " + orderId);

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


        } catch (SQLException e) {
            System.out.println(e);

        } finally {
            dbConn.killStatement();
        }
    }

    public void setNewOrderId(){
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

        }

    }
}
