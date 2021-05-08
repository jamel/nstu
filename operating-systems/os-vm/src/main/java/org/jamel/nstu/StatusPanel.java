package org.jamel.nstu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 20.05.2007
 * Time: 16:14:02
 * To change this template use File | Settings | File Templates.
 */
public class StatusPanel extends JPanel {
    public StatusPanel(OSSimulator simulator) {
        this.simulator = simulator;
        setLayout(new GridLayout(0, 1));
        add(osStatusPanel = new OSStatusPanel());
        add(processStatusPanel = new ProcessStatusPanel());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(speedPanel = new SpeedPanel(), BorderLayout.NORTH);
        panel.add(timerPanel = new TimerPanel(), BorderLayout.SOUTH);
        add(panel);
    }

    public void loadProcesses(ArrayList<ProcessView> processes) {
        processStatusPanel.processes.removeAllItems();
        for (ProcessView p: processes)
           processStatusPanel.processes.addItem(p.label);
        processStatusPanel.currSelectedProcess = 0;
    }

    public void step() {
        osStatusPanel.step();
        processStatusPanel.step();
        timerPanel.step();
    }

    private OSSimulator simulator;
    private OSStatusPanel osStatusPanel;
    private ProcessStatusPanel processStatusPanel;
    private TimerPanel timerPanel;
    private SpeedPanel speedPanel;

    private class OSStatusPanel extends JPanel {
        public OSStatusPanel() {
            setBorder(new TitledBorder(new EtchedBorder(), "Состояние ОС"));
            setLayout(new BorderLayout());
            JPanel panel1 = new JPanel(new GridLayout(0, 1));
            JPanel panel2 = new JPanel(new GridLayout(0, 1));
            panel2.setPreferredSize(new Dimension(80, 0));
            panel1.add(new Label("Количество страниц:")); panel2.add(numPages);
            panel1.add(new Label("Свободных страниц:"));  panel2.add(freePages);
            panel1.add(new Label("Доступных страниц:"));  panel2.add(emptyPages);
            panel1.add(new Label("Текущая задача:"));     panel2.add(currentProcess);
            panel1.add(new Label("Интервал задачи:"));    panel2.add(processInterval);
            panel1.add(new Label("Интервал менеджера:")); panel2.add(managerInterval);
            panel1.add(new Label("Интервал диска:"));     panel2.add(diskInterval);
            step();
            add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.EAST);
        }

        public void step() {
            numPages.setText(String.valueOf(simulator.numPages));
            freePages.setText(String.valueOf(simulator.numFreePages));
            emptyPages.setText(String.valueOf(simulator.numEmptyPages));
            currentProcess.setText(((ProcessView)simulator.currentProcess).label);
            processInterval.setText(String.valueOf(simulator.processInterval));
            managerInterval.setText(String.valueOf(simulator.managerInterval));
            diskInterval.setText(String.valueOf(simulator.diskInterval));
        }


        private JLabel numPages = new JLabel("0");
        private JLabel freePages = new JLabel("0");
        private JLabel emptyPages = new JLabel("0");
        private JLabel currentProcess = new JLabel("Процесс ?");
        private JLabel processInterval = new JLabel("0");
        private JLabel diskInterval = new JLabel("0");
        private JLabel managerInterval = new JLabel("0");
    }

    private class ProcessStatusPanel extends JPanel {
        public ProcessStatusPanel() {
            setBorder(new TitledBorder(new EtchedBorder(), "Состояние процесса"));
            setLayout(new BorderLayout());

            processes = new JComboBox();
            processes.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    currSelectedProcess = processes.getSelectedIndex();
                    step();
                }
            });

            JPanel panel1 = new JPanel(new GridLayout(0, 1));
            JPanel panel2 = new JPanel(new GridLayout(0, 1));
            panel2.setPreferredSize(new Dimension(80, 0));

            panel1.add(new Label("Задача:"));                   panel2.add(processes);
            panel1.add(new Label("Состояние:"));                panel2.add(processStatus);
            panel1.add(new Label("Текущая вирт. страница:"));   panel2.add(curVirtPage);
            panel1.add(new Label("Первая страница РН:"));       panel2.add(firstPageWS);
            panel1.add(new Label("Активное время:"));           panel2.add(activeTime);
            panel1.add(new Label("Текущий РН:"));               panel2.add(currentWS);
            panel1.add(new Label("Старый РН:"));                panel2.add(oldWS);
            panel1.add(new Label("Загружаемая физ. страница:")); panel2.add(loadPhisPage);
            add(panel1, BorderLayout.CENTER); add(panel2, BorderLayout.EAST);
        }

        public void step() {
            if (currSelectedProcess != -1) {
                Process p = simulator.processes.get(currSelectedProcess);

                processStatus.setText("READY");
                if (p.status == Process.WAIT)
                    processStatus.setText("WAIT");
                else if (p.status == Process.SWAP)
                    processStatus.setText("SWAP");

                curVirtPage.setText(String.valueOf(p.Cvpg));
                firstPageWS.setText(String.valueOf(p.Page0));
                activeTime.setText(String.valueOf(p.Wktim));
                currentWS.setText(String.valueOf(p.Wkset));
                oldWS.setText(String.valueOf(p.Wkset0));
                loadPhisPage.setText(String.valueOf(p.Npp));
            }
        }

        private JComboBox processes;
        private int currSelectedProcess = -1;
        private JLabel processStatus = new JLabel("READY");
        private JLabel curVirtPage = new JLabel("0");
        private JLabel firstPageWS = new JLabel("0");
        private JLabel activeTime = new JLabel("0");
        private JLabel currentWS = new JLabel("0");
        private JLabel oldWS = new JLabel("0");
        private JLabel loadPhisPage = new JLabel("0");
    }

    private class TimerPanel extends JPanel {
        public TimerPanel() {
            setBorder(new TitledBorder(new EtchedBorder(), "Модельное время"));
            add(time);
        }

        public void step() {
            time.setText("" + simulator.systemTime);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getPreferredSize() {
            return new Dimension(250,60);
        }

        private JLabel time = new JLabel("0");
    }

    private class SpeedPanel extends JPanel {
        public SpeedPanel() {
            setBorder(new TitledBorder(new EtchedBorder(), "Тормозистор"));

            final JSlider slider = new JSlider(0, 100, 10);
            slider.setMajorTickSpacing(20);
            slider.setMinorTickSpacing(5);
            slider.setPaintTicks(true);
            slider.setPaintLabels(true);
            slider.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    simulator.sleepTime = slider.getValue() + 1;
                }
            });

            add(slider);
        }
    }
}
