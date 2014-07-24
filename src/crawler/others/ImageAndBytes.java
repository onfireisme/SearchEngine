package crawler.others;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.hadoop.hbase.util.Bytes;

import crawler.Database.Database;

public class ImageAndBytes {
	public static BufferedImage convertBytesToImage(byte [] byteArray) throws IOException{
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(byteArray));
		return img;
	}
	public static void writeImageToLocal(String path,BufferedImage image) throws IOException{
		//可以自己选择生成的image文件，不知道会不会有印象呀~~
		 //ImageIO.write(image, "jpg",new File("C:\\out.jpg"));
         //ImageIO.write(image, "gif",new File("C:\\out.gif"));
		if(image!=null){
			ImageIO.write(image, "png",new File(path+"lol.png"));
		}
		else{
			System.out.println("the image is null");
		}
	}
	public static void main(String args[]) throws Exception{
		System.out.println(Database.getSpecificRowColumnByByte(CrawlerConfiguration.ImageTableName,
				"http://ecx.images-amazon.com/images/I/510qMu1XQ4L._SL75_.jpg",
				 CrawlerConfiguration.ImageTableFamilyName,
				CrawlerConfiguration.ImageTableQualifierName));
		writeImageToLocal("/home/wangyan/lol/",convertBytesToImage(
				(Database.getSpecificRowColumnByByte(CrawlerConfiguration.ImageTableName,
						"http://ecx.images-amazon.com/images/I/510qMu1XQ4L._SL75_.jpg",
						 CrawlerConfiguration.ImageTableFamilyName,
						CrawlerConfiguration.ImageTableQualifierName))));
		//Database.showAllRecord(CrawlerConfiguration.DownloadedImageUrlTable);
	}
}
