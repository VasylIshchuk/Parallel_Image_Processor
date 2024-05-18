import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ImageProcessor {
   private BufferedImage image;
   public void loadImage(String path){
       File inputFile = new File(path);
       try {
           image = ImageIO.read(inputFile);
           System.out.println("Image uploaded :)");
       } catch (IOException e) {
           e.printStackTrace();;
       }
   }
   public void saveImage(String path, String formatImage){
       File outputFile = new File(path);
       try {
           ImageIO.write(image, formatImage, outputFile);
           System.out.println("Image saved :)");
       } catch (IOException e) {
           e.printStackTrace();;
       }
   }
   public void increaseBrightness(int constant){
       for(int x = 0; x<image.getWidth(); ++x){
           for(int y = 0; y<image.getHeight(); ++y){
               int pixelRGB = image.getRGB(x,y);
               int rgb = increaseBrightnessOfPixel(pixelRGB,constant);
               image.setRGB(x,y,rgb);
           }
       }
   }

    public void increaseBrightnessWithThreads(int constant)  {
        int numThreads = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();
        int chunkSize = image.getHeight()/numThreads;
        for(int i=0; i<numThreads; ++i) {
            int start =i*chunkSize;
            int end = (i == numThreads-1) ? image.getHeight() : start + chunkSize;
//            If the number of rows is not evenly divided by the number of threads,
//            the last chuck may be shorter or longer than expected, so if the loop reaches
//            the penultimate thread, 'end' returns 'image.getHeight()', the bottom border of the image
            threads.add(new Thread(() -> {
                for (int x = 0; x < image.getWidth(); ++x) {
                    for (int y = start; y < end; ++y) {
                        int pixelRGB = image.getRGB(x, y);
                        int rgb = increaseBrightnessOfPixel(pixelRGB, constant);
                        image.setRGB(x, y, rgb);
                    }
                }
            }
            ));
//            Or I could create new class 'SetBrightnessWorker' implementing the 'Runnable' interface,
//            that contains method 'run()' ,and pass its object to the 'Thread' constructor, instead of
//            writing everything in lambda expression.
            threads.get(i).start();
        }

        try {
            for(int i=0; i<threads.size(); ++i) {
                threads.get(i).join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        The idea behind multithreading is to allow multiple threads to execute at the same time,
//        thereby reducing the overall execution time of a task. To do this,
//        you have to start all the threads and then wait for them to complete(join).
    }

    //
    public void increaseBrightnessWithPoolOfThreads(int constant){
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
//        Faster than if we just use threads
        for (int i = 0; i < image.getHeight(); ++i) {
            final int y = i;
            executorService.execute(() -> {
                for (int x = 0; x < image.getWidth(); ++x) {
                    int pixelRGB = image.getRGB(x, y);
                    increaseBrightnessOfPixel(pixelRGB, constant);
                    int rgb = increaseBrightnessOfPixel(pixelRGB, constant);
                    image.setRGB(x, y, rgb);
                }
            });
//            For example, if 'numTreads' is 12 and image - 100 rows, so the first 12 threads
//            will be served by 12 threads after completion, those threads will continue
//            to serve the next 12 threads i etc.
        }
        executorService.shutdown();
//        After calling 'shutdown()', the 'ExecutorService' will no longer accept new tasks.
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
//            Is used to block the current thread until all tasks in 'ExecutorService'
//            have completed or timed out.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

   private int increaseBrightnessOfPixel(int pixelRGB, int constant ){
       int mask = 0xff;//255 in hex
       int blue = pixelRGB & mask;
       int green = (pixelRGB >> 8) & mask;
       int red = (pixelRGB >> 16) & mask;
       int alpha = (pixelRGB >> 24) & mask;
//       Since image_bigger uses  an alpha component, I have to add an alpha,
//          but if I only had an image everything would be fine ,
//          since the image doesn't contain that component.
//       Alpha component  is transparency  in  ARGB.
//       Alpha  gets the last remaining 8 bits.
       blue = increaseColor(blue,constant);
       green = increaseColor(green,constant);
       red = increaseColor(red,constant);
//       or (= clamp(double value, double min, double max) ) instead of new method.
//       clamp method from Math, which clamps the value to fit between min and max.
       int rgb =  blue | (green << 8) | (red << 16) | (alpha<<24) ;
//       or  = blue + (green << 8) + (red << 16) + (alpha<<24)
       return rgb;
   }
   private int increaseColor(int color,int constant){
       color +=constant;
       if (color>255) return 255;
       return  color;
   }
}
