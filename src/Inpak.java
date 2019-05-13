import java.awt.*;
import java.util.ArrayList;

public class Inpak extends Robot {
    private int amountOfArticlesPacked = 0;
    private String toCoords;
    private ArrayList<Bin> binOrder = new ArrayList<>();
    private int currentBin = 1;
    private Graphics graphics;

    public Inpak() {//, Graphics graphics
        status = "aan het inpakken";
    }

    //binlist is created by bpp algorithm. articles is created by tsp algorithm.
    public void sendCoordinatesToArduino(ArrayList<Bin> binList, ArrayList<Article> articleList){
        toCoords = "";
        binOrder.clear();
        for (Article a: articleList) {
            for (Bin b: binList) {
                for (Article ab: b.getArticles()) {
                    if (ab.getLocation().getX() == a.getLocation().getX() && ab.getLocation().getY() == a.getLocation().getY()) {
                        toCoords += Integer.toString(b.getBinNumber());
                        binOrder.add(b);
                    }
                }
            }
        }
//        sendToCoords(toCoords);

    }

    public int binPercentageFilled(int binId) {
        int amountPacked = 0;
        int totalInBin = 0;

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
            return Math.round(amountPacked/totalInBin)*100;
        }
    }

    public int getAmountOfArticlesPacked() {
        return this.amountOfArticlesPacked;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCurrentBinBin() {
        return currentBin;
    }
}
