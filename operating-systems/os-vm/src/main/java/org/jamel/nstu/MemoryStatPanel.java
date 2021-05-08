package org.jamel.nstu;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 23.05.2007
 * Time: 18:21:34
 * To change this template use File | Settings | File Templates.
 */
public class MemoryStatPanel extends JPanel {
    public MemoryStatPanel(OSSimulator simulator) {
        this.simulator = simulator;
        setLayout(new BorderLayout());
        table = new JTable(new MemoryTable());
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void step() {
        table.tableChanged(new TableModelEvent(new MemoryTable()));
    }

    private JTable table;
    private OSSimulator simulator;

    private String[] columns = {"Адрес", "Статус", "Задача", "Страница", "Количество обращений"};

    private class MemoryTable extends AbstractTableModel {
        public int getRowCount() {
            return simulator.table.numPages;
        }

        public int getColumnCount() {
            return columns.length;
        }

        public String getColumnName(int c) {
            return columns[c];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Page p = simulator.table.pages[rowIndex];
            switch(columnIndex) {
                case 0 : return String.valueOf(rowIndex);
                case 1 :
                    if (p.status == Page.EMPTY)
                        return "EMPTY";
                    else if (p.status == Page.FREE)
                        return "FREE";
                    else
                        return "PRIVATE";
                case 2 :
                    if (p.nProcess == -1)
                        return "---";
                    return simulator.processes.get(p.nProcess).label;
                case 3 : return String.valueOf(p.nPage);
                case 4: return String.valueOf(p.nCalls);
                default:
                    return null;
            }
        }
    }
}
