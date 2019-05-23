import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer {
    private int id;
    protected String name, userID, lastName, address, residence, postalcode;
    private Order order;
    private Bin bin;

    //sets private variable
    public Customer (Order order) {
        this.order = order;
    }

    //gets customer information from database
    public void getCustomerInfo(){
        if (!Start.dbDoneLoading) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        getCustomerInfo();
    }  else {
        Start.dbDoneLoading = false;
    }
    DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        ResultSet rsCustomer = dbConn.getResultSetFromDb("SELECT UserID, UserLastName, UserAddress, UserResidence, UserPostalCode FROM users JOIN orders ON users.UserID = orders.CustomerID WHERE OrderID = " + order.getOrderNr());
        System.out.println(rsCustomer);

        try {
            rsCustomer.first();
        userID = rsCustomer.getString("UserID");
        lastName = rsCustomer.getString("UserLastName");
        address = rsCustomer.getString("UserAddress");
        residence = rsCustomer.getString("UserResidence");
        postalcode = rsCustomer.getString("UserPostalCode");

    } catch (
    SQLException sqle){
        System.out.println("Er is een SQL fout opgetreden in EditArticleDialog.java in methode setArticle");
    } finally {
        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
    }
}

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public Bin getBin() {
        return bin;
    }

    public Order getOrder() {
        return order;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
