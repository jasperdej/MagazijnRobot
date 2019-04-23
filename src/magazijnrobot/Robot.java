package magazijnrobot;

public abstract class Robot {
    protected String status;
    protected Order order;

    public Robot (Order order) {
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public Order getOrder() {
        return order;
    }
}
