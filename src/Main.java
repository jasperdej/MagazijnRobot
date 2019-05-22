import java.util.ArrayList;

public class Main {
    private ArduinoConn arduinoConn = new ArduinoConn();
    private ScreenManager screenManager;
    private boolean dbScreensUp = true;
    private Order order = new Order();

    private OrderPick orderPick = new OrderPick();
    private Inpak inpak = new Inpak();

    private BPP bestFit;
    private ArrayList<Article> BPP_List = new ArrayList<>();
    private ArrayList<Bin> finalBinList = new ArrayList<>();

    private CoordinatePoint coordinatePointOP = new CoordinatePoint(0, 0);
    private TSP tsp = new TSP();
    private ArrayList<Article> TSP_List;

    private Customer customer = new Customer(order);
    private Pakbon pakbon = new Pakbon(customer);

    private int binId1 = 1;
    private int binId2 = 2;
    private int binId3 = 3;

    private boolean isPaused = false;
    private boolean isReset = false;

    public Main(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }


    public void runMainAlgorithm() {
        //starts orderScreen and inventoryScreen. they have a slight delay because of database issues when executing queries simultaneously.
        screenManager.setInpak(inpak);
        screenManager.setOrderPick(orderPick);
        screenManager.startDbScreens();
        screenManager.getRobotDraw().setCoordinatePointOp(orderPick.getCurrentLocation());
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

            binId1 = 1;
            binId2 = 2;
            binId3 = 3;
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

            bestFit = new BPP(order);
            tsp = new TSP();

            //BPP algorithm gives a arraylist with bin objects. bin objects have an arraylist filled with article objects.
            //articles contains all articles of current order in bin order.
            BPP_List = bestFit.getArticleList();

            //sets Articles in fastest order for orderPick robot.
            //3 articles are sent to Tsp algorithm, this shuffles the bpp order slightly.
            //this is done in order to maintain bin order slightly
            System.out.println("BPPlength: " + BPP_List.size());
            for (Article a: BPP_List) {
                System.out.println(a.getId());
            }
            if (BPP_List.size() >= 3) {
                for (int i = 1; i <= BPP_List.size() - BPP_List.size() % 3; i = i + 3) {
                    tsp.setArticlesInput(BPP_List.get(i - 1), BPP_List.get(i), BPP_List.get(i + 1));
                    tsp.setInOrder();
                    TSP_List = tsp.getArticlesOutput();
                    System.out.println("in de if-statement " + i);
                }
                System.out.println("tsplength: " + TSP_List.size());
                if (BPP_List.size() % 3 == 1) {
                    TSP_List.add(BPP_List.get(BPP_List.size()-1));
                } else if (BPP_List.size() % 3 == 2) {
                    TSP_List.add(BPP_List.get(BPP_List.size() - 1));
                    TSP_List.add(BPP_List.get(BPP_List.size() - 2));
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
            inpak.setCurrentBin(finalBinList.get(0).getBinNumber());

            //ints for executing loop and updating robotscreen.
            int amountPackedIp = 0;
            int amountPickedOp = 0;
            int totalArticlesInOrder = TSP_List.size();
            int i = 0;

            while (!isOrderDone) {
                String recievedFromInpak = inpak.recievedFromInpak();
                String recievedFromOrderpick = orderPick.recievedFromOrderpick();

                //prints string recieved form orderpickrobot.
                if (recievedFromOrderpick.length() != 0) {
//                    System.out.println("ORDERPICK: " + recievedFromOrderpick);
                }

                //prints string recieved from inpakrobot.
                if (recievedFromInpak.length() !=0) {
                    System.out.println("INPAK: " + recievedFromInpak);
                }

                //gets coordinatepoint send by orderpickrobot.
                if (!recievedFromOrderpick.contains("Packed") && recievedFromOrderpick.length() != 0) {
                    String coordinateOrderpick = (recievedFromOrderpick.substring(0,2).replaceAll("\\uFEFF", "").replaceAll("\n", "")).trim();
                    if (coordinateOrderpick.length() == 2) {
                        if (Integer.parseInt(coordinateOrderpick) >= 11 || Integer.parseInt(coordinateOrderpick) == 01 || Integer.parseInt(coordinateOrderpick) == 02 || Integer.parseInt(coordinateOrderpick) == 03) {
                            orderPick.getCurrentLocation().setX(Integer.parseInt(recievedFromOrderpick.substring(0,1)));
                            orderPick.getCurrentLocation().setY(Integer.parseInt(recievedFromOrderpick.substring(1,2)));
                        }
                    }
                }

                if (recievedFromOrderpick.contains("Packed")) {
                    if (TSP_List.size() - amountPickedOp >= 3) {
                        amountPickedOp = amountPickedOp + 3;
                    } else {
                        amountPickedOp = TSP_List.size();
                    }
                    orderPick.setAmountOfArticlesPicked(amountPickedOp);
                    if (amountPickedOp == totalArticlesInOrder) {
                        isOpDone = true;
                    }
                }

                if (recievedFromInpak.contains("Scanned") && i < order.getAmountOfArticles()) {
                    amountPackedIp++;

                    //updates amount packed for robotscreen. robotscreen automatically updates 4/3 times a second.
                    inpak.setAmountOfArticlesPacked(amountPackedIp);

                    //sets new bin as current bin.

                    //opens a new bin dialog when a bin is full.
                    boolean lastOfCurrentBin = true;
                    if (inpak.binPercentageFilled(inpak.getCurrentBin()) < 100) {
                        lastOfCurrentBin = false;
//                        System.out.println("bin% = " + inpak.binPercentageFilled(inpak.getCurrentBin()) + "succeeded" + " -- currentbinnr " + inpak.getCurrentBin());
                    } else {
//                        System.out.println("bin% = " + inpak.binPercentageFilled(inpak.getCurrentBin()) + "failed" + " -- currentbinnr " + inpak.getCurrentBin());
                    }

                    if (lastOfCurrentBin) {
                        screenManager.createBinDialog(inpak.getCurrentBin());
                        if (finalBinList.contains(inpak.getCurrentBin()+3)) {
                            if (inpak.getCurrentBin() % 3 == 0) {
                                binId1 = inpak.getCurrentBin()+3;
                            } else if (inpak.getCurrentBin() % 3 == 1) {
                                binId2 = inpak.getCurrentBin()+3;
                            } else {
                                binId3 = inpak.getCurrentBin()+3;
                            }
                        }
                    }

                    i++;
                    if (i != finalBinList.size()) {
//                        System.out.println("i: " + i + "newbinid: " + finalBinList.get(i).getBinNumber() + "currentid: " + inpak.getCurrentBin());
                        inpak.setCurrentBin(finalBinList.get(i).getBinNumber());
                        inpak.setBin(finalBinList.get(i));
                    }



                    System.out.println("INPAK PACKED: " + amountPackedIp);
                    if (amountPackedIp == totalArticlesInOrder) {
                        isIpDone = true;
                    }
                }
                if (isIpDone && isOpDone || isReset) {
                    isOrderDone = true;
                }
            }

            //updates database. order is now picked and status is changed to "verwerkt".
            //if reset button is pressed, database is not updated because orderpicking has failed catastrophically.
            if (!isReset) {
                updateDatabase();
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

    public void createPakBon(int binId) {
        if (inpak.getBin() == null) {
            for (Bin b: finalBinList) {
                if (b.getBinNumber() == inpak.getCurrentBin()) {
                    customer.setBin(b);
                }
            }
        } else {
            customer.setBin(inpak.getBin());
        }
        pakbon.createPakbon();
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

    public int getBinIdUknown(int i) {
        if (i % 3 == 1 || i == 1) {
            return binId1;
        } else if (i % 3 == 2 || i == 2) {
            return binId2;
        } else {
            return binId3;
        }
    }

    public void setBinId1(int binId1) {
        this.binId1 = binId1;
    }

    public void setBinId2(int binId2) {
        this.binId2 = binId2;
    }

    public void setBinId3(int binId3) {
        this.binId3 = binId3;
    }

    public ArrayList<Bin> getFinalBinList() {
        return finalBinList;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        System.out.println("isPaused: " + isPaused);
    }

    public void setReset(boolean reset) {
        isReset = reset;
        System.out.println("isReset: " + isReset);
    }

    public boolean isReset() {
        return isReset;
    }
}
