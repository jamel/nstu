package org.jamel.nstu;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 16.05.2007
 * Time: 20:05:59
 * To change this template use File | Settings | File Templates.
 */
public class OSSimulator implements Runnable {
    public OSSimulator() {
        processes = new ArrayList<>(numProcesses);
        table = new VMTableView(numPages);
        VMTableView.processes = processes;
    }

    public void start() {
        modelator = new Thread(this);
        modelator.start();
    }

    public void stop() {
        modelator = null;
    }

    public synchronized void step() {
        simulation();
    }

    public void reset() {
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (me == modelator) {
            step();
            try {
                Thread.sleep(sleepTime);
            } catch(InterruptedException e) {
                break;
            }
        }
    }

    public void initModel(int nPages, int nProcesses) {
        stop();
        numPages = nPages;
        numProcesses = nProcesses;
        // Сброс очередей
        diskQueue = new LinkedList<>();
        managerQueue = new LinkedList<>();

        // Сброс модельного времени
        systemTime = 0;

        /*processes = new ArrayList<ProcessView>(numProcesses);
        table = new VMTableView(numPages);
        table.processes = processes;*/
        processes.clear();
        table.resize(numPages);
        table.resize();

        for (int i = 0; i < numProcesses; i++) {			// Установка дескрипторов задач
            ProcessView p = new ProcessView("Процесс " + (i+1), 10);
            p.Nwkset = NWkset;
            p.Wkset0 = 0;
            p.Wktim  = 0;
            p.Cvpg   = 0;
            p.Page0  = 0;
            p.Totim  = 0;
            managerQueueIn(p);
            processes.add(p);
        }
        currentProcess = processes.get(0);
        currentProcessIndex = 0;

        diskInterval = processInterval = -1;    // Сброс квантов диска и ЦП
        managerInterval = 1;				    // Установка кванта менеджера

        TWKS = memoryTime = processorTime = diskTime = 0;
        WKSET = NWKS = memoryLoad = processorLoad = diskLoad = 0;
        numSuccessCalls = numFailedCalls = 0;
        numFreePages = 0;
        numEmptyPages = numPages;

        // Определение значения K0, выше которого "хвост" экспоненты дает
        // недопустимый номер виртуальной страницы в датчике случайных чисел
        int avrPages = 0;
        if (numProcesses != 0) {
            int sumPages = 0;
            for (Process p: processes)
                sumPages += p.numPages;
            avrPages = sumPages / numProcesses;
        }
        K0 = (int)(1000.0 * Math.exp(- (Kmem/100.0) * avrPages) + 2);
    }

    public void initModel(InitModelDialog dlg) {
        stop();
        numProcesses = dlg.getProcesses().size();
        numPages = dlg.getNumPages();
        if (numProcesses == 0 || numPages == 0)
            return;

        // Сброс очередей
        diskQueue = new LinkedList<>();
        managerQueue = new LinkedList<>();

        // Сброс модельного времени
        systemTime = 0;

        /*processes = new ArrayList<ProcessView>(numProcesses);
        table = new VMTableView(numPages);
        table.processes = processes;*/
        processes.clear();
        for (ProcessView p: dlg.getProcesses()) {
            p.Nwkset = NWkset;
            p.Wkset0 = 0;
            p.Wktim  = 0;
            p.Cvpg   = 0;
            p.Page0  = 0;
            p.Totim  = 0;
            p.clearMemory();
            managerQueueIn(p);
            processes.add(p);
        }
        table.resize(numPages);
        table.resize();

        currentProcess = processes.get(0);
        currentProcessIndex = 0;

        diskInterval = processInterval = -1;    // Сброс квантов диска и ЦП
        managerInterval = 1;				    // Установка кванта менеджера

        diskInt = dlg.getDiskInt();
        processInt = dlg.getProcessInterval();
        managerInt = dlg.getManagerInt();
        memoryInt = dlg.getMemoryInterval();
        Kmem = dlg.getK0();
        nAlgorithm = dlg.getAlgorithm();

        // Определение значения K0, выше которого "хвост" экспоненты дает
        // недопустимый номер виртуальной страницы в датчике случайных чисел
        int avrPages = 0;
        if (numProcesses != 0) {
            int sumPages = 0;
            for (Process p: processes)
                sumPages += p.numPages;
            avrPages = sumPages / numProcesses;
        }
        K0 = (int)(1000.0 * Math.exp(- (Kmem/100.0) * avrPages) + 2);

        WkSetV = dlg.getWorkSetTime();
        Dpgswap = dlg.getPageTime();
        workSetInt = dlg.getWorkSetInterval();

        TWKS = memoryTime = processorTime = diskTime = 0;
        WKSET = NWKS = memoryLoad = processorLoad = diskLoad = 0;
        numSuccessCalls = numFailedCalls = 0;
        numFreePages = 0;
        numEmptyPages = numPages;
    }

