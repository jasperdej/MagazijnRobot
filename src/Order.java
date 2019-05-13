import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Order {
    private int orderNr;
    private ArrayList<Article> articles;
    private int amountOfArticles;
    private double totalWeight;

    //looks for new orderid from database. if new orderid is found, the arraylist with articles is filled with it's articles.
    public void getNewOrderIdFromDb() {
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            getNewOrderIdFromDb();
        } else {
            Start.dbDoneLoading = false;
        }

        orderNr = -1;

        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("select min(orderid) from orders where status = 'wachten op actie';");

        try{
            rs.first();
            orderNr = rs.getInt("min(orderid)");

        } catch (SQLException sqle) {
            //empty resultset returned. method keeps returning until a new order is found.
        } catch (Exception e) {
            getNewOrderIdFromDb();
        }

        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;


        if (orderNr == -1) {
            getNewOrderIdFromDb();
        } else {
            getArticlesFromDb();
        }
    }

    //gets articles from current order from database.
    public void getArticlesFromDb() {
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            getArticlesFromDb();
        } else {
            Start.dbDoneLoading = false;
        }
        //get articles from curront order from database. resultset contains results from query.
        articles = new ArrayList<>();
        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet rs = dbConn.getResultSetFromDb("select ol.stockitemid, sih.binlocation, si.typicalWeightperunit, si.stockitemname, ol.quantity from orderlines ol join stockitems si on ol.stockitemid = si.stockitemid join stockitemholdings sih on ol.stockitemid = sih.stockitemid where orderid = " + orderNr);

        try{
            rs.last();
            amountOfArticles = rs.getRow();
            rs.first();
            for (int i = 0; i < amountOfArticles; i++) {
                articles.add(new Article(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getString(4), rs.getInt(5)));
                rs.next();
            }

        } catch (SQLException sqle) {
            System.out.println(sqle);
            System.out.println("Er is een SQL fout opgetreden in Order.java in methode getArticlesFromDb");
        } catch (Exception e) {
            getArticlesFromDb();
        }
        dbConn.killStatement();
        DbConn.dbKill();

        Start.dbDoneLoading = true;
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