/******************************************************************************************************

This file "SqlObjectsValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
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
 * 这是一个关于 SQL 对象名称校验的处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月23日
 */
public class SqlObjectsValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		//非空校验
		if(value==null || value.replaceAll("\\s+", "").length()<=0) {
			String execInfo = String.format("parameter %s invalid. The value is empty now.", name);
			throw new ParameterException(execInfo);
		}
		//格式校验 一个 可能带逗号的 表或者视图名称字符串
		String regexp = "[a-zA-Z0-9\\_]+(\\s*\\,\\s*[a-zA-Z0-9\\_]+)*";
		//
		boolean isOk = Pattern.matches(regexp, value==null?"":value);
		//
		if(!isOk) {
			String execInfo = String.format("parameter %s invalid. This string should look like this: TABLE_001, VIEW_002", name);
			throw new ParameterException(execInfo);
		}
	}

}