    private void simulation() {
        if (numProcesses == 0)
            return;
        systemTime++;			    // Системное время +1

        // Через каждые 10 единиц модельного времени подсчет суммы свободных
        // доступных и свободных страниц для определения среднего
        if (systemTime % 10 == 0)
            memoryTime += (numPages - numFreePages - numEmptyPages);

        if (processInterval > 0) { // значение кванта ЦП для активной задачи
            processorTime++;              // увеличиваем время полезной работы ЦП
            currentProcess.Wktim++; // Поле Wktim подситывает активное
                                    // время процесса

            if (--processInterval == 0)
                timeInterruption();
            else {  // В противном случае моделируется
                    // одно обращение активной задачи к памяти
                currentProcess.Totim++;  // Подсчитывается общее время работы
                                         // задачи, если оно кратно интервалу
                if (currentProcess.Totim % WkSetV == 0) {
                    // смены РН, то случайным образом
                    // выбирается новая начальная
                    currentProcess.Page0 = (int)(Math.random() * currentProcess.numPages);
                }  // виртуальная страница Раge0
                int nPage;
                do {    // Моделирование обращения к памяти:
                    // Функция распределения обращений к страницам имеет вид
                    // 		p(n) = K * exp( - Kmem * n), где
                    // p(n) - вероятность выбора страницы n,
                    // Кмем - коэффициент неравномерности обращения к страницам
                    // К    - весовой коэффициент, устанавливающий сумму вероят-
                    //        ностей в 1
                    // Для получения случайного номера страницы
                    // вычисляется обратная функция для датчика равномерно
                    // распределенных случайных чисел с интервалом от К0/1000 до 1,
                    // где K0 - определяет "хвост" экспоненты, дающей значения выше
                    // Nvpg (количество страниц виртуальной памяти задачи):
                    // 		n = - ln (rand(K0/1000..1) / K) / Kmem
                    nPage = (int)(-Math.log((K0 + Math.random()*(1000-K0)) / 1000.0)
                                / (Kmem / 100.0));
                    //nPage = (int)(Math.random()*currentProcess.numPages);
                } while (nPage >= currentProcess.numPages); // Дополнительная защита от превышения
                                                            // номера виртуальной страницы
                nPage = (currentProcess.Page0 + nPage) % currentProcess.numPages;
                pageInterruption(nPage);    // Процедура pageInterruption() моделирует
            }                               // обращение к виртуальной странице nPage
        }
        if (diskInterval > 0) {  // Tdskfin моделирует выполнение диском
                            // операции ввода/вывода
            diskTime++;                         // Фиксируется полное время рапботы диска
            if (--diskInterval == 0)             // Если Tdskfin переходит из 0 в -1,
                diskQueueOut();               // то моделируется завершение дисковой
                                            // операции чтения страницы
        }
        if ((managerInterval > 0) && (--managerInterval == 0))  // Tmanfin моделирует квант менеджера
            managerInterruption();  			            // При переходе из 0 в -1 моделируется
                                                // вызов менеджера по прерыванию таймера

        if (systemTime != 0 ){
            processorLoad = (int)(processorTime * 100.0 / systemTime);		// Процент использования времени ЦП
            diskLoad = (int)(diskTime * 100.0 / systemTime);		// Процент использования времени диска
                                                    //
            memoryLoad = (int)((memoryTime * 100.0 / numPages) / (systemTime / 10 + 1));

            int n = 0;
            for (int i = 0; i < numProcesses; i++)
                n += processes.get(i).Totim;

            VTSK = (int)((n / numProcesses) * 100.0 / systemTime); 	// Средняя скорость выполнения задания
            VMEM = (int)(numFailedCalls * 100.0 / (numFailedCalls + numSuccessCalls));
        }
    }

