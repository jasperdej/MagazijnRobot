import java.util.ArrayList;

public class Bin {

    private static int binCount = 0;
    private int binNumber;
    private Order order;
    private Double totalWeight;
    private ArrayList<Article> articles;

    //sets private variables.
    public Bin(Order order) {
        binCount++;
        this.order = order;
        binNumber = binCount;
        totalWeight = 0.0;
        articles = new ArrayList<>();
    }

    public int getBinNumber() {
        return this.binNumber;
    }

    //prints all bins with their articles.
    public void printBin() {
        for (Article i : articles) {
            System.out.println("- " + i.getWeight());
        }
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void addToTotalWeight(double weight) {
        totalWeight += weight;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void addItem(Article article) {
        articles.add(article);
    }

    public static void setBinCount(int binCount) {
        Bin.binCount = binCount;
    }
}
