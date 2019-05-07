public class OrderPick extends Robot{
    private CoordinatePoint currentLocation;
    private int amountOfArticlesPicked;



    public static void sendToCoords(String coord){
        ArduinoConn.arduinoInpakRobot.serialWrite(coord);
        System.out.println("Coords gestuurd");
    }



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
