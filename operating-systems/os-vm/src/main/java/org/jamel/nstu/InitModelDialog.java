package org.jamel.nstu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
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
        for (String alg : algorithms)
            algorithm.addItem(alg);

        pageTime.setModel(new SpinnerNumberModel(70, 50, 400, 10));
        numPages.setModel(new SpinnerNumberModel(30, 10, 70, 1));
        managerInterval.setModel(new SpinnerNumberModel(100, 100, 800, 10));
        diskInterval.setModel(new SpinnerNumberModel(10, 10, 50, 1));
        k0.setModel(new SpinnerNumberModel(100, 20, 200, 10));
        workSetTime.setModel(new SpinnerNumberModel(300, 200, 800, 10));
        processInterval.setModel(new SpinnerNumberModel(10, 10, 50, 1));
        memoryInterval.setModel(new SpinnerNumberModel(50, 40, 200, 10));
        workSetInterval.setModel(new SpinnerNumberModel(30, 30, 180, 10));

        insertProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (newProcDlg == null)
                    newProcDlg = new NewProcessDialog();
                if (newProcDlg.doModal()) {
                    for (ProcessView p: processes)
                        if (p.label.equals(newProcDlg.getProcessName())) {
                            JOptionPane.showMessageDialog(InitModelDialog.this, "Задача с таким именем уже существует",
                                    "Ошибка", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    processes.add(new ProcessView(newProcDlg.getProcessName(), newProcDlg.getnPages()));
                    table.tableChanged(new TableModelEvent(new ProcessesTable()));
                }
            }
        });

        removeProcess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int proc = table.getSelectedRowCount();
                if (proc != -1) {
                    processes.remove(proc);
                    table.tableChanged(new TableModelEvent(new ProcessesTable()));
                }
            }
        });

        removeAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processes.clear();
                table.tableChanged(new TableModelEvent(new ProcessesTable()));
                NewProcessDialog.count = 0;
            }
        });

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
        if (processes.size() == 0)
            JOptionPane.showMessageDialog(this, "Вы не создали ни одного процесса",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        else {
            ok = true;
            dispose();
        }
    }

    private void onCancel() {
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
        return (Integer)pageTime.getValue();
    }

    public int getNumPages() {
        return (Integer)numPages.getValue();
    }

    public int getManagerInt() {
        return (Integer)managerInterval.getValue();
    }

    public int getDiskInt() {
        return (Integer)diskInterval.getValue();
    }

    public int getK0() {
        return (Integer)k0.getValue();
    }

    public int getWorkSetTime() {
        return (Integer)workSetTime.getValue();
    }

    public int getProcessInterval() {
        return (Integer)processInterval.getValue();
    }

    public int getMemoryInterval() {
        return (Integer)memoryInterval.getValue();
    }

    public int getWorkSetInterval() {
        return (Integer)workSetInterval.getValue();
    }

    public int getAlgorithm() {
        return algorithm.getSelectedIndex();
    }


    private boolean ok;
    private String[] algorithms  = {"LRU локальный","FIFO локальный",
                                    "LRU глобальный","FIFO глобальный"};
    private ArrayList<ProcessView> processes = new ArrayList<ProcessView>(5);
    NewProcessDialog newProcDlg;

    private String[] columns = {"№", "Имя задачи", "Количество страниц"};

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
            switch(columnIndex) {
                case 0: return String.valueOf(rowIndex);
                case 1: return p.label;
                case 2: return p.numPages;
                default: return null;
            }
        }
    }
}
