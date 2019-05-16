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

    private TSP tsp;
    private ArrayList<Article> TSP_List;

    private int binId1 = 1;
    private int binId2 = 2;
    private int binId3 = 3;

    private boolean isPaused = false;

    public Main(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }


    public void runMainAlgorithm() {
        //starts orderScreen and inventoryScreen. they have a slight delay because of database issues when executing queries simultaneously.
        screenManager.setInpak(inpak);
        screenManager.setOrderPick(orderPick);
        screenManager.startDbScreens();
        screenManager.start();

        //connects both arduino's, first the orderpick and then the inpak.
        // the arduino's keep sending "orderpick" and "inpak" untill a succesfull connection is established.
        arduinoConn.arduinoConnectInpakRobot();
        while (inpak.recievedFromInpak().contains("Inpak")) {
            System.out.println("aan het verbinden ip");
        }
        inpak.setStatus("verbonden");

        arduinoConn.arduinoConnectPickRobot();
        while (orderPick.recievedFromOrderpick().contains("Orderpick")) {
            System.out.println("aan het verbinden op");
        }
        orderPick.setStatus("verbonden");

        while (true) {
            boolean isOrderDone = false;
            boolean isOpDone = false;
            boolean isIpDone = false;
            orderPick.setStatus("Wachten op actie");
            inpak.setStatus("Wachten op actie");

            orderPick.setAmountOfArticlesPicked(0);
            inpak.setAmountOfArticlesPacked(0);
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

            //send both algorithms to work.
            //BPP algorithm sets articles in order they are to be packed.
            // TSP shuffles that order slightly, this makes the orderpick robot take a more efficient route.
            bestFit = new ModifiedBestFit(order);

            //BPP algorithm gives a arraylist with bin objects. bin objects have an arraylist filled with article objects.
            //articles contains all articles of current order in bin order.
            BPP_List = bestFit.getArticleList();

            //sets Articles in fastest order for orderPick robot.
            //3 articles are sent to Tsp algorithm, this shuffles the bpp order slightly.
            //this is done in order to maintain bin order slightly
            if (BPP_List.size() >= 3) {
                for (int i = 1; i <= BPP_List.size() - BPP_List.size() % 3; i = i + 3) {
                    tsp = new TSP(BPP_List.get(i - 1), BPP_List.get(i), BPP_List.get(i + 1));
                    for (Article a: tsp.getArticlesOutput()) {
                        TSP_List.add(a);
                    }
                }
            } else {
                TSP_List = BPP_List;
            }

            //send algorithm outcomes to robots.
            //orderPick robot wants a string with coordinates. coordinate x: 3, y: 5 is send as 35.
            orderPick.sendCoordinatesToArduino(TSP_List);
            orderPick.setStatus("Aan het picken");

            //Inpak robot wants a string with coordinates. coordinate x: 3 is send as 3.
            finalBinList = inpak.sendCoordinatesToArduino(bestFit.getBinList(), TSP_List);
            inpak.setStatus("Aan het inpakken");

            //ints for executing loop and updating robotscreen.
            int amountPackedIp = 0;
            int amountPickedOp = 0;
            int totalArticlesInOrder = TSP_List.size();

            while (!isOrderDone) {
                String recievedFromInpak = inpak.recievedFromInpak();
                String recievedFromOrderpick = orderPick.recievedFromOrderpick();
                if (recievedFromOrderpick.length() != 0) {
                    System.out.println("ORDERPICK: " + recievedFromOrderpick);
                }

                if (recievedFromInpak.length() !=0) {
                    System.out.println("INPAK: " + recievedFromInpak);
                }

                if (recievedFromOrderpick.contains("Packed")) {
                    amountPickedOp++;
                    System.out.println("ORDERPICK PICKED: " + amountPickedOp);
                    orderPick.setAmountOfArticlesPicked(amountPickedOp);
                    if (amountPickedOp == totalArticlesInOrder) {
                        isOpDone = true;
                    }
                }

                if (recievedFromInpak.contains("Scanned")) {
                    boolean lastOfCurrentBin = true;
                    inpak.setCurrentBin(finalBinList.get(amountPackedIp).getBinNumber());
                    amountPackedIp++;
                    inpak.setAmountOfArticlesPacked(amountPackedIp);

                    for (int i = amountPackedIp; i < finalBinList.size(); i++) {
                        if (finalBinList.get(i-1).getBinNumber() == inpak.getCurrentBin()) {
                            lastOfCurrentBin = false;
                        }
                    }
                    if (lastOfCurrentBin) {
                        if (finalBinList.contains(inpak.getCurrentBin()+3)) {
                            if (inpak.getCurrentBin() % 3 == 0) {
                                binId1 = inpak.getCurrentBin()+3;
                            } else if (inpak.getCurrentBin() % 3 == 1) {
                                binId2 = inpak.getCurrentBin()+3;
                            } else {
                                binId3 = inpak.getCurrentBin()+3;
                            }
                        }
                        screenManager.createBinDialog(inpak.getCurrentBin());
                    }

                    System.out.println("INPAK PACKED: " + amountPackedIp);
                    if (amountPackedIp == totalArticlesInOrder) {
                        isIpDone = true;
                    }
                }
                if (isIpDone && isOpDone) {
                    isOrderDone = true;
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
            while (isPaused) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ie) {
                    System.out.println(ie);
                }
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
        screenManager.updateDbscreens();
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

    public int getBinId1() {
        return binId1;
    }

    public int getBinId2() {
        return binId2;
    }

    public int getBinId3() {
        return binId3;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
