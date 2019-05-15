import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TransferQueue;

public class Main {
    private ArduinoConn arduinoConn = new ArduinoConn();
    private ScreenManager screenManager;
    private boolean dbScreensUp = true;
    private Order order = new Order();

    private OrderPick orderPick = new OrderPick();
    private Inpak inpak = new Inpak();

    private ModifiedBestFit bestFit;
    private ArrayList<Article> BPP_List = new ArrayList<>();
    private ArrayList<Bin> finalBinList = new ArrayList<>();

    private TSP_Algorithm tsp_algorithm;
    private ArrayList<Article> TSP_List;

    public Main(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }


    int i = 0;

    public void runMainAlgorithm() {
        //starts orderScreen and inventoryScreen. they have a slight delay because of database issues when executing queries simultaneously.
        screenManager.setInpak(inpak);
        screenManager.setOrderPick(orderPick);
        screenManager.start();


        arduinoConn.arduinoConnectInpakRobot();
        inpak.setStatus("verbonden");
//        arduinoConn.arduinoConnectPickRobot();
        orderPick.setStatus("verbonden");

        boolean opConnected = true;
        boolean ipConnected = false;

        while (!opConnected || !ipConnected) {
//            if (orderPick.recievedFromArduino().contains("Orderpick")) {
//                opConnected = true;
//            }
            if (inpak.recievedFromArduino().contains("Inpak")) {
                ipConnected = true;
            }
        }


        while (true) {
            boolean isOrderDone = false;
            boolean isOpDone = false;
            orderPick.setStatus("Wachten op actie");
            inpak.setStatus("Wachten op actie");

            orderPick.setAmountOfArticlesPicked(0);
            inpak.setAmountOfArticlesPacked(0);
            i++;
            if (TSP_List != null) {
                TSP_List.clear();
            }

            if (BPP_List != null) {
                BPP_List.clear();
            }

            if (finalBinList != null) {
                finalBinList.clear();
            }

            //get new order from database.
            order.getNewOrderIdFromDb();
            System.out.println("orderid: " + order.getOrderNr());
            //update robot screen to current order.


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
            finalBinList = inpak.sendCoordinatesToArduino(bestFit.getBinList(), TSP_List);
            orderPick.setStatus("Aan het picken");
            inpak.setStatus("Aan het inpakken");
            System.out.println(inpak.getStatus());

            //ints for executing loop and updating robotscreen.
            int amountPackedIp = 0;
            int amountPickedOp = 0;
            int totalArticlesInOrder = TSP_List.size();

            while (!isOrderDone) {
                String recievedFromInpak = inpak.recievedFromArduino();
//                String recievedFromOrderpick = orderPick.recievedFromArduino();
                System.out.println("INPAK: " + recievedFromInpak);
//                System.out.println("ORDERPICK: " + recievedFromOrderpick);

//                if (recievedFromOrderpick.contains("packed")) {
//                    amountPickedOp++;
//                    System.out.println("ORDERPICK PICKED: " + amountPickedOp);
//                    orderPick.setAmountOfArticlesPicked(amountPickedOp);
//                    if (amountPickedOp == totalArticlesInOrder) {
//                        isOpDone = true;
//                    }
//                }

                isOpDone = true;
                if (recievedFromInpak.contains("Scanned")) {
                    inpak.setCurrentBin(finalBinList.get(amountPackedIp).getBinNumber());
                    amountPackedIp++;
                    inpak.setAmountOfArticlesPacked(amountPackedIp);
                    System.out.println("INPAK PACKED: " + amountPackedIp);
                    if (amountPackedIp == totalArticlesInOrder && isOpDone) {
                        isOrderDone = true;
                        System.out.println("done");
                    }
                }
            }

            //to-do list.
            //get information from robots.
            //send correct information to robotScreen.
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            } finally {
                //updates database. order is now picked and status is changed to "verwerkt".
                System.out.println("volgende loop");
//                updateDatabase();
            }

        }
    }

    public void updateDatabase() {
        if (!Start.dbDoneLoading) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            updateDatabase();
        }  else {
            Start.dbDoneLoading = false;
        }
        DbConn dbConn = new DbConn();
        DbConn.dbConnect();
        dbConn.updateDb("UPDATE orders SET status = 'verwerkt' WHERE orderid = " + order.getOrderNr());
        dbConn.killStatement();
        DbConn.dbKill();
        Start.dbDoneLoading = true;
    }

    public Order getCurrentOrder() {
        return this.order;
    }

    public Inpak getInpak() {
        return inpak;
    }

    public OrderPick getOrderPick() {
        return orderPick;
    }
}