    private void managerInterruption() {
        int	vp;
        if (nAlgorithm < GLOBAL) {	// Локальное замещение - поиск неиспользуемых страниц
            for (int pp = 0; pp < numPages; pp++)
                if (table.pages[pp].status == Page.Status.PRIVATE) { // Страница в состоянии ЗАНЯТА
                    int ts = table.pages[pp].nProcess;	// Фиксация номера задачи и номера
                    vp = table.pages[pp].nPage;	// виртуальной страницы
                    if (systemTime - table.pages[pp].callTime > Dpgswap)
                    	// Превышен квант неиспользования
                        freePage(processes.get(ts), vp); // страницы - освободить ее
                }
        }
        int nn = numFreePages + numEmptyPages;  // Попытка размещения РН задач
        while (!managerQueue.isEmpty()) {       // из очереди мекнеджера на
            				                    // множестве свободных и доступных
                    			                // страниц
            Process p = managerQueue.element();
            if (p.Wkset0 <= nn) {	        // Старый РН может быть размещен
                nn -= p.Wkset0;	// Уменьшить остаток свободных
                                // страниц и исключить задачу
                //int ts = processes.indexOf(p);	// из очереди менеджера
                managerQueue.remove();
                // Установить параметры задачи:
                //p = processes.get(ts);
                p.Dtim = processInt;	// Квант ЦП
                p.Wktim = 0;	// Активное время
                p.Wkset = 0;	// Текущий РН
                loadPage(p, p.Cvpg);    // Инициализировать загрузку текущей виртуальной страницы
            }
            else
                break;			// Выход из цикла - размещение  не возможно
        }
        managerInterval = managerInt; // Планировать следующий вызов менеджера установкой кванта
    }


    // моделирует обращение к виртуальной странице nPage
    private void pageInterruption(int nPage) {
        int	pp;
        currentProcess.Cvpg = nPage;			// Фиксация номера страницы
        if ((pp = currentProcess.pages[nPage]) != -1) { // Страница в памяти и назначена
            table.pages[pp].callTime = systemTime;		// задаче (состояние ЗАНЯТА)
            table.pages[pp].nCalls++;			// Изменить время и количество
            numSuccessCalls++;			// обращений к странице, а также
        }				// счетчик "успешных" обращений
        else {    				// Страничный  сбой:
            				// Поиск среди свободных страниц,
            for (pp = 0; pp < numPages; pp++)	// в которую ранее была загружена
                            // требуемая страница и еще не замещена
                if ((table.pages[pp].status == Page.Status.FREE) &&
                        (table.pages[pp].nProcess == currentProcessIndex) &&
                        (table.pages[pp].nPage == nPage)) break;

            if (pp != numPages) {
                		// Такая найдена - повторное назначение
                        // ее виртуальной странице и включение в РН
                numSuccessCalls++;		// Счетчик "успешных обращений"
                currentProcess.pages[nPage] = pp;	// Назначить виртуальную страницу на
                            // физическую
                currentProcess.Wkset++;		// Увеличить РН задачи
                table.pages[pp].nCalls++;		// Увеличить счетчик обращений
                table.pages[pp].callTime = systemTime;	// Время последнего обращения
                table.pages[pp].status = Page.Status.PRIVATE;	// Состояние страницы - ЗАНЯТА
                numFreePages--;		// Уменьшить число свободных страниц
            }
            else {				// Страничный сбой - замещение
                			// свободной страницы
                numFailedCalls++;		// Счетчик "неудачных" обращений
                currentProcess.Dtim = processInterval; 	// Зафиксировать остаток кванта ЦП
                loadPage(currentProcess, nPage);  	// Загрузить страницу с вытеснением
                schedule();		// Вызвать диспетчер
            }
        }
    }

