public class Article {

    private int id;
    private CoordinatePoint location;
    private Double weight;
    private String name;
    private int amountPicked;
    private int amountReserved;

    public Article(int id, int binLlocation, double weight, String name, int amountReserved) {
        this.id = id;

        //splits two digit number from database into two separate digits. x coordinate is first digit, y coordinate is second digit.
        this.location = new CoordinatePoint(Integer.parseInt(Integer.toString(binLlocation).substring(0, 1)), Integer.parseInt(Integer.toString(binLlocation).substring(0, 2)) - Integer.parseInt(Integer.toString(binLlocation).substring(0, 1)) * 10);
        this.weight = weight;
        this.name = name;
        this.amountPicked = 0;
        this.amountReserved = amountReserved;
    }


    public int getId() {
        return id;
    }

    public CoordinatePoint getLocation() {
        return location;
    }

    public Double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public int getAmountPicked() {
        return amountPicked;
    }

    public int getAmountReserved() {
        return amountReserved;
    }

    public void printWeight() {
        System.out.println(weight);
    }
}
