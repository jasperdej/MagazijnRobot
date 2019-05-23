public abstract class Robot {
    protected String status = "Aan het verbinden";

    //sends coordinates of new order to arduino.
    public static void sendToCoords(String coord){
        StackTraceElement[] stactrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stactrace[2];
        String className = e.getClassName();
        if (className.equals("OrderPick")) {
            ArduinoConn.arduinoPickRobot.serialWrite(coord);
            System.out.println("TO OP: " + coord);
        } else {
            ArduinoConn.arduinoInpakRobot.serialWrite(coord);
            System.out.println("TO IP: " + coord);
        }
    }

    //listens to Arduino. returns string send by arduino
    public String recievedFromInpak() {
        String arduino = ArduinoConn.arduinoInpakRobot.serialRead();
        return arduino;
    }

    public String recievedFromOrderpick() {
        return ArduinoConn.arduinoPickRobot.serialRead();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
