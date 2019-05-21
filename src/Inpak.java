import java.awt.*;
import java.util.ArrayList;

public class Inpak extends Robot {
    private int amountOfArticlesPacked = 0;
    private String toCoords;
    private ArrayList<Bin> binOrder = new ArrayList<>();
    private int currentBin;
    private Graphics graphics;
    private Bin bin;

    //binlist is created by bpp algorithm. articles is created by tsp algorithm.
    public ArrayList<Bin> sendCoordinatesToArduino(ArrayList<Bin> binList, ArrayList<Article> articleList){
        toCoords = "";
        binOrder.clear();
        for (Article a: articleList) {
            for (Bin b: binList) {
                for (Article ab: b.getArticles()) {
                    if (a == ab) {
                        if (b.getBinNumber() < 10){
                            toCoords += "0" + b.getBinNumber() + ",";
                        } else {
                            toCoords += b.getBinNumber() + ",";
                        }
                        binOrder.add(b);
                    }
                }
            }
        }
        toCoords+="00";
        sendToCoords(toCoords);
        return binOrder;
    }

    public void sendToArduino(String string) {
        sendToCoords(string);
    }

    public int binPercentageFilled(int binId) {
        int amountPacked = 0;
        int totalInBin = 0;
        double binPercentageFilled = 0;

        if (binOrder.size() == 0) {
            return 0;
        }

        for (int i = 0; i < amountOfArticlesPacked; i++) {
            if (binOrder.get(i).getBinNumber() == binId) {
                amountPacked++;
            }
        }
        for (Bin b: binOrder) {
            if (b.getBinNumber() == binId) {
                totalInBin++;
            }
        }
        if (totalInBin == 0) {
            return 0;
        } else {
            binPercentageFilled = (double) amountPacked/totalInBin * 100;
            return (int) Math.round(binPercentageFilled);
        }
    }

    public void setAmountOfArticlesPacked(int amountOfArticlesPacked) {
        this.amountOfArticlesPacked = amountOfArticlesPacked;
    }

    public int getAmountOfArticlesPacked() {
        return this.amountOfArticlesPacked;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCurrentBin(int currentBin) {
        this.currentBin = currentBin;
    }

    public int getCurrentBin() {
        return currentBin;
    }

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }
}
