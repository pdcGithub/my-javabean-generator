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

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mickarea.generator.beans.TabOrViewTmpObj;
import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.DatabasePoolManager;
import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.SimpleDBData;
import net.mickarea.generator.models.SqlResult;
import net.mickarea.generator.opts.abstractclasses.AbstractController;
import net.mickarea.generator.utils.MyDBUtil;
import net.mickarea.generator.utils.MyFileUtil;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 关于MySQL数据库的相关处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月11日-2024年9月28日
 */
public class MysqlController extends AbstractController {

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
				result.setData("数据库连接成功.");
			}else {
				throw new Exception("在连接数据库时，发生了一些异常.");
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
				throw new Exception("数据库什么内容都没有返回.");
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
	 * @see net.mickarea.generator.opts.abstractclasses.AbstractController#genMyResults(java.lang.String, java.lang.String, javax.sql.DataSource, java.lang.String, java.lang.String)
	 */
	@Override
	protected GenResult genMyResults(String tabName, String schemaName, DataSource ds, String fileDir, String charSet) {
		
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
				result = new GenResult(tabName, "", false, "找不到任何关于这个表的相关信息", "", System.currentTimeMillis()-start);
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
			result = new GenResult(tabName, "", false, "执行文件生成处理异常. 请检查日志文件以查看详细信息.", "", System.currentTimeMillis()-start);
		}
		
		return result;
	}
	
}
