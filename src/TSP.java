import java.util.ArrayList;

public class TSP {
    private ArrayList<Article> articlesInput = new ArrayList<>();
    private ArrayList<Article> currentArticleOrder = new ArrayList<>();
    private ArrayList<Article> articlesOutput = new ArrayList<>();
    private double smallestDistance;

    public TSP (Article article1, Article article2, Article article3) {
        articlesInput.add(article1);
        articlesInput.add(article2);
        articlesInput.add(article3);
    }


    public void setInOrder() {
        int a;
        for (int i = 0; i < 3; i++) {
            for (int n = 0 + i; n < 3 + i; n++) {
                if (n >= 3) {
                    a = n - 3;
                } else {
                    a = n;
                }
                currentArticleOrder.add(articlesInput.get(a));
            }
            routeDistance(currentArticleOrder);
            currentArticleOrder.clear();
        }
    }

    private void routeDistance(ArrayList<Article> articles) {
        double afstand = 0;

        double xasPenalty = 1.36;
        double diagonalPenalty = 1.5;

        int x;
        int y;
        CoordinatePoint temp1;
        CoordinatePoint temp2;

        for (int i = 0; i < 2; i++) {
            temp1 = articles.get(i).getLocation();
            temp2 = articles.get(i+1).getLocation();

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
//            System.out.println(articles.get(0).getId() + " , " + afstand);

        }
        if (smallestDistance > afstand || smallestDistance == 0) {
//            System.out.println(smallestDistance + " - " + afstand);
            smallestDistance = afstand;
            if (articlesOutput.size() == 0) {
                articlesOutput.add(articles.get(0));
                articlesOutput.add(articles.get(1));
                articlesOutput.add(articles.get(2));
            } else {
                articlesOutput.set(0, articles.get(0));
                articlesOutput.set(1, articles.get(1));
                articlesOutput.set(2, articles.get(2));
            }
        }
    }

    public void printArticlesOutput() {
        for (Article a: articlesOutput) {
            System.out.println(a.getId());
        }
        System.out.println("totale afstand: " + smallestDistance);
    }

    public ArrayList<Article> getArticlesOutput() {
        return articlesOutput;
    }
}
