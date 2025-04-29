/******************************************************************************************************

This file "CharsetValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import java.nio.charset.Charset;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 一个字符集信息校验
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月23日
 */
public class CharsetValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		//首先判空
		if(value==null || value.replaceAll("\\s+", "").length()<=0) {
			String execInfo = String.format("parameter %s invalid. The value is empty now.", name);
			throw new ParameterException(execInfo);
		}
		//然后，判断字符集是否支持
		boolean isSupported = false;
		try {
			isSupported = Charset.isSupported(value);
		} catch (Exception e) {
			//因为报错就是不支持，所以不用处理了。
		}
		if(!isSupported) {
			String execInfo = String.format("parameter %s invalid. The name of the character set is not supported.", name);
			throw new ParameterException(execInfo);
		}
	}

}