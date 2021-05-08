import java.awt.*;
import java.awt.geom.*;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 16.05.2007
 * Time: 7:53:57
 *
 * Класс графического представления задачи
 */

public class ProcessView extends Process {
    public ProcessView(String label, int numPages) {
        super(numPages);
        this.label = label;
    }

    public void moveBy(int dx, int dy) {
        x += dx;  y += dy;
    }

    public boolean contains(Point2D p) {
        return (p.getX() >= x && p.getX() < x + PAGE_WIDTH) &&
               (p.getY() >= y && p.getY() < y + numPages * PAGE_HEIGHT);
    }

    public void draw(Graphics2D g) {
        Rectangle rect = new Rectangle(x, y, PAGE_WIDTH, PAGE_HEIGHT);
        Color color = (selected ? Color.RED : Color.BLACK);
        for (int i = 0; i < numPages; i++) {
            if (pages[i] == -1)
                g.setPaint(new Color(100, 100, 100));
            else {
                g.setColor(color);
                g.setPaint(new GradientPaint(x, rect.y, new Color(251, 250, 247),
                        x, rect.y + PAGE_HEIGHT, new Color(189, 189, 163)));
            }
            g.fill(rect);
            g.setColor(new Color(130, 130, 130));
            g.drawLine(x, rect.y, x + PAGE_WIDTH, rect.y);
            rect.setLocation(x, rect.y + PAGE_HEIGHT);
        }
        g.setColor(color);
        g.drawString(label, x, y - 3);
        g.drawRect(x, y, PAGE_WIDTH, numPages * PAGE_HEIGHT);
    }

    public String label;
    public int x, y;
    public boolean selected;

    //public static VMTableView table;
    public static Dimension screenSize;

    // высота страницы памяти для процесса
    public static final int PAGE_HEIGHT = 15;
    // ширина страницы памяти для процесса
    public static final int PAGE_WIDTH = 50;
}
