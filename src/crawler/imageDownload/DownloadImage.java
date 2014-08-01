package crawler.imageDownload;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import crawler.Database.Database;
import crawler.Download.DownloadThread;
import crawler.Download.GetUrlFromHbase;
import crawler.others.CrawlerConfiguration;

public class DownloadImage {
	 public static byte[] getImageByUrl(String strUrl) throws IOException{  
		 URL url = new URL(strUrl);
		 InputStream in = new BufferedInputStream(url.openStream());
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		 int n = 0;
		 while (-1!=(n=in.read(buf))){
		    out.write(buf, 0, n);
		 }
		 out.close();
		 in.close();
		 return out.toByteArray();
	 }
	 public static void downloadImage(int tableIndex) throws Exception{
		 ArrayList<String>imageUrlArray=new ArrayList<String>();
			ThreadPoolExecutor pool = getFixedThreadPool(CrawlerConfiguration.ThreadPoolNumber);
			//我擦。。这代码都五层嵌套了。。完全不能忍啊！！想办法改改，逻辑太复杂了
			while(true){
				if(pool.getActiveCount()+5<CrawlerConfiguration.ThreadPoolNumber){
					//System.out.println("the active thread number"+pool.getActiveCount());
					//System.out.println("url size is"+urlArray.size());
					//第一步,判断此时线程池是否需要补充新的线程
					if(imageUrlArray.size()==0){
						if(Database.isTableEmpty(CrawlerConfiguration.WaitingDownloadImageUrlTable+
								String.valueOf(tableIndex))==true){
							//如果表为空，关闭线程池，break
							System.out.println("the job is finished");
							pool.shutdown();
							//如果有必须要，可以把主表的数据清0
							break;
						}
						else{
							//获取相关数据，执行pool
						//	System.out.println("get the url, url size is"+urlArray.size());
							imageUrlArray=GetImageUrlFromHbase.getImageUrlFromHbase(tableIndex,
									CrawlerConfiguration.ThreadPoolNumber);
						}
					}
					else{
						for(int j=0;j<CrawlerConfiguration.ThreadPoolNumber-pool.getActiveCount();
								j++){
							if(imageUrlArray.size()!=0){
								pool.execute(new DownloadImageThread(imageUrlArray.get(0)));
								imageUrlArray.remove(0);
							}
							else{
								System.out.println("the urlarray is null");
							}
						}
					}
					
				}
			}
	 }
	 public static ThreadPoolExecutor getFixedThreadPool(int threadNumber){
		 return new ThreadPoolExecutor(threadNumber, threadNumber,0L, TimeUnit.MILLISECONDS,   
				new LinkedBlockingQueue<Runnable>());   
	 }
	 public static void main(String args[]) throws Exception{
		 DownloadImage.downloadImage(5);
		 //Database.showAllRecord(CrawlerConfiguration.ImageTableName);
		 //Database.showAllRecord(CrawlerConfiguration.WaitingDownloadImageUrlTable+"1");
		 //Database.showAllRecord(CrawlerConfiguration.DownloadedImageUrlTable);
	 }
}
