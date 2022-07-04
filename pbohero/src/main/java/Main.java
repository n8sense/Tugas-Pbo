import frame.HeroViewFrame;
import helpers.Koneksi;

public class Main {
    public static void main(String[] args) {
        Koneksi.getConnection();
        HeroViewFrame viewFrame = new HeroViewFrame();
        viewFrame.setVisible(true);
    }
}