    // моделирует прерывание по таймеру
    private void timeInterruption() {
        currentProcess.Dtim  =  processInt;			// Установить новый интервал
        if (currentProcess.Wktim >= memoryInt) {	// Активное время задачи превысило
                                                    // квант памяти - выгрузка.
            if (currentProcess.Nwkset != 0) {		// Если не истек интервал накопления РН
                currentProcess.Nwkset--;
                    if (currentProcess.Wkset > currentProcess.Wkset0) // Скорректировать размер РН
                       currentProcess.Wkset0 = currentProcess.Wkset;  // при превышении текущего РН
            }                                                         // над старым
            currentProcess.Wktim = 0;			// Сбросить активное время процесса
            if (nAlgorithm < GLOBAL) {		// Если локальное замещение - поставить в очередь менеджера
                managerQueueIn(currentProcess);	//
                freeAllWorkSet(currentProcess);	// и освободить весь рабочий набор
                managerInterruption();		// Вызвать менеджер для попытки
            }			// размещения РН новой задачи
        }
        else {					// Квант памяти не превышен -
            currentProcess.Dtim = diskInt;		// Установить новый квант ЦП
            if ((currentProcess.Wktim >= workSetInt) &&	// Активное время превысило
                (currentProcess.Wktim < workSetInt + processInt)) {// квант РН
                // Сбор статистики РН
                NWKS++;
                TWKS += currentProcess.Wkset;
                WKSET = (int) ((double)TWKS / NWKS);

                int	n;		// Изменение рабочего набора
                if ((nAlgorithm < GLOBAL)  && (n = currentProcess.Wkset - currentProcess.Wkset0) >0) {
                                    // При локальном замещении и
                    freeWorkSet(currentProcess, n); // превышении текущим РН старого РН
                    currentProcess.Wkset = currentProcess.Wkset0;// освободить "лишние" страницы
                }			// РН задачи
            }
        }
        schedule();
    }

    private void schedule() {
        for (int n = 0; n < numProcesses; n++) {		// задач
            currentProcessIndex = (currentProcessIndex + 1) % numProcesses;
            currentProcess = processes.get(currentProcessIndex);
            if (currentProcess.status == Process.READY) break;
        }
        if (currentProcess.status == Process.READY)	// Найдена готовая - установить квант ЦП
            processInterval = currentProcess.Dtim;
        else
            processInterval = -1;		// Иначе - состояние "нет готовых задач"
    }

    private void freeWorkSet(Process process, int nPage) {
        int i = -1, pp;
        while (nPage-- !=0) {			// Цикл поиска и освобождения страницы
            long t = 10000000;
            for (int vp = 0; vp <process.numPages; vp++) {	// Просмотр виртуальных страниц
                if ((pp = process.pages[vp]) != -1) { // Загружена
                    switch (nAlgorithm) {
                        case localLRU : // LRU - поиск минимального времени последнего обращения
                            if (table.pages[pp].callTime < t) {
                                i = vp;
                                t = table.pages[pp].callTime;
                            }
                            break;
                        case localFIFO : // FIFO - поиск минимального времени загрузки
                            if (table.pages[pp].loadTime < t) {
                                i = vp;
                                t = table.pages[pp].loadTime;
                            }
                            break;
                    }
                }
            }
            if (i != -1)
                freePage(process, i);		// Освобождение страницы с номером i
        }
    }

    private void freeAllWorkSet(Process process) {
        for (int vp = 0; vp < process.numPages; vp++) {
            if (process.pages[vp] != -1)	// Страница загружена - освободить
                freePage(process, vp);
            }
    }

