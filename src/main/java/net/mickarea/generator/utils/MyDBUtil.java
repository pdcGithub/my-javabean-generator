/******************************************************************************************************

This file "DBUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import net.mickarea.generator.models.SimpleDBData;

/**
 * &gt;&gt;&nbsp;关于数据库操作的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日-2024年6月15日
 */
public final class MyDBUtil {

	/**
	 * 私有构造函数，防止被 new 创建对象
	 */
	private MyDBUtil() {
		// TODO Auto-generated constructor stub
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
	private static final void loadSimpleDBData(SimpleDBData sdb, ResultSet rs) throws Exception {
		
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
					tmpDatas.add(translateDictInfoType(rs, i));
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
	
	/**
	 * 根据从 JDBC 获取的原始数据对象，做一些类型转换，方便后续处理
	 * @param rs 结果集对象
	 * @param colIndex 列号 从 1 开始
	 * @return 转换后的对象
	 * @throws Exception 如果执行异常，直接抛出错误。
	 */
	private static final Object translateDictInfoType(ResultSet rs, int colIndex) throws Exception {
		
		//获取原始的数据对象
		Object result = rs.getObject(colIndex);
		if(result==null) {
			//如果为空，则直接返回。
			return result;
		}
		
		//2024-4-5    如果对象是数字类型，比如 BigInteger, Integer , Long , Double 之类的，则需要转换为 BigDecimal
		//          这么处理主要是为了方便 java bean 在修改属性类型时，转换用
		if(result instanceof Number) {
			result = rs.getBigDecimal(colIndex);
		}
		//对于oracle的TIMESTAMP类型，一律转为 java.sql.Timestamp
		if(result instanceof oracle.sql.TIMESTAMP) {
			result = rs.getTimestamp(colIndex);
		}
		//对于 oracle 的 ROWID 类型，一律转换为 String 类型
		if(result instanceof oracle.sql.ROWID) {
			result = ((oracle.sql.ROWID)result).stringValue();
		}
		//对于 oracle 的 OracleClob 类型，一律转换为 String 类型
		if(result instanceof oracle.jdbc.OracleClob) {
			result = ((oracle.sql.CLOB)result).stringValue();
		}
		//对于 oracle 的 OracleNClob 类型，一律转换为 String 类型
		if(result instanceof oracle.jdbc.OracleNClob) {
			result = ((oracle.sql.NCLOB)result).stringValue();
		}
		//对于 oracle 的 OracleBlob 类型，一律转换为 byte[] 类型
		if(result instanceof oracle.jdbc.OracleBlob) {
			result = ((oracle.sql.BLOB)result).getBytes();
		}
		
		//处理完毕，放入临时变量
		return result;
	}
}
