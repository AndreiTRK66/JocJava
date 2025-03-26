import java.awt.*;
import java.awt.Color;


public class EnemyBullet {
    private int x, y;
    private int speed = 7;
    private int width = 6, height = 16;

    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}