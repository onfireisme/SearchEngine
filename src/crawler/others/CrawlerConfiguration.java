package crawler.others;

public class CrawlerConfiguration {
	public static final String UrlTableName="url";
	
	//web表相关配置
	public static final String WebTableName="web";
	public static final String WebTableContentFamily="content";
	public static final String WebTablePageQualifier="page";
	
	//waiting download 模块相关配置
	public static final String WaitingDownloadUrlTableName="WDurl";
	public static final String WaitingDownloadUrlMainTableName="WDMurl";
	public static final String WaitingDownloadUrlMainTableFamilyName="tableInfo";
	public static final String WaitingDownloadUrlMainTableQualifierName="tableInfo";
	public static final int WaitingDownloadUrlTableNumber=5;
	public static final int WaitingDownloadUrlMax=300;

	// parse相关模块配置
	public static final String WaitingParseUrlTableName="WPurl";
	public static final String ParsedUrlTableName="Purl";
	public static final int ParsedUrlTableNumber=5;
	
	public static final String PageInfoTableName="PInfo";
	public static final String PageInfoFamilyName="PInfo";
	public static final String PageInfoImageUrlQualifier="imageUrl";
	public static final String PageInfoUrlQualifier="url";
	public static final String PageInfoKeyWordQualifier="keyWord";
	
	//图片下载模块
	public static final String ImageTableName="image";
	public static final String ImageTableFamilyName="c";
	public static final String ImageTableQualifierName="";
	
	public static final String ImageUrlTable="Iurl";
	
	public static final String WaitingDownloadImageUrlMainTable="WDIMurl";
	public static final String WaitingDownloadImageUrlMainTableFamilyName="tableInfo";
	public static final String WaitingDownloadImageUrlMainTableQualifierName="tableInfo";
	
	public static final String WaitingDownloadImageUrlTable="WDIurl";
	public static final int WaitingDownloadImageUrlTableNumber=5;
	
	public static final String DownloadedImageUrlTable="DIurl";
	//这个table的family的肯定就是status,value是downloaded了
	
	//图片解析模块
	public static final String ParsedImageUrlTable="PIurl";
	//
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
