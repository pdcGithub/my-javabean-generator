/******************************************************************************************************

This file "TabOrViewTmpObj.java" is part of project "entitytools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.beans;

/**
 * >> 一个临时的库表信息对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月17日-2023年8月8日
 */
public class TabOrViewTmpObj {

	private String columnName; //列名
	
	private String propertyName;//java属性名
	
	private String propertyType;//java数据类型
	
	private Long ordinalPosition;//列序号
	
	private String dataType; //数据类型（数据库中的）
	
	private String columnType;//列数据类型（数据库中的）
	
	private String columnKey;//库表键值情况
	
	private String extra;    //扩展属性，自增情况等等。。。
	
	private String columnComment; //数据库字段的注释内容；即 comment
	
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public Long getOrdinalPosition() {
		return ordinalPosition;
	}
	public void setOrdinalPosition(Long ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getColumnKey() {
		return columnKey;
	}
	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TabOrViewTmpObj [columnName=" + columnName + ", propertyName=" + propertyName + ", propertyType="
				+ propertyType + ", ordinalPosition=" + ordinalPosition + ", dataType=" + dataType + ", columnType="
				+ columnType + ", columnKey=" + columnKey + ", extra=" + extra + ", columnComment=" + columnComment
				+ "]";
	}
	
}
