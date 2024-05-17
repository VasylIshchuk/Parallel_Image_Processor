import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
   private BufferedImage image;
   public void loadImage(String path){
       File inputFile = new File(path);
       try {
           image = ImageIO.read(inputFile);
           System.out.println("Image uploaded :)");
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
   public void saveImage(String path, String formatImage){
       File outputFile = new File(path);
       try {
           ImageIO.write(image, formatImage, outputFile);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
}
