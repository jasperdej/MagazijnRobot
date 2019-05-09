import java.util.ArrayList;

public class Bin {

    private static int binCount = 0;
    private int binNumber;
    private Order order;
    private Double totalWeight;
    private CoordinatePoint topCoordinatePoint;
    private CoordinatePoint bottomCoordinatePoint;
    private double maximumWeight;
    private int maximumQuantity;
    private ArrayList<Article> articles;
    private String status;
    private int percentageFilled;

    //needs adjusting
    public Bin(Order order, double maximumWeight, int maximumQuantity) {
        this.order = order;
        binCount++;
        binNumber = binCount;
        totalWeight = 0.0;
        articles = new ArrayList<>();
    }

    public int getBinNumber() {
        return this.binNumber;
    }

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

    public ArrayList<Article> getItems() {
        return articles;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void addItem(Article article) {
        articles.add(article);
    }
}
