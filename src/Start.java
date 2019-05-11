
import java.awt.*;
import java.util.ArrayList;

public class Start {

    public static boolean dbScreensDoneLoading = true;

    public static void main(String[] args) {
        //gets screens up and running. it might take a while for orderscreen and inventoryscreen to load.
        //screenmanager starts a new thread. runs parallel to main program.
        //screens which get their information from database run on a seperate thread so screens remain operational and up-to-date.
        ScreenManager screenManager = new ScreenManager();
        Main mainAlgorithm = new Main(screenManager);
        RobotDraw robotDraw = new RobotDraw(screenManager, mainAlgorithm);
        RobotScreen robotScreen = new RobotScreen(screenManager, robotDraw, mainAlgorithm);
        screenManager.setRobotDraw(robotDraw);
        screenManager.setRobotScreen(robotScreen);

        //has to be last thing done in start class, it is an endless loop.
        mainAlgorithm.runMainAlgorithm();

    }

}