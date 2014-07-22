package crawler.others;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChineseToUtf8 {
	public static String convertCnToUft8(String chinese) throws UnsupportedEncodingException{
		//String result=URLEncoder.encode(chinese,"UTF-8");
		String result="";
		if(isIncludeEnglish(chinese)==true){
			return result;
		}
		String []array=URLEncoder.encode(chinese,"UTF-8").split("%"); 
		for(int i=0;i<array.length;i++){
			result=result+array[i];
		}
		if(result==""){
			result=null;
		}
		return result;
	}
	public static String convertUtf8ToCn(String utf8) throws UnsupportedEncodingException{
		//我们都知道，utf8必须是偶数位
		String result="";
		for(int i=0;i<utf8.length();i=i+2){
			result=result+"%"+utf8.substring(i, i+2);
		}
		result=URLDecoder.decode(result,"UTF-8");
		return result;
	}
	//妈蛋，如果不是中文，这样转码就会出问题呀~~
	public static boolean isIncludeChinese(String str){
		Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
	    Matcher m=p.matcher(str); 
	    if(m.find()){
	    	return true;
	    }
	    return false;
	}
	public static boolean isIncludeEnglish(String str){
		Pattern p=Pattern.compile("[a-zA-Z]"); 
	    Matcher m=p.matcher(str); 
	    if(m.find()){
	    	return true;
	    }
	    return false;
	}
	/*
	public static void main(String args[]) throws Exception{
		String str="你l";
		//System.out.println(str.substring(0, 3));
		System.out.println(ChineseToUtf8.isIncludeEnglish(str));
		System.out.println(((ChineseToUtf8.convertCnToUft8(str))));
		//str.length();
		//System.out.println(str.length());
	}
	*/
}
