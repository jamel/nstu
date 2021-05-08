package org.jamel.nstu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;

public class ResultsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JTable table;
    private JLabel numPages;
    private JLabel pageTime;
    private JLabel processInterval;
    private JLabel managerInterval;
    private JLabel diskInterval;
    private JLabel memoryInterval;
    private JLabel workSetInterval;
    private JLabel k0;
    private JLabel workSetTime;
    private JLabel algorithm;
    private JLabel cpuLoad;
    private JLabel diskLoad;
    private JLabel memoryLoad;
    private JLabel avrWorkSet;
    private JLabel processSpeed;
    private JLabel pageErrors;

    public ResultsDialog(OSSimulator simul) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSave);
        setTitle("Результаты моделирования");
        pack();

        this.simulator = simul;
        table.setModel(new ProcessesTable());

        buttonCancel.addActionListener(this::onCancel);
        buttonSave.addActionListener(this::onSave);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel(null);
            }

            public void windowActivated(WindowEvent e) {
                super.windowActivated(e);

                numPages.setText("" + simulator.numPages);
                pageTime.setText("" + simulator.Dpgswap);
                processInterval.setText("" + simulator.processInt);
                managerInterval.setText("" + simulator.managerInt);
                diskInterval.setText("" + simulator.diskInt);
                memoryInterval.setText("" + simulator.memoryInt);
                workSetInterval.setText("" + simulator.workSetInt);
                k0.setText("" + simulator.Kmem);
                workSetTime.setText("" + simulator.WkSetV);
                algorithm.setText(algorithms[simulator.nAlgorithm]);

                cpuLoad.setText("" + simulator.processorLoad + " %");
                diskLoad.setText("" + simulator.diskLoad + " %");
                memoryLoad.setText("" + simulator.memoryLoad + " %");
                avrWorkSet.setText("" + simulator.WKSET);
                processSpeed.setText("" + simulator.VTSK + " %");
                pageErrors.setText("" + simulator.VMEM + " %");

                countVirtMem = 0;
                for (ProcessView p : simulator.processes) {
                    countVirtMem += p.numPages;
                }
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(
            this::onCancel,
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onCancel(ActionEvent e) {
        // add your code here if necessary
        dispose();
    }

    private void onSave(ActionEvent e) {
        chooser.setFileFilter(new TxtFilter());
        int result = chooser.showDialog(this, "Сохранить");
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = chooser.getSelectedFile().getPath();
            try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
                out.print("Количество задач               ");
                out.println(simulator.processes.size());
                out.print("Всего страниц вирт. памяти     ");
                out.println(countVirtMem);
                out.print("Количество страниц памяти      ");
                out.println(simulator.numPages);
                out.print("Время неиспользования страницы ");
                out.println(simulator.Dpgswap);
                out.print("Квант диспетчера               ");
                out.println(simulator.processInt);
                out.print("Квант менеджера                ");
                out.println(simulator.managerInt);
                out.print("Квант диска                    ");
                out.println(simulator.diskInt);
                out.print("Квант памяти                   ");
                out.println(simulator.memoryInt);
                out.print("Квант рабочего набора          ");
                out.println(simulator.workSetInt);
                out.print("Коэфф. неравномерности *100    ");
                out.println(simulator.Kmem);
                out.print("Время изменения РН             ");
                out.println(simulator.WkSetV);
                out.print("Алгоритм замещения             ");
                out.println(algorithms[simulator.nAlgorithm]);

                out.println();
                out.print("Загрузка ЦП                    ");
                out.println(simulator.processorLoad + " %");
                out.print("Загрузка диска                 ");
                out.println(simulator.diskLoad + " %");
                out.print("Загрузка памяти                ");
                out.println(simulator.memoryLoad + " %");
                out.print("Средний рабочий набор          ");
                out.println(simulator.WKSET);
                out.print("Скорость задачи                ");
                out.println(simulator.VTSK + " %");
                out.print("Страничные сбои                ");
                out.println(simulator.VMEM + " %");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "При записи данных в файл произошла ошибка: " + ex.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public boolean doModal() {
        setVisible(true);
        return true;
    }

    private OSSimulator simulator;
    private int countVirtMem;
    private JFileChooser chooser = new JFileChooser(new File("."));
    private String[] columns = {"№", "Имя задачи", "Количество страниц"};
    private String[] algorithms = {
        "LRU локальный", "FIFO локальный",
        "LRU глобальный", "FIFO глобальный"
    };

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
            .setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
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
        buttonSave = new JButton();
        buttonSave.setText("Сохранить в файл");
        panel2.add(buttonSave, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Закрыть");
        panel2.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 2, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory
            .createTitledBorder(BorderFactory.createEtchedBorder(), "Процессы", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(300, -1), null,
            0, false));
        table = new JTable();
        scrollPane1.setViewportView(table);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(10, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Параметры модели",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        processInterval = new JLabel();
        processInterval.setText("Label");
        panel5.add(processInterval, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pageTime = new JLabel();
        pageTime.setText("Label");
        panel5.add(pageTime, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        diskInterval = new JLabel();
        diskInterval.setText("Label");
        panel5.add(diskInterval, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memoryInterval = new JLabel();
        memoryInterval.setText("Label");
        panel5.add(memoryInterval, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        workSetInterval = new JLabel();
        workSetInterval.setText("Label");
        panel5.add(workSetInterval, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        k0 = new JLabel();
        k0.setText("Label");
        panel5.add(k0, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        workSetTime = new JLabel();
        workSetTime.setText("Label");
        panel5.add(workSetTime, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        algorithm = new JLabel();
        algorithm.setText("Label");
        panel5.add(algorithm, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numPages = new JLabel();
        numPages.setText("Label");
        panel5.add(numPages, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Количество страниц памяти");
        panel5.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Время неиспользования страницы");
        panel5.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Квант диспетчера");
        panel5.add(label3, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Квант менеджера");
        panel5.add(label4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Квант диска");
        panel5.add(label5, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Квант памяти");
        panel5.add(label6, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Квант рабочего набора");
        panel5.add(label7, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Коэфф. неравномерности  х100");
        panel5.add(label8, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Время изменения РН");
        panel5.add(label9, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Алгоритм замещения");
        panel5.add(label10, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        managerInterval = new JLabel();
        managerInterval.setText("Label");
        panel5.add(managerInterval, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel6.setBorder(BorderFactory
            .createTitledBorder(BorderFactory.createEtchedBorder(), "Результаты", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, null, null));
        pageErrors = new JLabel();
        pageErrors.setText("Label");
        panel6.add(pageErrors, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        processSpeed = new JLabel();
        processSpeed.setText("Label");
        panel6.add(processSpeed, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        avrWorkSet = new JLabel();
        avrWorkSet.setText("Label");
        panel6.add(avrWorkSet, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        memoryLoad = new JLabel();
        memoryLoad.setText("Label");
        panel6.add(memoryLoad, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        diskLoad = new JLabel();
        diskLoad.setText("Label");
        panel6.add(diskLoad, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cpuLoad = new JLabel();
        cpuLoad.setText("Label");
        panel6.add(cpuLoad, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Загрузка ЦП");
        panel6.add(label11, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Загрузка диска");
        panel6.add(label12, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Загрузка памяти");
        panel6.add(label13, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Средний рабочий набор");
        panel6.add(label14, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Скорость задачи");
        panel6.add(label15, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Страничные сбои");
        panel6.add(label16, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private static class TxtFilter extends FileFilter {
        public boolean accept(File pathname) {
            return pathname.getName().toLowerCase().endsWith(".txt");
        }

        public String getDescription() {
            return "Текстовые файлы (*.txt)";
        }
    }

    private class ProcessesTable extends AbstractTableModel {
        public int getRowCount() {
            return simulator.processes.size() + 1;
        }

        public int getColumnCount() {
            return columns.length;
        }

        public String getColumnName(int column) {
            return columns[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex == simulator.processes.size()) {
                if (columnIndex == 0) {
                    return null;
                } else if (columnIndex == 1) {
                    return "Всего страниц";
                }
                return "" + countVirtMem;
            } else {
                ProcessView p = simulator.processes.get(rowIndex);
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
}
