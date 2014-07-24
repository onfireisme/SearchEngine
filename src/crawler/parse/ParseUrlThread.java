package crawler.parse;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huaban.analysis.jieba.JiebaSegmenter;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ParseUrlThread implements Runnable {
	private String url;
	private JiebaSegmenter segmenter;
	private MaxentTagger tagger;

	ParseUrlThread(String url,JiebaSegmenter segmenter,
			MaxentTagger tagger) {
		this.url = url;
		this.segmenter=segmenter;
		this.tagger=tagger;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//第一步，获取page
			String page=ParseModuleReadFromHbase.getPageFromHbase(url);
			if(page==null||page.equals("")){
				System.out.println("the page is null");
				return;
			}
			//第二步，解析页面，获得相关数据
			ArrayList<String>urlArray=ParsePage.getUrl(page);
			ArrayList<String>imageUrlArray=ParsePage.getImageUrl(page);
			System.out.println(ParsePage.getTitle(page));
			ArrayList<String>keyWordArray=ExtractKeyWords.extractKeyWords(ParsePage.getTitle(page),
					segmenter,tagger);
			//存储数据
			SaveParsedUrlToHbase.saveKeyWord(keyWordArray, url);
			SaveParsedUrlToHbase.saveParsedUrl(urlArray);
			SaveParsedUrlToHbase.saveParsedImageUrl(imageUrlArray);
			SaveParsedUrlToHbase.saveAllToPageInfo(url, keyWordArray, urlArray, imageUrlArray);
			//DebugFunctions.showArray(imageUrlArray);
			/*
			
			*/
			/*
			SaveParsedUrlToHbase.saveParsedUrl(
					ParsePage.parsePage(
							ParseModuleReadFromHbase.getPageFromHbase(this.url)));
							*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}/*
	public static void main(String args[]) throws Exception{
		JiebaSegmenter segmenter=ExtractKeyWords.getJiebaSegmenter();
		MaxentTagger tagger=ExtractKeyWords.getMaxentTagger();
		
		
		//ParseUrlThread t=new ParseUrlThread("http://www.taobao.com",
		//		segmenter,tagger);
		//t.run();
		//Database.showAllRecord(CrawlerConfiguration.PageInfoTableName);
		Database.showAllRecord(CrawlerConfiguration.ParsedImageUrlTable);
		//Database.showAllRecord(CrawlerConfiguration.ParsedUrlTableName);
		//Database.showAllTable();
	}*/

}
