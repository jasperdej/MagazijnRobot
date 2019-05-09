import java.util.ArrayList;

public class TSP_Algorithm {
    private double totaleAfstand;
    private CoordinatePoint nulpunt = new CoordinatePoint(0, 0);
    private CoordinatePoint[] volgorde = new CoordinatePoint[3];
    private CoordinatePoint[] CoordinatePoint = new CoordinatePoint[3];
    private CoordinatePoint temp1;
    private CoordinatePoint temp2;
    private ArrayList<Article> articles;

    public TSP_Algorithm(Article a1, Article a2, Article a3) {
        articles.add(a1);
        articles.add(a2);
        articles.add(a3);

        CoordinatePoint[0] = a1.getLocation();
        CoordinatePoint[1] = a2.getLocation();
        CoordinatePoint[2] = a3.getLocation();
        zetInVolgorde();
    }

    private void zetInVolgorde(){
        berekenRouteAfstand();

        for (int x = 0; x < 2; x++) {
            shuffleArray();
            berekenRouteAfstand();
        }
    }

    private void berekenRouteAfstand () {
        double afstand = 0;
        double xasPenalty = 1.36;
        double diagonalPenalty = 1.5;
        int x;
        int y;
        temp1 = nulpunt;

        for (int i = 0; i < 4; i++) {
            if (i < 3) {
                temp2 = CoordinatePoint[i];
            } else {
                temp2 = nulpunt;
            }

            if (temp1.getX() < temp2.getX()) {
                x = (temp2.getX() - temp1.getX());
            } else {
                x = (temp1.getX() - temp2.getX());
            }

            if (temp1.getY() < temp2.getY()) {
                y = temp2.getY() - temp1.getY();
            } else {
                y = temp1.getY() - temp2.getY();
            }

            if (x > y && x != 0 && y != 0) {
                afstand += (x-y) * xasPenalty + y * diagonalPenalty;
            } else if (x < y && x != 0 && y != 0) {
                afstand += y - x + x * diagonalPenalty;
            } else if (y != 0){
                afstand += y;
            } else {
                afstand += x * xasPenalty;
            }
            temp1 = temp2;
        }

        if (totaleAfstand > afstand || totaleAfstand == 0) {
            totaleAfstand = afstand;
            volgorde[0] = CoordinatePoint[0];
            volgorde[1] = CoordinatePoint[1];
            volgorde[2] = CoordinatePoint[2];
        }

    }

    private void shuffleArray() {
        temp1 = CoordinatePoint[0];
        temp2 = CoordinatePoint[1];

        CoordinatePoint[0] = CoordinatePoint[2];
        CoordinatePoint[1] = temp1;
        CoordinatePoint[2] = temp2;

    }

    public ArrayList<Article> getArticles() {
        ArrayList<Article> returnArticles = new ArrayList<>();
        for (CoordinatePoint c : volgorde) {
            for (Article a : articles) {
                if (c.getX() == a.getLocation().getX() && c.getY() == a.getLocation().getY()) {
                    returnArticles.add(a);
                }
            }
        }
        return returnArticles;
    }


    public void printVolgorde() {
        for (CoordinatePoint c : volgorde) {
            System.out.println(c.toString());
        }
    }
}
