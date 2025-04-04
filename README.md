# Image Processing with Threads and Histograms

## 🎯 Project Goal

This project focuses on image processing tasks, such as adjusting brightness, processing images in parallel using threads, and calculating and visualizing histograms of image channels. The goal is to apply different multithreading techniques to speed up image manipulation and processing.

## 📄 Description

The application includes the following functionality:
- Load and save images using file paths with `BufferedImage`.
- Increase image brightness by a specified constant.
- Perform brightness adjustment using parallel threads based on the number of processor cores.
- Use a thread pool to perform parallel processing by dividing the image into rows, and compare performance.
- Calculate and generate histograms for specific image channels (e.g., red, green, blue).
- Generate and visualize histograms as images.
  
## 🛠️ Technologies

- Java, BufferedImage
- Multithreading, ExecutorService
- Image histogram calculations
