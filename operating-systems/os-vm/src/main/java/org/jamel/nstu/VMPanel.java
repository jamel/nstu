import javax.swing.*;
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 15.05.2007
 * Time: 12:24:14
 */
public class VMPanel extends JPanel
        implements MouseListener, MouseMotionListener
{
    public VMPanel(VMTableView table, ArrayList<ProcessView> processes) {
        this.table = table;
        this.processes =  processes;
        if (table != null && processes != null)
            addMouseListener(this);
    }

    public void setProcesses(ArrayList<ProcessView> processes) {
        this.processes =  processes;
        if (table != null && processes != null)
            addMouseListener(this);
    }

    public void setTable(VMTableView table) {
        this.table = table;
        if (table != null && processes != null)
            addMouseListener(this);
    }

    protected void paintComponent(Graphics g) {
        if (table == null || processes == null)
            return;
        Dimension d = getSize();
        if ((bufImage == null) || !d.equals(bufSize)) {
            bufImage = createImage(d.width, d.height);
            bufSize = d;
            if (bufGraphics != null)
                bufGraphics.dispose();
            bufGraphics = (Graphics2D)bufImage.getGraphics();
            bufGraphics.setFont(getFont());

            VMTableView.screenSize = ProcessView.screenSize = d;
            table.resize();
        }
        bufGraphics.setColor(getBackground());
        bufGraphics.fillRect(0, 0, d.width, d.height);

        Rectangle rect = new Rectangle(table.x1 - 45, 0, 30, d.height);
        bufGraphics.setPaint(new Color(192, 192, 192));
        bufGraphics.fill(rect);

        rect.setLocation(table.x2 + 15, 0);
        bufGraphics.fill(rect);
        AffineTransform oldTransform = bufGraphics.getTransform();
        AffineTransform transform = new AffineTransform();
        transform.translate(d.width/2, d.height/2);
        transform.rotate(- Math.PI / 2);
        bufGraphics.setTransform(transform);
        FontMetrics fm = g.getFontMetrics();
        String str = "Система преобразования адресов";
        int w = fm.stringWidth(str);
        int h = fm.getHeight() + 4;
        bufGraphics.setColor(new Color(100, 100, 100));
        bufGraphics.drawString(str, -w/2, -((table.x2 - table.x1)/2 + 30 + (15 - h)/2));
        bufGraphics.drawString(str, -w/2, (table.x2 - table.x1)/2 + 35 + (15 - h)/2);
        bufGraphics.setTransform(oldTransform);

        table.draw(bufGraphics);
        for (ProcessView p: processes) {
            if (p != currentProcess)
                p.draw(bufGraphics);
        }
        if (currentProcess != null)
            currentProcess.draw(bufGraphics);


        g.drawImage(bufImage, 0, 0, null);
    }

    ProcessView findProcess(Point2D p) {
        for (ProcessView process : processes) {
            if (process.contains(p))
                return process;
        }

        return null;
    }

    private VMTableView table;
    private ArrayList<ProcessView> processes;

    private ProcessView currentProcess;
    private Point pStart;

    private Image bufImage;
    private Graphics2D bufGraphics;
    private Dimension bufSize;


    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        addMouseMotionListener(this);
        pStart = e.getPoint();
        currentProcess = findProcess(e.getPoint());
        if (currentProcess != null) {
            currentProcess.selected = true;
            repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        removeMouseMotionListener(this);

        int x = e.getX();
        int y = e.getY();

        if (x < 0) x = 5;
        if (x >= bufSize.width) x = bufSize.width-5;
        if (y < 0) y = 5;
        if (y >= bufSize.height) y = bufSize.height - 5;

        if (currentProcess != null && pStart != null) {
            currentProcess.moveBy(x - (int)pStart.getX(), y - (int)pStart.getY());
            currentProcess.selected = false;
            repaint();
        }
        pStart = null;
        currentProcess = null;
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (currentProcess != null && pStart != null) {
            currentProcess.moveBy(x - (int)pStart.getX(), y - (int)pStart.getY());
            pStart.move(x, y);
            repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {

    }
}