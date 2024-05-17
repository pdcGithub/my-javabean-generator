/******************************************************************************************************

This file "ValidUtil.java" is part of project "generator" , which is belong to Michael Pang (Its Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import net.mickarea.generator.models.ValidResult;

/**
 * &gt;&gt;&nbsp;关于参数的校验处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日-2024年5月17日
 */
public final class MyValidUtil {
	
	/**
	 * 私有构造函数，防止被 new 创建对象
	 */
	private MyValidUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 允许的数据库类型
	 */
	public static final List<String> AVAILABLE_DATABASE_TYPE = Arrays.asList("mysql","oracle","sqlserver");
	/**
	 * 允许的字符集
	 */
	public static final List<String> AVAILABLE_CHARSET = Arrays.asList("UTF-8", "GBK", "GB2312", "GB18030", "BIG5", "ISO-8859-1");

	/**
	 * &gt;&gt;&nbsp;校验将要执行的参数
	 * @param databaseType 数据库类型
	 * @param poolName 连接池名称
	 * @param jdbcDriver jdbc驱动类名
	 * @param jdbcUrl jdbc链接url
	 * @param dbUser 创建数据库链接的用户
	 * @param dbPasswd 创建数据库链接的用户密码
	 * @param isAutoCommit 事务是否自动提交
	 * @param connTimeout 链接超时阈值
	 * @param minThreadNum 连接池最小链接数
	 * @param maxThreadNum 连接池最大链接数
	 * @param schema 数据库对象（表、视图）所属实例名
	 * @param schemaUser 数据库对象（表、视图）所属用户名
	 * @param charSet 生成的java文件字符集
	 * @param actionType 动作类型：object / sql
	 * @param objOrSql 根据actionType切换，库表字符串 或者 一个sql 语句
	 * @param fileDir 文件存放位置
	 * @return 如果校验不通过，会返回对应信息
	 */
	public static final ValidResult validArguments(String databaseType, String poolName, String jdbcDriver, 
			String jdbcUrl, String dbUser, String dbPasswd, String isAutoCommit, String connTimeout, 
			String minThreadNum, String maxThreadNum, String schema, String schemaUser, String charSet,
			String actionType, String objOrSql, String fileDir) {
		//返回的结果对象
		ValidResult result = new ValidResult();
		try {
			//
			if(databaseType!=null && !AVAILABLE_DATABASE_TYPE.contains(databaseType.toLowerCase())) {
				throw new Exception("database type is invalid.["+databaseType+"]");
			}
			//
			if(poolName!=null && !Pattern.matches("[0-9a-zA-Z]+", poolName)) {
				throw new Exception("pool name is invalid.["+poolName+"]");
			}
			//
			if(jdbcDriver!=null && !Pattern.matches("[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+", jdbcDriver)) {
				throw new Exception("jdbc driver name is invalid.["+jdbcDriver+"]");
			}
			//
			if(jdbcUrl!=null && !Pattern.matches("jdbc:(oracle|mysql|sqlserver):[\\.=:@;0-9a-zA-Z\\_\\\\\\/]+", jdbcUrl)) {
				throw new Exception("jdbc url is invalid.["+jdbcUrl+"]");
			}
			//
			if(dbUser!=null && !Pattern.matches(".+", dbUser)) {
				throw new Exception("user of db connection is invalid.["+dbUser+"]");
			}
			//
			if(dbPasswd!=null && !Pattern.matches(".+", dbPasswd)) {
				throw new Exception("password of db connection is invalid.["+dbPasswd+"]");
			}
			//
			if(isAutoCommit!=null && !Pattern.matches("true|false", isAutoCommit.toLowerCase())) {
				throw new Exception("the value of commit type is invalid.["+isAutoCommit.toLowerCase()+"]");
			}
			//
			if(connTimeout !=null && !Pattern.matches("[1-9](\\d+)?", connTimeout)) {
				throw new Exception("the value of connection timeout is invalid.["+connTimeout+"]");
			}else {
				try {
					int tmp = new Integer(connTimeout);
					if(tmp<1000 || tmp>60000) {
						throw new Exception("the value of connection timeout is too small or too large.["+connTimeout+"]");
					}
				}catch(NumberFormatException e1) {
					throw new Exception("the value of connection timeout cannot be converted.["+connTimeout+"]");
				}
			}
			//
			if(minThreadNum!=null && !Pattern.matches("[1-9](\\d+)?", minThreadNum)) {
				throw new Exception("the value of minThreadNum is invalid.["+minThreadNum+"]");
			}else {
				try {
					int tmp = new Integer(minThreadNum);
					if(tmp>10) {
						throw new Exception("the value of minThreadNum is too large.["+minThreadNum+"]");
					}
				}catch(NumberFormatException e1) {
					throw new Exception("the value of minThreadNum cannot be converted.["+minThreadNum+"]");
				}
			}
			//
			if(maxThreadNum!=null && !Pattern.matches("[1-9](\\d+)?", maxThreadNum)) {
				throw new Exception("the value of maxThreadNum is invalid.["+maxThreadNum+"]");
			}else {
				try {
					int tmp = new Integer(maxThreadNum);
					if(tmp>50) {
						throw new Exception("the value of maxThreadNum is too large.["+maxThreadNum+"]");
					}
				}catch(NumberFormatException e1) {
					throw new Exception("the value of maxThreadNum cannot be converted.["+maxThreadNum+"]");
				}
			}
			//
			if(schema!=null && !Pattern.matches("[0-9A-Za-z\\_]+", schema)) {
				throw new Exception("the schema name is invalid.["+schema+"]");
			}
			//
			if(schemaUser!=null && !Pattern.matches("[0-9A-Za-z\\_]+", schemaUser)) {
				throw new Exception("the schema user name is invalid.["+schemaUser+"]");
			}
			//
			if(charSet!=null && !AVAILABLE_CHARSET.contains(charSet)) {
				throw new Exception("charSet is invalid.["+charSet+"]");
			}
			//
			if(actionType!=null && !Pattern.matches("object|sql", actionType.toLowerCase())) {
				throw new Exception("actionType is invalid.["+actionType+"]");
			}
			//
			if(objOrSql!=null) {
				if(actionType.equalsIgnoreCase("object") && !Pattern.matches("[\\$\\_a-zA-Z0-9]+(\\,[\\$\\_a-zA-Z0-9]+)*", objOrSql)) {
					throw new Exception("the string of tables is invalid.["+objOrSql+"]");
				}else if(actionType.equalsIgnoreCase("sql") && !Pattern.matches("select.+", objOrSql)) {
					throw new Exception("the string of sql is invalid.["+objOrSql+"]");
				}
			}
			//
			if(fileDir!=null && !Pattern.matches("([a-zA-Z]\\:)?([\\/\\\\][\\w\\_\\- ]+)+[\\/\\\\]?", fileDir)) {
				throw new Exception("the dir path of target files is invalid.["+fileDir+"]");
			}
		}catch(Exception e) {
			result.setValid(false);
			result.setMessage(e.getMessage());
		}
		//返回
		return result;
	}
	
}
