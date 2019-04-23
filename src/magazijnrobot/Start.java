package magazijnrobot;

public class Start {

    public static void main(String[] args) {
        Route r1 = new Route(new CoordinatePoint(1, 5), new CoordinatePoint(2, 1), new CoordinatePoint(6, 6));
        r1.printVolgorde();
    }

}
