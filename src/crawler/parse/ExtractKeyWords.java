package crawler.parse;
import crawler.others.ChineseToUtf8;
import crawler.others.DebugFunctions;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yecht.ruby.GenericResolver.Extra;


import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.Word;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ExtractKeyWords {
	//由于每次，加载词典都需要很长时间，所以，把segmenter对象传入是个不错的选择。
	public static ArrayList<String> getNounAndVerb(String sentence,
			JiebaSegmenter segmenter) throws UnsupportedEncodingException {
		
		ArrayList<String>result=new ArrayList<String>();
	    List<SegToken> segArray=segmenter.process(sentence, SegMode.INDEX);
	    String wordType;
	    Word word;
	    for(int i=0;i<segArray.size();i++){
	    	//if(segArray.get(i).toString()()=="")
	    	word=segArray.get(i).word;
	    	wordType=word.getTokenType();
	    	if(wordType.contains("n")||wordType.contains("v")){
	    		result.add(word.getToken());
	    	}
	    }
	    return result;
	}
	public static ArrayList<String> convertKeyWord(ArrayList<String> keyWord) throws UnsupportedEncodingException{
		ArrayList<String>convertedKeyWord=new ArrayList<String>();
		if(keyWord==null||keyWord.size()==0){
			return convertedKeyWord;
		}
		System.out.println("lol");
		if(ExtractKeyWords.isChinese(keyWord.get(0))){
			for(int i=0;i<keyWord.size();i++){
				convertedKeyWord.add(i,ChineseToUtf8.convertCnToUft8(keyWord.get(i)));
		    }
			return convertedKeyWord;
		}
		else{
			if(ExtractKeyWords.isEnglish(keyWord.get(0))){
				return keyWord;
			}
		}
		return convertedKeyWord;
	}
	public static ArrayList<String> extractKeyWords(String sentence,JiebaSegmenter segmenter,
			MaxentTagger tagger) throws UnsupportedEncodingException{
		ArrayList<String>result=new ArrayList<String>();
		if(isChinese(sentence)){
			result=getNounAndVerb(sentence, segmenter);
		}
		else{
			if(isEnglish(sentence)){
				result=taggerEnglishKeyWord(sentence, tagger);
			}
		}
		return result;
	}
	public static ArrayList<String> taggerEnglishKeyWord(String str,MaxentTagger tagger){
		//str =" why got names test is lol";
		ArrayList<String> result=new ArrayList<String>();
		if(str.equals("")||str==null){
			return result;
		}
		str=tagger.tagString(str);
		String[] strArray=str.split(" ");
		
		String []tempArray=new String[2];
		//System.out.println(str);
		for(int i=0;i<strArray.length;i++){
			tempArray=strArray[i].split("_");
			//System.out.println(tempArray.length);
			if(tempArray.length>=2&&(tempArray[1].contains("NN")||tempArray[1].contains("VB"))){
				result.add(tempArray[0]);
			}
		}
		return result;
		//then we need to process this str
		//String tagged = tagger.(str);
	}
	public static MaxentTagger getMaxentTagger(){
		return new MaxentTagger("taggers/english-bidirectional-distsim.tagger");
	}
	public static JiebaSegmenter getJiebaSegmenter(){
		return new JiebaSegmenter();
	}
	public static boolean isEnglish(String str){
		if(ChineseToUtf8.isIncludeChinese(str)){
			return false;
		}
		if(ChineseToUtf8.isIncludeEnglish(str)){
			return true;
		}
		else{
			return false;
		}
	}
	public static boolean isChinese(String str){
		if(ChineseToUtf8.isIncludeChinese(str)){
			return true;
		}
		return false;
	}
	public static void main(String args[]) throws Exception{
		ArrayList<String>test=null;
		//test.add("test");
		convertKeyWord(test);
	}
	/*
	public static void main(String args[]) throws Exception{
		//String str = new String("暗示大家".getBytes(),"UTF-8");
//		String str="测试";
		//System.out.println(ExtractKeyWords.isAllChinese("是"));
		
		DebugFunctions.showArray(ExtractKeyWords.extractKeyWords("lol is test 我的 好人",
						ExtractKeyWords.getJiebaSegmenter(),
						ExtractKeyWords.getMaxentTagger()));
		System.out.println(ExtractKeyWords.isMatchHbaseRequirement("lol"));
		
	} */
}
