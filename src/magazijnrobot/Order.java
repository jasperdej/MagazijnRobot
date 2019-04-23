package magazijnrobot;

public class Order {
    private int orderNr;
    private String status;
    private int amountOfArticles;
    private double totalWeight;
    private Article[] article;
    private Customer customer;

    public Order () {

    }

    public String getStatus() {
        return status;
    }

    public int getAmountOfArticles() {
        return amountOfArticles;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public Article[] getArticle() {
        return article;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderNr() {
        return orderNr;
    }
}
