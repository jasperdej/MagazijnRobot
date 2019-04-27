package magazijnrobot;

import java.awt.*;

public class Inpak extends Robot {
    private int amountOfArticlesPacked = 1;
    private Bin[] bins;
    private Bin currentBin;
    private Graphics graphics;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int article = 2;

    public Inpak(Order order, Bin bin1, Bin bin2, Bin bin3) {//, Graphics graphics
        super(order);
        this.graphics = graphics;
        bins = new Bin[]{bin1, bin2, bin3};
        currentBin = bins[1];
        status = "a";
    }

    public int getAmountOfArticlesPacked() {
        return this.amountOfArticlesPacked;
    }

    public Bin getCurrentBin() {
        return this.currentBin;
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
