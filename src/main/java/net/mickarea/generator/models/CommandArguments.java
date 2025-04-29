/******************************************************************************************************

This file "CommandArguments.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

import net.mickarea.generator.constants.MyConstants.ACTION_TYPE;
import net.mickarea.generator.utils.MyValidUtil;

/**
 * &gt;&gt;&nbsp;由命令行传入的参数对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日
 */
public class CommandArguments {

	private String databaseType ; //数据库类型
	private String poolName ; //连接池名称
	private String jdbcDriver ; //jdbc驱动类名
	private String jdbcUrl ; //jdbc链接url
	private String dbUser ; //创建数据库链接的用户
	private String dbPasswd ; //创建数据库链接的用户密码
	private String isAutoCommit ; //事务是否自动提交
	private String connTimeout ; //链接超时阈值
	private String minThreadNum ; //连接池最小链接数
	private String maxThreadNum ; //连接池最大链接数
	private String schema ; //数据库对象（表、视图）所属实例名
	private String schemaUser ; //数据库对象（表、视图）所属用户名
	private String charSet ; //生成的java文件字符集
	private String actionType ; //动作类型：object / sql
	private String objOrSql ; //根据actionType切换，库表字符串 或者 一个sql 语句
	private String fileDir ; //文件的保存路径
	
	/**
	 * 无参构造函数
	 */
	public CommandArguments() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
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
	 */
	public CommandArguments(String databaseType, String poolName, String jdbcDriver, String jdbcUrl, String dbUser,
			String dbPasswd, String isAutoCommit, String connTimeout, String minThreadNum, String maxThreadNum) {
		super();
		this.databaseType = databaseType;
		this.poolName = poolName;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.dbUser = dbUser;
		this.dbPasswd = dbPasswd;
		this.isAutoCommit = isAutoCommit;
		this.connTimeout = connTimeout;
		this.minThreadNum = minThreadNum;
		this.maxThreadNum = maxThreadNum;
	}

	/**
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
	 */
	public CommandArguments(String databaseType, String poolName, String jdbcDriver, String jdbcUrl, String dbUser,
			String dbPasswd, String isAutoCommit, String connTimeout, String minThreadNum, String maxThreadNum,
			String schema, String schemaUser) {
		super();
		this.databaseType = databaseType;
		this.poolName = poolName;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.dbUser = dbUser;
		this.dbPasswd = dbPasswd;
		this.isAutoCommit = isAutoCommit;
		this.connTimeout = connTimeout;
		this.minThreadNum = minThreadNum;
		this.maxThreadNum = maxThreadNum;
		this.schema = schema;
		this.schemaUser = schemaUser;
	}

	/**
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
	 * @param fileDir 文件存放路径
	 */
	public CommandArguments(String databaseType, String poolName, String jdbcDriver, String jdbcUrl, String dbUser,
			String dbPasswd, String isAutoCommit, String connTimeout, String minThreadNum, String maxThreadNum,
			String schema, String schemaUser, String charSet, String actionType, String objOrSql, String fileDir) {
		super();
		this.databaseType = databaseType;
		this.poolName = poolName;
		this.jdbcDriver = jdbcDriver;
		this.jdbcUrl = jdbcUrl;
		this.dbUser = dbUser;
		this.dbPasswd = dbPasswd;
		this.isAutoCommit = isAutoCommit;
		this.connTimeout = connTimeout;
		this.minThreadNum = minThreadNum;
		this.maxThreadNum = maxThreadNum;
		this.schema = schema;
		this.schemaUser = schemaUser;
		this.charSet = charSet;
		this.actionType = actionType;
		this.objOrSql = objOrSql;
		this.fileDir = fileDir;
	}
	
	/**
	 * 参数赋值，将 命令行接收的参数，转化为 功能要用的参数
	 * @param a 命令行接收到的参数对象
	 */
	public CommandArguments(NewCommToolArgs a) {
		super();
		this.databaseType = a.databaseType.name();
		this.poolName = a.poolName;
		this.jdbcDriver = a.jdbcDriverName;
		this.jdbcUrl = a.jdbcUrl;
		this.dbUser = a.dbUserName;
		this.dbPasswd = a.dbUserPasswd;
		this.isAutoCommit = a.isAutoCommit+"";
		this.connTimeout = a.connectionTimeout+"";
		this.minThreadNum = a.minThread+"";
		this.maxThreadNum = a.maxThread+"";
		this.schema = a.schema;
		this.schemaUser = a.schemaUser;
		this.charSet = a.fileCharset;
		this.actionType = a.actionType.name().toLowerCase();
		if(a.actionType == ACTION_TYPE.OBJECT) {
			this.objOrSql = a.sqlObjects;
		}else {
			this.objOrSql = a.sqlString;
		}
		this.fileDir = a.entityDir;
	}

