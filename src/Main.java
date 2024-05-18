import java.util.Locale;

public class Main {
    public static void main(String[] args)  {
        ImageProcessor ip = new ImageProcessor();
        ip.loadImage("image.png");

        long startTime1 = System.currentTimeMillis();
        ip.increaseBrightness(0);
        long stopTime1 = System.currentTimeMillis();

        long startTime2 = System.currentTimeMillis();
        ip.increaseBrightnessWithThreads(0);
        long stopTime2 = System.currentTimeMillis();

        long startTime3 = System.currentTimeMillis();
        ip.increaseBrightnessWithPoolOfThreads(100);
        long stopTime3 = System.currentTimeMillis();

        System.out.println(String.format(Locale.ENGLISH,
                "Without threads: %d", stopTime1 - startTime1));
        System.out.println(String.format(Locale.ENGLISH,
                "With threads: %d",stopTime2 - startTime2));
        System.out.println(String.format(Locale.ENGLISH,
                "With pool of threads: %d",stopTime3 - startTime3));

        ip.saveImage("brighter_image.png", "png");
    }
}