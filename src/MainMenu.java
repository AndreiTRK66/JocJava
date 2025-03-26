import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class MainMenu extends JPanel {

    private Image backgroundImage = Toolkit.getDefaultToolkit().createImage("resources/background.jpg");

    public MainMenu(GamePanel parent) {
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(backgroundImage, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException ignored) {}

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        JButton startButton = new JButton("Start Joc");
        JButton optionsButton = new JButton("Opțiuni");
        JButton quitButton = new JButton("Ieșire");

        Font buttonFont = new Font("Arial", Font.BOLD, 24);
        startButton.setFont(buttonFont);
        optionsButton.setFont(buttonFont);
        quitButton.setFont(buttonFont);

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton.addActionListener(e -> {
            parent.removeAll();
            parent.revalidate();
            parent.repaint();
            parent.setLayout(null);
            parent.startGame();
        });

        optionsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Audio settings will be available soon!", "Options", JOptionPane.INFORMATION_MESSAGE);
        });

        quitButton.addActionListener(e -> System.exit(0));

        add(Box.createVerticalGlue());
        add(startButton);
        add(Box.createVerticalStrut(20));
        add(optionsButton);
        add(Box.createVerticalStrut(20));
        add(quitButton);
        add(Box.createVerticalGlue());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
