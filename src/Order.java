import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Order {

    private int orderNr;
    private ArrayList<Article> articles;
    private int amountOfArticles;
    private double totalWeight;

    private String status = "a";
    private Customer customer;

    public Order(int orderNr) {
        this.orderNr = orderNr;

        articles = new ArrayList<>(); //10 wordt aantal items uit de database
    }

    private void getOrderInformationFromDb() {
        //get results from database. resultset contains all results from query.
        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("select status, customerid from orders where orderid = " + orderNr);

        try {
            this.status = rs.getString(0);
            this.customer = new Customer(rs.getInt(1));
        } catch (SQLException sqle) {

        }


        dbConn.killStatement();


    }

    private void getArticlesFromDb() {

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
