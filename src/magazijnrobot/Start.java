package magazijnrobot;

public class Start {

    public static void main(String[] args) {
        //Establishes a connection with the database.
//        DbConn.dbConnect();
//        new InventoryScreen();
        new OrderScreen();

//        new RobotScreen();
        Order order = new Order(18);
        Bin bin1 = new Bin(order, 15,15);
        Bin bin2 = new Bin(order, 16,16);
        Bin bin3 = new Bin(order, 17,17);
        RobotScreen robotScreen = new RobotScreen(bin1, bin2, bin3);


//        RobotScreen robotScreen = new RobotScreen(new Bin(new Order(69), 25, 25), new Bin(new Order(69), 25, 25),new Bin(new Order(69), 25, 25));
//        Inpak inpak = new Inpak(new Order(15), new Bin(new Order(15), 15, 15), new Bin(new Order(15), 15, 15), new Bin(new Order(15), 15, 15), robotScreen.getRobotDraw().getGraphics());


//        CoordinatePoint testCoordinate = new CoordinatePoint(0, 2);
//        Order testOrder = new Order(1);
//        Article testArticle0 = new Article(1, testCoordinate, 12);
//        Article testArticle1 = new Article(2, testCoordinate, 3);
//        Article testArticle2 = new Article(3, testCoordinate, 4.5);
//        Article testArticle3 = new Article(4, testCoordinate, 5);
//        Article testArticle4 = new Article(5, testCoordinate, 4);
//        Article testArticle5 = new Article(6, testCoordinate, 2);
//        Article testArticle6 = new Article(7, testCoordinate, 7);
//        Article testArticle7 = new Article(8, testCoordinate, 2);
//        Article testArticle8 = new Article(9, testCoordinate, 6);
//        Article testArticle9 = new Article(10, testCoordinate, 3);
//        testOrder.addArticle(testArticle0);
//        testOrder.addArticle(testArticle1);
//        testOrder.addArticle(testArticle2);
//        testOrder.addArticle(testArticle3);
//        testOrder.addArticle(testArticle4);
//        testOrder.addArticle(testArticle5);
//        testOrder.addArticle(testArticle6);
//        testOrder.addArticle(testArticle7);
//        testOrder.addArticle(testArticle8);
//        testOrder.addArticle(testArticle9);
//
//        ModifiedBestFit bppTest = new ModifiedBestFit(testOrder, 10, 3);
//        bppTest.packItems();
//        bppTest.printPackedItems();
    }

}
