public class DbScreens extends Thread{
    private OrderScreen orderScreen;
    private InventoryScreen inventoryScreen;
    private ScreenManager screenManager;

    //starts orderscreen and inventoryscreen
    @Override
    public void run() {
        orderScreen = new OrderScreen();
        screenManager.setOrderScreen(orderScreen);
        orderScreen.setScreenManager(screenManager);

        inventoryScreen = new InventoryScreen();
        screenManager.setInventoryScreen(inventoryScreen);
        inventoryScreen.setScreenManager(screenManager);
        return;
    }

    public void setScreenManager(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }
}