    private void freePage(Process process, int vp) {
        int	phpg = process.pages[vp];	// Определение номера физической страницы
        process.pages[vp] = -1;	// Виртуальная страница - не загружена
        process.Wkset--;		// Уменьшить размер рабочего набора задачи
        table.pages[phpg].status = Page.Status.FREE;
        numFreePages++;			// Увеличить счетчик свободных страниц
    }

    private void loadPage(Process process, int vpg) {
        int npp;
        if (numEmptyPages != 0) {   // Есть доступная (неиспользуемая страница)
            			            // Поиск в физической памяти доступной

            for (npp = 0; npp < numPages; npp++)
                if (table.pages[npp].status == Page.Status.EMPTY) break;

            numEmptyPages--;			// Уменьшимть число доступных страниц
            setPage(process, vpg, npp);		// Установка виртуальной страницы на найденную
            diskQueueIn(process, 1, npp); 	// Постановка задачи в очередь к диску для загрузки страницы
        }
        else {
            if (numFreePages == 0)	// Нет свободной физической страницы
                if (nAlgorithm < GLOBAL)	// Локальное замещение -
                    freeWorkSet(process, 1); // вытеснение в собственном РН
                else			    // Глобальное замещение - вытеснение
                    freeGlobal();	// страницы из множества всех задач
            // Поиск первой свободной страницы
            for (npp = 0; npp < numPages; npp++)
                if (table.pages[npp].status == Page.Status.FREE) break;

            numFreePages--;			// Уменьшить число свободных страниц
            setPage(process, vpg, npp);		// Установка виртуальной страницы на найденную свободную
            // Считается, что содержимое свободной страницы менялось - нужно выгрузить
            diskQueueIn(process, 2, npp);
        }
    }

    private void setPage(Process process, int nvp, int npp) {
        process.pages[nvp]=npp;	// Назначить виртуальную на физическую
        process.Wkset++;		// Увеличить РН задачи
        table.pages[npp].nCalls = 1; // Установить количество обращений - 1
        table.pages[npp].nProcess = processes.indexOf(process);		// Установить номер задачи и виртуальной
        table.pages[npp].nPage = nvp;		// страницы в таблице физическихз страниц
        table.pages[npp].loadTime = systemTime;		// Установить время обращения
        table.pages[npp].status = Page.Status.PRIVATE;	// Состояние физической страницы - ЗАНЯТА
    }

    private void diskQueueIn(Process p, int operation, int pp) {
                            				// Изменение очереди диска
        p.status = Process.WAIT;			// Состояние задачи - ожидание
        p.Ndiskio = operation;				// Время выполнения операций:
        p.Npp = pp;				// 1 - ввод, 2 - вывод/ввод
        if (diskQueue.isEmpty())       // Список пуст - запуск операции
            diskInterval = p.Ndiskio * diskInt;	// ввода-вывода для следующей задачи
        diskQueue.add((ProcessView)p);
    }

    private void diskQueueOut() {
                                        				// Изменение очереди диска
        Process p = diskQueue.element();				// Выбор первой задачи из списка
        processes.get(processes.indexOf(p)).status = Process.READY;
        table.pages[p.Npp].loadTime = systemTime;		// Установка времени загрузки и
        table.pages[p.Npp].callTime = systemTime;		// времени обращения к странице
        diskInterval = -1;				// Квант работы диска не установлен

        diskQueue.remove();
        if (!diskQueue.isEmpty())       // Список не пуст - запуск операции
            diskInterval = diskQueue.element().Ndiskio * diskInt;	// ввода-вывода для следующей задачи
        schedule();				// Вызов диспетчера
    }

    private void managerQueueIn(Process p) {
                    			    // Изменение очереди менеджера
        p.status = Process.SWAP;    // Состояние задачи - выгружена
        p.MngTim = systemTime;			// Запомнить время помещения
        managerQueue.add((ProcessView)p);
    }

