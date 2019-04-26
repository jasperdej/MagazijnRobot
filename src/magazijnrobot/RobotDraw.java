package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class RobotDraw extends JPanel{
    private RobotScreen robotScreen;
    private OrderPick orderPickRobot;
    private Inpak inpakRobot;
    private Bin bin1;
    private Bin bin2;
    private Bin bin3;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Graphics2D g2;

    public  RobotDraw(RobotScreen robotScreen, OrderPick orderPickRobot, Inpak inpakRobot) {
        this.robotScreen = robotScreen;
        this.orderPickRobot = orderPickRobot;
        this.inpakRobot = inpakRobot;
        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension());
        setBackground(Color.GRAY);
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        g2 = (Graphics2D) graphics;
        g2.setStroke(new BasicStroke(10));
        g2.draw(new Line2D.Float(screenSize.width/2, 0, screenSize.width/2, screenSize.height/2));

//        graphics.drawLine();
    }
}
