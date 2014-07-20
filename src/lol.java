import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class lol {
	public int mytest;
	public static void test(int a){
		a=2;
	}
	public void mytest(lol mylol){
		mylol.mytest=3;
	}
    	
    	/*
    	BufferedReader br = new BufferedReader(new FileReader("/home/wangyan/url.txt"));
    	String[] strArray2=new String[200] ;
    	String data = br.readLine();//一次读入一行，直到读入null为文件结束  
        //StringBuffer result= new StringBuffer("");
        String str;
        String[] strArray;
        int i=0;
    	while( data!=null){  
    	      str=data.toString();
    	      strArray=str.split("\\s+");
    	      if(strArray.length>=1){
        	      strArray2[i]="www."+strArray[1];
        	      i++;
    	      }
    	     data = br.readLine(); //接着读下一行  
    	}  
	    //System.out.print(result.toString());
    	br.close();
    	String test="test";
    	FileWriter writer = new FileWriter("/home/wangyan/android/test.txt");
        //BufferedWriter bw = new BufferedWriter(writer);
        for(int j=0;j<160;j++){
        	writer.write(strArray2[j]);
        	writer.write("\n");
        }
    	writer.close();
        //bw.write(result.toString());
    } 
    */
}
