package search.getImageAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;

import crawler.Database.Database;
import crawler.others.ChineseToUtf8;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
import crawler.others.ImageAndBytes;

public class GetImageByKeyWord {
	public static ArrayList<String> getUrlByKeyWord(String keyWord,int urlNumber) throws IOException{
		//判断是中文还是英文
		ArrayList<String>urlResult=new ArrayList<String>();
		if(ChineseToUtf8.isAllEnglish(keyWord)){
			urlResult=Database.getSpecificRowKeys(keyWord, 0, urlNumber);
		}
		else{
			if(ChineseToUtf8.isIncludeChinese(keyWord)){
				String tableName=ChineseToUtf8.convertCnToUft8(keyWord);
				urlResult=Database.getSpecificRowKeys(tableName, 0, urlNumber);
			}
		}
		return urlResult;
	}
	public static ArrayList<String> getImageUrlByUrl(String url) throws IOException{
		ArrayList<String>imageUrl=new ArrayList<String>();
		String imageUrlString=Database.getSpecificRowColumn(CrawlerConfiguration.PageInfoTableName,
				url, 
				CrawlerConfiguration.PageInfoFamilyName,
				CrawlerConfiguration.PageInfoImageUrlQualifier);
		//System.out.println(imageUrlString);
		System.out.println(imageUrlString);
		if(imageUrlString==null){
			return imageUrl;
		}
		String[] imageUrlArray=imageUrlString.split(" ");
		if(imageUrlArray.length>0){
			imageUrl.addAll(Arrays.asList(imageUrlArray));
		}
		if(imageUrl.size()>0){
			imageUrl.remove(0);
		}
		return imageUrl;
	}
	public static byte[] getImageByImageUrl(String imageUrl) throws IOException{
		if(imageUrl.length()==0||imageUrl.equals(" ")){
			return null;
		}
		//这里要获取所有的cell
		byte []image = null;
		Result result=Database.getOneRecord(CrawlerConfiguration.ImageTableName, imageUrl);
		if(result==null){
			System.out.println("this image is null");
			return null;
		}
		Cell[] cell=result.rawCells();
		System.out.println(cell.length);

		if(cell.length==1){
			return CellUtil.cloneValue(cell[0]);
		}
		if(cell.length==2){
			byte[] one = CellUtil.cloneValue(cell[0]);
			byte[] two = CellUtil.cloneValue(cell[1]);
			image = new byte[one.length + two.length];
			System.arraycopy(one,0,image,0         ,one.length);
			System.arraycopy(two,0,image,one.length,two.length);
		}
		if(cell.length==3){
			byte[] one = CellUtil.cloneValue(cell[0]);
			byte[] two = CellUtil.cloneValue(cell[1]);
			byte[] three = CellUtil.cloneValue(cell[2]);
			image = new byte[one.length + two.length+three.length];
			System.arraycopy(one,0,image,0         ,one.length);
			System.arraycopy(two,0,image,one.length,two.length);
			System.arraycopy(two,0,image,two.length,three.length);
		}
		return image;
	}
	public static void main(String args[]) throws Exception{
		//DebugFunctions.showArray(getUrlByKeyWord(
				//ChineseToUtf8.convertUtf8ToCn("E4B88AE5AEB6"), 4));
		//System.out.println(ChineseToUtf8.convertUtf8ToCn("E4B88AE5AEB6"));
		//Database.showAllRecord(CrawlerConfiguration.KeyWordTableIndex);
		
	
		Database.showAllRecord(CrawlerConfiguration.KeyWordTableIndex);
		ArrayList<String> urlArray=GetImageByKeyWord.getUrlByKeyWord("Shopping", 10);
		DebugFunctions.showArray(urlArray);
		//GetImageByKeyWord.getImageUrlByUrl(urlArray.get(0));
		DebugFunctions.showArray(GetImageByKeyWord.getImageUrlByUrl(urlArray.get(1)));
		
		if(urlArray.size()>0){
			ImageAndBytes.writeMultiImageToLocal(GetImageByKeyWord.getImageUrlByUrl(urlArray.get(1)));
		}
		
		//Database.showAllRecord(Cralwer)
		//getImageByImageUrl("http://ecx.images-amazon.com/images/I/41os1oecl-L._AA110_.jpg");
		//Database.showAllRecord(CrawlerConfiguration.DownloadedImageUrlTable);
	}
}
