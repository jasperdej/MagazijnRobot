import arduino.Arduino;

import java.util.ArrayList;

public class OrderPick extends Robot{
    private CoordinatePoint currentLocation;
    private int amountOfArticlesPicked;
    private String toCoords;

    public OrderPick () {
        currentLocation = new CoordinatePoint(0, 0);
        status = "a";
    }

    public void sendCoordinatesToArduino(ArrayList<Article> articles){
        toCoords = "";
        for (int i = 0; i < articles.size(); i++) {
            toCoords += Integer.toString(articles.get(i).getLocation().getX());
            toCoords += Integer.toString(articles.get(i).getLocation().getY());
            toCoords += ",";
            if (i+1 % 3 == 0 || i == articles.size()-1) {
                toCoords += "0,";
            }
        }
        sendToCoords(toCoords);
    }

    public CoordinatePoint getCurrentLocation() {
        return currentLocation;
    }

    public int getAmountOfArticlesPicked() {
        return amountOfArticlesPicked;
    }
}
