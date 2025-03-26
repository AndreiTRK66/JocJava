import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Player {
    private int x, y;
    private int width = 100, height = 100;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private long lastShot = 0;
    private int shootDelay = 400;
    private boolean doubleShot = false;
    private Image sprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        sprite = new ImageIcon("resources/player.png").getImage();
    }
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    

    public void setPosition(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, width, height, null);
    }

    public void shoot() {
        long now = System.currentTimeMillis();
        if (now - lastShot >= shootDelay) {
            if (doubleShot) {
                bullets.add(new Bullet(x + 5, y));
                bullets.add(new Bullet(x + width - 15, y));
            } else {
                bullets.add(new Bullet(x + width / 2 - 5, y));
            }
            lastShot = now;
        }
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public void upgradeShootSpeed() {
        shootDelay = Math.max(100, shootDelay - 100);
    }

    public void enableDoubleShot() {
        doubleShot = true;
    }
}