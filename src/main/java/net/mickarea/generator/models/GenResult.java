/******************************************************************************************************

This file "GenResult.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

/**
 * &gt;&gt;&nbsp;生成的Java 实体类的相关信息
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月16日-2024年6月15日
 */
public class GenResult {

	//数据库对象名
	private String dbObjName;
	
	//实体对象名	
	private String entityName;
	
	//处理状态
	private boolean status;
	
	//异常信息
	private String statusInfo;
	
	//文件存放路径
	private String filePath;
	
	//数据库处理耗时
	private long dbTakes;

	/**
	 * 生成的Java 实体类的相关信息
	 */
	public GenResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 生成的Java 实体类的相关信息
	 * @param dbObjName 数据库对象名
	 * @param entityName 实体对象名
	 * @param status 处理状态
	 * @param statusInfo 处理信息
	 * @param filePath 文件存放路径
	 */
	public GenResult(String dbObjName, String entityName, boolean status, String statusInfo, String filePath, long dbTakes) {
		super();
		this.dbObjName = dbObjName;
		this.entityName = entityName;
		this.status = status;
		this.statusInfo = statusInfo;
		this.filePath = filePath;
		this.dbTakes = dbTakes;
	}
	
	/**
	 * @return the dbObjName
	 */
	public String getDbObjName() {
		return dbObjName;
	}

	/**
	 * @param dbObjName the dbObjName to set
	 */
	public void setDbObjName(String dbObjName) {
		this.dbObjName = dbObjName;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the status
	 */
	public boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the statusInfo
	 */
	public String getStatusInfo() {
		return statusInfo;
	}

	/**
	 * @param statusInfo the statusInfo to set
	 */
	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}
	
	/**
	 * @return the dbTakes
	 */
	public long getDbTakes() {
		return dbTakes;
	}

	/**
	 * @param dbTakes the dbTakes to set
	 */
	public void setDbTakes(long dbTakes) {
		this.dbTakes = dbTakes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenResult [dbObjName=" + dbObjName + ", entityName=" + entityName + ", status=" + status
				+ ", statusInfo=" + statusInfo + ", filePath=" + filePath + ", dbTakes="+dbTakes+"]";
	}
	
}
