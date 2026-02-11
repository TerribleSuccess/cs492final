import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class secret {
    public static void encode(String filePath, String secret) {
        try {
            //Take input file convert to buffered image and send to hide data function
            String data = secret;
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);
            BufferedImage encodedImage = hideData(image, data);
            //Save File
            File output = new File(file.getParent(), "encoded_" + file.getName());
            ImageIO.write(encodedImage, "png", output);
            //Success Message
            JOptionPane.showMessageDialog(null, "Success! Saved to: " + output.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    //Convert string to binary
    public static String dataToBinary(String data) {
        StringBuilder binary = new StringBuilder();
        for (char c : data.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        return binary.toString();
    }

    public static BufferedImage hideData(BufferedImage img, String data) {
        String binaryData = dataToBinary(data);
        int dataIndex = 0;
        int dataLength = binaryData.length();

        //LxW for the loop bounds
        int width = img.getWidth();
        int height = img.getHeight();
        
        outerLoop:
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);

                //Indexes for rgb color values
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                //Set the new rgb lsb values for the pixel
                if (dataIndex < dataLength) {
                    red = (red & 0xFE) | (binaryData.charAt(dataIndex) - '0');
                    dataIndex++;
                }
                if (dataIndex < dataLength) {
                    green = (green & 0xFE) | (binaryData.charAt(dataIndex) - '0');
                    dataIndex++;
                }
                if (dataIndex < dataLength) {
                    blue = (blue & 0xFE) | (binaryData.charAt(dataIndex) - '0');
                    dataIndex++;
                }

                //Create the new pixel
                int newPixel = (red << 16) | (green << 8) | blue;
                //Put it in the image
                img.setRGB(x, y, (0xFF << 24) | newPixel);
                if (dataIndex >= dataLength) {
                    break outerLoop;
                }
            }
        }
        return img;
    }




    public static String decode(String filePath) {
        try {
            //Take File Switch to bufferedimage
            File file = new File(filePath);
            BufferedImage image = ImageIO.read(file);

            //Call and return
            String hiddenMessage = decodeMain(image);
            return hiddenMessage;
        } catch (IOException e) {
            return "";
        }
    }

        public static String decodeMain(BufferedImage img) {
        //Final String
        StringBuilder binData = new StringBuilder();
        int width = img.getWidth();
        int height = img.getHeight();
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = img.getRGB(x, y);

                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                String r = String.format("%8s", Integer.toBinaryString(red)).replace(' ', '0');
                String g = String.format("%8s", Integer.toBinaryString(green)).replace(' ', '0');
                String b = String.format("%8s", Integer.toBinaryString(blue)).replace(' ', '0');

                //Append 8th bit in rgb
                binData.append(r.charAt(7));
                binData.append(g.charAt(7));
                binData.append(b.charAt(7));
            }
        }
        // Group into 8 bits and covert to ascii
        StringBuilder message = new StringBuilder();
        for (int i = 0; i + 8 <= binData.length(); i += 8) {
            //Seperate into 8 bits
            String byteStr = binData.substring(i, i + 8);
            //Convert to
            int charCode = Integer.parseInt(byteStr, 2);
            message.append((char) charCode);
        }
        //Return the extracted image
        return message.toString();
    }
}
