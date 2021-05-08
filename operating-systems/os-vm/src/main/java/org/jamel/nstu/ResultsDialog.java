package org.jamel.nstu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
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
                for (ProcessView p: simulator.processes)
                    countVirtMem += p.numPages;
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
                if (columnIndex == 0)
                    return null;
                else if (columnIndex == 1)
                    return "Всего страниц";
                return "" + countVirtMem;
            }
            else {
                ProcessView p = simulator.processes.get(rowIndex);
                switch(columnIndex) {
                    case 0: return String.valueOf(rowIndex);
                    case 1: return p.label;
                    case 2: return p.numPages;
                    default: return null;
                }
            }
        }
    }
}
