package crawler.imageDownload;

import java.io.IOException;


public class DownloadImageThread implements Runnable {
	private String imageUrl;
	DownloadImageThread(String url){
		this.imageUrl=url;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			SaveImageToHbase.saveImageToHbase(
					DownloadImage.getImageByUrl(this.imageUrl), this.imageUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
