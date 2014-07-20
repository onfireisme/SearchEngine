package crawler.parse;

import crawler.Database.Database;

public class ParseUrlThread implements Runnable {
	private String url;

	ParseUrlThread(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		System.out.println("lol");
		// TODO Auto-generated method stub
		// 分别是三个函数
		// 第一个函数，根据url从web中读取页面
		// 第二个函数，解析page
		// 第三个函数，存入Purl 表
		try {
			SaveParsedUrlToHbase.saveParsedUrl(
					ParsePage.parsePage(
							ParseModuleReadFromHbase.getPageFromHbase(this.url)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/*
	public static void main(String args[]) throws Exception{
		
		Database.showAllRecord("Purl");
		//ParseUrlThread temp=new ParseUrlThread("www.msn.com");
		//temp.run();
	}
	
	public static void main(String args[]) throws Exception{
		Database.showAllRecord("Purl");
		//new ParseUrlThread("www.msn.com");
		//Database.showAllRecord("Purl");
		//new 
	}
	*/

}
