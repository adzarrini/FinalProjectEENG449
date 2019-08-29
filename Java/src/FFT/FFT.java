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
        FFT singletonFft = new FFT();
        String filePath = "images/bird.png";
        BufferedImage picture = ImageIO.read(new File(filePath));
        
        int pixels[][] = singletonFft.grabPixels(picture);
        pixels = singletonFft.grayScale(pixels); 
        singletonFft.saveImage(picture, pixels, appendFilepath(filePath, "_grayScale"));

        Complex complexPixels[][] = singletonFft.turnComplex(pixels);
        Complex fft2[][] = singletonFft.fft2(complexPixels);

        Complex lowpass[][] = singletonFft.lowPass(fft2.length, 0.4);
        Complex ifft2[][] = singletonFft.dotProduct(fft2, lowpass);
        Complex result[][] = singletonFft.ifft2(ifft2);
        singletonFft.saveImage(picture, singletonFft.turnReal(result), appendFilepath(filePath, "_filter"));

        // System.out.println(complexPixels.length + "\t" + complexPixels[0].length);
        // Complex fft[] = singletonFft.fft(complexPixels[0]);
    }

    public int[][] turnReal(Complex[][] a) {
        int height = a.length;
        int width = a[0].length;
        int b[][] = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                b[i][j] = Math.min(255, (int) Math.round(a[i][j].abs()));
            }
        }
        return b;
    }

    public Complex[][] turnComplex(int[][] a) {
        int height = a.length;
        int width = a[0].length;
        Complex b[][] = new Complex[height][width];
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                b[i][j] = new Complex((double)a[i][j], 0.0);
            }
        }
        return b;
    }

    public Complex[][] dotProduct(Complex[][] a, Complex[][] b) {
        int n = a.length;
        int m = a[0].length;
        Complex[][] ab = new Complex[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ab[j][i] = a[i][j].times(b[i][j]);
            }
        }
        return ab;
    }

    public Complex[][] ifft2(Complex[][] a) {
        int n = a.length;
        int m = a[0].length;
        Complex[][] A = new Complex[n][m];
        for (int i = 0; i < n; i++) {
            A[i] = ifft(a[i]);
        }
        A = transpose(A);
        for (int i = 0; i < n; i++) {
            A[i] = ifft(A[i]);
        }
        return A;
    }

    public Complex[][] fft2(Complex[][] a) {
        int n = a.length;
        int m = a[0].length;
        Complex[][] A = new Complex[n][m];
        for(int i = 0; i < n; i++) {
            A[i] = fft(a[i]);
        }
        A = transpose(A);
        for (int i = 0; i < n; i++) {
            A[i] = fft(A[i]);
        }
        A = transpose(A);
        return A;
    }

    public Complex[] ifft(Complex[] a) {
        int n = a.length;
        Complex[] T = new Complex[n];

        // take conjugate
        for (int i = 0; i < n; i++) {
            T[i] = a[i].conjugate();
        }

        // compute forward FFT
        T = fft(T);

        // take conjugate again
        for (int i = 0; i < n; i++) {
            T[i] = T[i].conjugate();
        }

        // divide by n
        for (int i = 0; i < n; i++) {
            T[i] = T[i].scale(1.0 / n);
        }

        return T;
    }

    private Complex[] fft(Complex[] a) {
        int n = a.length;

        // base case
        if (n == 1)
            return new Complex[] { a[0] };

        // checks until power of 2
        if (n % 2 != 0) {
            throw new IllegalArgumentException("Array size must be a power of 2");
        }

        // split into even and odd
        Complex[] even = new Complex[n / 2];
        Complex[] odd = new Complex[n / 2];
        for (int k = 0; k < n / 2; k++) {
            even[k] = a[2 * k];
            odd[k] = a[2 * k + 1];
        }
        
        Complex[] e = fft(even);
        Complex[] o = fft(odd);

        Complex[] F = combine(e,o);
        return F;
    }

    private Complex[] combine(Complex[] e, Complex[] o) {
        int n = e.length*2; // should be same as o.length
        Complex[] A = new Complex[n];
        for(int k = 0; k < n/2; k++) {
            double k_n = -2 * Math.PI * k / n; // value of exponent without i
            Complex w_k = new Complex(Math.cos(k_n), Math.sin(k_n)); // Eulers formula and sigma term
            A[k] = e[k].plus(w_k.times(o[k]));
            A[k + n/2] = e[k].minus(w_k.times(o[k]));
        }

        return A;
    }

    // n is a power of 2
    public Complex[][] transpose(Complex[][] a) {
        int n = a.length;
        int m = a[0].length;
        Complex[][] A = new Complex[m][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                A[j][i] = a[i][j];
            }
        }
        return A;
    }

    public Complex[][] lowPass(int n, double K0) {
        Complex[][] filter = new Complex[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                double kx0 = (0.5 + i / (double) n) % 1 - 0.5;
                double kx1 = kx0 * 2 * Math.PI;
                double ky0 = (0.5 + j / (double) n) % 1 - 0.5;
                double ky1 = ky0 * 2 * Math.PI;
                int val = ((Math.pow(kx1, 2) + Math.pow(ky1, 2)) < Math.pow(K0, 2))?1:0;
                filter[i][j] = new Complex(val, 0);
            }
        }
        return filter;
    }

    public void print2Darray(Complex[][] a) {
        int n = a.length;
        int m = a[0].length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void displayImage(String filePath) {
        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(filePath);
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // converts argb int format to a single value for the grayscale
    public int[][] grayScale(int pixels[][]) {
        int height = pixels.length;
        int width = pixels[0].length;
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int p = pixels[i][j];
                int r = (p >> 16) & 0xff, g = (p >> 8) & 0xff, b = p & 0xff;

                pixels[i][j] = (int) Math.round(0.2989 * r + 0.5870 * g + 0.1140 * b);
            }
        }        
        return pixels;
    }

    // This implies saving of grayScale image since we use gray scale if FFT
    public void saveImage(BufferedImage image, int pixels[][], String filePath) throws IOException {
        int height = pixels.length;
        int width = pixels[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int p = pixels[i][j];
                int pixel = (255 << 24) + (p << 16) + (p << 8) + p;
                image.setRGB(i, j, pixel);
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
    public int[][] grabPixels(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] pixels = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i][j] = image.getRGB(i, j);                
            }
        }
        return pixels;
    }

}