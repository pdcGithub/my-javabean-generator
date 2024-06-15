/******************************************************************************************************

This file "AbstractController.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts.abstractclasses;

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
 * 一个抽象的控制器，用于实现 IController 接口。并将一些共用的东西，实现
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月15日
 */
public abstract class AbstractController implements IController {
	
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
	protected abstract GenResult genMyResults(String tabName, String schemaName, DataSource ds, String fileDir, String charSet) ;
}
