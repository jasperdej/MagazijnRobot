import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditPersonDialog extends JDialog implements ActionListener {

    private int personId;
    private boolean personExists = true;

    public EditPersonDialog(JFrame jFrame, int personId){
        super(jFrame,true);
        this.personId = personId;
        createDialog();
    }

    public EditPersonDialog(JFrame jFrame){
        super(jFrame,true);
        setNewPersonId();
        personExists = false;
        createDialog();
    }

    public void createDialog(){
        setSize(450,400);
        setTitle("Persoon " + personId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel test = new JLabel("ID = " + personId + ", exists = " + personExists);
        add(test);

        setVisible(true);
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
