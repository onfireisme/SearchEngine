package crawler.parse;
import java.util.ArrayList;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
public class SaveParsedUrlToHbase {
	public static void saveParsedUrl(ArrayList<String>urlArray) throws Exception{
		if(urlArray.size()==0){
			System.out.println("the parsed url array is null");
			return;
		}
		ArrayList<String> temp=new ArrayList<String>();
		for(int i=0;i<urlArray.size();i++){
			temp.add(CrawlerConfiguration.WaitingDownloadStatus);
		}
		//DebugFunctions.showArray(urlArray);
		Database.addMultiRecords(CrawlerConfiguration.ParsedUrlTableName,
				urlArray, CrawlerConfiguration.DownloadStatusFamilyName, 
				CrawlerConfiguration.DownloadStatusQualifier,
				temp);
	}
	public static void saveParsedImageUrl(ArrayList<String>imageUrlArray) throws Exception{
		if(imageUrlArray.size()==0){
			System.out.println("the parsed image url array is null");
			return;
		}
		ArrayList<String> temp=new ArrayList<String>();
		for(int i=0;i<imageUrlArray.size();i++){
			temp.add(CrawlerConfiguration.WaitingDownloadStatus);
		}
		Database.addMultiRecords(CrawlerConfiguration.ParsedImageUrlTable,
				imageUrlArray, CrawlerConfiguration.DownloadStatusFamilyName, 
				CrawlerConfiguration.DownloadStatusQualifier,
				temp);
		//Database.addMultiRecords(tableName, rowKeyArray, family, qualifier, valueArray)
	}
	public static void saveKeyWord(ArrayList<String> keyWordArray){
		//每个keyword都创建一个table
		String []tempArray={CrawlerConfiguration.TempFamilyName};
		String tableName;
		for(int i=0;i<keyWordArray.size();i++){
			/*
			//首先，创建table
			Database.creatTable(keyWordArray.get(i), tempArray);
			Database.addRecord(keyWordArray, rowKey, family, qualifier, value)
			*/
		}
	}
	public static void saveAllToPageInfo(){
		
	}
}
