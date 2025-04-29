/******************************************************************************************************

This file "RunningModelValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import net.mickarea.generator.constants.MyConstants.RUNNING_MODE;

/**
 * 参数校验处理：运行模式。它应该是 MyConstants.RUNNING_MODE 中的一个枚举值。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月21日
 */
public class RunningModelValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		//先获取所有的枚举值
		RUNNING_MODE[] modes = RUNNING_MODE.values();
		//判断入参是否符合模式字符串
		boolean isOk = true;
		try {
			RUNNING_MODE.valueOf(value.toUpperCase());
		}catch(Exception e) {
			isOk = false;
		}
		//
		if(!isOk) {
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<modes.length;i++) {
				sb.append(modes[i].name());
				if(i<modes.length-1)
					sb.append(", ");
			}
			String exec_info = String.format("parameter %s invalid. It should be one of them (%s).", name, sb.toString());
			throw new ParameterException(exec_info);
		}
	}

}
