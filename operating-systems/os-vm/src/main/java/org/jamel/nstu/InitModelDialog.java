package org.jamel.nstu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;


public class InitModelDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton insertProcess;
    private JButton removeProcess;
    private JButton removeAll;
    private JSpinner pageTime;
    private JSpinner numPages;
    private JSpinner managerInterval;
    private JSpinner diskInterval;
    private JSpinner k0;
    private JSpinner workSetTime;
    private JSpinner processInterval;
    private JSpinner memoryInterval;
    private JSpinner workSetInterval;
    private JComboBox algorithm;
    private JTable table;

    public InitModelDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Настройка параметров модели");
        pack();

        table.setModel(new ProcessesTable());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (String alg : algorithms) {
            algorithm.addItem(alg);
        }

        pageTime.setModel(new SpinnerNumberModel(70, 50, 400, 10));
        numPages.setModel(new SpinnerNumberModel(30, 10, 70, 1));
        managerInterval.setModel(new SpinnerNumberModel(100, 100, 800, 10));
        diskInterval.setModel(new SpinnerNumberModel(10, 10, 50, 1));
        k0.setModel(new SpinnerNumberModel(100, 20, 200, 10));
        workSetTime.setModel(new SpinnerNumberModel(300, 200, 800, 10));
        processInterval.setModel(new SpinnerNumberModel(10, 10, 50, 1));
        memoryInterval.setModel(new SpinnerNumberModel(50, 40, 200, 10));
        workSetInterval.setModel(new SpinnerNumberModel(30, 30, 180, 10));

        insertProcess.addActionListener(e -> {
            if (newProcDlg == null) {
                newProcDlg = new NewProcessDialog();
            }
            if (newProcDlg.doModal()) {
                for (ProcessView p : processes) {
                    if (p.label.equals(newProcDlg.getProcessName())) {
                        JOptionPane.showMessageDialog(InitModelDialog.this, "Задача с таким именем уже существует",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                processes.add(new ProcessView(newProcDlg.getProcessName(), newProcDlg.getnPages()));
                table.tableChanged(new TableModelEvent(new ProcessesTable()));
            }
        });

        removeProcess.addActionListener(e -> {
            int proc = table.getSelectedRowCount();
            if (proc != -1) {
                processes.remove(proc);
                table.tableChanged(new TableModelEvent(new ProcessesTable()));
            }
        });

        removeAll.addActionListener(e -> {
            processes.clear();
            table.tableChanged(new TableModelEvent(new ProcessesTable()));
            NewProcessDialog.count = 0;
        });

        buttonOK.addActionListener(this::onOK);

        buttonCancel.addActionListener(this::onCancel);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(null);
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
            this::onCancel,
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK(ActionEvent e) {
        // add your code here
        if (processes.size() == 0) {
            JOptionPane.showMessageDialog(this, "Вы не создали ни одного процесса",
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        } else {
            ok = true;
            dispose();
        }
    }

    private void onCancel(ActionEvent e) {
        // add your code here if necessary
        dispose();
    }

    public boolean doModal() {
        ok = false;
        setVisible(true);
        return ok;
    }

    public ArrayList<ProcessView> getProcesses() {
        return processes;
    }

    public int getPageTime() {
        return (Integer) pageTime.getValue();
    }

    public int getNumPages() {
        return (Integer) numPages.getValue();
    }

    public int getManagerInt() {
        return (Integer) managerInterval.getValue();
    }

    public int getDiskInt() {
        return (Integer) diskInterval.getValue();
    }

    public int getK0() {
        return (Integer) k0.getValue();
    }

    public int getWorkSetTime() {
        return (Integer) workSetTime.getValue();
    }

    public int getProcessInterval() {
        return (Integer) processInterval.getValue();
    }

    public int getMemoryInterval() {
        return (Integer) memoryInterval.getValue();
    }

    public int getWorkSetInterval() {
        return (Integer) workSetInterval.getValue();
    }

    public int getAlgorithm() {
        return algorithm.getSelectedIndex();
    }

    private boolean ok;
    private String[] algorithms = {
        "LRU локальный", "FIFO локальный",
        "LRU глобальный", "FIFO глобальный"
    };
    private final ArrayList<ProcessView> processes = new ArrayList<>(5);
    NewProcessDialog newProcDlg;

    private final String[] columns = {"№", "Имя задачи", "Количество страниц"};

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        contentPane = new JPanel();
        contentPane
            .setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 2,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(
            new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setText("OK");
        panel2.add(buttonOK, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 5, 10, 10), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory
            .createTitledBorder(BorderFactory.createEtchedBorder(), "Задачи", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 4, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(300, 100), null,
            0, false));
        table = new JTable();
        scrollPane1.setViewportView(table);
        insertProcess = new JButton();
        insertProcess.setText("Добавить");
        panel4.add(insertProcess, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeProcess = new JButton();
        removeProcess.setText("Удалить");
        panel4.add(removeProcess, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeAll = new JButton();
        removeAll.setBorderPainted(true);
        removeAll.setContentAreaFilled(true);
        removeAll.setDefaultCapable(true);
        removeAll.setFocusPainted(true);
        removeAll.setFocusTraversalPolicyProvider(false);
        removeAll.setOpaque(true);
        removeAll.setRequestFocusEnabled(true);
        removeAll.setRolloverEnabled(true);
        removeAll.setText("Удалить все");
        panel4.add(removeAll, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Параметры по умолчанию",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Время изменения рабочего набора:");
        panel5.add(label1, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        diskInterval = new JSpinner();
        panel5.add(diskInterval, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        workSetTime = new JSpinner();
        panel5.add(workSetTime, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        managerInterval = new JSpinner();
        panel5.add(managerInterval, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        numPages = new JSpinner();
        panel5.add(numPages, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        final JLabel label2 = new JLabel();
        label2.setText("Количество страниц памяти:");
        panel5.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Квант менеджера:");
        panel5.add(label3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Квант диска:");
        panel5.add(label4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        k0 = new JSpinner();
        panel5.add(k0, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        final JLabel label5 = new JLabel();
        label5.setText("Коэффициент неравномерности (x100):");
        panel5.add(label5, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Подбираемые параметры",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        workSetInterval = new JSpinner();
        panel6.add(workSetInterval, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        final JLabel label6 = new JLabel();
        label6.setText("Время неиспользования страницы:");
        panel6.add(label6, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Квант диспетчера:");
        panel6.add(label7, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Квант памяти:");
        panel6.add(label8, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Квант рабочего набора:");
        panel6.add(label9, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memoryInterval = new JSpinner();
        panel6.add(memoryInterval, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        final JLabel label10 = new JLabel();
        label10.setText("Алгоритм замещения:");
        panel6.add(label10, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pageTime = new JSpinner();
        panel6.add(pageTime, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        processInterval = new JSpinner();
        panel6.add(processInterval, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0,
            false));
        algorithm = new JComboBox();
        panel6.add(algorithm, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0,
            false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private class ProcessesTable extends AbstractTableModel {
        public int getRowCount() {
            return processes.size();
        }

        public int getColumnCount() {
            return columns.length;
        }

        public String getColumnName(int column) {
            return columns[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            ProcessView p = processes.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return String.valueOf(rowIndex);
                case 1:
                    return p.label;
                case 2:
                    return p.numPages;
                default:
                    return null;
            }
        }
    }
}
