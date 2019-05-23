import javax.swing.*;

public class ScreenManager extends Thread {
    private RobotScreen robotScreen;
    private RobotDraw robotDraw;
    private OrderScreen orderScreen;
    private InventoryScreen inventoryScreen;
    private JFrame currentScreen;
    private DbScreens dbScreens;
    private Main main;

    private OrderPick orderPick;
    private Inpak inpak;

    //starts screens and keeps refreshing robotscreen.
    public void run() {
        robotScreen.setUpOp(orderPick);
        robotScreen.setUpIp(inpak);
        robotDraw.setInpak(inpak);
        robotScreen.createScreen();
        robotDraw.setVisible(true);
        currentScreen = robotScreen;

        dbScreens = new DbScreens();
        dbScreens.setScreenManager(this);
        dbScreens.start();
        this.orderPick = main.getOrderPick();
        this.inpak = main.getInpak();
        while (true) {
            robotDraw.repaint();
            updateRobotScreen(orderPick, inpak);

                updateDbscreens();

            try {
                Thread.sleep(750);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
        }
    }

    //starts new thread for starting order- and inventoryscreen.
    public void startDbScreens() {
        Thread thread = new Thread(dbScreens);
        thread.start();
    }

    //updates robotscreen values
    public void updateRobotScreen(OrderPick orderPick, Inpak inpak) {
        robotScreen.updateOp(orderPick);
        robotScreen.updateIp(inpak);
        robotDraw.repaint();
    }

    //updates order- and inventoryscreen.
    public void updateDbscreens() {
        try {
            if(orderScreen.getIsEdited()) {
                orderScreen.refreshAllOrders();
            }
            if(inventoryScreen.getIsEdited()) {
                inventoryScreen.refreshInventoryScreen();
            }
        } catch(NullPointerException npe){
            //will arrive here when orderscreen or inventoryscreen isn't done loading yet
        }
    }

    //switches between screens.
    public void buttonPressed(String nameOfScreen) {
        if (nameOfScreen.equals("RobotScreen")) {
            if (currentScreen != robotScreen) {
                robotScreen.setVisible(true);
                robotDraw.setVisible(true);
                currentScreen.setVisible(false);
                currentScreen = robotScreen;
            }
        } else if (nameOfScreen.equals("OrderScreen") && orderScreen != null) {
            if (currentScreen != orderScreen) {
                orderScreen.setVisible(true);
                currentScreen.setVisible(false);
                currentScreen = orderScreen;
            }
        } else if (nameOfScreen.equals("InventoryScreen") && inventoryScreen != null) {
            if (currentScreen != inventoryScreen) {
                inventoryScreen.setVisible(true);
                currentScreen.setVisible(false);
                currentScreen = inventoryScreen;
            }
        }
    }

    public void createBinDialog(int binId) {
        new BinDialog(robotScreen.getJframe(), binId, main);
    }

    public void setRobotScreen(RobotScreen robotScreen) {
        this.robotScreen = robotScreen;
    }

    public void setRobotDraw(RobotDraw robotDraw) {
        this.robotDraw = robotDraw;
    }

    public void setOrderScreen(OrderScreen orderScreen) {
        this.orderScreen = orderScreen;
    }

    public void setInventoryScreen(InventoryScreen inventoryScreen) {
        this.inventoryScreen = inventoryScreen;
    }

    public RobotDraw getRobotDraw() {
        return robotDraw;
    }

    public void setOrderPick(OrderPick orderPick) {
        this.orderPick = orderPick;
    }

    public void setInpak(Inpak inpak) {
        this.inpak = inpak;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
