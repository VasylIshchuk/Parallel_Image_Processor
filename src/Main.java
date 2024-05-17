public class Main {
    public static void main(String[] args) {
        ImageProcessor ip = new ImageProcessor();
        ip.loadImage("image.png");
        ip.increaseBrightness(100);
        ip.saveImage("brighter_image.png", "png");
    }
}