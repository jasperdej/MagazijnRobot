
import java.awt.*;
import java.util.ArrayList;

public class Start {

    public static void main(String[] args) {
        TestScreen testScreen = new TestScreen();

        //gets screens up and running. it might take a while for orderscreen and inventoryscreen to load.
        //screenmanager starts a new thread. runs parallel to main program.
        //screens which get their information from database run on a seperate thread so screens remain operational and up-to-date.
        ScreenManager screenManager = new ScreenManager();
        RobotDraw robotDraw = new RobotDraw(screenManager);
        RobotScreen robotScreen = new RobotScreen(screenManager, robotDraw);
        screenManager.setRobotDraw(robotDraw);
        screenManager.setRobotScreen(robotScreen);
//        screenManager.start();

        Order order = new Order();
        ModifiedBestFit bestFit;
        ArrayList<Article> articles;

//        while (true) {
//            //get new order from database.
//            order.getNewOrderIdFromDb();
//
//            //send both algorithms to work.
//            bestFit = new ModifiedBestFit(order);
//
//            //BPP algorithm gives a arraylist with bin objects. bin objects have an arraylist filled with article objects.
//            //articles contains all articles of current order in bin order.
//            articles = new ArrayList<>(bestFit.getArticleList());
//            for (int i = 0; i < articles.size(); i++) {
//
//            }
//
//
//            //get Articles for next run.
//
//
//            //send algorithm outcomes to robots.
//            //get information from robots.
//            //send correct information to robotscreen.
//            //keep updating robotscreen and keep sending coordinates to robots.
//            /*updaten we robotscherm in een andere thread of in deze?*/
//
//        }
    }

}
