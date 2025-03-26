import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    public MainMenu(GamePanel parent) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

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
            JOptionPane.showMessageDialog(this, "Setările audio vor fi disponibile în curând!", "Opțiuni", JOptionPane.INFORMATION_MESSAGE);
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
}