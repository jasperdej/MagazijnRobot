package magazijnrobot;

import java.util.ArrayList;

public class Bin {

    private int binNumber;
    private Order order;
    private Double totalWeight;
    private CoordinatePoint topCoordinatePoint;
    private CoordinatePoint bottomCoordinatePoint;
    private int maximumWeight;
    private int maximumQuantity;
    private ArrayList<Article> articles;
    private String status;
    private int percentageFilled;

    //needs adjusting
    public Bin(Order order, int binNumber) {
        this.order = order;
        this.binNumber = binNumber;
        totalWeight = 0.0;
        articles = new ArrayList<>();
    }

    public int getBinNumber() {
        return binNumber;
    }

    public void printBin() {
        for (Article i : articles) {
            System.out.println("- " + i.getId());
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

    public void addItem(Article article) {
        articles.add(article);
    }
}
