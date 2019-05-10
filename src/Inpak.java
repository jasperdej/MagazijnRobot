import java.awt.*;
import java.util.ArrayList;

public class Inpak extends Robot {
    private int amountOfArticlesPacked = 1;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int article = 2;
    private String toCoords;

    public Inpak() {//, Graphics graphics
        status = "a";
    }

    //binlist is created by bpp algorithm. articles is created by tsp algorithm.
    public void sendCoordinatesToArduino(ArrayList<Bin> binList, ArrayList<Article> articleList){
        toCoords = "";
        for (Article a: articleList) {
            for (Bin b: binList) {
                for (Article ab: b.getArticles()) {
                    if (ab.getLocation().getX() == a.getLocation().getX() && ab.getLocation().getY() == a.getLocation().getY()) {
                        toCoords += Integer.toString(b.getBinNumber());
                    }
                }
            }
        }
        sendToCoords(toCoords);
    }

    public int getAmountOfArticlesPacked() {
        return this.amountOfArticlesPacked;
    }

    public void setArticle(int article) {
        if (article >= 0 && article <= 2) {
            this.article = article;
        } else {
            System.out.println("FOUTE INVOER OP INPAK.JAVA methode: setArticle. int 0 tot 2 verwacht, " + article + " gekregen");
        }
    }

    public void drawArticle(Graphics graphics) {
        graphics.fillRect((screenSize.width/2 + screenSize.width/50 + 7) + article * screenSize.width/10 + article * screenSize.width/50, screenSize.width/8-40, screenSize.height/10 + 2, screenSize.height/10 + 2);
    }
}
