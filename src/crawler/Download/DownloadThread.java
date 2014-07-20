package crawler.Download;

public class DownloadThread implements Runnable {
	private String url;
	private int tableIndex;
	DownloadThread(String url,int tableIndex){
		this.url=url;
		this.tableIndex=tableIndex;
	}
	@Override
	public void run() {
		byte[] result=DownloadPage.downloadPage(this.url);
		if(result!=null){
			try {
				SaveHtmlToHbase.saveHtmlToHbase(this.tableIndex, result, this.url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		// TODO Auto-generated method stub
		//first,download page.
		//如果下载成功，则存入hbase，否则结束线程
	}
	
}
