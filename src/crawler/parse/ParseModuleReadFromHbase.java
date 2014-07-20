package crawler.parse;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
public class ParseModuleReadFromHbase {
	public static ArrayList<String> readParseUrl(int tableIndex,int readUrlNumber) throws IOException{
		ArrayList<Result>urlResult=Database.getSpecificRows(
				CrawlerConfiguration.WaitingParseUrlTableName+String.valueOf(tableIndex),
				0, readUrlNumber);
		ArrayList<String> url=new ArrayList<String>();
		Cell[] cell; 
		String urlString;
		for(int i=0;i<urlResult.size();i++){
			cell=urlResult.get(i).rawCells();
			urlString=Bytes.toString(CellUtil.cloneRow(cell[0]));
			url.add(urlString);
		}
		DebugFunctions.showArray(url);
		//删除这里不删除，先调试一下
		Database.delMultiRecords(CrawlerConfiguration.WaitingParseUrlTableName+String.valueOf(tableIndex)
				,url);
		return url;
	}
	public static String getPageFromHbase(String rowKey) throws IOException{
		String temp= Database.getSpecificRowColumn(CrawlerConfiguration.WebTableName, rowKey,
				CrawlerConfiguration.WebTableContentFamily, 
				CrawlerConfiguration.WebTablePageQualifier);
		return temp;
	}
	/*
	public static void main(String args[]) throws Exception{
		Database.showAllRecord("WPurl2");
		ParseModuleReadFromHbase.readParseUrl(2, 0);
		Database.showAllRecord("WPurl2");
	}
	*/
}