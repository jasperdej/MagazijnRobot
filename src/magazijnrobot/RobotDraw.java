package magazijnrobot;

import javax.swing.*;

public class RobotDraw extends JPanel {
    private RobotScreen robotScreen;
    private OrderPick orderPickRobot;
    private Inpak inpakRobot;
    private Bin bin1;
    private Bin bin2;
    private Bin bin3;


    public  RobotDraw(RobotScreen robotScreen, OrderPick orderPickRobot, Inpak inpakRobot) {
        this.robotScreen = robotScreen;
        this.orderPickRobot = orderPickRobot;
        this.inpakRobot = inpakRobot;
    }
}
