package org.jamel.nstu;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 15.05.2007
 * Time: 13:37:34
 * To change this template use File | Settings | File Templates.
 */
public class Page {
    public Page(int stat, int proc, int page) {
        status = stat;
        nProcess = proc;
        nPage = page;
    }

    public Page(Page p) {
        status = p.status;
        nProcess = p.nProcess;
	    nPage = p.nPage;
	    loadTime = p.loadTime;
	    callTime = p.callTime;
	    nCalls = p.nCalls;
    }

    public Page() {}

    public int status;      // состояние страницы
    public int nProcess;    // номер процесса
	public int nPage;       // номер страницы процесса
	public long	loadTime;   // время загрузки
	public long	callTime;	// время последнего обращения
	public int nCalls;      // количество обращений

    public static final int EMPTY = 0;
    public static final int PRIVATE = 1;
    public static final int FREE = 2;
}
