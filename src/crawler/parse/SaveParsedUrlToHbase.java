package crawler.parse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	/*
	 * 这里假设传入的就是table应该有的名字
	 * 如果是中文，就是
	 */
	public static void saveKeyWord(ArrayList<String> keyWordArray,String url) throws Exception{
		//每个keyword都创建一个table
		String []tempArray={CrawlerConfiguration.TempFamilyName};
		String tableName;
		for(int i=0;i<keyWordArray.size();i++){
			tableName=keyWordArray.get(i);
			if(isTableNameLegal(tableName)){
				Database.creatTable(tableName, tempArray);
				Database.addRecord(tableName,
						url, CrawlerConfiguration.TempFamilyName,
						CrawlerConfiguration.TempQualifierName,
						CrawlerConfiguration.TempValue);
			}
		}
	}
	public static void saveAllToPageInfo(String url,
			ArrayList<String> keyWordArray,
			ArrayList<String> urlArray,
			ArrayList<String> imageUrlArray){
		ArrayList<String>qualifierArray=new ArrayList<String>();
		ArrayList<String>valueArray=new ArrayList<String>();
		int i=0;
		String urlString="";
		for(i=0;i<urlArray.size();i++){
			urlString=urlString+" "+urlArray.get(i);
			qualifierArray.add(CrawlerConfiguration.PageInfoUrlQualifier);
			valueArray.add(urlString);
		}
		
		String imageUrlString="";
		for(i=0;i<imageUrlArray.size();i++){
			imageUrlString=imageUrlString+" "+imageUrlArray.get(i);
			qualifierArray.add(CrawlerConfiguration.PageInfoImageUrlQualifier);
			valueArray.add(imageUrlString);
		}
		
		String keyWordString="";
		for(i=0;i<keyWordArray.size();i++){
			keyWordString=keyWordString+" "+keyWordArray.get(i);
			qualifierArray.add(CrawlerConfiguration.PageInfoKeyWordQualifier);
			valueArray.add(keyWordString);
		}
		Database.addMultiColumnRecord(CrawlerConfiguration.PageInfoTableName,
				url, CrawlerConfiguration.PageInfoFamilyName, qualifierArray, 
				valueArray);
	}
	public static boolean isTableNameLegal(String tableName){
		if(tableName==null||tableName.equals("")){
			return false;
		}
		Pattern patt = Pattern.compile("^[a-zA-Z_0-9-.]+$");
        Matcher matcher = patt.matcher(tableName);
        return matcher.matches();
	}
		

}
