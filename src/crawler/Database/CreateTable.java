package crawler.Database;
import java.util.ArrayList;

import crawler.others.CrawlerConfiguration;;
public class CreateTable {
	public void downloadModuleTableInitial() throws Exception{
		
		//创建download main table
		String[]tempArray=new String[1];
		tempArray[0]=CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName;
		Database.creatTable(CrawlerConfiguration.WaitingDownloadUrlMainTableName,tempArray);
		//创建download table，这里只需要五个表。
		tempArray[0]=CrawlerConfiguration.TempFamilyName;
		for(int i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.creatTable(CrawlerConfiguration.WaitingDownloadUrlTableName+Integer.toString(i+1),
					tempArray);
		}
		
		//初始化download main table
		ArrayList<String>rowKeyArray=new ArrayList<String>();
		ArrayList<String>valueArray=new ArrayList<String>();
		for(int j=0;j<CrawlerConfiguration.WaitingDownloadUrlTableNumber;j++){
			rowKeyArray.add(CrawlerConfiguration.WaitingDownloadUrlTableName+Integer.toString(j+1));
			valueArray.add(Integer.toString(0));
		}
		Database.addMultiRecords(CrawlerConfiguration.WaitingDownloadUrlMainTableName, 
				rowKeyArray, CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName, 
				CrawlerConfiguration.WaitingDownloadUrlMainTableQualifierName, valueArray);
	}
	public void ParseModuleInitial() throws Exception{
		String[]tempArray=new String[1];
		tempArray[0]=CrawlerConfiguration.TempFamilyName;
		//这里的话，是跟download对应的，download有几个二级表，这里也就有几个表
		for(int i=0;i<CrawlerConfiguration.ParseUrlTableNumber;i++){
			Database.creatTable(CrawlerConfiguration.WaitingParseUrlTableName+Integer.toString(i+1),
					tempArray);
		}
		tempArray[0]=CrawlerConfiguration.DownloadStatusFamilyName;
		Database.creatTable(CrawlerConfiguration.ParseUrlTableName,tempArray);
	}
	public void webTableInitial() throws Exception{
		String[]tempArray=new String[1];
		tempArray[0]=CrawlerConfiguration.WebTableContentFamily;
		Database.creatTable(CrawlerConfiguration.WebTableName,tempArray);
	}
	public void urlTableIntial() throws Exception{
		String[]family=new String[1];
		family[0]=CrawlerConfiguration.DownloadStatusFamilyName;
		Database.creatTable(CrawlerConfiguration.UrlTableName, family);
	}
	public void deleteWDmodule() throws Exception{
		Database.deleteTable(CrawlerConfiguration.WaitingDownloadUrlMainTableName);
		for(int i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.deleteTable(CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(i+1));
		}
	}
	/*
	public static void main(String args[]) throws Exception { 
		//CreateTable ct=new CreateTable();
		//ct.deleteWDmodule();
		//ct.downloadModuleTableInitial();
		Database.showAllTable();
	}

	public static void main(String args[]) throws Exception { 
		Database.deleteTable("Purl1");
		Database.deleteTable("Purl2");
		Database.deleteTable("Purl3");
		Database.deleteTable("Purl4");
		Database.deleteTable("Purl5");

		CreateTable ct=new CreateTable();
		ct.ParseModuleInitial();
        //String[] familys = {"grade", "course"};  
	}
	*/
}
