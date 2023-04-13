package Utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Utilities {
    public static BufferedImage GetLogo() throws IOException {
        return ImageIO.read(Utilities.class.getResourceAsStream("/Assets/ahegoe.png"));
    }

    public static void Log(String value){
        System.out.println(value);
    }
}
