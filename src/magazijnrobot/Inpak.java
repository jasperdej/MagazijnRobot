package magazijnrobot;

public class Inpak extends Robot {
    private int amountOfArticlesPacked = 1;
    private Bin[] bins;
    private Bin currentBin;

    public Inpak(Order order, Bin bin1, Bin bin2, Bin bin3) {
        super(order);
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
}
