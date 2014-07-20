package crawler.Download;

import crawler.Database.Database;
import crawler.others.CrawlerConfiguration;

public class SaveHtmlToHbase {
	public static void saveHtmlToHbase(int tableIndex, byte[] page, String url)
			throws Exception {
		// 第一，存入web,存入rowkey和data
		Database.addRecord(CrawlerConfiguration.WebTableName, url,
				CrawlerConfiguration.WebTableContentFamily,
				CrawlerConfiguration.WebTablePageQualifier, page);
		// 注意，下面两步过于麻烦，如果每次都是要单独写一行进去，并且这一行数据量如此之小
		// 可以考虑，在所有线程运行完毕后，再一起把数据存储起来。但是，这又会导致一个问题
		// 就是万一线程卡起来，或者有一个线程始终无法结束。还是先这样吧，不搞这么复杂的东西了，先这样运行一下看看

		// 第二，存入url
		Database.addRecord(CrawlerConfiguration.UrlTableName, url,
				CrawlerConfiguration.DownloadStatusFamilyName,
				CrawlerConfiguration.DownloadStatusQualifier,
				CrawlerConfiguration.DownloadedStatus);
		// 第三，存入watingParse table
		Database.addRecord(CrawlerConfiguration.WaitingParseUrlTableName+String.valueOf(tableIndex),
				url,
				CrawlerConfiguration.TempFamilyName,
				CrawlerConfiguration.TempQualifierName,
				CrawlerConfiguration.TempValue);
	}
}
