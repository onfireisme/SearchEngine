package crawler.others;

public class CrawlerConfiguration {
	public static final String UrlTableName="url";
	
	//web表相关配置
	public static final String WebTableName="web";
	public static final String WebTableContentFamily="content";
	public static final String WebTablePageQualifier="page";
	
	//waiting download 模块相关配置
	public static final String WaitingDownloadUrlTableName="Durl";
	public static final String WaitingDownloadUrlMainTableName="MDurl";
	public static final String WaitingDownloadUrlMainTableFamilyName="tableInfo";
	public static final String WaitingDownloadUrlMainTableQualifierName="tableInfo";
	public static final int WaitingDownloadUrlTableNumber=5;
	public static final int WaitingDownloadUrlMax=300;

	//waiting parse相关模块配置
	public static final String WaitingParseUrlTableName="WPurl";
	public static final String ParseUrlTableName="Purl";
	public static final int ParseUrlTableNumber=5;
	
	public static final String StatusFamilyName="status";
	public static final String StatusQualifier="s";
	public static final String ReadDefaultStatus="unread";
	public static final String ReadStatus="read";
	
	public static final String TempFamilyName="t";
	public static final String TempQualifierName="";
	public static final String TempValue="";
	
	public static final String LockDefaultStatus="unlock";
	public static final String LockStatus="locking";
	
	//下载状态
	public static final String DownloadStatusFamilyName="s";
	public static final String DownloadStatusQualifier="";
	public static final String DownloadedStatus="downloaded";
	public static final String WaitingDownloadStatus="waitingDownload";
	
	// 线程池线程的最大数目
	public static final int ThreadPoolNumber=10;
	
	//this re
	public static final int MaxRowsGetFromTable=500;
	
	public static final int AccumulateNumber=500;
	
	public static final int MaxDeleteNumber=500;
	
	public static final int MaxRequestUrlFromDurl=300;
	
	public static final int MaxRow=2000;
	
	public static final String initialUrlTxtPath="/home/wangyan/android/url.txt";
}
