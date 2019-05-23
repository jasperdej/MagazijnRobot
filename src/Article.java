import java.awt.*;

public class Article {

    private int id;
    private CoordinatePoint location;
    private Double weight;
    private String name;
    private int amountPicked;
    private int amountReserved;

    //sets a few private variables.
    public Article(int id, int binLocation, double weight, String name, int amountReserved) {
        this.id = id;

        //splits two digit number from database into two separate digits. x coordinate is first digit, y coordinate is second digit.
        this.location = new CoordinatePoint(Integer.parseInt(Integer.toString(binLocation).substring(0, 1)), Integer.parseInt(Integer.toString(binLocation).substring(0, 2)) - Integer.parseInt(Integer.toString(binLocation).substring(0, 1)) * 10);
        this.weight = weight;
        this.name = name;
        this.amountPicked = 0;
        this.amountReserved = amountReserved;
    }

    //paints Article ID in warehouse.
    public void paintCoordinates(Graphics graphics) {
        //adds id to all articles in warehouse.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        int widthDiffLeft = width/2/5;
        int heightDiffLeft = height/2/5;

        graphics.drawString(Integer.toString(id), widthDiffLeft * location.getX() - widthDiffLeft/2 - 8, (5 *(heightDiffLeft-8) ) - location.getY() *(heightDiffLeft -8) + heightDiffLeft - (heightDiffLeft-8)/2+4 + 10);

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

    public int getAmountReserved() {
        return amountReserved;
    }

    public void printWeight() {
        System.out.println(weight);
    }

    public Double getSumWeight(){
        double sumWeight = Math.round((weight * amountReserved) * 1000) / 1000;
        return sumWeight;
    }
}
