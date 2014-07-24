package crawler.imageDownload;

import java.util.Arrays;

import org.apache.hadoop.hbase.util.Bytes;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;

public class SaveImageToHbase {
	public static void saveImageToHbase(byte[]imageByteArray,String imageUrl) throws Exception{
		//没这么麻烦，哪怕imageByteArrayl为空，照样也插入，读取的时候，如果为空，则标志图片读取失败
		//判断图片是否过大，如果过大，则分开存储，如果不是，则直接存入hbase
		int imageSize=imageByteArray.length/1024/1024;
		if(imageSize<8){
			Database.addRecord(CrawlerConfiguration.ImageTableName,
					imageUrl, 
					CrawlerConfiguration.ImageTableFamilyName,
					CrawlerConfiguration.ImageTableQualifierName,imageByteArray);
		}
		else{
			if(imageSize<24){
				if(imageByteArray.length<16*1024*1024){
					byte[]array1=Arrays.copyOfRange(imageByteArray, 0, 8*1024*1024);
					byte[]array2=Arrays.copyOfRange(imageByteArray,8*1024*1024,imageByteArray.length+1);
					Database.addRecord(CrawlerConfiguration.ImageTableName,
							imageUrl, 
							CrawlerConfiguration.ImageTableFamilyName,
							CrawlerConfiguration.ImageTableQualifierName,array2);
					Database.addRecord(CrawlerConfiguration.ImageTableName,
							imageUrl, 
							CrawlerConfiguration.ImageTableFamilyName,
							CrawlerConfiguration.ImageTableQualifierName,array1);
				}
				else{
					byte[]array1=Arrays.copyOfRange(imageByteArray, 0, 8*1024*1024);
					byte[]array2=Arrays.copyOfRange(imageByteArray,8*1024*1024,16*1024*1024);
					byte[]array3=Arrays.copyOfRange(imageByteArray,16*1024*1024,imageByteArray.length+1);
					Database.addRecord(CrawlerConfiguration.ImageTableName,
							imageUrl, 
							CrawlerConfiguration.ImageTableFamilyName,
							CrawlerConfiguration.ImageTableQualifierName,array3);
					Database.addRecord(CrawlerConfiguration.ImageTableName,
							imageUrl, 
							CrawlerConfiguration.ImageTableFamilyName,
							CrawlerConfiguration.ImageTableQualifierName,array2);
					Database.addRecord(CrawlerConfiguration.ImageTableName,
							imageUrl, 
							CrawlerConfiguration.ImageTableFamilyName,
							CrawlerConfiguration.ImageTableQualifierName,array1);
				}
				
				//把数组分割
			}
			else{
				//把数组表示为0，直接存入
				byte[]temp=new byte[0];
				Database.addRecord(CrawlerConfiguration.ImageTableName,
						imageUrl, 
						CrawlerConfiguration.ImageTableFamilyName,
						CrawlerConfiguration.ImageTableQualifierName,temp);
			}
		}
		//最后一步，把imgaurl 存入imageUrl表中
		Database.addRecord(CrawlerConfiguration.DownloadedImageUrlTable, 
				imageUrl, CrawlerConfiguration.DownloadStatusFamilyName,
				CrawlerConfiguration.DownloadStatusQualifier, 
				CrawlerConfiguration.DownloadedStatus);
	}
}
