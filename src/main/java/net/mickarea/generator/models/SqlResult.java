/******************************************************************************************************

This file "ValidResult.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

/**
 * &gt;&gt;&nbsp;一个数据库操作执行结果的信息对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日
 */
public class SqlResult {
	
	/**
	 * isOk 是否执行成功，成功为 true ，否则，false
	 */
	private boolean isOk = true;
	/**
	 * message 如果执行失败，可以将对应信息存放于此。
	 */
	private String message = "";
	/**
	 * data 关于执行结果的一些数据
	 */
	private String data = "";
	
	/**
	 * 构造函数
	 */
	public SqlResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造函数 
	 * @param isValid 是否执行成功，成功为 true ，否则，false
	 * @param message 如果校验不通过，可以将对应信息存放于此。
	 */
	public SqlResult(boolean isOk, String message) {
		super();
		this.isOk = isOk;
		this.message = message;
	}
	
	// getter and setter 
	
	public boolean getOk() {
		return isOk;
	}
	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SqlResult [isOk=" + isOk + ", message=" + message + ", data=" + data + "]";
	}
	
}
