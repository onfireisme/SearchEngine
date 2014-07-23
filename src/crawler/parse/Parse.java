package crawler.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.util.Bytes;

import com.huaban.analysis.jieba.JiebaSegmenter;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Parse {
	//这个函数只需要tableIndex即可
	public static void  parseMain(int tableIndex) throws IOException{
		ArrayList<String>urlArray=new ArrayList<String>();
		JiebaSegmenter segmenter=ExtractKeyWords.getJiebaSegmenter();
		MaxentTagger tagger=ExtractKeyWords.getMaxentTagger();
		ThreadPoolExecutor pool = getFixedThreadPool(CrawlerConfiguration.ThreadPoolNumber);
		while(true){
			if(pool.getActiveCount()+5<CrawlerConfiguration.ThreadPoolNumber){
				System.out.println("the active thread number"+pool.getActiveCount());
				System.out.println("the waiting download url is:");
				Database.showAllRecord("WPurl1");
				//System.out.println("url size is"+urlArray.size());
				//第一步,判断此时线程池是否需要补充新的线程
				if(urlArray.size()==0){
					if(Database.isTableEmpty(CrawlerConfiguration.WaitingParseUrlTableName+
							String.valueOf(tableIndex))==true){
						//如果表为空，关闭线程池，break
						System.out.println("the job is finished");
						pool.shutdown();
						break;
					}
					else{
						//获取相关数据，执行pool
						System.out.println("get the url, url size is"+urlArray.size());
						urlArray=ParseModuleReadFromHbase.readParseUrl(tableIndex,
								CrawlerConfiguration.ThreadPoolNumber); 
					}
				}
				else{
					for(int j=0;j<CrawlerConfiguration.ThreadPoolNumber-pool.getActiveCount();
							j++){
						if(urlArray.size()!=0){
							pool.execute(new ParseUrlThread(urlArray.get(0),segmenter,tagger));
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
	}
	/*
	public static void main(String args[]) throws Exception{
		Database.showAllRecord("Purl");
		//Database.showAllRecord("url");

		//Parse.parseMain(1);
	}
	/*
	*/
}
