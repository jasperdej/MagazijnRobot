package magazijnrobot;

import java.util.ArrayList;

public class Order {

    private int orderNr;
    private String status = "a";
    private int amountOfArticles;
    private double totalWeight;
    private ArrayList<Article> articles;
    private Customer customer;

    public Order(int orderNr) {
        this.orderNr = orderNr;
        //haal rest op uit database
        articles = new ArrayList<>(); //10 wordt aantal items uit de database
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

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderNr() {
        return orderNr;
    }

    public void addArticle(Article article) {
        articles.add(article);
    }
}
