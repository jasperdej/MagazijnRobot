package magazijnrobot;

public class OrderPick extends Robot{
    private CoordinatePoint currentLocation;
    private int amountOfArticlesPicked;


    public OrderPick (Order order) {
        super(order);
        currentLocation = new CoordinatePoint(0, 0);
        status = "a";
    }

    public CoordinatePoint getCurrentLocation() {
        return currentLocation;
    }

    public int getAmountOfArticlesPicked() {
        return amountOfArticlesPicked;
    }
}
