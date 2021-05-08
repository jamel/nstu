package org.jamel.nstu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class NewProcessDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField processName;
    private JSpinner numPages;

    public NewProcessDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Создание новой задачи");
        pack();

        numPages.setModel(new SpinnerNumberModel(10, 1, 50, 1));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        ok = true;
        procName = processName.getText();
        nPages = (Integer)numPages.getValue();
        count++;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        ok = false;
        dispose();
    }

    public boolean doModal() {
        ok = false;
        processName.setText("Задача " + count);
        setVisible(true);
        return ok;
    }

    public int getnPages() {
        return nPages;
    }

    public String getProcessName() {
        return procName;
    }

    private boolean ok;
    private String procName;
    private int nPages;
    public static int count = 0;

}
