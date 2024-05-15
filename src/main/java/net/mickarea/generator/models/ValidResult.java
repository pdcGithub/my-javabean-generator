/******************************************************************************************************

This file "ValidResult.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

/**
 * &gt;&gt;&nbsp;一个校验结果的信息对象
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日
 */
public class ValidResult {
	
	/**
	 * isValid 是否校验通过，通过为 true ，否则，false
	 */
	private boolean isValid = true;
	/**
	 * message 如果校验不通过，可以将对应信息存放于此。
	 */
	private String message = "";
	
	/**
	 * 构造函数
	 */
	public ValidResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 构造函数 
	 * @param isValid 是否校验通过，通过为 true ，否则，false
	 * @param message 如果校验不通过，可以将对应信息存放于此。
	 */
	public ValidResult(boolean isValid, String message) {
		super();
		this.isValid = isValid;
		this.message = message;
	}

	// getter and setter 
	
	public boolean getValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "ValidResult [isValid=" + isValid + ", message=" + message + "]";
	}
	
}
