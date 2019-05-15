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

    public void run() {
        robotScreen.setUpOp(orderPick);
        robotScreen.setUpIp(inpak);
        robotDraw.setInpak(inpak);
        robotScreen.createScreen();
        robotDraw.setVisible(true);
        currentScreen = robotScreen;

        dbScreens = new DbScreens();
        dbScreens.setScreenManager(this);
        this.orderPick = main.getOrderPick();
        this.inpak = main.getInpak();
        while (true) {
            robotDraw.repaint();
            updateRobotScreen(orderPick, inpak);
            try {
                Thread.sleep(750);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
        }
    }

    public DbScreens startDbScreens() {
        Thread thread = new Thread(dbScreens);
        thread.start();
        return this.dbScreens;
    }

    public void updateRobotScreen(OrderPick orderPick, Inpak inpak) {
        robotScreen.updateOp(orderPick);
        robotScreen.updateIp(inpak);
        robotDraw.repaint();
    }

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
