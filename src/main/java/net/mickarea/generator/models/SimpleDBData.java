/******************************************************************************************************

This file "SimpleDBData.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

import java.util.List;

/**
 * &gt;&gt;&nbsp;一个简单的数据库查询结果对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2022年12月13日-2023年7月16日
 */
public class SimpleDBData {

	public static String OK = "ok";
	public static String ERR = "error";
	
	private List<String> columnName;
	private List<String> columnClassName;
	private List<String> columnTypeName;
	private Object[][] data;
	private String responseStatus = OK;
	private String responseInfo = null;
	
	public List<String> getColumnName() {
		return columnName;
	}
	public void setColumnName(List<String> columnName) {
		this.columnName = columnName;
	}
	public List<String> getColumnClassName() {
		return columnClassName;
	}
	public void setColumnClassName(List<String> columnClassName) {
		this.columnClassName = columnClassName;
	}
	public Object[][] getData() {
		return data;
	}
	public void setData(Object[][] data) {
		this.data = data;
	}
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseInfo() {
		return responseInfo;
	}
	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}
	public List<String> getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(List<String> columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	
	/**
	 * &gt;&gt;&nbsp;关于 SimpleDBData 对象的销毁操作。因为这个对象包含较多内容，如果不处理可能内存会占用较多。这里手工销毁对象
	 */
	public void destroy() {
		//清空字段名列表
		if(this.columnName!=null) {
			this.columnName.clear();
			this.columnName=null;
		}
		//清空类型名列表
		if(this.columnClassName!=null) {
			this.columnClassName.clear();
			this.columnClassName=null;
		}
		//清空类型名列表
		if(this.columnTypeName!=null) {
			this.columnTypeName.clear();
			this.columnTypeName=null;
		}
		//清空数据
		if(this.data!=null) {
			this.data=null;
		}
		//清空状态
		if(this.responseStatus!=null) {
			this.responseStatus=null;
		}
		//清空响应信息
		if(this.responseInfo!=null) {
			this.responseInfo=null;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("数据库信息对象打印开始>>>>>>>>>>>>>>>>>>>>>\n");
		sb.append("获取到状态信号为："+this.responseStatus+"，状态信息："+this.responseInfo+"\n");
		sb.append("获取到列名：\n");
		sb.append(this.columnName==null?"<对象为空>\n":(this.columnName.toString())+"\n");
		sb.append("获取到列数据类型：\n");
		sb.append(this.columnClassName==null?"<对象为空>\n":(this.columnClassName.toString())+"\n");
		sb.append("获取到列数据库字段类型：\n");
		sb.append(this.columnTypeName==null?"<对象为空>\n":(this.columnTypeName.toString())+"\n");
		sb.append("获取到数据：\n");
		if(this.data!=null) {
			 for(int i=0;i<this.data.length;i++) {
				 for(int j=0;j<this.data[i].length;j++) {
					 sb.append(this.data[i][j]);
					 if(j<this.data[i].length-1) {
						 sb.append(", ");
					 }
				 }
				 sb.append("\n");
			 }
		} else {
			sb.append("<对象为空>\n");
		}
		sb.append("数据库信息对象打印结束<<<<<<<<<<<<<<<<<<<<<");
		return sb.toString();
	}
	
}
