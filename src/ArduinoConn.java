import arduino.*;

public class ArduinoConn {

    protected static Arduino arduinoPickRobot;
    protected static Arduino arduinoInpakRobot;
    private static boolean isPickArduino = false;
    private static boolean isInpakArduino = false;
    private static int i;

    //automatically searches for the right comm port and connects to PickRobot.
    public static void arduinoConnectPickRobot() {
        i=0;
        while(!isPickArduino) {
            i++;
            if (i > 15) {
                i = 1;
            }
                System.out.println("Poort: " + i);
                arduinoPickRobot = new Arduino("COM" + i, 9600);
                if (arduinoPickRobot.openConnection()) {
                    try {
                        Thread.sleep(1500);
                        String arduino = arduinoPickRobot.serialRead();
                        System.out.println(arduino);
                        if(arduino.contains("Orderpick")){
                            isPickArduino = true;
                            System.out.println("Ah yes, the port you were looking for...");
                            arduinoPickRobot.serialWrite("Connected");

                        }else{
                            System.out.println("Close, but not close enough");
                        }
                    } catch (Exception e) {
                    }
                }
        }
    }

    //automatically searches for the right comm port and connects to InpakRobot.
    public static void arduinoConnectInpakRobot() {
        i=0;
        while(!isInpakArduino) {
            i++;
            if (i > 15) {
                i = 1;
            }
            System.out.println("Poort: " + i);
            arduinoInpakRobot = new Arduino("COM" + i, 9600);
            if (arduinoInpakRobot.openConnection()) {
                try {
                    Thread.sleep(1500);
                    String arduino = arduinoInpakRobot.serialRead();
                    System.out.println(arduino);
                    if(arduino.contains("Inpak")){
                        isInpakArduino = true;
                        System.out.println("Ah yes, the port you were looking for");
                        arduinoInpakRobot.serialWrite("Connected");
                    }else{
                        System.out.println("Close, but not close enough");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}



