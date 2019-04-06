import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

class FFT {
    public static void main(String[] args) {
        String filepath = "images/Lenna.png";
        try {
            BufferedImage picture = ImageIO.read(new File(filepath));
        }
        catch (IOException e) {
            String workingDir = System.getProperty("user.dir");
            System.out.println("Current working directory : " + workingDir);
            e.printStackTrace();
        }
        displayImage(filepath);
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



}