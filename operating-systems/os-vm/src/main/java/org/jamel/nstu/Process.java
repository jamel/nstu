/**
 * Created by IntelliJ IDEA.
 * User: Root
 * Date: 16.05.2007
 * Time: 7:51:16
 * To change this template use File | Settings | File Templates.
 */
public class Process {
    public Process(int numPages) {
        this.numPages = numPages;
        pages = new int[numPages];
        clearMemory();
    }

    public void clearMemory() {
        for (int i = 0; i < numPages; i++)
            pages[i] =  -1;
    }

    public int numPages;
    public int[] pages;

    public int status;
    public int	Cvpg;	// Текущая виртуальная страница
	public int	Page0;	// Начальная страница РН
	public long Totim;	// Полное время процесса
	public int	Wktim;	// Активное время процесса
	public int	Dtim;	// Величина интервала процесса - остаток Dtm
	public int	Wkset;	// Рабочий набор
	public int	Wkset0;	// Старый рабочий набор
	public long MngTim;	// Время постановка к менеджеру
	public int	Nwkset;	// Количество циклов определения РН
	public int	Npp;	// Номер загружаемой  физической страницы
	public int	Ndiskio;// Количество операций в/в - 1 или 2

    public static final int READY = 0;
    public static final int SWAP = 1;
    public static final int WAIT = 2;
}
