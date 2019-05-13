import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

public class Main {
    private ScreenManager screenManager;
    private boolean dbScreensUp = true;
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


    public void runMainAlgorithm() {
        screenManager.getRobotDraw().setInpak(inpak);
        while (true) {
            orderPick.setStatus("aan het picken");
            inpak.setStatus("wachten op OP");
            if (TSP_List != null) {
                TSP_List.clear();
            }

            //get new order from database.
            order.getNewOrderIdFromDb();
            //update robot screen to current order.

            //starts orderScreen and inventoryScreen. they have a slight delay because of database issues when executing queries simultaneously.
            if (dbScreensUp) {
                if (screenManager.startDbScreens() != null) {
                    dbScreensUp = false;
                } else {
                    screenManager.startDbScreens();
                    screenManager.start();
                }
            }

            //send both algorithms to work.
            bestFit = new ModifiedBestFit(order);

            //BPP algorithm gives a arraylist with bin objects. bin objects have an arraylist filled with article objects.
            //articles contains all articles of current order in bin order.
            BPP_List = bestFit.getArticleList();

            //sets Articles in fastest order for orderPick robot.
            if (BPP_List.size() >= 3) {
                for (int i = 1; i <= BPP_List.size() - BPP_List.size() % 3; i = i + 3) {
                    tsp_algorithm = new TSP_Algorithm(BPP_List.get(i - 1), BPP_List.get(i), BPP_List.get(i + 1));
                    for (Article a: tsp_algorithm.getArticles()) {
                        TSP_List.add(a);
                    }
                }
            } else {
                TSP_List = BPP_List;
            }

            //send algorithm outcomes to robots.
            //orderPick robot wants a string with coordinates. coordinate x: 3, y: 5 is send as 35.
//            orderPick.sendCoordinatesToArduino(TSP_List);

            //Inpak robot wants a string with coordinates. coordinate x: 3 is send as 3.
            inpak.sendCoordinatesToArduino(bestFit.getBinList(), TSP_List);



            //to-do list.
            //get information from robots.
            //send correct information to robotScreen.
            //keep updating robotScreen and keep sending coordinates to robots.
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                screenManager.updateRobotScreen(orderPick, inpak);
            }

            }
    }

    public Order getCurrentOrder() {
        return this.order;
    }
}
