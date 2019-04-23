package magazijnrobot;

import javax.swing.*;

public class InventoryScreen extends JFrame {
    private Article[] allArticles;
    private int amountOfArticles;

    public InventoryScreen () {
        fillArticle();
        setTitle("Voorraad overzicht");

        //sets screensize to fullscreen.
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);





        setVisible(true);
    }

    //put all articles from database in allArticles.
    private void fillArticle() {
        //gegevens uit de database halen
    }

    //responsible for projecting all articles on screen.
    private void showAllArticles() {
        for(Article a: allArticles) {
            showArticle(a.getId());
        }
    }

    //projects article rows on screen.
    private void showArticle(int id) {
        add(new JLabel("text vanuit de db."));
    }

}
