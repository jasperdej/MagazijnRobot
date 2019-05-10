import javax.swing.*;

public class ScreenManager extends Thread {
    private RobotScreen robotScreen;
    private RobotDraw robotDraw;
    private OrderScreen orderScreen;
    private InventoryScreen inventoryScreen;
    private JFrame currentScreen;
    private DbScreens dbScreens;

    public void run() {
        robotScreen.updateOp(new OrderPick());
        robotScreen.updateIp(new Inpak());
        robotScreen.createScreen();
        robotDraw.setVisible(true);
        currentScreen = robotScreen;

        dbScreens = new DbScreens();
        dbScreens.setScreenManager(this);
    }

    public void startDbScreens() {
        Thread thread = new Thread(dbScreens);
        thread.start();
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
}
