import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 15.05.2007
 * Time: 12:20:29
 * To change this template use File | Settings | File Templates.
 */
public class VMFrame extends JFrame implements Runnable{
    public VMFrame() {
        setTitle("VirtualMemory v1.0");
        setSize(900, 600);

        simulator.initModel(30, 3);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Память", null, (vmPanel = new VMPanel(simulator.table, simulator.processes)));
        tabbedPane.addTab("Статистика работы ОС", null, (statPanel = new StatisticPanel(simulator)));
        tabbedPane.addTab("Состояние памяти ОС", null, memoryStatPanel = new MemoryStatPanel(simulator));
        tabbedPane.addTab("Состояние процессов", null, processStatPanel = new ProcessesPanel(simulator));
        tabbedPane.setPreferredSize(new Dimension(600, -1));

        statusPanel = new StatusPanel(simulator);
        statusPanel.loadProcesses(simulator.processes);
        add(statusPanel, BorderLayout.EAST);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedPane, statusPanel);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
        //add(new ButtonPanel(), BorderLayout.SOUTH);

        Action a;
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.add(a = new AbstractAction("Параметры модели",
                new ImageIcon(getClass().getResource("/resources/images/settings.gif"))) {
            public void actionPerformed(ActionEvent e) {
                stop();
                if (initModelDlg.doModal()) {
                    simulator.initModel(initModelDlg);
                    if (vmPanel.isShowing())
                        vmPanel.repaint();
                    statusPanel.loadProcesses(simulator.processes);
                    statusPanel.step();
                }
            }
        });
        a.putValue(Action.SHORT_DESCRIPTION, a.getValue(Action.NAME));
        toolBar.addSeparator();

        toolBar.add(a = new AbstractAction("Запуск моделирования",
                new ImageIcon(getClass().getResource("/resources/images/execute.gif"))) {
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        a.putValue(Action.SHORT_DESCRIPTION, a.getValue(Action.NAME));

        toolBar.add(a = new AbstractAction("Один шаг моделирования",
                new ImageIcon(getClass().getResource("/resources/images/step.gif"))) {
            public void actionPerformed(ActionEvent e) {
                step();
            }
        });
        a.putValue(Action.SHORT_DESCRIPTION, a.getValue(Action.NAME));

        toolBar.add(a = new AbstractAction("Приостановка моделирования",
                new ImageIcon(getClass().getResource("/resources/images/pause.gif"))) {
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        a.putValue(Action.SHORT_DESCRIPTION, a.getValue(Action.NAME));

        toolBar.addSeparator();
        toolBar.add(a = new AbstractAction("Результаты моделирования",
                new ImageIcon(getClass().getResource("/resources/images/results.gif"))) {
            public void actionPerformed(ActionEvent e) {
                stop();
                resDialog.setVisible(true);
            }
        });
        a.putValue(Action.SHORT_DESCRIPTION, a.getValue(Action.NAME));

        toolBar.addSeparator();
        toolBar.add(a = new AbstractAction("О программе",
                new ImageIcon(getClass().getResource("/resources/images/about.gif"))) {
            public void actionPerformed(ActionEvent e) {
                aboutDialog.setVisible(true);
            }
        });
        a.putValue(Action.SHORT_DESCRIPTION, a.getValue(Action.NAME));
        add(toolBar, BorderLayout.NORTH);
    }

    public void step() {
        simulator.step();
        processStatPanel.step();
        memoryStatPanel.step();
        statusPanel.step();
        vmPanel.repaint();
        statPanel.step();
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (me == thread) {
            step();
            try {
                Thread.sleep(simulator.sleepTime);
            }
            catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
        statPanel.start();
    }

    public void stop() {
        thread = null;
        statPanel.stop();
    }

    private VMPanel vmPanel;
    private StatisticPanel statPanel;
    private Thread thread;
    private OSSimulator simulator = new OSSimulator();
    private StatusPanel statusPanel;
    private MemoryStatPanel memoryStatPanel;
    private ProcessesPanel processStatPanel;
    private InitModelDialog initModelDlg = new InitModelDialog();
    private ResultsDialog resDialog = new ResultsDialog(simulator);
    private AboutDialog aboutDialog = new AboutDialog();
}