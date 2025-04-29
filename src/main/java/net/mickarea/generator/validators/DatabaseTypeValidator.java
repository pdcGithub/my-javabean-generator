/******************************************************************************************************

This file "DatabaseTypeValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import net.mickarea.generator.constants.MyConstants.DATABASE_TYPE;

/**
 * 参数校验处理：数据库类型。它应该是 mysql, oracle, sqlserver 之类的一个字符串
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月22日
 */
public class DatabaseTypeValidator implements IParameterValidator {
	
	/**
	 * 开始校验
	 */
	@Override
	public void validate(String name, String value) throws ParameterException {
		//
		DATABASE_TYPE[] types = DATABASE_TYPE.values();
		//
		//判断入参是否符合模式字符串
		boolean isOk = true;
		try {
			DATABASE_TYPE.valueOf(value.toUpperCase());
		}catch(Exception e) {
			isOk = false;
		}
		//
		if(!isOk) {
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<types.length;i++) {
				sb.append(types[i].name());
				if(i<types.length-1)
					sb.append(", ");
			}
			String exec_info = String.format("parameter %s invalid. It should be one of them (%s).", name, sb.toString());
			throw new ParameterException(exec_info);
		}
	}

}
