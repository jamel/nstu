package org.jamel.nstu;

import java.awt.*;
import java.util.Objects;

import javax.swing.*;

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
        SwingUtilities.invokeLater(() -> splashScreen.setVisible(true));

        final VMFrame frame = new VMFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
            hideSplashScreen();
        });
    }

    public void createSplashScreen() {
        splashLabel = new JLabel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/hello.jpg"))));
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
