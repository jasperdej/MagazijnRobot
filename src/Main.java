import java.util.ArrayList;

public class Main {
    private ScreenManager screenManager;
    private boolean isFirstTime = true;
    private Order order = new Order();
    private OrderPick orderPick = new OrderPick();
    private Inpak inpak = new Inpak();

    private ModifiedBestFit bestFit;
    private ArrayList<Article> BPP_List = new ArrayList<>();

    private TSP_Algorithm tsp_algorithm;
    private ArrayList<Article> TSP_List;

    public Main(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    private int i = 0;

    public void runMainAlgorithm() {
        while (true) {
            i++;
            orderPick.setStatus("wachten op Coördinaten.");
            inpak.setStatus("wachten op Coördinaten." + i);
            TSP_List = new ArrayList<>();

            //get new order from database.
            order.getNewOrderIdFromDb();

            //starts orderScreen and inventoryScreen. they have a slight delay because of database issues when executing queries simultaneously.
            if (isFirstTime) {
                screenManager.startDbScreens();
                isFirstTime = false;
            }

            //send both algorithms to work.
            bestFit = new ModifiedBestFit(order);

            //BPP algorithm gives a arraylist with bin objects. bin objects have an arraylist filled with article objects.
            //articles contains all articles of current order in bin order.
            BPP_List = bestFit.getArticleList();

            //sets Articles in fastest order for orderPick robot.
            if (BPP_List.size() > 3) {
                for (int i = 0; i < BPP_List.size() - BPP_List.size() % 3; i = i + 3) {
                    tsp_algorithm = new TSP_Algorithm(BPP_List.get(i), BPP_List.get(i + 1), BPP_List.get(i + 2));
                    for (Article a: tsp_algorithm.getArticles()) {
                        TSP_List.add(a);
                    }
                }
            }

            //send algorithm outcomes to robots.
            //orderPick robot wants a string with coordinates. coordinate x: 3, y: 5 is send as 35.
//            orderPick.sendCoordinatesToArduino(TSP_List);

            //Inpak robot wants a string with coordinates. coordinate x: 3 is send as 3.
//            inpak.sendCoordinatesToArduino(bestFit.getBinList(), TSP_List);


            //to-do list.
            //get information from robots.
            //send correct information to robotScreen.
            //keep updating robotScreen and keep sending coordinates to robots.
            try {
                Thread.sleep(20000);
            } catch (Exception e) {
                System.out.println(e);
            }

            }
    }

    public Order getCurrentOrder() {
        return this.order;
    }
}