    private void freeGlobal() {
        long t;				// Поиск производится по страницам
        int	i = -1, pp;			// всех задач, загруженнм в память,
        t = 10000000;				// то есть по физической памяти
        for (pp = 0; pp < numPages; pp++) {
            if (table.pages[pp].status == Page.Status.PRIVATE)	// Страница памяти в состоянии ЗАНЯТА
            switch (nAlgorithm) {
                case globalLRU : // LRU - поиск по минимальному времени последнего обращения
                    if (table.pages[pp].callTime < t) {
                        i = pp;
                        t = table.pages[pp].callTime;
                    }
                    break;
                case globalFIFO : // FIFO - поиск по минимальному времени загрузки страницы
                    if (table.pages[pp].loadTime < t) {
                        i = pp;
                        t = table.pages[pp].loadTime;
                    }
                    break;
            }
        }				// i - номер выбранной физической
        if (i != -1) {
            Process p = processes.get(table.pages[i].nProcess);
            freePage(p, table.pages[i].nPage);  // страницы - освобождение
        }                                       // соответствующей ей виртуальной
    }

    public static int sleepTime = 10;
    private Thread modelator;

    public VMTableView table;
    public ArrayList<ProcessView> processes;
    public int numProcesses;       // количество задач

    public Queue<ProcessView> diskQueue;       // очередь к диску
    public Queue<ProcessView> managerQueue;    // очередь к менеджеру


    //-------------------------------------------------------------------------
    //  Глобальные параметры операционной системы
    //-------------------------------------------------------------------------
    public Process currentProcess;
    private int currentProcessIndex; // Номер текущей задачи
    public long systemTime;        // Системное время

    private long processorTime;     // Поллное время загрузки ЦП
    public int processorLoad;      // Процент загрузки ЦП
    private long diskTime;          // Полное время загрузки диска
    public int diskLoad;           // Процент загрузки диска
    private long memoryTime;        // Полное время загрузки оперативной памяти
    public int memoryLoad;         // Процент используемых страниц ОП

    //-------------------------------------------------------------------------
    // Параметры виртульной памяти
    //-------------------------------------------------------------------------
    public int numPages;           // Количество страниц виртуальной памяти
    public int numFreePages;       // Количество страниц занятых данными,
                                    // но не связанных с задачами
    public int numEmptyPages;      // Количество свободных страниц
    public int numPrivatePages;    // Количество страниц занятых данными и
                                    // связанных с задачами

    //-------------------------------------------------------------------------
    // Интервалы времени завершения событий:
    //-------------------------------------------------------------------------
    public int processInterval;	// Завершение кванта ЦП активной задачи
    public int diskInterval;	    // Завершение ввода/вывода диска
    public int managerInterval;    // Прерывание по таймеру для вызова менеджера

    private long numSuccessCalls;   // Количество удачных и сбойных обращений
    private long numFailedCalls;	// страницам

    //-------------------------------------------------------------------------
    //  Временные интервалы системы моделирования
    //  и статистика работы модели
    //-------------------------------------------------------------------------
    public int processInt = 10;    // Рабочий период таймера
    public int diskInt = 10;       // Время операции диска
    public int managerInt = 100;   // Интервал менеджера страниц
    public int memoryInt = 50;	    // Квант памяти
    public int workSetInt = 30;    // Время рабочего набора

    public int nAlgorithm = 0;		// Номер алгоритма вытеснения страниц

    public int Dpgswap=70;	// Максимальное время неиспользования страницы
    public int WkSetV=300;	// Скорость изменения РН

    long TWKS;		// Статистика рабочего набора
    int	NWKS;		//
    int	WKSET;		// Средний рабочий набор

    int	Kmem=100;	// Коэффициент распределения *100
    int	K0;		    // Коэффициент, "отрезающий" хвост экспоненты

    int	VTSK;		// Относительная скорость выполнения задачи


    int	VMEM;
    int	NWkset=10;	// Количество циклов определения РН

    public static final int GLOBAL = 2;
    public static final int localLRU = 0;
    public static final int localFIFO = 1;
    public static final int globalLRU = 2;
    public static final int globalFIFO = 3;
}
