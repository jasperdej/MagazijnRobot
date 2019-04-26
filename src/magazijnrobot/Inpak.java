package magazijnrobot;

import java.awt.*;

public class Inpak extends Robot {
    private int amountOfArticlesPacked = 1;
    private Bin[] bins;
    private Bin currentBin;
    private Graphics graphics;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

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

    public void drawArticle(Graphics graphics) {
        graphics.fillRect(screenSize.width/2 + screenSize.width/50 + 5, screenSize.width/8-40, screenSize.height/6 - 20, screenSize.height/6 - 20);
    }
}
