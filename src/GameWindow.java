import javax.swing.*;

public class GameWindow extends JFrame {
    public static void main(String[] args) {
        new GameWindow();
    }

    public GameWindow() {
        setTitle("Invazia - Joc");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel gamePanel = new GamePanel();
        add(gamePanel);

        gamePanel.showMainMenu(); // ← Mutat aici, înainte de vizibilitate

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
