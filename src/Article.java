public class Article {

    private int id;
    private String name;
    private Double weight;
    private int amountAvailable;
    private int amountReserved;
    private CoordinatePoint location;

    public Article(int id, CoordinatePoint location, double weight/*weight = temporary*/) {
        this.id = id;
        this.location = location;
        //aangevuld uit de database
        this.weight = weight;//temporary
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getWeight() {
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

    public void printWeight() {
        System.out.println(weight);
    }

}
