package Utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Utilities {
    public static BufferedImage GetLogo() throws IOException {
        return ImageIO.read(Utilities.class.getResourceAsStream("/Assets/ahegoe.png"));
    }

    public static <T> void Log(T value){
        System.out.println(value);
    }

    public static int RandomInt(int min, int exMax){
        Random random = new Random();
        return random.nextInt((exMax - min)) + min;
    }
}
