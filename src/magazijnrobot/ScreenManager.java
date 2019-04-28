package magazijnrobot;

import javax.swing.*;

public class ScreenManager extends Thread {
    private RobotScreen robotScreen;
    private RobotDraw robotDraw;
    private OrderScreen orderScreen;
    private InventoryScreen inventoryScreen;
    private JFrame currentScreen;

    public void run() {
        robotScreen.updateOp(new OrderPick(new Order(15)));
        robotScreen.updateIp(new Inpak(new Order(16), new Bin(new Order(15), 15, 15), new Bin(new Order(15), 15, 15), new Bin(new Order(15), 15, 15)));
        robotScreen.createScreen();
        robotScreen.setVisible(true);
        robotDraw.setVisible(true);
        currentScreen = robotScreen;

        Thread orderScreenThread = new Thread(orderScreen);
        orderScreenThread.start();

        Thread inventoryScreenThread = new Thread(inventoryScreen);
        inventoryScreenThread.start();

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

    public void buttonPressed(String nameOfScreen) {
        System.out.println(nameOfScreen);
        if (nameOfScreen.equals("RobotScreen")) {
//            if (currentScreen != robotScreen) {
                currentScreen.setVisible(false);
                robotScreen.setVisible(true);
//            }
        } else if (nameOfScreen.equals("OrderScreen")) {
            if (currentScreen != orderScreen && orderScreen.isReady()) {
                currentScreen.setVisible(false);
                orderScreen.setVisible(true);
            }
        } else if (nameOfScreen.equals("InventoryScreen")) {
            if (currentScreen != inventoryScreen && inventoryScreen.isReady()) {
                currentScreen.setVisible(false);
                inventoryScreen.setVisible(true);
            }
        }
    }

}
