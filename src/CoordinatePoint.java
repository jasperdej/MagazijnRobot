public class CoordinatePoint {
    private int x;
    private int y;

    //sets x and y coordinate
    public CoordinatePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    //used for printing coordinatepoints on robotscreen
    public String toString() {
        return ("x: " + x + ", y: " + y);
    }
}