	/**
	 * @return the databaseType
	 */
	public String getDatabaseType() {
		return databaseType;
	}

	/**
	 * @param databaseType the databaseType to set
	 */
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * @return the poolName
	 */
	public String getPoolName() {
		return poolName;
	}

	/**
	 * @param poolName the poolName to set
	 */
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	/**
	 * @return the jdbcDriver
	 */
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/**
	 * @param jdbcDriver the jdbcDriver to set
	 */
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	/**
	 * @return the jdbcUrl
	 */
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	/**
	 * @param jdbcUrl the jdbcUrl to set
	 */
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	/**
	 * @return the dbUser
	 */
	public String getDbUser() {
		return dbUser;
	}

	/**
	 * @param dbUser the dbUser to set
	 */
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	/**
	 * @return the dbPasswd
	 */
	public String getDbPasswd() {
		return dbPasswd;
	}

	/**
	 * @param dbPasswd the dbPasswd to set
	 */
	public void setDbPasswd(String dbPasswd) {
		this.dbPasswd = dbPasswd;
	}

	/**
	 * @return the isAutoCommit
	 */
	public String getIsAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * @param isAutoCommit the isAutoCommit to set
	 */
	public void setIsAutoCommit(String isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	/**
	 * @return the connTimeout
	 */
	public String getConnTimeout() {
		return connTimeout;
	}

	/**
	 * @param connTimeout the connTimeout to set
	 */
	public void setConnTimeout(String connTimeout) {
		this.connTimeout = connTimeout;
	}

	/**
	 * @return the minThreadNum
	 */
	public String getMinThreadNum() {
		return minThreadNum;
	}

	/**
	 * @param minThreadNum the minThreadNum to set
	 */
	public void setMinThreadNum(String minThreadNum) {
		this.minThreadNum = minThreadNum;
	}

	/**
	 * @return the maxThreadNum
	 */
	public String getMaxThreadNum() {
		return maxThreadNum;
	}

	/**
	 * @param maxThreadNum the maxThreadNum to set
	 */
	public void setMaxThreadNum(String maxThreadNum) {
		this.maxThreadNum = maxThreadNum;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * @param schema the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	/**
	 * @return the schemaUser
	 */
	public String getSchemaUser() {
		return schemaUser;
	}

	/**
	 * @param schemaUser the schemaUser to set
	 */
	public void setSchemaUser(String schemaUser) {
		this.schemaUser = schemaUser;
	}

	/**
	 * @return the charSet
	 */
	public String getCharSet() {
		return charSet;
	}

	/**
	 * @param charSet the charSet to set
	 */
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}

	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the objOrSql
	 */
	public String getObjOrSql() {
		return objOrSql;
	}

	/**
	 * @param objOrSql the objOrSql to set
	 */
	public void setObjOrSql(String objOrSql) {
		this.objOrSql = objOrSql;
	}
	
	/**
	 * @return the fileDir
	 */
	public String getFileDir() {
		return fileDir;
	}

	/**
	 * @param fileDir the fileDir to set
	 */
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}

	/**
	 * &gt;&gt;&nbsp; 校验参数的有效性
	 * @return 返回一个验证结果对象 ValidResult
	 */
	public ValidResult valid() {
		return MyValidUtil.validArguments(databaseType, poolName, jdbcDriver, jdbcUrl, dbUser, dbPasswd, 
				isAutoCommit, connTimeout, minThreadNum, maxThreadNum, 
				schema, schemaUser, 
				charSet, actionType, objOrSql, fileDir);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CommandArguments [databaseType=" + databaseType + ", poolName=" + poolName + ", jdbcDriver="
				+ jdbcDriver + ", jdbcUrl=" + jdbcUrl + ", dbUser=" + dbUser + ", dbPasswd=" + dbPasswd
				+ ", isAutoCommit=" + isAutoCommit + ", connTimeout=" + connTimeout + ", minThreadNum=" + minThreadNum
				+ ", maxThreadNum=" + maxThreadNum + ", schema=" + schema + ", schemaUser=" + schemaUser + ", charSet="
				+ charSet + ", actionType=" + actionType + ", objOrSql=" + objOrSql + ", fileDir=" + fileDir + "]";
	}
	
}
