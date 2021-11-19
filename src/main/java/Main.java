import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // get default webcam and open it
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        System.out.print("\033[7m");

        while (true) {
//            System.out.println("\033[2J");
            // get image
            BufferedImage image = webcam.getImage();

            // save image to PNG file
            File file = new File("us.png");
            try {
                ImageIO.write(image, "PNG", file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                asciiImage(file, webcam.getViewSize().width, webcam.getViewSize().height);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static File selectImage() {
        // Select a temporary image with a file picker and return the selected file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    //        file = selectImage();
//        if (file == null) {
//            System.err.println("No file selected");
//            System.exit(1);
//            return;
//        }
//
//        System.out.println("Enter a width and height");
//        String widthString = JOptionPane.showInputDialog("Width");
//        String heightString = JOptionPane.showInputDialog("Height");
//
//        int width = -1, height = -1;
//        if (!widthString.equals("")) {
//            width = Integer.parseInt(widthString);
//            height = Integer.parseInt(heightString);
//        }

    public static void asciiImage(File file, int width, int height) throws IOException {
        BufferedImage image = ImageIO.read(file);
        if (width != -1 && height != -1) {
            image = resizeImage(width, height, image);
        }

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                // Retrieve contents of a pixel
                int pixel = image.getRGB(x,y);
                // Create a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieve the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                // Calculate the average of the R G B values
                int average = (red + green + blue) / 3;

                Console1 console1 = new Console1();
                JTextField textField = console1.getTextField1();
                StringBuilder builder = new StringBuilder();

                // Output
                if (x == 0)
                    System.out.println();
                if (average > 230)
                    builder.append(".");
                else if (average > 200)
                    builder.append("'");
                else if (average > 180)
                    builder.append("\"");
                else if (average > 150)
                    builder.append(":");
                else if (average > 120)
                    builder.append("*");
                else if (average > 80)
                    builder.append("+");
                else if (average > 50)
                    builder.append("%");
                else if (average >= 20)
                    builder.append("#");
                else if (average >= 0)
                    builder.append("@");
                builder.append(" ");

//                textField.setText(builder.toString());
                System.out.print(builder);
            }
        }
    }

    public static BufferedImage resizeImage(int width, int height, BufferedImage image) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return newImage;
    }
}
