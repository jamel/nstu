package org.jamel.nstu;

import java.awt.*;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 20.05.2007
 * Time: 11:11:29
 * To change this template use File | Settings | File Templates.
 */
public class StatisticPanel extends JPanel {
    public StatisticPanel(OSSimulator simulator) {
        this.simulator = simulator;

        setLayout(new GridLayout(3, 2));
        cpuState = new MonitorPanel("Загрузка ЦП", 100, "%", "%");
        add(cpuState);

        int avrPages = 0;
        if (simulator.numProcesses != 0) {
            int sumPages = 0;
            for (Process p: simulator.processes)
                sumPages += p.numPages;
            avrPages = sumPages / simulator.numProcesses;
        }

        avrWorkSetState = new MonitorPanel("Средний рабочий набор", avrPages, " Среднее количество страниц на задачу", " Текущее среднее значение РН");
        add(avrWorkSetState);

        memoryState = new MonitorPanel("Загрузка памяти", 100, "%", "%");
        add(memoryState);

        processSpeedState = new MonitorPanel("Скорость задачи", 100, "%", "%");
        add(processSpeedState);

        diskState = new MonitorPanel("Загрузка диска", 100, "%", "%");
        add(diskState);

        pageFailedState = new MonitorPanel("Страничные сбои", 100, "%", "%");
        add(pageFailedState);
    }

    public void step() {
        cpuState.surf.setCurrent(simulator.processorLoad);
        memoryState.surf.setCurrent(simulator.memoryLoad);
        diskState.surf.setCurrent(simulator.diskLoad);
        avrWorkSetState.surf.setCurrent(simulator.WKSET);
        processSpeedState.surf.setCurrent(simulator.VTSK);
        pageFailedState.surf.setCurrent(simulator.VMEM);
    }

    public void start() {
        cpuState.surf.start();
        memoryState.surf.start();
        diskState.surf.start();
        avrWorkSetState.surf.start();
        processSpeedState.surf.start();
        pageFailedState.surf.start();
    }

    public void stop() {
        cpuState.surf.stop();
        memoryState.surf.stop();
        diskState.surf.stop();
        avrWorkSetState.surf.stop();
        processSpeedState.surf.stop();
        pageFailedState.surf.stop();
    }

    private OSSimulator simulator;
    private MonitorPanel cpuState;
    private MonitorPanel memoryState;
    private MonitorPanel diskState;
    private MonitorPanel avrWorkSetState;
    private MonitorPanel processSpeedState;
    private MonitorPanel pageFailedState;

}
