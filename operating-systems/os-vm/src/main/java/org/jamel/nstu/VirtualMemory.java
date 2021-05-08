import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 15.05.2007
 * Time: 12:18:18
 * To change this template use File | Settings | File Templates.
 */
public class VirtualMemory {
    public VirtualMemory() {
        createSplashScreen();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                splashScreen.setVisible(true);
            }
        });
        final VMFrame frame = new VMFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
                hideSplashScreen();
            }
        });
    }

    public void createSplashScreen() {
        splashLabel = new JLabel(new ImageIcon(getClass().getResource("/resources/images/hello.jpg")));
        splashScreen = new JWindow();
        splashScreen.getContentPane().add(splashLabel);
        splashScreen.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        splashScreen.setLocation(
         screenSize.width/2 - splashScreen.getSize().width/2,
         screenSize.height/2 - splashScreen.getSize().height/2);
    }

    public void hideSplashScreen() {
        splashScreen.setVisible(false);
        splashScreen = null;
        splashLabel = null;
    }


    private JLabel splashLabel;
    private JWindow splashScreen;


    public static void main(String[] args) {
        new VirtualMemory();
    }
}
