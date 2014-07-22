package crawler.Download;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
public class Download {
	public static void download(int tableIndex) throws Exception{
		ArrayList<String>urlArray=new ArrayList<String>();
		ThreadPoolExecutor pool = getFixedThreadPool(CrawlerConfiguration.ThreadPoolNumber);
		//我擦。。这代码都五层嵌套了。。完全不能忍啊！！想办法改改，逻辑太复杂了
		while(true){
			if(pool.getActiveCount()+5<CrawlerConfiguration.ThreadPoolNumber){
				//System.out.println("the active thread number"+pool.getActiveCount());
				//System.out.println("url size is"+urlArray.size());
				//第一步,判断此时线程池是否需要补充新的线程
				if(urlArray.size()==0){
					if(Database.isTableEmpty(CrawlerConfiguration.WaitingDownloadUrlTableName+
							String.valueOf(tableIndex))==true){
						//如果表为空，关闭线程池，break
						System.out.println("the job is finished");
						pool.shutdown();
						break;
					}
					else{
						//获取相关数据，执行pool
						System.out.println("get the url, url size is"+urlArray.size());
						urlArray= GetUrlFromHbase.getDataFromHbase(tableIndex,
								CrawlerConfiguration.ThreadPoolNumber);
					}
				}
				else{
					for(int j=0;j<CrawlerConfiguration.ThreadPoolNumber-pool.getActiveCount();
							j++){
						if(urlArray.size()!=0){
							pool.execute(new DownloadThread(urlArray.get(0), tableIndex));
							urlArray.remove(0);
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
	}/*
	public static void main(String args[]) throws Exception{
		//Download.download(1);
		Database.showAllRecord(CrawlerConfiguration.UrlTableName);
	}
	
	public static void main(String args[]) throws Exception{
		//Database.showAllRecord("MDurl");
		//Download.download(1);
		//System.out.println(Database.isTableEmpty("Durl2"));
		//DebugFunctions.showArray(GetUrlFromHbase.getDataFromHbase(2, 5));
		//Database.showAllRecord("Durl2");
		//Database.showAllRecord("Durl1");
		//DebugFunctions.showArray(GetUrlFromHbase.getDataFromHbase(1,
		//		CrawlerConfiguration.ThreadPoolNumber));
		//Database.showAllRecord("web");

	}
	*/
	
}
