package crawler.Download;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;

import crawler.Database.Database;

public class DownloadPage {
	public static byte [] downloadPage(String url){
		HttpClient httpClient = new HttpClient();
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		//GetMethod getMethod=new GetMethod("http://"+url);	
		GetMethod getMethod=new GetMethod(url);	 
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,5000);
		  //设置请求重试处理，用的是默认的重试处理：请求三次
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		          new DefaultHttpMethodRetryHandler());
		byte[] responseBody;
		try {
			int status = httpClient.executeMethod(getMethod);
			if (status >= 200 && status < 300) {
				responseBody = IOUtils.toByteArray(getMethod.getResponseBodyAsStream());
				return responseBody;
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //configuration of hadoop file system
		responseBody=null;
		return responseBody;
            
	}
	/*
	public static void main(String args[]) throws Exception { 
		String url="www.baidu.com";
		SaveHtmlToHbase.saveHtmlToHbase(1, DownloadPage.downloadPage(url), url);
		Database.showAllRecord("web");
		Database.showAllRecord("url");
		Database.showAllRecord("WPurl1");


	}
	*/
}
