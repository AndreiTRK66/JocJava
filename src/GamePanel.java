import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    private javax.swing.Timer gameTimer;
    private Player player;
    private java.util.List<Enemy> enemies = new ArrayList<>();
    private java.util.List<Enemy> spawnedMinions = new ArrayList<>();
    private java.util.List<EnemyBullet> enemyBullets = new ArrayList<>();
    private boolean paused = false;
    private boolean levelComplete = false;
    private boolean bossSpawned = false;
    private boolean upgradeChoice = false;
    private boolean showGameOverScreen = false;
    private boolean upgradeSpeedTaken = false;
    private boolean upgradePowerTaken = false;
    private boolean gameOver = false;
    private long startTime;
    private long score;
    private boolean gameWon = false;
    private long highscore = 0;
    private Image backgroundImage;

    private int level = 1;
    private int lives = 3;
    private int width, height;
    private Random rand = new Random();

    public GamePanel() {
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        // Force load the background to prevent white screen
        try {
    backgroundImage = ImageIO.read(new File("resources/background.jpg"));
} catch (IOException e) {
    e.printStackTrace();
}


        showMainMenu();
    }
    public void startGame() {
        removeAll();
        setLayout(null);
        revalidate();
        repaint();

        lives = 3;
        level = 1;
        score = 0;
        gameOver = false;
        showGameOverScreen = false;
        levelComplete = false;
        upgradeChoice = false;
        bossSpawned = false;
        upgradeSpeedTaken = false;
        upgradePowerTaken = false;

        width = getWidth();
        height = getHeight();
        player = new Player(width / 2, height - 100);
        enemies.clear();
        spawnedMinions.clear();
        enemyBullets.clear();

        spawnEnemies();
        startTime = System.currentTimeMillis();

        if (gameTimer != null) gameTimer.stop();
        gameTimer = new javax.swing.Timer(30, this);
        gameTimer.start();

        AudioPlayer.stopAll(); 
        AudioPlayer.loopSound("Dungeon.wav");
    }

    private void spawnEnemies() {
        enemies.clear();
        spawnedMinions.clear();
        if (level < 3) {
            AudioPlayer.stopAll();
            AudioPlayer.loopSound("Dungeon.wav");
            for (int i = 0; i < 10 + level * 3; i++) {
                enemies.add(new Enemy(rand.nextInt(width - 50), rand.nextInt(200), false));
            }
        } else {
            AudioPlayer.stopAll();
            AudioPlayer.loopSound("FinalBattle.wav");
            enemies.add(new Enemy(width / 2 - 50, 50, true));
            bossSpawned = true;
        }
    }

    private void updateGame() {
        if (gameOver) return;

        for (Enemy enemy : new ArrayList<>(enemies)) {
            enemy.update();
            if (rand.nextInt(100) < 2) {
                enemyBullets.add(new EnemyBullet(enemy.x + enemy.width / 2, enemy.y + enemy.height));
            }
        }

        if (bossSpawned && !enemies.isEmpty() && rand.nextInt(50) == 0) {
            Enemy boss = enemies.get(0);
            spawnedMinions.add(new Enemy(boss.x + rand.nextInt(boss.width), boss.y + boss.height, false));
        }

        for (Enemy minion : new ArrayList<>(spawnedMinions)) {
            minion.update();
            if (rand.nextInt(100) < 2) {
                enemyBullets.add(new EnemyBullet(minion.x + minion.width / 2, minion.y + minion.height));
            }
        }

        for (Bullet b : new ArrayList<>(player.getBullets())) {
            b.update();
            for (Enemy enemy : new ArrayList<>(enemies)) {
                if (b.getBounds().intersects(enemy.getBounds())) {
                    if (enemy.hit()) {
                        enemies.remove(enemy);
                        AudioPlayer.playSound("burning.wav");
                    }
                    player.getBullets().remove(b);
                    break;
                }
            }
            for (Enemy minion : new ArrayList<>(spawnedMinions)) {
                if (b.getBounds().intersects(minion.getBounds())) {
                    spawnedMinions.remove(minion);
                    player.getBullets().remove(b);
                    AudioPlayer.playSound("hitmonster.wav");
                    break;
                }
            }
        }

        for (EnemyBullet eb : new ArrayList<>(enemyBullets)) {
            eb.update();
            if (eb.getBounds().intersects(new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight()))) {
                enemyBullets.remove(eb);
                lives--;
                AudioPlayer.playSound("receivedamage.wav");
                if (lives <= 0) {
                    gameOver = true;
                    showGameOverScreen = true;
                    AudioPlayer.stopAll();
                    AudioPlayer.playSound("gameover.wav");
                    gameTimer.stop();
                    return;
                }
            }
            if (eb.isOffScreen(height)) {
                enemyBullets.remove(eb);
            }
        }

        if (!gameOver && enemies.isEmpty() && spawnedMinions.isEmpty()) {
            if (level < 3) {
                levelComplete = true;
                upgradeChoice = true;
                score = System.currentTimeMillis() - startTime;
                highscore = highscore == 0 ? score : Math.min(score, highscore);
                repaint();
                requestFocusInWindow();
            } else {
                gameWon = true; // <-- Marchez câștigul
                gameOver = true;
                showGameOverScreen = true;
                score = System.currentTimeMillis() - startTime;
                highscore = highscore == 0 ? score : Math.min(score, highscore);
                AudioPlayer.stopAll();
                AudioPlayer.playSound("gameover.wav"); // Poți pune alt sunet aici dacă vrei
                saveHighscore();
                gameTimer.stop();
            }
        }
    }

    private void saveHighscore() {
        try {
            java.nio.file.Files.writeString(java.nio.file.Path.of("highscore.txt"),
                "Best Time: " + (highscore / 1000) + " sec");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", Font.BOLD, 42));
    
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.drawString("Congrats, you saved the galaxy!", width / 2 - 300, height / 2 - 40);
        } else {
            g.setColor(Color.RED);
            g.drawString("GAME OVER", width / 2 - 150, height / 2 - 40);
        }
    
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 26));
        g.drawString("Highscore: " + (highscore / 1000) + " sec", width / 2 - 100, height / 2);
        g.drawString("Press ENTER to return to the menu", width / 2 - 200, height / 2 + 40);
    }
    

    private void drawUpgradeOptions(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("Choose Upgrade", width / 2 - 130, height / 2 - 60);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.PLAIN, 24));

        if (!upgradeSpeedTaken && (level == 1 || upgradePowerTaken))
            g.drawString("1 - Firespeed (foc mai rapid)", width / 2 - 150, height / 2 - 10);
        if (!upgradePowerTaken && (level == 1 || upgradeSpeedTaken))
            g.drawString("2 - Firepower (dublu foc)", width / 2 - 150, height / 2 + 30);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) paused = !paused;
        if (showGameOverScreen && e.getKeyCode() == KeyEvent.VK_ENTER) resetToMainMenu();
        if (upgradeChoice) {
            if (e.getKeyCode() == KeyEvent.VK_1 && !upgradeSpeedTaken) {
                player.upgradeShootSpeed();
                upgradeSpeedTaken = true;
                AudioPlayer.playSound("powerup.wav");
                nextLevel();
            }
            if (e.getKeyCode() == KeyEvent.VK_2 && !upgradePowerTaken) {
                player.enableDoubleShot();
                upgradePowerTaken = true;
                AudioPlayer.playSound("powerup.wav");
                nextLevel();
            }
        }
    }

    private void nextLevel() {
        level++;
        upgradeChoice = false;
        levelComplete = false;
        spawnEnemies();
        startTime = System.currentTimeMillis();
        gameTimer.start();
    }

    private void resetToMainMenu() {
        showMainMenu();
    }

    public void showMainMenu() {
        showGameOverScreen = false;
        gameWon = false;
        AudioPlayer.stopAll();
        AudioPlayer.loopSound("music.wav");
        removeAll();
        setLayout(new BorderLayout());
        
        add(new MainMenu(this), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    

    @Override public void actionPerformed(ActionEvent e) { updateGame(); repaint(); }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mousePressed(MouseEvent e) {
        player.shoot();
        AudioPlayer.playSound("shoot.wav");
    }
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) { player.setPosition(e.getX(), e.getY()); }
    @Override public void mouseDragged(MouseEvent e) { player.setPosition(e.getX(), e.getY()); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
        
        width = getWidth();
        height = getHeight();

        if (player != null) player.draw(g);
        for (Enemy e : enemies) e.draw(g);
        for (Enemy m : spawnedMinions) m.draw(g);
        for (Bullet b : player.getBullets()) b.draw(g);
        for (EnemyBullet eb : enemyBullets) eb.draw(g);
        drawHUD(g);

        if (paused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("PAUZĂ", width / 2 - 80, height / 2);
        }
        if (levelComplete) drawUpgradeOptions(g);
        if (showGameOverScreen) drawGameOverScreen(g);
    }

    private void drawHUD(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Nivel: " + level, 10, 20);
        g.drawString("Highscore: " + (highscore / 1000) + "s", 10, 40);
        Image heartFull = new ImageIcon("resources/heart_full.png").getImage();
        Image heartBlank = new ImageIcon("resources/heart_blank.png").getImage();
        int xStart = width - 40 * 3 - 10;
        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                g.drawImage(heartFull, xStart + i * 40, 10, 32, 32, null);
            } else {
                g.drawImage(heartBlank, xStart + i * 40, 10, 32, 32, null);
            }
        }
    }
}
