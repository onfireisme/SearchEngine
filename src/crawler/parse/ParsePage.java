package crawler.parse;

import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.visitors.ObjectFindingVisitor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import crawler.Download.GetUrlFromHbase;
import crawler.others.DebugFunctions;

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
	public static boolean isMatch(String urlString){
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
		ArrayList<String>urlArray=GetUrlFromHbase.getDataFromHbase(1, 10);
		//DebugFunctions.showArray(urlArray);
		DebugFunctions.showArray(ParsePage.parsePage(GetUrlFromHbase.readPageFromHbase(urlArray.get(0))));
		//ParsePage.parsePage(GetUrlFromHbase.readPageFromHbase(urlArray.get(0)));
	}
	*/
}
