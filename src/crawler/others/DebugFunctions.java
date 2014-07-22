package crawler.others;

import java.util.ArrayList;

public class DebugFunctions {
	public static void showArray(ArrayList<String> array){
		if(array==null){
			return;
		}
		for(int i=0;i<array.size();i++){
			System.out.println(array.get(i));
		}
	}
}
