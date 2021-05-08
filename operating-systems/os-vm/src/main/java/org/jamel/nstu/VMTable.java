package org.jamel.nstu;

/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 16.05.2007
 * Time: 7:51:55
 * To change this template use File | Settings | File Templates.
 */
public class VMTable {

    public VMTable(VMTable table) {
        numPages = table.numPages;
        pages = new Page[numPages];
        for (int i = 0; i < numPages; i++)
            pages[i] = new Page(table.pages[i]);
    }

    public VMTable(int numPages) {
        this.numPages = numPages;
        pages = new Page[numPages];
        for (int i = 0; i < numPages; i++)
            pages[i] = new Page(Page.EMPTY, -1, -1);
    }

    public void resize(int nPages) {
        numPages = nPages;
        pages = new Page[numPages];
        for (int i = 0; i < numPages; i++)
            pages[i] = new Page(Page.EMPTY, -1, -1);
    }
    
    public Page[] pages;
    public int numPages;  //количество страниц в таблице
}
