/******************************************************************************************************

This file "KeyLengthValidator.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 这里校验密钥长度的处理
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年9月16日
 */
public class KeyLengthValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		
		int result = 0;
		
		// 先校验是否为整数
		try {
			result=Integer.parseInt(value);
		} catch (Exception e) {
			String exec_info = String.format("parameter %s invalid. %s", name, "The received parameter is not an integer.");
			throw new ParameterException(exec_info);
		}
		
		// 再校验大小
		if(result<1024) {
			String exec_info = String.format("parameter %s invalid. %s", name, "The received parameter value is less than 1024, please check.");
			throw new ParameterException(exec_info);
		}
		
	}

}
