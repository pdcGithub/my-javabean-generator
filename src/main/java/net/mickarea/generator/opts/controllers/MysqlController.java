/******************************************************************************************************

This file "MysqlController.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mickarea.generator.beans.TabOrViewTmpObj;
import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.DatabasePoolManager;
import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.SimpleDBData;
import net.mickarea.generator.models.SqlResult;
import net.mickarea.generator.opts.interfaces.IController;
import net.mickarea.generator.utils.MyDBUtil;
import net.mickarea.generator.utils.MyFileUtil;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 关于MySQL数据库的相关处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月11日-2024年6月15日
 */
public class MysqlController implements IController {

	/* (non-Javadoc)
	 * @see net.mickarea.generator.opts.interfaces.IController#testConnection(net.mickarea.generator.models.CommandArguments)
	 */
	@Override
	public SqlResult testConnection(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		try {
			//创建连接池对象
			mana = DatabasePoolManager.getInstance(cArgs);
			//执行数据库查询
			SimpleDBData sdb = MyDBUtil.mySqlQuery(mana.getDataPoolByName(cArgs.getPoolName()), "select 1 ", null);
			//根据状态处理
			if(sdb.getResponseStatus().equalsIgnoreCase(SimpleDBData.ERR)) {
				throw new Exception(sdb.getResponseInfo());
			}else if(sdb.getData()!=null && sdb.getData().length>0) {
				result.setOk(true);
				result.setData("database connnection is ok.");
			}else {
				throw new Exception("something wrong while connecting db.");
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

	/* (non-Javadoc)
	 * @see net.mickarea.generator.opts.interfaces.IController#searchDictInfo(net.mickarea.generator.models.CommandArguments)
	 */
	@Override
	public SqlResult searchDictInfo(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		try {
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
				throw new Exception("nothing was returned by database.");
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

	/* (non-Javadoc)
	 * @see net.mickarea.generator.opts.interfaces.IController#genJavaEntityFile(net.mickarea.generator.models.CommandArguments)
	 */
	@Override
	public SqlResult genJavaEntityFile(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		try {
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
				//如果是 通过 数据库对象（表、视图）生成实体，则执行这个方法
				genResult = dbObjectToJavaEntity(mana.getDataPoolByName(cArgs.getPoolName()), cArgs.getSchema(), tableNames, charSet, fileDir);
			}else if(actionType.equalsIgnoreCase("sql")) {
				//如果是通过sql 语句生成 实体，则执行这个方法。
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

	/* (non-Javadoc)
	 * @see net.mickarea.generator.opts.interfaces.IController#dbObjectToJavaEntity(javax.sql.DataSource, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<GenResult> dbObjectToJavaEntity(DataSource ds, String schemaName, String tableNames, String charSet, String fileDir) throws Exception {
		
		//返回的结果列表
		//数据库对象名字符串，拆分并且转大写表示、去重复（并行处理）
		return Arrays.asList(tableNames.split(","))
				.parallelStream()
				.filter(name-> !MyStrUtil.isEmptyString(name))
				.map(String::toUpperCase)
				.distinct()
				.collect(
						Collector.of(
								ArrayList<GenResult>::new, 
								(list, item)->{
									//业务处理（根据参数，生成文件）
									list.add(this.genMyResults(item, schemaName, ds, fileDir, charSet));
								}, 
								(left, right)->{
									//资源合并
									left.addAll(right);
									return left;
								}, 
								Collector.Characteristics.IDENTITY_FINISH)
				);
	}

	/* (non-Javadoc)
	 * @see net.mickarea.generator.opts.interfaces.IController#dbSqlToJavaEntity(javax.sql.DataSource, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<GenResult> dbSqlToJavaEntity(DataSource ds, String sql, String charSet, String fileDir) throws Exception {
		
		long start = System.currentTimeMillis();
		
		//返回的结果列表
		List<GenResult> resultList = new ArrayList<GenResult>();
		
		//时间戳
		String tmpTabName = "sql_"+System.currentTimeMillis();
		//执行查询
		SimpleDBData sdb = MyDBUtil.mySqlQuery(ds, sql, null);
		
		//由于一个sql语句，只能生成一个Java类，这里也就不需要循环了。
		//如果sql执行异常，则返回异常信息
		if(sdb.getResponseStatus().equalsIgnoreCase(SimpleDBData.ERR)) {
			resultList.add(new GenResult(tmpTabName, "", false, sdb.getResponseInfo(), "", System.currentTimeMillis()-start));
			return resultList;
		}
		
		//由于这里是一个sql语句，所以没有数据字典信息，直接填充一些内容即可
		List<TabOrViewTmpObj> tmpObjs = new ArrayList<TabOrViewTmpObj>();
		for(int i=0;i<sdb.getColumnName().size();i++) {
			TabOrViewTmpObj tmp = new TabOrViewTmpObj();
			tmp.setColumnName(sdb.getColumnName().get(i));
			tmp.setPropertyName(MyStrUtil.genNameFromColumn(tmp.getColumnName()));
			tmp.setPropertyType(sdb.getColumnClassName().get(i).replaceFirst("java\\.lang\\.", ""));
			tmp.setColumnComment("");
			tmp.setColumnKey("");
			tmp.setColumnType(sdb.getColumnTypeName().get(i));
			tmp.setExtra("");
			tmp.setDataType("");
			tmp.setOrdinalPosition(0L);
			//
			tmpObjs.add(tmp);
		}
		
		//Java bean 文件生成。
		resultList.add(MyFileUtil.genJavaBeanFileFromDictInfo(tmpObjs, tmpTabName, fileDir, charSet, System.currentTimeMillis()-start));
		
		return resultList;
	}
	
	/**
	 * 根据表名、模式名等等，分析数据字典；然后生成一个 Java bean 文件，并返回对应的 结果信息
	 * @param tabName 表、视图 名称
	 * @param schemaName 模式名称（如果是mysql 数据库，是数据库实例名；如果是oracle 数据库，是用户名）
	 * @param ds 数据源（一般为数据连接池）
	 * @param fileDir 文件生成后的存放文件夹
	 * @param charSet 文件生成后的字符集
	 * @return 关于文件生成的结果信息
	 */
	private GenResult genMyResults(String tabName, String schemaName, DataSource ds, String fileDir, String charSet) {
		
		GenResult result = null;
		
		long start = System.currentTimeMillis();
		
		try {
			//准备数据字典查询语句
			String preSql = "select column_name, ordinal_position, data_type, column_type,column_key, extra, column_comment "
					+ "from information_schema.columns where table_schema=? and table_name=? order by ordinal_position";
			List<Object> params = new ArrayList<Object>();
			
			params.clear();
			params.add(schemaName);
			params.add(tabName);
			
			//执行
			SimpleDBData sdb = MyDBUtil.mySqlQuery(ds, preSql, params);
			if(sdb.getResponseStatus().equals(SimpleDBData.ERR)) {
				result = new GenResult(tabName, "", false, sdb.getResponseInfo(), "", System.currentTimeMillis()-start);
				return result;
			}
			
			if(sdb.getResponseStatus().equals(SimpleDBData.OK) && (sdb.getData()==null || sdb.getData().length<=0)) {
				result = new GenResult(tabName, "", false, "cannot find anything of this table", "", System.currentTimeMillis()-start);
				return result;
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
				//
				tmpObjs.add(t);
			}
			//再查询2个信息，java 属性名 和 java 数据类型
			String preSql2 = "select * from "+schemaName+"."+tabName+" where 1=2";
			SimpleDBData sdb2 = MyDBUtil.mySqlQuery(ds, preSql2, null);
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
			
			//Java bean 文件生成。
			result = MyFileUtil.genJavaBeanFileFromDictInfo(tmpObjs, tabName, fileDir, charSet, System.currentTimeMillis()-start);
			
		}catch(Exception e) {
			MyStrUtil.mylogger.error("执行文件生成处理异常", e);
			result = new GenResult(tabName, "", false, "something was wrong. check log file for more details.", "", System.currentTimeMillis()-start);
		}
		
		return result;
	}
	
}
