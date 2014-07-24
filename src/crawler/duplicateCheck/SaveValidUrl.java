package crawler.duplicateCheck;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.SaveInitialUrlToHbase;

class UrlNode{
	public int position;
	public int value;
	public UrlNode(int position,int value){
		this.position=position;
		this.value=value;
	}
}
public class SaveValidUrl {
	/*
	 * 这个函数的输入是string数组，然后负责存储到各个Durltable就可以了
	 */
	public int test;
	public void saveToHbase(ArrayList<String>urlArray){
		//第一步，获取相关的
	}
	public static ArrayList<UrlNode> initUrlNodeArray(ArrayList<Integer> urlNumberArray){
		ArrayList<UrlNode> urlNodeArray=new ArrayList<UrlNode>();
		for(int i=0;i<urlNumberArray.size();i++){
			//UrlNode tempNode=new UrlNode(i,urlNumberArray.get(i));
			urlNodeArray.add(new UrlNode(i,urlNumberArray.get(i)));
		}
		return urlNodeArray;
	}
	public static ArrayList<Integer> balanceUrlNumber(ArrayList<Integer> urlNumberArray,
			int totalUrlNumber){
		ArrayList<Integer> result=new ArrayList<Integer>(); 
		for(int j=0;j<urlNumberArray.size();j++){
			result.add(0);
		}
		ArrayList<UrlNode>urlNodeArray=initUrlNodeArray(urlNumberArray);
		sortUrlNode(urlNodeArray,0,urlNodeArray.size()-1);
		showUrlNode(urlNodeArray);
		int maxValue=urlNodeArray.get(urlNodeArray.size()-1).value;
		int i=0;
		while(i<urlNumberArray.size()-1){
			if(maxValue==urlNodeArray.get(0).value){
				break;
			}
			int value=urlNodeArray.get(i).value;
			int addNumber=maxValue-value;
			if(totalUrlNumber>=addNumber){
				totalUrlNumber=totalUrlNumber-addNumber;
				result.set(urlNodeArray.get(i).position,  new Integer(addNumber));
			}
			else{
				result.set(urlNodeArray.get(i).position,  new Integer(totalUrlNumber));
				totalUrlNumber=totalUrlNumber-addNumber;
				break;
			}
			i++;
		}
		//如果发现，全部表的url数目一样，这个时候，吧剩余的值全部分给余下的值
		if(totalUrlNumber>0){
			int averageNumber=totalUrlNumber/(urlNumberArray.size());
			int leftNumber=totalUrlNumber%urlNumberArray.size();
			int temp=result.get(0);
			result.set(0, temp+leftNumber);
			for(int k=0;k<result.size();k++){
				temp=result.get(k);
				result.set(k, temp+averageNumber);
			}
		}
		return result;
	}
	//从小到大排序
	public static void sortUrlNode(ArrayList<UrlNode> urlNodeArray,int begin,int end){
		if(begin==end){
			return ;
		}
		int countFlag=begin;
		int smallerCountFlag=begin;
		int lastElementValue=urlNodeArray.get(end).value;
		for(int i=begin;i<end;i++){
			countFlag++;
			if(lastElementValue>urlNodeArray.get(i).value){
				if(countFlag>smallerCountFlag){
					exchangeUrlNode(urlNodeArray, smallerCountFlag, countFlag-1);
				}
				smallerCountFlag++;
			}
		}
		//最后，你需要把最后一个节点，放到制定位置
		exchangeUrlNode(urlNodeArray, smallerCountFlag, end);
		if(smallerCountFlag<end){
			sortUrlNode(urlNodeArray,smallerCountFlag+1,end);
		}
		if(smallerCountFlag>begin){
			sortUrlNode(urlNodeArray,begin,smallerCountFlag-1);
		}

	}
	public static void exchangeUrlNode(ArrayList<UrlNode> urlNodeArray,int p1,int p2){
		UrlNode temp1=urlNodeArray.get(p2);
		UrlNode temp2=urlNodeArray.get(p1);
		urlNodeArray.set(p1, temp1);
		urlNodeArray.set(p2, temp2);
	}
	public static int getMaxNumber(int []urlNumberArray){
		//使用堆排序，自己写代码~~
		int copy[]=Arrays.copyOf(urlNumberArray,urlNumberArray.length);
		Arrays.sort(copy);
		return urlNumberArray[urlNumberArray.length-1];
	}
	/*
	 * 遍历一遍数组，获得值的位置，如果找不到就返回-1。
	 */
	public static int getNumberPosition(int []urlNumberArray,int value){
		for(int i=0;i<urlNumberArray.length;i++){
			if(value==urlNumberArray[i]){
				return i;
			}
		}
		return -1;
	}
	public static void showUrlNode(ArrayList<UrlNode> urlNodeArray){
		for(int i=0;i<urlNodeArray.size();i++){
			System.out.print(urlNodeArray.get(i).value+" ");
			//System.out.println(temp[i]);
		}
		System.out.println("");
	}
	public static void showIntegerArray(ArrayList<Integer> intArray){
		for(int i=0;i<intArray.size();i++){
			System.out.print(intArray.get(i)+" ");
		}
	}
	//这里获取相关的数据
	public static ArrayList<String> getWaitingDownloadTableInfo(){
		ArrayList<String> strArray=new ArrayList<String>();
		return strArray;
	}
	public static ArrayList<Integer>convertStringArrayToIntArray(ArrayList<String> strArray){
		ArrayList<Integer> intArray=new ArrayList<Integer>();
		for(int i=0;i<strArray.size();i++){
			intArray.add(Integer.parseInt(strArray.get(i)));
		}
		return intArray;
	}
	public static void saveValidUrlToHbase(ArrayList<String> urlArray) throws Exception{
		ArrayList<Integer> tableInfo= convertStringArrayToIntArray(Database.getSpecificQualifierRows(
				CrawlerConfiguration.WaitingDownloadUrlMainTableName, 
				CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName,
				CrawlerConfiguration.WaitingDownloadUrlMainTableQualifierName));
		ArrayList<Integer>assignMent=balanceUrlNumber(tableInfo, urlArray.size());
		//then we need to store the data to hbase,depend on the assignment
		int begin=0;
		int end=assignMent.get(0)-1;
		for(int i=0;i<assignMent.size();i++){
			saveUrlToSecondaryTable(urlArray,begin,end,i+1,tableInfo.get(i)+assignMent.get(i));
			begin=end+1;
			if(i!=assignMent.size()-1){
				end=end+assignMent.get(i+1);
			}
		}
	}
	public static void saveUrlToSecondaryTable(ArrayList<String>urlArray,
			int begin,int end,int tableIndex,int newCount) throws Exception{
		ArrayList<String>url=new ArrayList<String>();
		ArrayList<String>temp=new ArrayList<String>();
		for(int i=begin;i<=end;i++){
			url.add(urlArray.get(i));
			temp.add(CrawlerConfiguration.TempValue);
		}

		Database.addMultiRecords(CrawlerConfiguration.WaitingDownloadUrlTableName+Integer.toString(tableIndex),
				url, CrawlerConfiguration.TempFamilyName,
				CrawlerConfiguration.TempQualifierName, 
				temp);
		//插入成功后，要记得修改hbase中相应的值呀！！
		Database.addRecord(CrawlerConfiguration.WaitingDownloadUrlMainTableName,
				CrawlerConfiguration.WaitingDownloadUrlTableName+Integer.toString(tableIndex)
				, CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName
				, CrawlerConfiguration.WaitingDownloadUrlMainTableQualifierName
				, String.valueOf(newCount));
				
	}
	/*
	 * 下面是存储valid image url到hbase
	 */
	public static void saveImageUrlToHbase(ArrayList<String> urlArray) throws Exception{
		ArrayList<Integer> tableInfo= convertStringArrayToIntArray(Database.getSpecificQualifierRows(
				CrawlerConfiguration.WaitingDownloadImageUrlMainTable , 
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableFamilyName,
				CrawlerConfiguration.WaitingDownloadImageUrlMainTableQualifierName ));
		ArrayList<Integer>assignMent=balanceUrlNumber(tableInfo, urlArray.size());
		//then we need to store the data to hbase,depend on the assignment
		int begin=0;
		int end=assignMent.get(0)-1;
		for(int i=0;i<assignMent.size();i++){
			saveImageUrlToSecondaryTable(urlArray,begin,end,i+1,tableInfo.get(i)+assignMent.get(i));
			begin=end+1;
			if(i!=assignMent.size()-1){
				end=end+assignMent.get(i+1);
			}
		}
	}
	public static void saveImageUrlToSecondaryTable(ArrayList<String>urlArray,
			int begin,int end,int tableIndex,int newCount) throws Exception{
		ArrayList<String>url=new ArrayList<String>();
		ArrayList<String>temp=new ArrayList<String>();
		for(int i=begin;i<=end;i++){
			url.add(urlArray.get(i));
			temp.add(CrawlerConfiguration.TempValue);
		}

		Database.addMultiRecords(CrawlerConfiguration.WaitingDownloadImageUrlTable+Integer.toString(tableIndex),
				url, CrawlerConfiguration.TempFamilyName,
				CrawlerConfiguration.TempQualifierName, 
				temp);
		//插入成功后，要记得修改hbase中相应的值呀！！
		Database.addRecord(CrawlerConfiguration.WaitingDownloadImageUrlMainTable,
				CrawlerConfiguration.WaitingDownloadImageUrlTable+Integer.toString(tableIndex)
				, CrawlerConfiguration.WaitingDownloadUrlMainTableFamilyName
				, CrawlerConfiguration.WaitingDownloadUrlMainTableQualifierName
				, String.valueOf(newCount));
				
	}
	/*
	public static void main(String args[]) throws Exception{
		//int temp[]={5,10,3,1,0,8,2,9,1,20,0,0,0,100,7,2,1,8,10,25};
		//int temp[]={0,0,0,0,0};
		//ArrayList<Integer>integerArray=SaveValidUrl.balanceUrlNumber(temp, 50);
		//SaveValidUrl.showIntegerArray(integerArray);

		//SaveValidUrl.saveUrlToMainTable(SaveInitialUrlToHbase.readUrlFromTxt("/home/wangyan/android/url.txt"));
		Database.showAllRecord("MDurl");
		Database.showAllRecord("Durl1");
		Database.showAllRecord("Durl2");
		Database.showAllRecord("Durl3");
		Database.showAllRecord("Durl4");
		Database.showAllRecord("Durl5");

	}	
		Database.showAllRecord("Purl");
		Database.showAllRecord("WPurl1");
		Database.showAllRecord("WPurl2");
		Database.showAllRecord("WPurl3");
		Database.showAllRecord("WPurl4");
		Database.showAllRecord("WPurl5");

		//Database.showAllRecord(CrawlerConfiguration.WaitingDownloadUrlMainTableName);

		/*
		ArrayList<Integer>tempArray=SaveValidUrl.balanceTableUrlNumber(temp,10);
		for(int i=0;i<5;i++){
			System.out.println(tempArray.get(i));
			//System.out.println(temp[i]);
		}
		//System.out.print(SaveValidUrl.getMaxNumber(temp));
	}
	 */
}
