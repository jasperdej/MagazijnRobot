import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Order {
    private int orderNr;
    private ArrayList<Article> articles;
    private int amountOfArticles;
    private double totalWeight;

    public Order() {
        articles = new ArrayList<>();
    }

    //looks for new orderid from database. if new orderid is found, the arraylist with articles is filled with it's articles.
    public void getNewOrderIdFromDb() {
        orderNr = -1;
        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("select min(orderid) from orders where status = 'wachten op actie';");

        try{
            rs.first();
            if (rs.getInt(1) != -1) {
                orderNr = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
            System.out.println("Er is een SQL fout opgetreden in Order.java in methode getNewOrderIdFromDb");
        }
        dbConn.killStatement();
        DbConn.dbKill();

        if (orderNr == -1) {
            getNewOrderIdFromDb();
        } else {
            getArticlesFromDb();
        }
    }

    //gets articles from current order from database.
    public void getArticlesFromDb() {
        //get articles from curront order from database. resultset contains results from query.
        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("select ol.orderlineid, sih.binlocation, si.typicalWeightperunit, si.stockitemname, ol.quantity from orderlines ol join stockitems si on ol.stockitemid = si.stockitemid join stockitemholdings sih on ol.stockitemid = sih.stockitemid where orderid = " + orderNr);

        try{
            if (rs != null) {
                rs.last();
                amountOfArticles = rs.getRow();
                rs.first();
            }

            for (int i = 0; i < amountOfArticles; i++) {
                articles.add(new Article(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getString(4), rs.getInt(5)));
                rs.next();
            }

        } catch (SQLException sqle) {
            System.out.println(sqle);
            System.out.println("Er is een SQL fout opgetreden in Order.java in methode getArticlesFromDb");
        }
        dbConn.killStatement();
        DbConn.dbKill();
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

    public int getOrderNr() {
        return orderNr;
    }

}
