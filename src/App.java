
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class App {
    private static JLabel imageLabel = new JLabel(); // for preview
    private static File selectedFile = null;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Steganography Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        
        JLabel label = new JLabel("Hello, User! Please Select a Carrier Image.      ");
        JButton selectImageB = new JButton("Select Image");
        JButton encryptButton = new JButton("Hide Secrets");
        JLabel secretLabel = new JLabel("\nEnter Secret Message:");
        JTextArea secretTextArea = new JTextArea(3, 60);
        secretTextArea.setLineWrap(true);
        secretTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(secretTextArea);
        JButton decryptButton = new JButton("Reveal Secrets");

        //Control Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(label);
        topPanel.add(selectImageB);
        topPanel.add(secretLabel);
        topPanel.add(scrollPane);
        topPanel.add(encryptButton);
        topPanel.add(decryptButton);
        topPanel.setPreferredSize(new Dimension(800, 140));

        //Image Preview
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBorder(BorderFactory.createTitledBorder("Image Preview"));


        JPanel imageWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imageWrapper.add(imageLabel);
        imagePanel.add(imageWrapper, BorderLayout.CENTER);

        // Allows user to select a local file and verifies it's an image
        selectImageB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
                        "Image files", "jpg", "jpeg", "png", "gif", "bmp");
                fileChooser.setFileFilter(imageFilter);

                int returnVal = fileChooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();

                    //Display the image for the user to see
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                    Image scaledImage = icon.getImage().getScaledInstance(400, 200, Image.SCALE_DEFAULT);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    JOptionPane.showMessageDialog(null, "File selection cancelled");
                }
            }
        });

        //Hide Listener
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    System.out.println("Encrypting: " + selectedFile.getAbsolutePath());
                    secret.encode(selectedFile.getAbsolutePath(), secretTextArea.getText());
                } else {
                    //No selected file
                    JOptionPane.showMessageDialog(null, "Please select an image first");
                }
            }
        });

        //Reveal Listener
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    //Call to decode function
                    String message = secret.decode(selectedFile.getAbsolutePath());

                    //Display the result
                    JTextArea textArea = new JTextArea(message);
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 200));
                    JOptionPane.showMessageDialog(null, scrollPane, "Decoded Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    //No selected file
                    JOptionPane.showMessageDialog(null, "Please select an image first");
                }
            }
        });

        // Add components to frame
        frame.add(topPanel);
        frame.add(imagePanel);
        frame.setVisible(true);
    }
}
