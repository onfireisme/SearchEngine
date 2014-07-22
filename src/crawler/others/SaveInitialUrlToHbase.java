package crawler.others;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import crawler.Database.Database;
import crawler.duplicateCheck.SaveValidUrl;

public class SaveInitialUrlToHbase {
	public static final String rowKey="0";
	public static final String path="/home/wangyan/android/url.txt";
	public static final String Tname="Durl";
	public static final String qualifier="s";
	public static final String family="status";
	public static final String defaultValue="unlock";
	public static void insertFirstLine(String tablename,String family,String qualifier,String value) 
			throws Exception{
		//String rowKey="0";
		Database.addRecord(tablename, rowKey,family,qualifier,value);
	}
	public static ArrayList<String> readUrlFromTxt(String filePath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		//the initial url number is 160,or less
    	ArrayList<String> strArray=new ArrayList<String>();
    	
    	String data = br.readLine();//一次读入一行，直到读入null为文件结束  
    	String tempStr;
    	while(data!=null){
    		tempStr=data.toString();
    		strArray.add(tempStr);
   	     data = br.readLine(); //接着读下一行  
    	}
    	br.close();
    	return strArray;
	}
	/*
	 * 这个函数用不上了，使用saveValidUrl中的存储函数
	public static void saveToHbase(ArrayList<String>rowKeyArray) throws Exception{
		//initial the value array
		ArrayList<String>valueArray=new ArrayList<String>();
		for(int i=0;i<rowKeyArray.size();i++){
			valueArray.add(CrawlerConfiguration.ReadDefaultStatus );
		}
		Database.addMultiRecords(CrawlerConfiguration.WaitingDownloadUrlTableName,
				rowKeyArray, CrawlerConfiguration.StatusFamilyName,
				CrawlerConfiguration.StatusQualifier, valueArray);
	}
	*/
	/*
	public static void main(String args[]) throws Exception{
		//ArrayList<String> strArray=
		//		SaveInitialUrlToHbase.readUrlFromTxt(CrawlerConfiguration.initialUrlTxtPath);
		//SaveValidUrl.saveValidUrlToHbase(strArray);
		//SaveInitialUrlToHbase.saveToHbase(strArray);
		Database.showAllRecord(CrawlerConfiguration.WaitingDownloadUrlMainTableName);
		Database.showAllRecord("WDurl1");
	}
	*/
}
