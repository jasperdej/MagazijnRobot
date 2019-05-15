import arduino.Arduino;

public abstract class Robot {
    protected String status = "Aan het verbinden";

    //functie voor luisteren naar arduino.

    public static void sendToCoords(String coord){
        StackTraceElement[] stactrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stactrace[2];
        String className = e.getClassName();
        if (className.equals("Orderpick")) {
            ArduinoConn.arduinoPickRobot.serialWrite(coord);
        } else {
            ArduinoConn.arduinoInpakRobot.serialWrite(coord);
            System.out.println("Coords gestuurd");
            System.out.println(coord);
        }
    }

    public String recievedFromArduino() {
        String arduino = ArduinoConn.arduinoInpakRobot.serialRead();
        return arduino;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (status.equals("aan het inpakken")) {
//            Thread thread = new Thread()
        }
    }
}
