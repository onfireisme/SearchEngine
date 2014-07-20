package crawler.parse;
import java.util.ArrayList;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
public class SaveParsedUrlToHbase {
	public static void saveParsedUrl(ArrayList<String>urlArray) throws Exception{
		if(urlArray.size()==0){
			System.out.println("the parsed url array is null");
		}
		ArrayList<String> temp=new ArrayList<String>();
		for(int i=0;i<urlArray.size();i++){
			temp.add(CrawlerConfiguration.WaitingDownloadStatus);
		}
		DebugFunctions.showArray(urlArray);
		Database.addMultiRecords(CrawlerConfiguration.ParseUrlTableName,
				urlArray, CrawlerConfiguration.DownloadStatusFamilyName, 
				CrawlerConfiguration.DownloadStatusQualifier,
				temp);
	}
}
