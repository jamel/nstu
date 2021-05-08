package org.jamel.nstu;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 24.05.2007
 * Time: 15:42:38
 * To change this template use File | Settings | File Templates.
 */
public class ProcessesPanel extends JPanel {
    public ProcessesPanel(OSSimulator sim) {
        simulator = sim;
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.add(new JLabel("Расположение в системе:"));
        panel.add(location = new JComboBox<>(new String[] {
                "Все процессы",
                "Очередь менеджера",
                "Очередь диска"
        }));
        location.addItemListener(this::step);
        add(panel, BorderLayout.NORTH);

        table = new JTable(new ProcessesTable());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void step(ItemEvent e) {
        table.tableChanged(new TableModelEvent(new ProcessesTable()));
    }

    private OSSimulator simulator;
    private JTable table;
    private JComboBox location;

    private String[] columns = {
        "№", "Имя задачи", "Количество страниц",
        "Состояние", "Активное время",  "Текущий РН", "Старый РН"
    };

    private class ProcessesTable extends AbstractTableModel {
        public int getRowCount() {
            int index = location.getSelectedIndex();
            if (index == 0)
                return simulator.processes.size();
            else if (index == 1)
                return simulator.managerQueue.size();
            else if (index == 2)
                return simulator.diskQueue.size();
            return 0;
        }

        public int getColumnCount() {
            return columns.length;
        }

        public String getColumnName(int c) {
            return columns[c];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            ProcessView p = null;
            int index = location.getSelectedIndex();
            if (index == 0)
                p = simulator.processes.get(rowIndex);
            else if (index == 1) {
                if (rowIndex < simulator.managerQueue.size())
                    p = ((LinkedList<ProcessView>)simulator.managerQueue).get(rowIndex);
            }
            else if (index == 2) {
                if (rowIndex < simulator.diskQueue.size())
                    p = ((LinkedList<ProcessView>)simulator.diskQueue).get(rowIndex);
            }
            else
                return null;

            if (p == null)
                return null;

            switch(columnIndex) {
                case 0: return String.valueOf(rowIndex);
                case 1: return String.valueOf(p.label);
                case 2: return String.valueOf(p.numPages);
                case 3:
                    if (p.status == Process.READY)
                        return "READY";
                    else if (p.status == Process.SWAP)
                        return "SWAP";
                    else
                        return "WAIT";
                case 4: return String.valueOf(p.Wktim);
                case 5: return String.valueOf(p.Wkset);
                case 6: return String.valueOf(p.Wkset0);
                default:
                    return null;
            }
        }
    }
}
