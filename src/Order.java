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

    //gets articles from current order from database.
    private void getArticlesFromDb() {
        //get results from database. resultset contains all results from query.
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
        } finally {
            dbConn.killStatement();
        }
    }

    //gets new orderId from database. is often the next id.
    public void getNewOrderIdFromDb() {
        boolean newOrderId = false;
        int highestOrderId = getHighestOrderIDFromDb();
        int lowestOrderId = getLowestOrderIdFromDb();

        DbConn dbConn = new DbConn();
        dbConn.dbConnect();

        while (lowestOrderId < highestOrderId && !newOrderId) {
            ResultSet rsNewOrder = dbConn.getResultSetFromDb("select status from orders where orderid = " + lowestOrderId);
            try {
                rsNewOrder.next();
                if (rsNewOrder.getString(1).equals("wachten op actie")) {
                    newOrderId = true;
                    orderNr = lowestOrderId;
                }
            } catch (SQLException sqle) {
                System.out.println("Er is een SQL fout opgetreden in Order.java in methode getNewOrderIdFromDb");
                System.out.println(sqle);
            }

            lowestOrderId++;
        }
        dbConn.killStatement();

        getArticlesFromDb();
    }

    //gets highest orderid from database of orders that have not been picked yet.
    private int getHighestOrderIDFromDb() {
        int highestOrderId = -1;

        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet maxOrderId = dbConn.getResultSetFromDb("select max(orderid) from orders where status = \"wachten op actie\"");

        try {
            maxOrderId.next();
            highestOrderId = maxOrderId.getInt(1);
        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in Order.java in methode getHighestOrderIdFromDb");
            System.out.println(sqle);
        }
        return highestOrderId;
    }

    //gets lowest orderid from database of orders that have not been picked yet.
    private int getLowestOrderIdFromDb() {
        int lowestOrderId = -1;

        DbConn dbConn = new DbConn();
        dbConn.dbConnect();
        ResultSet minOrderId = dbConn.getResultSetFromDb("select min(orderid)from orders where status = 'wachten op actie'");

        try {
            minOrderId.next();
            lowestOrderId = minOrderId.getInt(1);
        } catch (SQLException sqle) {
            System.out.println("Er is een SQL fout opgetreden in Order.java in methode getLowestOrderIdFromDb");
            System.out.println(sqle);
        }
        return lowestOrderId;
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

    public void addArticle(Article article) {
        articles.add(article);
    }
}
