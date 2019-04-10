import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

class FFT {
    public static void main(String[] args) throws IOException {
        String filePath = "images/Lenna.png";
        BufferedImage picture = ImageIO.read(new File(filePath));
        
        int pixels[][] = grabPixels(picture);
        int width = picture.getWidth();
        int height = picture.getHeight();

        pixels = grayScale(pixels, width, height);
        saveImage(picture, pixels, width, height, appendFilepath(filePath, "_grayScale"));
    }

    public static void displayImage(String filePath) {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(filePath);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static int[][] grayScale(int pixels[][], int width, int height) {
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int p = pixels[i][j];
                int a = 255, r = (p >> 16) & 0xff, g = (p >> 8) & 0xff, b = p & 0xff;

                // Normalize and gamma correct:
                // gamma correction is a metric of adjusting making shadows either darker or lighter
                // this has it's own metric from the looks of it
                double rr = Math.pow(r / 255.0, 2.2);
                double gg = Math.pow(g / 255.0, 2.2);
                double bb = Math.pow(b / 255.0, 2.2);

                // Calculate luminance:
                double lum = 0.2126 * rr + 0.7152 * gg + 0.0722 * bb;

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (a << 24) + (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                pixels[i][j] = gray;
            }
        }        
        return pixels;
    }

    public static void saveImage(BufferedImage image, int pixels[][], int width, int height, String filePath) throws IOException {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, pixels[i][j]);
            }
        }
        ImageIO.write(image, "png", new File(filePath));
        displayImage(filePath);
    }

    public static String appendFilepath(String filePath, String append) {
        int dot = filePath.indexOf(".");
        return filePath.substring(0, dot) + append + filePath.substring(dot);
    }

    // separate pixel values into 2d array to make future FFT operations easier
    public static int[][] grabPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] pixels = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j] = image.getRGB(i, j);                
            }
        }
        return pixels;
    }

}