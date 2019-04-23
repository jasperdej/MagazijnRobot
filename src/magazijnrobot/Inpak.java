package magazijnrobot;

public class Inpak extends Robot {
    private int amountOfArticlesPacked;

    public Inpak(Order order) {
        super(order);
        status = "a";
    }
}
