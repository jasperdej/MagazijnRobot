package magazijnrobot;

public class Start {

    public static void main(String[] args) {
        Inpak inpak = new Inpak(new Order());
        System.out.println(inpak.status);
    }

}
