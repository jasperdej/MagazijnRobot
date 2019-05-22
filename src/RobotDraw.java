import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class RobotDraw extends JPanel{
    private ScreenManager screenManager;
    private Main main;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private Graphics2D g2;
    private Graphics graphics;
    private Inpak inpak;
    private int beltPosition = 0;
    private CoordinatePoint coordinatePointOp;

    public  RobotDraw(ScreenManager screenManager, Main main) {
        this.screenManager = screenManager;
        this.main = main;

        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension());
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
        int a;

        graphics.setColor(Color.BLACK);
        g2 = (Graphics2D) graphics;
        g2.setStroke(new BasicStroke(8));

        //left side of screen. draws warehouse.
        for (int i = 0; i <= 5; i++) {
            g2.draw(new Line2D.Float((screenSize.width/2)*i/5, 0, screenSize.width/2*i/5, screenSize.height/2)); // draws vertical lines
            g2.draw(new Line2D.Float(0, screenSize.height * i/11+4, screenSize.width/2, screenSize.height * i/11+4));
        }
        //adds coordinates to all locations in warehouse.
        for (int i = 1; i <=5 ; i++){
            for (int n = 1; n <= 5; n++) {
                graphics.drawString(i + " - " + n, widthDiffLeft * i - widthDiffLeft/2 - 8, (5 *(heightDiffLeft-8) ) - n*(heightDiffLeft -8) + heightDiffLeft - (heightDiffLeft-8)/2+4 - 20);
            }
        }

        g2.drawRect(widthDiffLeft * coordinatePointOp.getX() - widthDiffLeft/2 - 8 - widthDiffLeft/4, (5 *(heightDiffLeft-8) ) - coordinatePointOp.getY() *(heightDiffLeft -8) + heightDiffLeft - (heightDiffLeft-8)/2+4 - 20, widthDiffLeft/2, heightDiffLeft/2);
//        graphics.draw3DRect(widthDiffLeft * coordinatePointOp.getX() - widthDiffLeft/2 - 8 - widthDiffLeft/4, (5 *(heightDiffLeft-8) ) - coordinatePointOp.getY() *(heightDiffLeft -8) + heightDiffLeft - (heightDiffLeft-8)/2+4 - 20, widthDiffLeft/2, heightDiffLeft/2,true);

        //right side of screen. draws conveyor belt and bins.
        g2.draw(new Rectangle.Float(width/2 + width/50, heightDiffRight, width/3, heightDiffRight - 40));
        for (int i = 1; i <= 3; i++){

            int currentBin = inpak.getCurrentBin();
            if (currentBin == 1 || currentBin % 3 == 1) {
                if (i == 1) {
                    setColor(main.getBinId3());
                } else if (i == 2) {
                    setColor(main.getBinId1());
                } else {
                    setColor(main.getBinId2());
                }
            } else if (currentBin == 2 || currentBin % 3 == 2) {
                if (i == 1) {
                    setColor(main.getBinId1());
                } else if (i == 2) {
                    setColor(main.getBinId2());
                } else {
                    setColor(main.getBinId3());
                }
            } else {
                if (i == 1) {
                    setColor(main.getBinId2());
                } else if (i == 2) {
                    setColor(main.getBinId3());
                } else {
                    setColor(main.getBinId1());
                }
            }

            System.out.println("Variabele i: " + i + " - Color: " + graphics.getColor());
            System.out.println("=====================================================");
            //draws rectangle filled with color.
            graphics.fillRect(width - widthDiffRight - width/50,  (heightDiffRight) * i - heightDiffRight + 80 - 40 * i, widthDiffRight, heightDiffRight - 40);
            graphics.setColor(Color.BLACK);
            g2.draw(new Rectangle.Float(width - widthDiffRight - width/50,  (heightDiffRight) * i - heightDiffRight + 80 - 40 * i, widthDiffRight, heightDiffRight - 40));


        }

        if (main.getCurrentOrder().getOrderNr() != -1) {

            //draws all articles in warehouse.
            for (Article article : main.getCurrentOrder().getArticles()) {
                article.paintCoordinates(graphics);
            }

            //draws article on conveyor belt.
            if (inpak.getStatus().equals("Aan het inpakken")) {
                graphics.fillRect((screenSize.width / 2 + screenSize.width / 50 + 7) + beltPosition * screenSize.width / 10 + beltPosition * screenSize.width / 50, screenSize.width / 8 - 40, screenSize.height / 10 + 2, screenSize.height / 10 + 2);
                if (beltPosition == 2) {
                    beltPosition = 0;
                } else {
                    beltPosition++;
                }
            }

            //prints binpercentage and binID.
            if (inpak.getCurrentBin() == 1 || inpak.getCurrentBin() % 3 == 1) {
                graphics.drawString(Integer.toString(main.getBinId3()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) - heightDiffRight + 80 - 40 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId3()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) - heightDiffRight + 80 - 40 + (heightDiffRight - 40) / 2 + 10);

                graphics.drawString(Integer.toString(main.getBinId1()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 2 - heightDiffRight + 80 - 40 * 2 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId1()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 2 - heightDiffRight + 80 - 40 * 2 + (heightDiffRight - 40) / 2 + 10);

                graphics.drawString(Integer.toString(main.getBinId2()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 3 - heightDiffRight + 80 - 40 * 3 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId2()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 3 - heightDiffRight + 80 - 40 * 3 + (heightDiffRight - 40) / 2 + 10);


            } else if (inpak.getCurrentBin() == 2 || inpak.getCurrentBin() % 3 == 2) {
                graphics.drawString(Integer.toString(main.getBinId1()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) - heightDiffRight + 80 - 40 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId1()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) - heightDiffRight + 80 - 40 + (heightDiffRight - 40) / 2 + 10);

                graphics.drawString(Integer.toString(main.getBinId2()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 2 - heightDiffRight + 80 - 40 * 2 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId2()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 2 - heightDiffRight + 80 - 40 * 2 + (heightDiffRight - 40) / 2 + 10);

                graphics.drawString(Integer.toString(main.getBinId3()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 3 - heightDiffRight + 80 - 40 * 3 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId3()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 3 - heightDiffRight + 80 - 40 * 3 + (heightDiffRight - 40) / 2 + 10);


            } else {
                graphics.drawString(Integer.toString(main.getBinId2()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) - heightDiffRight + 80 - 40 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId2()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) - heightDiffRight + 80 - 40 + (heightDiffRight - 40) / 2 + 10);

                graphics.drawString(Integer.toString(main.getBinId3()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 2 - heightDiffRight + 80 - 40 * 2 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId3()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 2 - heightDiffRight + 80 - 40 * 2 + (heightDiffRight - 40) / 2 + 10);

                graphics.drawString(Integer.toString(main.getBinId1()), width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 3 - heightDiffRight + 80 - 40 * 3 + (heightDiffRight - 40) / 2 - 10);
                graphics.drawString(inpak.binPercentageFilled(main.getBinId1()) + "%", width - widthDiffRight - width / 50 + widthDiffRight / 2, (heightDiffRight) * 3 - heightDiffRight + 80 - 40 * 3 + (heightDiffRight - 40) / 2 + 10);

            }
        }

    }

    public void setInpak(Inpak inpak) {
        this.inpak = inpak;
    }

    public void setCoordinatePointOp(CoordinatePoint coordinatePointOp) {
        this.coordinatePointOp = coordinatePointOp;
    }

    private void setColor (int i) {
        System.out.println("binID: " + i);
        if (inpak.binPercentageFilled(main.getBinIdUknown(i)) == 0) {
            graphics.setColor(Color.green);
        } else if (inpak.binPercentageFilled(main.getBinIdUknown(i)) > 0 && inpak.binPercentageFilled(i) < 100) {
            graphics.setColor(Color.yellow);
        } else if (inpak.binPercentageFilled(main.getBinIdUknown(i)) == 100) {
            graphics.setColor(Color.RED);
        }
    }
}
