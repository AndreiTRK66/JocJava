import java.awt.*;
import java.awt.Color;


public class Bullet {
    private int x, y;
    private int speed = 10;
    private int width = 10, height = 20;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }

    public boolean isOffScreen() {
        return y + height < 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}