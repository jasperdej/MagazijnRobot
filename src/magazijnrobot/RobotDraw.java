package magazijnrobot;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class RobotDraw extends JPanel{
    private ScreenManager screenManager;
    private Bin bin1;
    private Bin bin2;
    private Bin bin3;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Graphics2D g2;
    private Graphics graphics;

    public  RobotDraw(ScreenManager screenManager) {
        this.screenManager = screenManager;
        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension());
        setBackground(Color.GRAY);
    }

    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.graphics = graphics;
        int width = screenSize.width;
        int height = screenSize.height;
        int widthDiffLeft = width/2/5;
        int heightDiffLeft = height/2/5;
        int widthDiffRight = width/8 - 40;
        int heightDiffRight = height/6;

        graphics.setColor(Color.BLACK);
        g2 = (Graphics2D) graphics;
        g2.setStroke(new BasicStroke(8));

        //left side
        for (int i = 0; i <= 5; i++) {
            g2.draw(new Line2D.Float((screenSize.width/2)*i/5, 0, screenSize.width/2*i/5, screenSize.height/2)); // draws vertical lines
            g2.draw(new Line2D.Float(0, screenSize.height * i/11+4, screenSize.width/2, screenSize.height * i/11+4));
        }
        for (int i = 1; i <=5 ; i++){
            for (int n = 1; n <= 5; n++) {
                graphics.drawString(i + " - " + n, widthDiffLeft * i - widthDiffLeft/2 - 8, (5 *(heightDiffLeft-8) ) - n*(heightDiffLeft -8) + heightDiffLeft - (heightDiffLeft-8)/2+4);
            }
        }

        //right side
        g2.draw(new Rectangle.Float(width/2 + width/50, heightDiffRight, width/3, heightDiffRight - 40));
        for (int i = 1; i <= 3; i++){
            g2.draw(new Rectangle.Float(width - widthDiffRight - width/50,  (heightDiffRight) * i - heightDiffRight + 80 - 40 * i, widthDiffRight, heightDiffRight - 40));
        }

    }

    public void changeVisible(boolean bool) {
        setVisible(bool);
    }
}
