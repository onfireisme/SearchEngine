package crawler.parse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.visitors.ObjectFindingVisitor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.huaban.analysis.jieba.JiebaSegmenter;

import crawler.Database.Database;
import crawler.Download.GetUrlFromHbase;
import crawler.others.ChineseToUtf8;
import crawler.others.CrawlerConfiguration;
import crawler.others.DebugFunctions;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ParsePage {
	private static final String regex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;\u4e00-\u9fa5]*[-a-zA-Z0-9+&@#/%=~_|]";
	public static ArrayList<String> parsePage(String htmlString) throws ParserException{
		// this function is used to get the 
		if(htmlString==null){
			return null;
		}
		ObjectFindingVisitor visitor = new ObjectFindingVisitor(
				LinkTag.class);
		LinkTag linkTag;
		Parser parser = new Parser(htmlString);
	    parser.setEncoding(parser.getEncoding());
	    parser.visitAllNodesWith(visitor);
	    Node[] nodes = visitor.getTags();
	    ArrayList<String> stringArray= new ArrayList<String>();
	    // bea
	    for (int i = 0; i < nodes.length; i++) {
	    	linkTag=(LinkTag)nodes[i];
	    	if(isMatch(linkTag.getLink())){
	    		//System.out.println(linkTag.getLink());
	    		if(!isPdf(linkTag.getLink())){
	    			if(!isImage(linkTag.getLink())){
	    				stringArray.add(linkTag.getLink().toString());
	    			}
	    			else{
	    				System.out.println("image link!!"+linkTag.getLink());
	    			}
	    		}
	    	}
	    }
	    if(stringArray.size()<=0){
	    	//System.out.println("we get no links at this page");
	    	stringArray=null;
	    }
	    return stringArray;
	}
	public static String getTitle(String page) throws ParserException{
		String result="";
		if(page==null){
			return result;
		}
		Parser parser = new Parser(page);
	    parser.setEncoding(parser.getEncoding());
        NodeFilter filter = new TagNameFilter ("title");
        NodeList nodes = parser.extractAllNodesThatMatch(filter); 
    	//System.out.println(nodes.size());
        if(nodes.size()>0){
        	result=nodes.elementAt(0).toPlainTextString();
        	//System.out.println(nodes.elementAt(i).toPlainTextString());
        }
        return result;
	}
	public static ArrayList<String>getImageUrl(String page) throws ParserException{
		ArrayList<String>imageUrlArray=new ArrayList<String>();
		if(page==null){
			return imageUrlArray;
		}
		Parser parser = new Parser(page);
	    parser.setEncoding(parser.getEncoding());
        NodeFilter filter = new TagNameFilter ("img");
        NodeList nodes = parser.extractAllNodesThatMatch(filter); 
        ImageTag imageTag;
        String url;
        for(int i=0;i<nodes.size();i++){
        	imageTag=(ImageTag)nodes.elementAt(i);
        	url=imageTag.getAttribute("src");
        	if(isMatch(url)){
        		if(isImage(url)){
        			imageUrlArray.add(url);
        		}
        	}
        	//System.out.println(nodes.elementAt(i).());
//        	nodes.elementAt(i)
        }
		return imageUrlArray;
	}
	public static ArrayList<String>getUrl(String page) throws ParserException{

		ArrayList<String>urlArray=new ArrayList<String>();
		if(page==null){
			return urlArray;
		}
		Parser parser = new Parser(page);
	    parser.setEncoding(parser.getEncoding());
        NodeFilter filter = new TagNameFilter ("a");
        NodeList nodes = parser.extractAllNodesThatMatch(filter);
        LinkTag linkTag;
        String url="";
        for(int i=0;i<nodes.size();i++){
        	linkTag=(LinkTag)nodes.elementAt(i);
        	url=linkTag.getLink();
        	if(isMatch(url)){
        		urlArray.add(url);
        	}
        }
		return urlArray;
	}
	public static boolean isMatch(String urlString){
		if(urlString==null||urlString.equals("")){
			return false;
		}
		Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        return matcher.matches();
	}
	/*
	 * 可以用这个函数，判断链接是否是img的链接
	 */
	public static boolean isImage(String url){
		boolean result=false;
		if(url.endsWith(".png")||url.endsWith(".jpg")
				||url.endsWith(".gif")
				||url.endsWith(".ico")
				||url.endsWith(".jpeg")
				||url.endsWith(".bmp")){
			result=true;
		}
		return result;
	}
	//pdf直接就删除了，没必要下载
	public static boolean isPdf(String url){
		boolean result=false;
		if(url.endsWith(".pdf")){
			result=true;
		}
		return result;
	}
	/*
	public static void main(String args[]) throws Exception{
		Database.showAllRecord(CrawlerConfiguration.UrlTableName);
		String temp=Database.getSpecificRowColumn(CrawlerConfiguration.WebTableName, 
				"http://www.amazon.com", CrawlerConfiguration.WebTableContentFamily,
				CrawlerConfiguration.WebTablePageQualifier);
		System.out.println(ParsePage.getTitle(temp));
	}
	
	public static void main(String args[]) throws Exception{
		ArrayList<String>urlArray=GetUrlFromHbase.getDataFromHbase(1, 10);
		//DebugFunctions.showArray(urlArray);
		DebugFunctions.showArray(ParsePage.parsePage(GetUrlFromHbase.readPageFromHbase(urlArray.get(0))));
		//ParsePage.parsePage(GetUrlFromHbase.readPageFromHbase(urlArray.get(0)));
	}
	*/
}
