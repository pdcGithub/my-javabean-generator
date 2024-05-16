/******************************************************************************************************

This file "DBUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mickarea.generator.beans.TabOrViewTmpObj;
import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.DatabasePoolManager;
import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.SimpleDBData;
import net.mickarea.generator.models.SqlResult;
import net.mickarea.generator.models.ValidResult;
import net.mickarea.generator.opts.MyWriter;

/**
 * &gt;&gt;&nbsp;关于数据库操作的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日
 */
public final class MyDBUtil {

	/**
	 * 私有构造函数，防止被 new 创建对象
	 */
	private MyDBUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;数据库连接测试
	 * @param cArgs 命令行传来的参数
	 * @return SqlResult 执行结果对象
	 */
	public static final SqlResult testDBConnection(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		try {
			//参数校验（暂时只支持 mysql8+ 数据库）
			if(cArgs==null) throw new Exception("the argument from command-line is empty ");
			if(!cArgs.getDatabaseType().equalsIgnoreCase("mysql")) throw new Exception("database["+cArgs.getDatabaseType()+"] is supported");
			//创建连接池对象
			mana = DatabasePoolManager.getInstance(cArgs);
			//执行数据库查询
			SimpleDBData sdb = MyDBUtil.mySqlQuery(mana.getDataPoolByName(cArgs.getPoolName()), "select 1 ", null);
			//根据状态处理
			if(sdb.getResponseStatus().equalsIgnoreCase(SimpleDBData.ERR)) {
				throw new Exception(sdb.getResponseInfo());
			}else if(sdb.getData()!=null && sdb.getData().length>0) {
				result.setOk(true);
			}else {
				throw new Exception("something wrong while connecting db");
			}
		}catch(Exception e) {
			result.setOk(false);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setMessage(String.format("DB Exception=> SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				result.setMessage(e.getMessage());
			}
			//增加日志输出
			MyStrUtil.mylogger.error(e.getMessage(), e);
		}finally {
			//资源释放
			if(mana!=null){
				mana.destroyDataPools();
				mana=null;
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;从数据库查询一些表或者视图信息，返回给调用者
	 * @param cArgs 命令行传来的参数
	 * @return SqlResult 执行结果对象
	 */
	public static final SqlResult getDatabaseObjects(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		try {
			//参数校验（暂时只支持 mysql8+ 数据库）
			if(cArgs==null) throw new Exception("the argument from command-line is empty ");
			if(!cArgs.getDatabaseType().equalsIgnoreCase("mysql")) throw new Exception("database["+cArgs.getDatabaseType()+"] not supported");
			//创建连接池对象
			mana = DatabasePoolManager.getInstance(cArgs);
			//执行数据库查询
			String preSql = "select table_name  from information_schema.tables where table_schema = ? order by table_name";
			List<Object> preSqlParams = Arrays.asList(cArgs.getSchema());
			//执行结果
			SimpleDBData sdb = MyDBUtil.mySqlQuery(mana.getDataPoolByName(cArgs.getPoolName()), preSql, preSqlParams);
			//根据状态处理
			if(sdb.getResponseStatus().equalsIgnoreCase(SimpleDBData.ERR)) {
				throw new Exception(sdb.getResponseInfo());
			}else if(sdb.getData()!=null && sdb.getData().length>0) {
				//临时结果
				List<String> tmpDatas = new ArrayList<String>();
				for(int i=0;i<sdb.getData().length;i++) {
					tmpDatas.add(sdb.getData()[i][0].toString().toUpperCase());
				}
				//将数据转换为字符串
				ObjectMapper mapper = new ObjectMapper();
				result.setOk(true);
				result.setMessage("");
				result.setData(mapper.writeValueAsString(tmpDatas));
			}else {
				throw new Exception("nothing was returned by database");
			}
			
		}catch(Exception e) {
			result.setOk(false);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setMessage(String.format("DB Exception=> SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				result.setMessage(e.getMessage());
			}
			//增加日志输出
			MyStrUtil.mylogger.error(e.getMessage(), e);
		}finally {
			//资源释放
			if(mana!=null){
				mana.destroyDataPools();
				mana=null;
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;实体类生成处理方法
	 * @param cArgs
	 * @return
	 */
	public static final SqlResult genJavaEntityFiles(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		try {
			//参数校验（暂时只支持 mysql8+ 数据库）
			if(cArgs==null) throw new Exception("the argument from command-line is empty ");
			if(!cArgs.getDatabaseType().equalsIgnoreCase("mysql")) throw new Exception("database["+cArgs.getDatabaseType()+"] not supported");
			//判断是 表、视图，或者是 sql 语句
			String actionType = cArgs.getActionType();
			//字符集
			String charSet = cArgs.getCharSet();
			//
			String sql = cArgs.getObjOrSql();
			String tableNames = cArgs.getObjOrSql();
			//文件存放路径
			String fileDir = cArgs.getFileDir();
			//创建连接池对象
			mana = DatabasePoolManager.getInstance(cArgs);
			//开始执行
			List<GenResult> genResult = null;
			if(actionType.equalsIgnoreCase("object")) {
				genResult = dbObjectToJavaEntity(mana.getDataPoolByName(cArgs.getPoolName()), cArgs.getSchema(), tableNames, charSet, fileDir);
			}else if(actionType.equalsIgnoreCase("sql")) {
				genResult = dbSqlToJavaEntity(mana.getDataPoolByName(cArgs.getPoolName()), sql, charSet, fileDir);
			}else {
				throw new Exception("action type not supported");
			}
			//根据结果，返回信息
			if(genResult!=null && genResult.size()>0) {
				ObjectMapper mapper = new ObjectMapper();
				result.setOk(true);
				result.setMessage("");
				result.setData(mapper.writeValueAsString(genResult));
			}else {
				throw new Exception("nothing was generated");
			}
		}catch(Exception e) {
			result.setOk(false);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setMessage(String.format("DB Exception=> SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				result.setMessage(e.getMessage());
			}
			//增加日志输出
			MyStrUtil.mylogger.error(e.getMessage(), e);
		}finally {
			//资源释放
			if(mana!=null){
				mana.destroyDataPools();
				mana=null;
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;将数据库表或者视图，转换为实体对象 java 类文件
	 * @param ds 数据库源
	 * @param schema 数据库实例名
	 * @param tableNames 表或者视图的字符串
	 * @param charSet 文件的字符集
	 * @param fileDir 文件所在的文件夹
	 * @return List<GenResult> 生成的Java 实体类的相关信息
	 * @throws Exception 一些异常
	 */
	private static final List<GenResult> dbObjectToJavaEntity(DataSource ds, String schema, String tableNames, String charSet, String fileDir) throws Exception {
		List<GenResult> resultList = new ArrayList<GenResult>();
		String[] tableNameArray = tableNames.split(",");
		String preSql = "select column_name, ordinal_position, data_type, column_type,column_key, extra, column_comment "
				+ "from information_schema.columns where table_schema=? and table_name=? order by ordinal_position";
		List<Object> params = new ArrayList<Object>();
		//开始循环处理
		for(String tabName : tableNameArray) {
			params.clear();
			params.add(schema);
			params.add(tabName);
			//执行
			SimpleDBData sdb = mySqlQuery(ds, preSql, params);
			if(sdb.getResponseStatus().equals(SimpleDBData.ERR)) {
				resultList.add(new GenResult(tabName, "", false, sdb.getResponseInfo(), ""));
				//报错则跳过
				continue;
			}
			if(sdb.getResponseStatus().equals(SimpleDBData.OK) && (sdb.getData()==null || sdb.getData().length<=0)) {
				resultList.add(new GenResult(tabName, "", false, "cannot find anything of this table", ""));
				//没内容则跳过
				continue;
			}
			//将执行的数据库字典信息，封装
			List<TabOrViewTmpObj> tmpObjs = new ArrayList<TabOrViewTmpObj>();
			for(int i=0;i<sdb.getData().length;i++) {
				TabOrViewTmpObj t = new TabOrViewTmpObj();
				t.setColumnName(sdb.getData()[i][0].toString());
				t.setOrdinalPosition(new Long(sdb.getData()[i][1].toString()));
				t.setDataType(sdb.getData()[i][2].toString());
				t.setColumnType(sdb.getData()[i][3].toString());
				t.setColumnKey(sdb.getData()[i][4].toString());
				t.setExtra(sdb.getData()[i][5].toString());
				t.setColumnComment(sdb.getData()[i][6].toString());
			}
			//再查询2个信息，java 属性名 和 java 数据类型
			String preSql2 = "select * from "+schema+"."+tabName+" where 1=2";
			SimpleDBData sdb2 = mySqlQuery(ds, preSql2, null);
			//上面已经校验过 表或者视图 是否存在，这里就不校验了。
			List<String> colClassNames = sdb2.getColumnClassName();
			List<String> colNames = sdb2.getColumnName();
			//开始补充信息
			for(int i=0;i<tmpObjs.size();i++) {
				if(tmpObjs.get(i).getColumnName().equalsIgnoreCase(colNames.get(i))) {
					//
					tmpObjs.get(i).setPropertyType(colClassNames.get(i).replaceFirst("java\\.lang\\.", ""));
					tmpObjs.get(i).setPropertyName(MyStrUtil.genNameFromColumn(colNames.get(i)));
				}
			}
			//根据表名，生成一个 Java 类名
			String entityName = MyStrUtil.genNameFromColumn(tabName);
			//根据文件夹，生成一个 Java 文件路径
			String entityFilePath = fileDir + File.separator + entityName + ".java";
			String fileName = entityName + ".java";
			//开始构造文件
			if(tmpObjs==null || tmpObjs.size()<=0) {
				resultList.add(new GenResult(tabName, entityName, false, "cannot found any dic info of table", ""));
				//异常则跳过
				continue;
			}else {
				//构造内容
				String header = MyWriter.writeFileHeader(entityName);
				String imports = MyWriter.writePackageAndImport();
				String codes = MyWriter.writeTableCode(entityName, tabName, tmpObjs);
				StringBuffer sb = new StringBuffer();
				sb.append(header);
				sb.append(imports);
				sb.append(codes);
				//写文件
				ValidResult myRe = MyFileUtil.saveToLocalpath(sb.toString(), fileDir, fileName, charSet);
				if(myRe.getValid()) {
					resultList.add(new GenResult(tabName, entityName, true, "", entityFilePath));
				}else {
					resultList.add(new GenResult(tabName, entityName, false, myRe.getMessage(), entityFilePath));
				}
			}
		}
		return resultList;
	}
	
	/**
	 * &gt;&gt;&nbsp;将sql语句，转换为实体对象 java 类文件
	 * @param ds 数据库源
	 * @param sql sql语句
	 * @param charSet 文件的字符集
	 * @param fileDir 文件所在的文件夹
	 * @return List<GenResult> 生成的Java 实体类的相关信息
	 * @throws Exception 一些异常
	 */
	private static final List<GenResult> dbSqlToJavaEntity(DataSource ds, String sql, String charSet, String fileDir) throws Exception {
		//List<GenResult> resultList = new ArrayList<GenResult>();
		throw new Exception("the generator of sql not done yet");
		//return resultList;
	}
	
	/**
	 * &gt;&gt;&nbsp;一个关于简易的sql查询处理方法
	 * @param ds DataSource 数据库对象
	 * @param preSql 要执行的预处理语句
	 * @param preSqlParams 要执行的语句 所对应的参数
	 * @return SimpleDBData 对象
	 * @throws Exception 一些异常
	 */
	public static final SimpleDBData mySqlQuery(DataSource ds, String preSql, List<Object> preSqlParams) throws Exception {
		//定义一个返回对象
		SimpleDBData sdb = new SimpleDBData();
		//数据库
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			//获取连接
			conn = ds.getConnection();
			//开始连接测试
			stmt = conn.prepareStatement(preSql);
			if(preSqlParams!=null && preSqlParams.size()>0) {
				for(int i=1;i<=preSqlParams.size();i++) {
					stmt.setObject(i, preSqlParams.get(i-1));
				}
			}
			//执行语句
			//调用SQL查询语句
			boolean hasResultSet = stmt.execute();
			if(hasResultSet) {
				rs = stmt.getResultSet();
				if(rs!=null) {
					//将 查找到的 结果集 封装到 SimpleDBData 对象中去
					loadSimpleDBData(sdb, rs);
				}
			} else {
				sdb.setResponseStatus(SimpleDBData.ERR);
				sdb.setResponseInfo("nothing return from sql");
			}
		}catch(Exception e) {
			//增加日志输出
			MyStrUtil.mylogger.error(e.getMessage(), e);
			throw e;
		}finally {
			//资源释放
			if(rs!=null) { try { rs.close();} catch (Exception e) {} rs=null;}
			if(stmt!=null) { try { stmt.close();} catch (Exception e) {} stmt=null;}
			if(conn!=null) { try { conn.close();} catch (Exception e) {} conn=null;}
		}
		return sdb;
	}
	
	/**
	 * &gt;&gt;&nbsp;将 查找到的 结果集 封装到 SimpleDBData 对象中去
	 * @param sdb 一个 SimpleDBData 对象。作为容器使用
	 * @param rs 数据库操作执行后，返回的结果集
	 */
	public static final void loadSimpleDBData(SimpleDBData sdb, ResultSet rs) throws Exception {
		
		if(sdb!=null && rs!=null) {
			
			ResultSetMetaData metaData = rs.getMetaData();
			
			int count = metaData.getColumnCount();
			
			//创建列名列表
			List<String> columns = new ArrayList<String>(count);
			List<String> columnsClass = new ArrayList<String>(count);
			List<String> columnsType = new ArrayList<String>(count);
			for(int i=1;i<=count;i++) {
				// metaData.getColumnName(i) 改为获取 display name 否则获取不到 别名
				columns.add(metaData.getColumnLabel(i));
				//如果数据库是 binary 或者 blog 之类的，类型信息会表述为 [B ，即 byte[] 数组
				columnsClass.add(metaData.getColumnClassName(i).equalsIgnoreCase("[B")?"byte[]":metaData.getColumnClassName(i));
				//
				columnsType.add(metaData.getColumnTypeName(i));
			}
			sdb.setColumnName(columns);
			sdb.setColumnClassName(columnsClass);
			sdb.setColumnTypeName(columnsType);
			
			//创建数据二维列表
			List<List<Object>> dataList = new ArrayList<List<Object>>();
			while(rs.next()) {
				List<Object> tmpDatas = new ArrayList<Object>();
				for(int i=1;i<=count;i++) {
					//数据需要获取Object类型才行，否则后面无法处理
					Object tmpObj = rs.getObject(i);
					//2024-4-5    如果对象是数字类型，比如 BigInteger, Integer , Long , Double 之类的，则需要转换为 BigDecimal
					//          这么处理主要是为了方便 java bean 在修改属性类型时，转换用
					if(tmpObj instanceof Number) {
						tmpObj = rs.getBigDecimal(i);
					}
					//处理完毕，放入临时变量
					tmpDatas.add(tmpObj);
				}
				dataList.add(tmpDatas);
			}
			
			//将二维列表转换为二维矩阵
			if(dataList.size()>0) {
				int rowCount = dataList.size();
				int columnCount = count;
				Object[][] obj = new Object[rowCount][columnCount];
				for(int i=0;i<rowCount;i++) {
					for(int j=0;j<columnCount;j++) {
						obj[i][j] = dataList.get(i).get(j);
					}
				}
				sdb.setData(obj);
				//清空临时变量
				dataList.clear();
			}
		}
	}
}
