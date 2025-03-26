import java.awt.*;
import java.awt.Color;

import java.util.Random;

import javax.swing.ImageIcon;

public class Enemy {
    protected int x, y;
    protected int width = 100, height = 100;
    protected int speedX = 2, speedY = 1;
    protected Image sprite;
    protected boolean isBoss = false;
    protected int health = 1;
    protected Random rand = new Random();

    public Enemy(int x, int y, boolean boss) {
        this.x = x;
        this.y = y;
        this.isBoss = boss;

        if (boss) {
            width = 300;
            height = 300;
            health = 20;
            sprite = new ImageIcon("resources/boss_red.png").getImage();
        } else {
            sprite = new ImageIcon("resources/enemy_red.png").getImage();
        }
    }

    public void update() {
        x += speedX;
        y += (rand.nextInt(100) < 5) ? speedY : 0;

        if (x <= 0 || x + width >= Toolkit.getDefaultToolkit().getScreenSize().width) {
            speedX *= -1;
        }
    }

    public void draw(Graphics g) {
        if (isBoss) {
            g.setColor(Color.RED);
            g.fillRect(x, y - 10, width, 5);
            g.setColor(Color.GREEN);
            g.fillRect(x, y - 10, (int)((width) * ((double)health / 20)), 5);
        }
        g.drawImage(sprite, x, y, width, height, null);
    }

    public boolean isOffScreen() {
        return y > Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean hit() {
        health--;
        return health <= 0;
    }

    public boolean isBoss() {
        return isBoss;
    }
}