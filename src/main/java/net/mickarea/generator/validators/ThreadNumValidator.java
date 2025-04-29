/******************************************************************************************************

This file "ThreadNumValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 * 这是线程数量校验。它应该是 1 到 50 之间
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月23日
 */
public class ThreadNumValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		int minValue = 1;
		int maxValue = 50;
		// 先校验是不是一个整数
		Integer tmp = 0;
		try {
			tmp = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			String execInfo = String.format("parameter %s invalid. It should be a number string and the max value is %s.", name, Integer.MAX_VALUE);
			throw new ParameterException(execInfo);
		}
		//然后校验，数据范围
		if(tmp.intValue()<minValue || tmp.intValue()>maxValue) {
			String execInfo = String.format("parameter %s invalid. The value should be greater than or equal to %s and less than or equal to %s.", name, minValue, maxValue);
			throw new ParameterException(execInfo);
		}
	}

}
