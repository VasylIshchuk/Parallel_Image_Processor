package image;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.imageio.ImageIO;
import java.awt.*;
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

    public void loadImage(String path) {
        File inputFile = new File(path);
        try {
            image = ImageIO.read(inputFile);
            System.out.println("Image uploaded :)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveImage(String path, String formatImage) {
        File outputFile = new File(path);
        try {
            ImageIO.write(image, formatImage, outputFile);
            System.out.println("Image saved :)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void increaseBrightness(int constant) {
        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                int pixelRGB = image.getRGB(x, y);
                int rgb = increaseBrightnessOfPixel(pixelRGB, constant);
                image.setRGB(x, y, rgb);
            }
        }
    }

    public void increaseBrightnessWithThreads(int constant) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = new ArrayList<>();
        int chunkSize = image.getHeight() / numThreads;
        for (int i = 0; i < numThreads; ++i) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? image.getHeight() : start + chunkSize;
            //  If the number of rows is not evenly divided by the number of threads,
            //  the last chuck may be shorter or longer than expected, so if the loop reaches
            //  the last thread, 'end' returns 'image.getHeight()', the bottom border of the image
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
            //  Or I could create new class 'SetBrightnessWorker' implementing the 'Runnable' interface,
            //  that contains method 'run()' ,and pass its object to the 'Thread' constructor, instead of
            //  writing everything in lambda expression.
            threads.get(i).start();
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //  The idea behind multithreading is to allow multiple threads to execute at the same time,
        //  thereby reducing the overall execution time of a task. To do this,
        //  you have to start all the threads and then wait for them to complete(join).
    }

    public void increaseBrightnessWithPoolOfThreads(int constant) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        //  Faster than if we just use threads
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
            //  For example, if 'numTreads' is 12 and image - 100 rows, so the first 12 threads
            //  will be served by 12 rows after completion, those threads will continue
            //  to serve the next 12 rows i etc.
            //  But if you use "Thead", a separate thread will be created for each of the 100 lines of the image,
            //  i.e. 100 threads were needed.
        }
        executorService.shutdown();
        //  After calling 'shutdown()', the 'ExecutorService' will no longer accept new tasks.
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
            //  Is used to block the current thread until all tasks in 'ExecutorService'
            //  have completed or timed out.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int increaseBrightnessOfPixel(int pixelRGB, int constant) {
        int mask = 0xff;//255 in hex
        int blue = pixelRGB & mask;
        int green = (pixelRGB >> 8) & mask;
        int red = (pixelRGB >> 16) & mask;
        int alpha = (pixelRGB >> 24) & mask;
        //  Since image_bigger uses  an alpha component, I have to add an alpha,
        //      but if I only had an image everything would be fine ,
        //      since the image doesn't contain that component.
        //  Alpha component  is transparency  in  ARGB.
        //  Alpha  gets the last remaining 8 bits.
        blue = increaseColor(blue, constant);
        green = increaseColor(green, constant);
        red = increaseColor(red, constant);
        alpha = increaseColor(alpha,constant);
        //  or (= clamp(double value, double min, double max) ) instead of new method.
        //  clamp method from Math, which clamps the value to fit between min and max.
        int rgb = blue | (green << 8) | (red << 16) | (alpha << 24);
        //  or  = blue + (green << 8) + (red << 16) + (alpha<<24)
        return rgb;

        //  OR

        //  Color color = new Color(image.getRGB(x,y));
        //  int blue = color.getBlue();
        //  int green = color.getGreen();
        //  int red = color.getRed();
        //  int alpha = color.getAlpha();
        //  blue = increaseColor(blue,value);
        //  green = increaseColor(green,value);
        //  red = increaseColor(red,value);
        //  alpha = increaseColor(alpha,value);
        //  int rgb = blue | (green << 8) | (red << 16) | (alpha<<24);
    }

    private int increaseColor(int color, int constant) {
        color += constant;
        if (color > 255) return 255;
        else if (color < 0) return 0;
        return color;
    }

    //    OR
    //
    //public class Clamp {
    //    static <T extends Comparable<T>> T clamp(T value, T min, T max) {
    //        if (value.compareTo(min) < 0) {
    //            return min;
    //        } else if (value.compareTo(max) > 0) {
    //            return max;
    //        } else {
    //            return value;
    //        }
    //    }
    //}
    public List<Integer> getHistogramOfImageChannel(String colorRGB) {
        List<Integer> listPixels = new ArrayList<>();
        //  = Collections.synchronizedList(new ArrayList<>());
        //  This is a better solution because the method will be more productive.

        //  ArrayList, it is not thread-safe by nature, that is, multiple threads can modify
        //  the ArrayList at the same time(so I use synchronized below), which can cause the program to malfunction
        //  ( for example, throw an ArrayIndexOutOfBoundsException ).

        //  Using Collections.synchronizedList turns an ArrayList into a thread-safe list,
        //  where all operations modifying the list are synchronized.
        //  This means that only one thread can modify the list at any given time.
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        int chunkSize = image.getHeight() / numThreads;
        for (int i = 0; i < numThreads; ++i) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? image.getHeight() : start + chunkSize;
            executorService.execute(() -> {
                for (int x = 0; x < image.getWidth(); ++x) {
                    for (int y = start; y < end; ++y) {
                        synchronized (ImageProcessor.class) {
                            int pixelRGB = image.getRGB(x, y);
                            listPixels.add(getPixelByColor(pixelRGB, colorRGB));
                            //  synchronized (ImageProcessor.class) is a synchronized block - which means that the
                            //  block of code inside is executed by only one thread at a time.
                            //  In your case, we use 'synchronized' because multiple
                            //  threads are accessing a shared 'listPixels' at the same time.
                        }
                    }
                }
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listPixels;
    }

    private int getPixelByColor(int pixelRGB, String colorRGB) {
        int mask = 0xff;
        switch (colorRGB) {
            case "blue":
                return pixelRGB & mask;
            case "green":
                return (pixelRGB >> 8) & mask;
            case "red":
                return (pixelRGB >> 16) & mask;
            case null, default:
                throw new IllegalArgumentException("Inappropriate color RGB");
        }
    }

    public void createHistogramChartOfImageChannel(
            List<Integer> imageHistogram, String path, String color) throws IOException {

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(1920)
                .height(1080)
                .title("Image channel histogram")
                .xAxisTitle("Pixel")
                .yAxisTitle("Frequency pixels")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        Color[] sliceColors = switch (color) {
            case "blue" -> new Color[]{new Color(0, 0, 255)};
            case "green" -> new Color[]{new Color(0, 255, 0)};
            case "red" -> new Color[]{new Color(255, 0, 0)};
            case null, default -> throw new IllegalArgumentException("Inappropriate color RGB");
        };
        chart.getStyler().setSeriesColors(sliceColors);
        //Series
        Histogram histogram1 = new Histogram(imageHistogram, 51, 0, 255);
        chart.addSeries(color, histogram1.getxAxisData(), histogram1.getyAxisData());

        // Save chart
        BitmapEncoder.saveBitmap(chart, path, BitmapEncoder.BitmapFormat.PNG);
    }

    public void createChartMatrixOfImageHistogram(
            List<Integer> imageHistogramBlue, List<Integer> imageHistogramGreen,
            List<Integer> imageHistogramRed, String path) throws IOException {

        XYChart chart = new XYChartBuilder()
                .width(1920)
                .height(1080)
                .title("Image histogram")
                .yAxisTitle("Pixel")
                .build();
        // Customize Chart
        Color[] sliceColors = new Color[]{
                new Color(0, 0, 255),
                new Color(0, 255, 0),
                new Color(255, 0, 0)
        };
        chart.getStyler().setSeriesColors(sliceColors);
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax(255.0);
        chart.addSeries("BLUE", countingFrequency(imageHistogramBlue));
        chart.addSeries("GREEN", countingFrequency(imageHistogramGreen));
        chart.addSeries("RED", countingFrequency(imageHistogramRed));

        BitmapEncoder.saveBitmap(chart, path, BitmapEncoder.BitmapFormat.PNG);
    }

    private List<Integer> countingFrequency(List<Integer> imageHistogram) {
        int[] frequency = new int[256];

        for (int value : imageHistogram) {
            frequency[value]++;
        }

        List<Integer> frequencyPixels = new ArrayList<>();
        for (int count : frequency) {
            frequencyPixels.add(count);
        }

        return frequencyPixels;
    }
}
