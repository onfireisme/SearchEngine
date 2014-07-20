package crawler.duplicateCheck;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;

public class DuplicateCheck {
	public static void copyUrlTableToParsedUrlTable() throws Exception{
		//把copyUrlTable的数据读出来，插入parsed url table就可以了
		//我们不能够一次性就读取所有的rowkey,所以，这里要分批读，分批
		//读的话，每次读一定数目，然后记录下最后一个的值，作为下一个值的起点。
		//String startRowKey=Database.getStartRowKey(CrawlerConfiguration.UrlTableName);
		HTable table = new HTable(Database.getConf(), CrawlerConfiguration.UrlTableName);
		Scan s = new Scan();
		// s.setCaching((CrawlerConfiguration.MaxRowsGetFromTable);
		ResultScanner ss = table.getScanner(s);
		Result tempResult=ss.next();
		int count=0;
		ArrayList<String>urlArray=new ArrayList<String>();
		ArrayList<String>statusArray=new ArrayList<String>();
		String status=CrawlerConfiguration.DownloadedStatus;
		//在执行这个mapReduce的时候，parse必须要停止，这是必要条件！！
		while(tempResult!=null){
			Cell cell[]=tempResult.rawCells();
			urlArray.add(new String(CellUtil.cloneRow(cell[0])));
			statusArray.add(status);
			if(count>=CrawlerConfiguration.MaxRowsGetFromTable){
				System.out.println("lol");
				count=0;
				Database.addMultiRecords(CrawlerConfiguration.ParseUrlTableName,
						urlArray, 
						CrawlerConfiguration.DownloadStatusFamilyName, 
						CrawlerConfiguration.DownloadStatusQualifier, statusArray);
				urlArray.clear();
				statusArray.clear();
			}
			count++;
			tempResult=ss.next();
		}
		//System.out.println("lol"+urlArray.size()+"lol1"+statusArray.size());
		//DebugFunctions.showArray(urlArray);
		Database.addMultiRecords(CrawlerConfiguration.ParseUrlTableName,
				urlArray, 
				CrawlerConfiguration.DownloadStatusFamilyName, 
				CrawlerConfiguration.DownloadStatusQualifier, statusArray);
		ss.close();
		table.close();
		//while()
	}

	public static void saveValidUrl() throws Exception{
		HTable table = new HTable(Database.getConf(), CrawlerConfiguration.ParseUrlTableName);
		Scan s = new Scan();
		// s.setCaching((CrawlerConfiguration.MaxRowsGetFromTable);
		ResultScanner ss = table.getScanner(s);
		Result tempResult=ss.next();
		int i=0;
		ArrayList<String>urlArray=new ArrayList<String>();
		String url;
		String status;
		while(tempResult!=null){
			Cell urlCell=tempResult.getColumnLatestCell(Bytes.toBytes(CrawlerConfiguration.DownloadStatusFamilyName), 
					Bytes.toBytes(CrawlerConfiguration.DownloadStatusQualifier));
			url=new String(CellUtil.cloneRow(urlCell));
			status=new String(CellUtil.cloneValue(urlCell));
			if(status.equals(CrawlerConfiguration.WaitingDownloadStatus)){
				urlArray.add(url);
				if(urlArray.size()>=CrawlerConfiguration.MaxRowsGetFromTable){
					SaveValidUrl.saveValidUrlToHbase(urlArray);
					urlArray.clear();
					Database.showAllRecord("MDurl");
				}
			}
			tempResult=ss.next();
		}
		SaveValidUrl.saveValidUrlToHbase(urlArray);
		ss.close();
		table.close();
	}
	public static void main(String args[]) throws Exception{
		DuplicateCheck.saveValidUrl();
		Database.showAllRecord("MDurl");
		//Database.showAllRecord("url");
	}
}
