public class Main {
    public static void main(String[] args) throws InterruptedException {
        ImageProcessor ip = new ImageProcessor();
        ip.loadImage("image.png");
        ip.increaseBrightnessWithThreads(90);
        ip.saveImage("brighter_image.png", "png");
    }
}