package crawler.imageDownload;

import java.io.IOException;
import java.util.ArrayList;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;

public class GetImageUrlFromHbase {
	public static ArrayList<String> getImageUrlFromHbase(int tableIndex,int urlNumber) throws Exception{
		//urlNumber的数目是有线程池的大小决定了，但是为了保证解耦性，这里还是外界传入吧
		ArrayList <String>url=Database.getSpecificRowKeys(
				CrawlerConfiguration.WaitingDownloadImageUrlTable+String.valueOf(tableIndex),
				0, 
				urlNumber);
		//修改两个地方的值
		//第一，删除获取的数值
		Database.delMultiRecords(CrawlerConfiguration.WaitingDownloadImageUrlTable+String.valueOf(tableIndex),
				url);
		//第二，修改主表的信息
		int previousUrlNumber=Integer.parseInt(Database.getSpecificRowColumn(
				CrawlerConfiguration.WaitingDownloadImageUrlMainTable
				, CrawlerConfiguration.WaitingDownloadImageUrlTable+String.valueOf(tableIndex), 
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableFamilyName,
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableQualifierName));
		String currentUrlNumber=String.valueOf(previousUrlNumber-url.size());
		Database.addRecord(CrawlerConfiguration.WaitingDownloadImageUrlMainTable, 
				CrawlerConfiguration.WaitingDownloadImageUrlTable+String.valueOf(tableIndex),
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableFamilyName,
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableQualifierName, 
				currentUrlNumber);
		return url;
	}

}
