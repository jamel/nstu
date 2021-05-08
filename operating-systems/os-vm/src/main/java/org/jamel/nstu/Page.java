package org.jamel.nstu;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 15.05.2007
 * Time: 13:37:34
 * To change this template use File | Settings | File Templates.
 */
public class Page {
    enum Status {
        EMPTY,
        PRIVATE,
        FREE,
    }

    public Page(Status status, int proc, int page) {
        this.status = status;
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

    public Status status; // состояние страницы
    public int nProcess;  // номер процесса
	public int nPage;     // номер страницы процесса
	public long	loadTime; // время загрузки
	public long	callTime; // время последнего обращения
	public int nCalls;    // количество обращений
}
