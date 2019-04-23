package magazijnrobot;

public class Article {

    private int id;
    private String name;
    private double weight;
    private int amountAvailable;
    private int amountReserved;
    private CoordinatePoint location;

    public Article(int id, String name, double weight, int amountAvailable, int amountReserved, CoordinatePoint location) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.amountAvailable = amountAvailable;
        this.amountReserved = amountReserved;
        this.location = location;
    }

    public Article(int id, String name, double weight, int amountAvailable, CoordinatePoint location) {
        this(id, name, weight, amountAvailable, 0, location);
    }
//comment

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public int getAmountReserved() {
        return amountReserved;
    }

    public CoordinatePoint getLocation() {
        return location;
    }

}
