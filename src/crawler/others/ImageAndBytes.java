package crawler.others;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.hadoop.hbase.util.Bytes;

import search.getImageAPI.GetImageByKeyWord;

import crawler.Database.Database;

public class ImageAndBytes {
	public static BufferedImage convertBytesToImage(byte [] byteArray) throws IOException{
		if(byteArray==null||byteArray.length==0){
			return null;
		}
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(byteArray));
		return img;
	}
	public static void writeImageToLocal(String path,BufferedImage image,String fileName) throws IOException{
		if(image==null){
			return;
		}
		//可以自己选择生成的image文件，不知道会不会有印象呀~~
		 //ImageIO.write(image, "jpg",new File("C:\\out.jpg"));
         //ImageIO.write(image, "gif",new File("C:\\out.gif"));
		if(image!=null){
			ImageIO.write(image, "png",new File(path+fileName));
		}
		else{
			System.out.println("the image is null");
		}
	}
	public static void writeMultiImageToLocal(ArrayList<String>imageUrl) throws IOException{
		String path="/home/wangyan/lol/";
		String name="temp";
		for(int i=0;i<imageUrl.size();i++){
			writeImageToLocal(path,
					convertBytesToImage(GetImageByKeyWord.getImageByImageUrl(imageUrl.get(i))),
					name+Integer.toString(i));
		}
	}
	public static void main(String args[]) throws Exception{
		ArrayList<String> imageUrl= new ArrayList<String>();
		String test1="http://www.baidu.com/img/bdlogo.gif";
		GetImageByKeyWord.getImageByImageUrl("http://www.baidu.com/img/bdlogo.gif");
		//imageUrl.add(test1);
		//System.out.println(imageUrl.size());
		//writeMultiImageToLocal(imageUrl);
	}
}
