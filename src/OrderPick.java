import arduino.Arduino;

import java.util.ArrayList;

public class OrderPick extends Robot{
    private CoordinatePoint currentLocation = new CoordinatePoint(0 , 0);
    private int amountOfArticlesPicked = 0;
    private String toCoords;

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
