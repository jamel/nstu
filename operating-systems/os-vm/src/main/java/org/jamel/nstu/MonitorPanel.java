package org.jamel.nstu;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;


public class MonitorPanel extends JPanel {

    public Surface surf;

    public MonitorPanel(String title, int maxValue, String maxValueString, String curValueString) {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder(new EtchedBorder(), title));
        add(surf = new Surface(maxValue, maxValueString, curValueString));
    }

    public class Surface extends JPanel implements Runnable {
        public Thread thread;
        private int w, h;
        private BufferedImage bimg;
        private Graphics2D big;
        private Font font = new Font("Times New Roman", Font.PLAIN, 11);
        private Runtime r = Runtime.getRuntime();
        private int columnInc;
        private float pts[];
        private int ptNum;
        private int ascent, descent;

        private Rectangle graphOutlineRect = new Rectangle();
        private Rectangle2D mfRect = new Rectangle2D.Float();
        private Rectangle2D muRect = new Rectangle2D.Float();
        private Line2D graphLine = new Line2D.Float();
        private Color graphColor = new Color(46, 139, 87);
        private Color mfColor = new Color(0, 100, 0);

        private float maxValue;
        private float curValue;
        private String maxValueString;
        private String curValueString;

        public Surface(int maxValue, String maxValueString, String curValueString) {
            this.maxValue = maxValue;
            this.maxValueString = maxValueString;
            this.curValueString = curValueString;
            setBackground(Color.black);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            return new Dimension(50,70);
        }

        public void setMax(int max) {
            maxValue = max;
        }

        public void setCurrent(int cur) {
            curValue = cur;
        }

        public void setCurValueString(String curValueString) {
            this.curValueString = curValueString;
        }

        public void setMaxValueString(String maxValueString) {
            this.maxValueString = maxValueString;
        }
            
        public void paint(Graphics g) {
            Dimension d = getSize();
            if (d.width != w || d.height != h) {
                if (d.width >= getPreferredSize().width &&
                d.height >= getPreferredSize().height) {
                    w = d.width;
                    h = d.height;
                    bimg = (BufferedImage) createImage(w, h);
                    big = bimg.createGraphics();
                    big.setFont(font);
                    FontMetrics fm = big.getFontMetrics(font);
                    ascent = (int) fm.getAscent();
                    descent = (int) fm.getDescent();
                }
            }

            big.setBackground(getBackground());
            big.clearRect(0,0,w,h);

            big.setColor(Color.green);
            big.drawString(String.valueOf(maxValue) + maxValueString,  4.0f, (float) ascent+0.5f);
            big.drawString(String.valueOf(curValue) + curValueString, 4, h-descent);

            float ssH = ascent + descent;
            float remainingHeight = (float) (h - (ssH*2) - 0.5f);
            float blockHeight = remainingHeight/10;
            float blockWidth = 30.0f;
            float remainingWidth = (float) (w - blockWidth - 10);

            big.setColor(mfColor);
            int MemUsage = (int) ((maxValue - curValue) * 10 / maxValue);
            int i = 0;
            for ( ; i < MemUsage ; i++) { 
                mfRect.setRect(5,(float) ssH+i*blockHeight,
                                blockWidth,(float) blockHeight-1);
                big.fill(mfRect);
            }

            big.setColor(Color.green);
            for ( ; i < 10; i++)  {
                muRect.setRect(5,(float) ssH+i*blockHeight,
                                blockWidth,(float) blockHeight-1);
                big.fill(muRect);
            }

            big.setColor(graphColor);
            int graphX = 40;
            int graphY = (int) ssH;
            int graphW = w - graphX - 5;
            int graphH = (int) remainingHeight;
            graphOutlineRect.setRect(graphX, graphY, graphW, graphH);
            big.draw(graphOutlineRect);

            int graphRow = graphH/10;

            for (int j = graphY; j <= graphH+graphY; j += graphRow) {
                graphLine.setLine(graphX,j,graphX+graphW,j);
                big.draw(graphLine);
            }
        
            int graphColumn = graphW/15;

            if (columnInc == 0) {
                columnInc = graphColumn;
            }

            for (int j = graphX+columnInc; j < graphW+graphX; j+=graphColumn) {
                graphLine.setLine(j,graphY,j,graphY+graphH);
                big.draw(graphLine);
            }

            --columnInc;

            if (pts == null) {
                pts = new float[graphW];
                ptNum = 0;
            } else if (pts.length != graphW) {
                float tmp[] = null;
                if (ptNum < graphW) {     
                    tmp = new float[ptNum];
                    System.arraycopy(pts, 0, tmp, 0, tmp.length);
                } else {        
                    tmp = new float[graphW];
                    System.arraycopy(pts, pts.length-tmp.length, tmp, 0, tmp.length);
                    ptNum = tmp.length - 2;
                }
                pts = new float[graphW];
                System.arraycopy(tmp, 0, pts, 0, tmp.length);
            } else {
                big.setColor(Color.yellow);
                pts[ptNum++] = (maxValue - curValue) / maxValue;
                for (int j=graphX+graphW-ptNum, k=0; k < ptNum; k++, j++) {
                    if (k != 0) {
                        int y1 = (int)(graphY + graphH * pts[k-1]);
                        int y2 = (int)(graphY + graphH * pts[k]);
                        if (pts[k] != pts[k-1]) {
                            //big.drawLine(j-1, pts[k-1], j, pts[k]);
                            big.drawLine(j-1, y1, j, y2);
                        } else {
                            //big.fillRect(j, pts[k], 1, 1);
                            big.fillRect(j, y2, 1, 1);
                        }
                    }
                }
                if (ptNum >= pts.length-1) {
                    for (int j = 1; j < ptNum; j++) {
                        pts[j-1] = pts[j];
                    }
                    --ptNum;
                }
            }
            g.drawImage(bimg, 0, 0, this);
        }

        public void start() {
            thread = new Thread(this);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }

        public synchronized void stop() {
            thread = null;
            notify();
        }

        public void run() {

            Thread me = Thread.currentThread();

            while (thread == me && !isShowing() || getSize().width == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { return; }
            }
    
            while (thread == me) {
                repaint();
                try {
                    Thread.sleep(OSSimulator.sleepTime * 10 + 50);
                } catch (InterruptedException e) { break; }
            }
        }
    }
}
