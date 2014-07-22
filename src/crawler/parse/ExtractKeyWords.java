package crawler.parse;
import crawler.others.ChineseToUtf8;
import crawler.others.DebugFunctions;


import java.util.ArrayList;
import java.util.List;


import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.Word;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ExtractKeyWords {
	//由于每次，加载词典都需要很长时间，所以，把segmenter对象传入是个不错的选择。
	public static ArrayList<String> getNounAndVerb(String sentence,JiebaSegmenter segmenter) {
		
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
	public static void taggerEnglishKeyWord(String str,MaxentTagger tagger){
		str ="test is lol";
		str=tagger.tagString(str);
		//then we need to process this str
		//String tagged = tagger.(str);
		System.out.println(str);
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
		//String str = new String("暗示大家".getBytes(),"UTF-8");
//		String str="测试";
		ExtractKeyWords.english();
		
	} /**/
}