import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args)  {
        ImageProcessor ip = new ImageProcessor();
        ip.loadImage("image.png");

        long startTime1 = System.currentTimeMillis();
        List<Integer> listRed = ip.getImageHistogramChannel("red");
        long stopTime1 = System.currentTimeMillis();

        long startTime2 = System.currentTimeMillis();
         List<Integer> listBlue = ip.getImageHistogramChannel("blue");
        long stopTime2 = System.currentTimeMillis();

        long startTime3 = System.currentTimeMillis();
        List<Integer> listGreen = ip.getImageHistogramChannel("green");
        long stopTime3 = System.currentTimeMillis();

        System.out.println(String.format(Locale.ENGLISH,
                "Without threads: %d", stopTime1 - startTime1));
        System.out.println(String.format(Locale.ENGLISH,
                "With threads: %d",stopTime2 - startTime2));
        System.out.println(String.format(Locale.ENGLISH,
                "With pool of threads: %d",stopTime3 - startTime3));

//        ip.saveImage("brighter_image.png", "png");
    }
}