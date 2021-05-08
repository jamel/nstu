package org.jamel.nstu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.util.Objects;

import javax.swing.*;

public class AboutDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel image;
    private JLabel sourceCodeLink;

    public AboutDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("О программе VirtualMemory v1.0");

        addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                image.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/me.gif"))));
                pack();
            }
        });

        buttonOK.addActionListener(this::onOK);

        sourceCodeLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sourceCodeLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String url = ((JLabel) e.getSource()).getText();
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    Thread thread = Thread.currentThread();
                    thread.getUncaughtExceptionHandler().uncaughtException(thread, ex);
                }
            }
        });
    }

    private void onOK(ActionEvent e) {
        // add your code here
        dispose();
    }

}
