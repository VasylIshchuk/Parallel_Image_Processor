import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        ImageProcessor ip = new ImageProcessor();
        ip.loadImage("image.png");

        List<Integer> listRed = ip.getHistogramOfImageChannel("red");
        List<Integer> listGreen = ip.getHistogramOfImageChannel("green");
        List<Integer> listBlue = ip.getHistogramOfImageChannel("blue");

        ip.createHistogramChartOfImageChannel(listRed,"Histogram_Red_Channel","red");
        ip.createHistogramChartOfImageChannel(listGreen,"Histogram_Green_Channel","green");
        ip.createHistogramChartOfImageChannel(listBlue,"Histogram_Blue_Channel","blue");

        ip.createChartMatrixOfImageHistogram(listBlue, listGreen,listRed,"Chart_Matrix_RGB");

//        ip.saveImage("brighter_image.png", "png");
    }
}