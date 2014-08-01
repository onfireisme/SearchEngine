package crawler.Database;
import java.util.ArrayList;

import crawler.duplicateCheck.SaveValidUrl;
import crawler.others.CrawlerConfiguration;
import crawler.others.SaveInitialUrlToHbase;
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
			
		//添加pageInfo表
		tempArray[0]=CrawlerConfiguration.PageInfoFamilyName;
		Database.creatTable(CrawlerConfiguration.PageInfoTableName, tempArray);
	}
	public void imageDownloadInitial() throws Exception{
		//image的下载模块，跟page下载是差不多的，不过是一个新的模块
		//第一步，创建image表
		String[]tempArray={CrawlerConfiguration.ImageTableFamilyName};
		Database.creatTable(CrawlerConfiguration.ImageTableName, tempArray);
		//第二步，创建image waitingdownload url表，一个主表，然后几个子表
		tempArray[0]=CrawlerConfiguration.WaitingDownloadImageUrlMainTableFamilyName;
		Database.creatTable(CrawlerConfiguration.WaitingDownloadImageUrlMainTable,tempArray);
		tempArray[0]=CrawlerConfiguration.TempFamilyName;
		for(int i=0;i<CrawlerConfiguration.WaitingDownloadImageUrlTableNumber;i++){
			Database.creatTable(CrawlerConfiguration.WaitingDownloadImageUrlTable+Integer.toString(i+1),tempArray);
		}
		//初始化 main table
		ArrayList<String>rowKeyArray=new ArrayList<String>();
		ArrayList<String>valueArray=new ArrayList<String>();
		for(int j=0;j<CrawlerConfiguration.WaitingDownloadImageUrlTableNumber;j++){
			rowKeyArray.add(CrawlerConfiguration.WaitingDownloadImageUrlTable+Integer.toString(j+1));
			valueArray.add(Integer.toString(0));
		}
		Database.addMultiRecords(CrawlerConfiguration.WaitingDownloadImageUrlMainTable,
				rowKeyArray, CrawlerConfiguration.WaitingDownloadImageUrlMainTableFamilyName, 
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableQualifierName, valueArray);
		//创建表来存储已经下载过的image url
		tempArray[0]=CrawlerConfiguration.DownloadStatusFamilyName;
		Database.creatTable(CrawlerConfiguration.DownloadedImageUrlTable, tempArray);
	}
	public void imageParsedModuleInitial() throws Exception{
		String[]tempArray={CrawlerConfiguration.DownloadStatusFamilyName};
		Database.creatTable(CrawlerConfiguration.ParsedImageUrlTable, tempArray);
	}
	public void parseModuleInitial() throws Exception{
		String[]tempArray=new String[1];
		tempArray[0]=CrawlerConfiguration.TempFamilyName;
		//这里的话，是跟download对应的，download有几个二级表，这里也就有几个表
		for(int i=0;i<CrawlerConfiguration.ParsedUrlTableNumber;i++){
			Database.creatTable(CrawlerConfiguration.WaitingParseUrlTableName+Integer.toString(i+1),
					tempArray);
		}
		tempArray[0]=CrawlerConfiguration.DownloadStatusFamilyName;
		Database.creatTable(CrawlerConfiguration.ParsedUrlTableName,tempArray);
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
	public void keyWordTableIndex() throws Exception{
		String[] family=new String[1];
		family[0]=CrawlerConfiguration.KeyWordTableIndexFamily;
		Database.creatTable(CrawlerConfiguration.KeyWordTableIndex, family);
	}
	public void createTable() throws Exception{
		downloadModuleTableInitial();
		imageDownloadInitial();
		imageParsedModuleInitial();
		parseModuleInitial();
		webTableInitial();
		urlTableIntial();
		keyWordTableIndex();
	}
	
	public void deleteWDmodule() throws Exception{
		Database.deleteTable(CrawlerConfiguration.WaitingDownloadUrlMainTableName);
		for(int i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.deleteTable(CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(i+1));
		}
	}
	public void deleteAllTable() throws Exception{
		//下载模块的表
		Database.deleteTable(CrawlerConfiguration.WaitingDownloadUrlMainTableName);
		int i=0;
		for(i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.deleteTable(CrawlerConfiguration.WaitingDownloadUrlTableName+String.valueOf(i+1));
		}
		//web表
		Database.deleteTable(CrawlerConfiguration.WebTableName);
		//url表
		Database.deleteTable(CrawlerConfiguration.UrlTableName);
		//解析模块
		for(i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.deleteTable(CrawlerConfiguration.WaitingParseUrlTableName+String.valueOf(i+1));
		}
		Database.deleteTable(CrawlerConfiguration.ParsedUrlTableName);
		//图片下载模块
		Database.deleteTable(CrawlerConfiguration.ImageTableName);
		Database.deleteTable(CrawlerConfiguration.DownloadedImageUrlTable);
		Database.deleteTable(CrawlerConfiguration.WaitingDownloadImageUrlMainTable);
		for(i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.deleteTable(CrawlerConfiguration.WaitingDownloadImageUrlTable+String.valueOf(i+1));
		}
	}
	public static void main(String args[]) throws Exception { 
		/*
		Database.deleteAllTable();
		CreateTable ct=new CreateTable();
		ct.createTable();
		*/
		Database.showAllTable();
		//初始化下载列表
	}
	/*
	public static void main(String args[]) throws Exception { 
		CreateTable ct=new CreateTable();
		for(int i=0;i<CrawlerConfiguration.WaitingDownloadUrlTableNumber;i++){
			Database.deleteTable(CrawlerConfiguration.WaitingDownloadImageUrlTable+String.valueOf(i+1));
		}
		ct.imageDownloadInitial();
		Database.showAllTable();
	}
	
	public static void main(String args[]) throws Exception { 
		//CreateTable ct=new CreateTable();
		//ct.deleteWDmodule();
		//ct.downloadModuleTableInitial();
		Database.showAllTable();
	}
	*/
}
