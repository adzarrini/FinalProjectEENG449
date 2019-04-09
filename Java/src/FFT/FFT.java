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
        String filepath = "images/Lenna.png";
        BufferedImage picture = ImageIO.read(new File(filepath));
        String grayScale = grayScale(picture, filepath);
        displayImage(grayScale);
    }

    public static void displayImage(String filepath) {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(filepath);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static String grayScale(BufferedImage image, String filepath) throws IOException{
        int width = image.getWidth();
        int height = image.getHeight();

        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int p = image.getRGB(i, j);
                int a = 255, r = (p >> 16) & 0xff, g = (p >> 8) & 0xff, b = p & 0xff;
                int avg = (r+g+b)/3;
                p = 0;
                p |= (a << 24);
                p |= (avg << 16);
                p |= (avg << 8);
                p |= avg;
                image.setRGB(i, j, p);
            }
        }
        String newPath = appendFilepath(filepath, "_grayScale");
        ImageIO.write(image, "png", new File(newPath));
        return newPath;
    }

    public static String appendFilepath(String filepath, String append) {
        int dot = filepath.indexOf(".");
        return filepath.substring(0, dot) + append + filepath.substring(dot);
    }

}