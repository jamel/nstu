package org.jamel.nstu;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 16.05.2007
 * Time: 7:54:07
 *
 * Класс графического представления таблицы виртуальной памяти
 */
public class VMTableView extends VMTable {

    public VMTableView(VMTable table) {
        super(table);
    }

    public VMTableView(int numPages) {
        super(numPages);
    }

    public void resize() {
        pageHeight = TABLE_HEIGHT / numPages;
        if (screenSize.height > TABLE_HEIGHT + 60) // по 30 пискелей снизу и сверху
            pageHeight = (screenSize.height - 60) / numPages;
        if (pageHeight == 0) pageHeight = 1;
        x1 = (screenSize.width - TABLE_WIDTH) >> 1;
        x2 = x1 + TABLE_WIDTH;
        y1 = 30; //отступ сверху
        y2 = y1 + pageHeight * numPages;
    }

    public void draw(Graphics2D g) {
        Rectangle rect = new Rectangle(x1, y1, TABLE_WIDTH, pageHeight);
        for (int i = 0; i < numPages; i++) {
            if (pages[i].status == Page.Status.EMPTY)
                g.setPaint(new Color(100, 100, 100));
            else {
                if (pages[i].status == Page.Status.PRIVATE) {
                    ProcessView p = processes.get(pages[i].nProcess);
                    g.setColor((p.selected ? Color.RED : Color.BLUE));
                    if (p.x + ProcessView.PAGE_WIDTH / 2 < screenSize.width / 2)
                        drawArrowLeft(g, p.x + ProcessView.PAGE_WIDTH,
                            p.y + pages[i].nPage*ProcessView.PAGE_HEIGHT + ProcessView.PAGE_HEIGHT/2,
                            x1, rect.y + pageHeight/2);
                    else
                        drawArrowRight(g, p.x,
                            p.y + pages[i].nPage*ProcessView.PAGE_HEIGHT + ProcessView.PAGE_HEIGHT/2,
                            x2, rect.y + pageHeight/2);
                   g.setPaint(new GradientPaint(x1, rect.y, new Color(251, 250, 247),
                        x1, rect.y + rect.height, new Color(189, 189, 163)));
                }
                else // pages[i].status == FREE
                  g.setPaint(new GradientPaint(x1, rect.y, new Color(198, 195, 198),
                      x1, rect.y + rect.height, new Color(157, 152, 141)));

            }
            g.fill(rect);
            g.setColor(new Color(130, 130, 130));
            g.drawLine(x1, rect.y, x1 + TABLE_WIDTH, rect.y);
            rect.y += rect.height;
        }
        g.setColor(Color.BLACK);
        g.drawRect(x1, y1, x2-x1, y2-y1);
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth("Физическая память");
        int h = fm.getHeight() + 4;
        g.drawString("Физическая память", x1 + TABLE_WIDTH/2 - w/2, y2 + h);
    }

    private void drawArrowLeft(Graphics2D g, int x1, int y1, int x2, int y2) {
        CubicCurve2D curve = new CubicCurve2D.Float(x1, y1, x1+100, y1,
                x2-100, y2, x2, y2);
        g.draw(curve);
        g.fillPolygon(new int[] {x2-10, x2-12, x2, x2-12},
                      new int[] {y2, y2-4, y2, y2+4}, 4);
    }

    private void drawArrowRight(Graphics2D g, int x1, int y1, int x2, int y2) {
        CubicCurve2D curve = new CubicCurve2D.Float(x1, y1, x1-100, y1,
                x2+100, y2, x2, y2);
        g.draw(curve);
        g.fillPolygon(new int[] {x2+10, x2+12, x2, x2+12},
                      new int[] {y2, y2-4, y2, y2+4}, 4);
    }

    public int x1, y1, x2, y2; // положение таблицы на панели
    public int pageHeight; // высота страницы в пикселях

    public static Dimension screenSize = new Dimension(-1, -1);
    public static ArrayList<ProcessView> processes;

    // минимальная ширина таблицы
    public static final int TABLE_WIDTH = 100;
    //минимальная высота таблицы
    public static final int TABLE_HEIGHT = 400;
}
