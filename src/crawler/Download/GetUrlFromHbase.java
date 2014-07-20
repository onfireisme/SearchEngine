package crawler.Download;
import java.io.IOException;
import java.util.ArrayList;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
public class GetUrlFromHbase {
	public static ArrayList<String> getDataFromHbase(int tableIndex,int urlNumber) throws Exception{
		//urlNumber的数目是有线程池的大小决定了，但是为了保证解耦性，这里还是外界传入吧
		ArrayList <String>url=Database.getSpecificRowKeys(
				CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(tableIndex),
				0, 
				urlNumber);
		//修改两个地方的值
		//第一，删除获取的数值
		Database.delMultiRecords(CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(tableIndex),
				url);
		//第二，修改主表的信息
		int previousUrlNumber=Integer.parseInt(Database.getSpecificRowColumn(CrawlerConfiguration.WaitingDownloadUrlMainTableName
				, CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(tableIndex), 
				CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName,
				CrawlerConfiguration.WaitingDownloadUrlMainTableQualifierName));
		String currentUrlNumber=String.valueOf(previousUrlNumber-url.size());
		Database.addRecord(CrawlerConfiguration.WaitingDownloadUrlMainTableName, 
				CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(tableIndex),
				CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName,
				CrawlerConfiguration.WaitingDownloadUrlMainTableQualifierName, 
				currentUrlNumber);
		return url;
	}
	public static String readPageFromHbase(String url) throws IOException{
		return Database.getSpecificRowColumn(CrawlerConfiguration.WebTableName, 
				url, CrawlerConfiguration.WebTableContentFamily, 
				CrawlerConfiguration.WebTablePageQualifier);
	}
	/*
	public static void main(String args[]) throws Exception{
		ArrayList<String>urlArray=GetUrlFromHbase.getDataFromHbase(1, 10);
		DebugFunctions.showArray(urlArray);
		System.out.println(GetUrlFromHbase.readPageFromHbase(urlArray.get(0)));
		//Database.showAllRecord("WPurl1");
	}
	*/
}
