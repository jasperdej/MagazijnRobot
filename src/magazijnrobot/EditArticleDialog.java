package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditArticleDialog extends JDialog implements ActionListener {

    private int articleId;
    private boolean articleExists = true;

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
        setSize(450,400);
        setTitle("Artikel " + articleId);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JLabel test = new JLabel("ID = " + articleId + ", exists = " + articleExists);
        add(test);

        setVisible(true);
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
