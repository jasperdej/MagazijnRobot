import java.util.ArrayList;

public class OrderPick extends Robot{
    private CoordinatePoint currentLocation = new CoordinatePoint(0 , 0);
    private int amountOfArticlesPicked = 0;
    private String toCoords;

    public void sendCoordinatesToArduino(ArrayList<Article> articles){
        toCoords = "";
        for (int i = 1; i <= articles.size(); i++) {
            toCoords += Integer.toString(articles.get(i-1).getLocation().getX());
            toCoords += Integer.toString(articles.get(i-1).getLocation().getY());
            toCoords += ",";
            if (i % 3 == 0 || i == articles.size()) {
                toCoords += "03,02,11";
                if (i != articles.size()) {
                    toCoords+=",";
                }
            }
        }
        sendToCoords(toCoords);
    }

    public void sendToArduino(String string) {
        sendToCoords(string);
    }

    public CoordinatePoint getCurrentLocation() {
        return currentLocation;
    }

    public void setAmountOfArticlesPicked(int amountOfArticlesPicked) {
        this.amountOfArticlesPicked = amountOfArticlesPicked;
    }

    public int getAmountOfArticlesPicked() {
        return amountOfArticlesPicked;
    }
}
