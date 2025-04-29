/******************************************************************************************************

This file "JDBCUrlValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import java.util.regex.Pattern;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 参数校验处理：JDBC 连接 URL 。它应该是 一个 URL 的字符串
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月23日
 */
public class JDBCUrlValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		//
		String regexp = "[jJ][dD][bB][cC]\\:\\S+";
		//
		boolean isOk = Pattern.matches(regexp, value==null?"":value);
		//
		if(!isOk) {
			String execInfo = String.format("parameter %s invalid. It should be a url string, like 'jdbc:mysql://127.0.0.1:3306/myspace_db'.", name);
			throw new ParameterException(execInfo);
		}
	}

}
