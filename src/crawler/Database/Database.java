package crawler.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.regionserver.HRegion.RowLock;
import org.apache.hadoop.hbase.util.Bytes;
import crawler.others.*;

public class Database {
	private static Configuration conf = null;
	/*
	 * intial the basic setup
	 */
	static {
		conf = HBaseConfiguration.create();
		// Configuration HBASE_CONFIG = new Configuration();
		/*
		 * //与hbase/conf/hbase-site.xml中hbase.zookeeper.quorum配置的值相同
		 * HBASE_CONFIG.set("hbase.zookeeper.quorum", "10.1.1.1");
		 * //与hbase/conf/
		 * hbase-site.xml中hbase.zookeeper.property.clientPort配置的值相同
		 * HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", "2181"); conf
		 * = HBaseConfiguration.create(HBASE_CONFIG);
		 */
	}
	public static Configuration getConf(){
		return conf;
	}
	/*
	 * create table
	 */
	public static void creatTable(String tableName, String[] familys)
			throws Exception {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println(tableName+"table already exists!");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			for (int i = 0; i < familys.length; i++) {
				tableDesc.addFamily(new HColumnDescriptor(familys[i]));
			}
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
		}
		admin.close();
	}

	public static void creatTable(String tableName) throws Exception {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (admin.tableExists(tableName)) {
			System.out.println("table already exists!");
		} else {
			HTable table = new HTable(conf, tableName);
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			admin.createTable(tableDesc);
			System.out.println("create table " + tableName + " ok.");
			table.close();
		}
		admin.close();
	}

	/**
	 * 删除表
	 */
	public static void deleteTable(String tableName) throws Exception {
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println("delete table " + tableName + " ok.");
			}
			else{
				System.out.println("table does not exist!");
			}
			admin.close();
		} catch (MasterNotRunningException e) {
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 添加表的family
	 */
	public static void addFamily(String tableName, String family)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		HColumnDescriptor des = new HColumnDescriptor(family);
		admin.addColumn(tableName, des);
		admin.enableTable(tableName);
		admin.close();
	}

	/*
	 * 修改表的family 这个函数有问题，先到这里吧。
	 */
	/*
	 * public static void alterFaimly(String tableName,String family) throws
	 * IOException{ HBaseAdmin admin = new HBaseAdmin(conf);
	 * admin.disableTable(tableName); /* 这里碰到了一个大问题，就是我发现，是有直接修改table的函数
	 * 不过这个HcolumnDescriptor要如何写？？不会。。所以，我的方法是，删除后重新定义一个 HColumnDescriptor des=
	 * new HColumnDescriptor(family); admin.modifyColumn(tableName, des);
	 */
	/*
	 * admin.enableTable(tableName); admin.close(); }
	 */
	/*
	 * 删除表的family
	 */
	public static void delFamily(String tableName, String family)
			throws IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		admin.disableTable(tableName);
		admin.deleteColumn(tableName, family);
		admin.enableTable(tableName);
		admin.close();
	}

	/**
	 * 插入一行记录 qualifier equal to a column of a family
	 */
	public static void addRecord(String tableName, String rowKey,
			String family, String qualifier, String value) throws Exception {
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					Bytes.toBytes(value));
			table.put(put);
			System.out.println("insert recored " + rowKey + " to table "
					+ tableName + " ok.");
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//加一个多态吧
	public static void addRecord(String tableName, String rowKey,
			String family, String qualifier, byte[] value) throws Exception {
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier),
					value);
			table.put(put);
			System.out.println("insert recored " + rowKey + " to table "
					+ tableName + " ok.");
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 插入多列数据
	 */
	public static void addMultiColumnRecord(String tableName,String rowKey,
			String family,ArrayList<String>qualifierArray,ArrayList<String>valueArray){
		try {
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			if(qualifierArray.size()==0){
				table.close();
				return;
			}
			for(int i=0;i<qualifierArray.size();i++){
				put.add(Bytes.toBytes(family), Bytes.toBytes(qualifierArray.get(i)),
						Bytes.toBytes(valueArray.get(i)));
			}
			table.put(put);
			System.out.println("insert recored " + rowKey + " to table "
					+ tableName + " ok.");
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 插入多行记录 第一，插入的多行，其family和qualifier都是相同的
	 * 第二，是否要直接传入一个put的List，这样就不用传两个arrayList了，问题是put的List有点太大了。
	 */
	public static void addMultiRecords(String tableName,
			ArrayList<String> rowKeyArray, String family, String qualifier,
			ArrayList<String> valueArray) throws Exception {
		// rowkey一定要跟value相互对应，不然这个就没法玩了。
		HTable table = new HTable(conf, tableName);
		if (rowKeyArray.size() != valueArray.size()) {
			System.out
					.println("there is something wrong,the size of the rowKeyArray doesn't equal "
							+ "to valueArray ");
			table.close();
			return;
		}
		byte[] familyBytes = Bytes.toBytes(family);
		byte[] qualifierBytes = Bytes.toBytes(qualifier);
		ArrayList<Put> putArray = new ArrayList<Put>();
		for (int i = 0; i < rowKeyArray.size(); i++) {
			Put put = new Put(Bytes.toBytes(rowKeyArray.get(i)));
			put.add(familyBytes, qualifierBytes,
					Bytes.toBytes(valueArray.get(i)));
			putArray.add(put);
			if (i % (CrawlerConfiguration.AccumulateNumber) == 0) {
				table.put(putArray);
				putArray.clear();
			}
		}
		if (putArray.size() != 0) {
			table.put(putArray);
			putArray.clear();
		}
		System.out.println("insert multi record finished");
		table.close();
	}
	/*
	 * 由于不能之传入rowkey,所以这个函数是没有用的。。坑爹呀。。 在传rowkey的时候，必须要传入相关的数据
	 */
	/*
	 * //这种情况是只往表里面插入rowKey的情况。 public static void addRecord (String tableName,
	 * String rowKey) throws Exception{ try { HTable table = new HTable(conf,
	 * tableName); Put put = new Put(Bytes.toBytes(rowKey));
	 * //put.add(Bytes.toBytes
	 * (family),Bytes.toBytes(qualifier),Bytes.toBytes(value)); table.put(put);
	 * System.out.println("insert recored " + rowKey + " to table " + tableName
	 * +" ok."); table.close(); } catch (IOException e) { e.printStackTrace(); }
	 * }
	 */
	/**
	 * 删除一行记录
	 */
	public static void delRecord(String tableName, String rowKey)
			throws IOException {
		HTable table = new HTable(conf, tableName);
		List<Delete> list = new ArrayList<Delete>();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
		System.out.println("del recored " + rowKey + " ok.");
		table.close();
	}
	/*
	 * 删除多行记录
	 */
	public static void delMultiRecords(String tableName,
			ArrayList<String> keyRowArray) throws IOException {
		HTable table = new HTable(conf, tableName);
		List<Delete> deleteList = new ArrayList<Delete>();
		for (int i = 0; i < keyRowArray.size(); i++) {
			Delete delete = new Delete(Bytes.toBytes(keyRowArray.get(i)));
			deleteList.add(delete);
			if (i % CrawlerConfiguration.MaxDeleteNumber == 0) {
				table.delete(deleteList);
				deleteList.clear();
			}
		}
		if (deleteList.size() != 0) {
			table.delete(deleteList);
			deleteList.clear();
		}
		table.close();
	}
	/**
	 * 查找一行记录
	 */
	public static Result getOneRecord(String tableName, String rowKey)
			throws IOException {
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		Result rs = table.get(get);
		if(rs.isEmpty()){
			rs=null;
		}
		/*
		Cell[] cell = rs.rawCells();
		for (int j = 0; j < cell.length; j++) {
			System.out.print("rowkey is:"
					+ new String(CellUtil.cloneRow(cell[j])) + ";");
			System.out.print("family:"
					+ new String(CellUtil.cloneFamily(cell[j])) + ";");
			System.out.print("qualifier:"
					+ new String(CellUtil.cloneQualifier(cell[j])) + ";");
			System.out.println("value is:"
					+ new String(CellUtil.cloneValue(cell[j])) + ";");
			System.out.print("time stamp is:" + cell[j].getTimestamp());
		}
		*/
		
		table.close();
		return rs;
	}

	/*
	 * 这个函数现在暂时用不到。
	 */
	public static void getRecordNumber(String tableName) throws IOException {

	}
	/*
	 * 获取制定的列的值，需要指定table name,family name,qualifier name
	 * 可以选择，是否返回指定的行数，如果不加上，则默认返回所有的行数
	 */
	public static ArrayList<String>getSpecificQualifierRows(String tableName,
			int beginPosition,int endPosition,String familyName,String qualifier) throws IOException{
		ArrayList<String> result=new ArrayList<String>();
		ArrayList<Result> resultArray=getSpecificRows(tableName, beginPosition, endPosition);
		Cell cell;
		for(int i=0;i<resultArray.size();i++){
			cell=resultArray.get(i).getColumnLatestCell(Bytes.toBytes(familyName), 
					Bytes.toBytes(qualifier));
			result.add(new String(CellUtil.cloneValue(cell)));
		}
		return result;
	}
	public static ArrayList<String>getSpecificQualifierRows(String tableName,
			String familyName,String qualifier) throws IOException{
		ArrayList<String> result=new ArrayList<String>();
		ArrayList<Result> resultArray=getAllRows(tableName);
		Cell cell;
		for(int i=0;i<resultArray.size();i++){
			cell=resultArray.get(i).getColumnLatestCell(Bytes.toBytes(familyName), 
					Bytes.toBytes(qualifier));
			result.add(new String(CellUtil.cloneValue(cell)));
		}
		return result;
	}
	/*
	 * 获取指定qualifier和row的key值
	 */
	public static String getSpecificRowColumn(String tableName,String rowKey,
			String familyName,String qualifier) throws IOException{
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		Result result = table.get(get);
		if(result.isEmpty()){
			//System.out.println("lol");
			table.close();
			return null;
		}
		Cell cell=result.getColumnLatestCell(Bytes.toBytes(familyName), 
				Bytes.toBytes(qualifier));
		table.close();
		return new String(CellUtil.cloneValue(cell));
	}
	public static byte[] getSpecificRowColumnByByte(String tableName,String rowKey,
			String familyName,String qualifier) throws IOException{
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		Result result = table.get(get);
		if(result.isEmpty()){
			//System.out.println("lol");
			table.close();
			return null;
		}
		Cell cell=result.getColumnLatestCell(Bytes.toBytes(familyName), 
				Bytes.toBytes(qualifier));
		table.close();
		return CellUtil.cloneValue(cell);
	}
	/*
	 * 这里专门使用这个函数的原因，是我要经常读取这个数据
	 */
	public static ArrayList<String> getSpecificRowKeys(
			String tableName,int beginPosition,int endPosition) throws IOException{
		ArrayList<Result>resultArray=getSpecificRows(tableName, beginPosition, endPosition);
		ArrayList<String> url=new ArrayList<String>();
		Cell[] cell; 
		String urlString;
		for(int i=0;i<resultArray.size();i++){
			cell=resultArray.get(i).rawCells();
			urlString=Bytes.toString(CellUtil.cloneRow(cell[0]));
			url.add(urlString);
		}
		return url;
	}
	/*
	 * 获取指定数目的前N行，返回值是Result类型，这个是难免的了。
	 */
	public static ArrayList<Result> getSpecificRows(String tableName,
			int beginPosition, int endPosition) throws IOException {
		HTable table = new HTable(conf, tableName);
		Scan s = new Scan();
		s.setBatch(CrawlerConfiguration.MaxRowsGetFromTable);
		ResultScanner ss = table.getScanner(s);
		int i = 0;
		ArrayList<Result> resultArray = new ArrayList<Result>();
		for (Result r : ss) {
			if (i >= beginPosition) {
				resultArray.add(r);
			}
			if (i == endPosition) {
				break;
			}
			i++;
		}
		table.close();
		return resultArray;
	}
	/*
	 * 获取所有的数据，不过不建议这么做，太容易内存溢出
	 */
	public static ArrayList<Result> getAllRows(String tableName) throws IOException{
		HTable table = new HTable(conf, tableName);
		Scan s = new Scan();
		s.setBatch(CrawlerConfiguration.MaxRowsGetFromTable);
		ResultScanner ss = table.getScanner(s);
		ArrayList<Result> resultArray = new ArrayList<Result>();
		int i=0;
		for (Result r : ss) {
			resultArray.add(r);
			i++;
			if(i==CrawlerConfiguration.MaxRow){
				System.out.println("you get too much rows one time,more than 2000 rows!!");
			}
		}
		table.close();
		return resultArray;
	}
	/*
	 * 现实所有的table
	 */
	public static void showAllTable() throws MasterNotRunningException, ZooKeeperConnectionException, IOException{
		HBaseAdmin admin = new HBaseAdmin(conf);
		String[]temp=admin.getTableNames();
		for(int i=0;i<temp.length;i++){
			System.out.println(temp[i]);
		}
		admin.close();
	}
	/**
	 * 显示所有数据
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 * @throws MasterNotRunningException 
	 */
	public static void showAllRecord(String tableName) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		if (!admin.tableExists(tableName)) {
			System.out.println("table already does not exist!");
			admin.close();
			return;
		}
		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
			for (Result r : ss) {
				Cell[] cell = r.rawCells();
				for (int j = 0; j < cell.length; j++) {
					System.out.print("rowkey is:"
							+ new String(CellUtil.cloneRow(cell[j])) + ";");
					System.out.print("family:"
							+ new String(CellUtil.cloneFamily(cell[j])) + ";");
					System.out.print("qualifier:"
							+ new String(CellUtil.cloneQualifier(cell[j]))
							+ ";");
					System.out.println("value is:"
							+ new String(CellUtil.cloneValue(cell[j])) + ";");
					System.out.print("time stamp is:" + cell[j].getTimestamp());
				}
			}
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		admin.close();
	}
	/*
	 * 显示制定的行
	 */
	public static void showSpecificRows(String tableName, int beginPosition,
			int endPosition) {
		try {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
			int i = 0;
			for (Result r : ss) {
				if (i >= beginPosition) {
					Cell[] cell = r.rawCells();
					for (int j = 0; j < cell.length; j++) {
						System.out.print("rowkey is:"
								+ new String(CellUtil.cloneRow(cell[j])) + ";");
						System.out.print("family:"
								+ new String(CellUtil.cloneFamily(cell[j]))
								+ ";");
						System.out.print("qualifier:"
								+ new String(CellUtil.cloneQualifier(cell[j]))
								+ ";");
						System.out.println("value is:"
								+ new String(CellUtil.cloneValue(cell[j]))
								+ ";");
						System.out.print("time stamp is:"
								+ cell[j].getTimestamp());
					}
				}
				if (i == endPosition) {
					break;
				}
				i++;
			}
			table.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 显示所有的列族
	 */
	public static void showAllFamily(String tableName)
			throws TableNotFoundException, IOException {
		HBaseAdmin admin = new HBaseAdmin(conf);
		HTableDescriptor Tdes = admin.getTableDescriptor(Bytes
				.toBytes(tableName));
		HColumnDescriptor[] Cdes = Tdes.getColumnFamilies();
		for (int i = 0; i < Cdes.length; i++) {
			System.out.println(Cdes[i].getNameAsString());
		}
		admin.close();
	}

	/*
	 * 给定一个result数组，显示这个result数组
	 */
	public static void showResultArray(ArrayList<Result> resultArray) {
		for (int i = 0; i < resultArray.size(); i++) {
			Cell[] cell = resultArray.get(i).rawCells();
			for (int j = 0; j < cell.length; j++) {
				System.out.print("rowkey is:"
						+ new String(CellUtil.cloneRow(cell[j])) + ";");
				System.out.print("family:"
						+ new String(CellUtil.cloneFamily(cell[j])) + ";");
				System.out.print("qualifier:"
						+ new String(CellUtil.cloneQualifier(cell[j])) + ";");
				System.out.println("value is:"
						+ new String(CellUtil.cloneValue(cell[j])) + ";");
				System.out.print("time stamp is:" + cell[j].getTimestamp());
			}
		}
	}

	/*
	 * 获取第一行的数据 第一种方法，使用HTable带的函数来实现这个功能，问题是，我发现它返回的数据无法读取呀！不知道为什么
	 * 第二种方法，使用scan，只读取前1行。实现这个功能。
	 */
	public static String getStartRowKey(String tableName) throws IOException {
		/*
		 * 为什么它什么都没有返回？？获取的数据为null，无法显示，这也太坑爹了吧。 HTable table = new HTable(conf,
		 * tableName); byte[][] startKeyByteArray=table.getStartKeys(); String
		 * [] startKeyStringArray=new String[startKeyByteArray.length]; for(int
		 * i=0;i<startKeyByteArray.length;i++){ startKeyStringArray[i]=new
		 * String(startKeyByteArray[i]); } table.close();
		 */
		// 方法二，使用scan只读取一行
		HTable table = new HTable(conf, tableName);
		Scan s = new Scan();
		// s.setCaching((CrawlerConfiguration.MaxRowsGetFromTable);
		ResultScanner ss = table.getScanner(s);
		Result firstRow = ss.next();
		String startRowKey = new String(firstRow.getRow());
		ss.close();
		table.close();
		return startRowKey;
	}

	/*
	 * 判断表是否为空
	 */
	public static boolean isTableEmpty(String tableName) throws IOException {
		List<Result>resultArray=Database.getSpecificRows(tableName, 0, 2);
		if(resultArray.size()==0){
			return true;
		}
		else{
			return false;
		}
	}
}
