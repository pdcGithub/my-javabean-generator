/******************************************************************************************************

This file "StringIsNullValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 这个是对于一些字符串参数，进行非空校验
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月23日
 */
public class StringIsNullValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		
		if(value==null || value.replaceAll("\\s+", "").length()<=0) {
			String execInfo = String.format("parameter %s invalid. The value is empty now.", name);
			throw new ParameterException(execInfo);
		}
		
	}

}
