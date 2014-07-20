package crawler.others;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import crawler.Database.Database;

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
	
	//public static void main(String args[]) throws Exception{
		/*
		String[]familys=new String[1];
		familys[0]=CrawlerConfiguration.StatusFamilyName;
		Database.creatTable(CrawlerConfiguration.WaitingDownloadUrlTableName, familys);
		ArrayList<String> strArray=
				SaveInitialUrlToHbase.readUrlFromTxt(CrawlerConfiguration.initialUrlTxtPath);
		SaveInitialUrlToHbase.saveToHbase(strArray);
	
		SaveInitialUrlToHbase.insertFirstLine(CrawlerConfiguration.WaitingDownloadUrlTableName,
				CrawlerConfiguration.StatusFamilyName
				,SaveInitialUrlToHbase.qualifier,CrawlerConfiguration.ReadDefaultStatus); 
		
		Database.delRecord(CrawlerConfiguration.WaitingDownloadUrlTableName, "0");
		ArrayList<Result> resultArray=Database.getSpecifiedRowRecord(CrawlerConfiguration.WaitingDownloadUrlTableName,
				200);
		Database.showResultArray(resultArray);
		ArrayList<String> strArray=
				SaveInitialUrlToHbase.readUrlFromTxt(CrawlerConfiguration.initialUrlTxtPath);
		SaveInitialUrlToHbase.saveToHbase(strArray);
		ArrayList<Result> resultArray=Database.getSpecifiedRowRecord(CrawlerConfiguration.WaitingDownloadUrlTableName,
				200);
		Database.showResultArray(resultArray);*/
		//Database.addFamily(SaveInitialUrlToHbase.Tname, "status");
		//Database.showAllFamily(SaveInitialUrlToHbase.Tname);
		//System.out.println(strArray.get(1));
        //String[] familys = {"grade", "course"};
		//Database.getAllRecord(CrawlerConfiguration.WaitingDownloadUrlTableName);
	//}
}
