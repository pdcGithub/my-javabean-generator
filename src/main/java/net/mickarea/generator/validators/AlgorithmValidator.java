/******************************************************************************************************

This file "AlgorithmValidator.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import net.mickarea.generator.constants.MyConstants.ASYMMETRIC_ENCRYPTION_ALGORITHM;
import net.mickarea.generator.constants.MyConstants.DIGITAL_SIGNATURE_ALGORITHM;

/**
 * 关于数字签名和字符串加密时，对应的算法名称校验
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年9月16日
 */
public class AlgorithmValidator implements IParameterValidator {

	@Override
	public void validate(String name, String value) throws ParameterException {
		
		// 数字签名算法
		DIGITAL_SIGNATURE_ALGORITHM[] dsAlgos = DIGITAL_SIGNATURE_ALGORITHM.values();
		
		// 加密算法
		ASYMMETRIC_ENCRYPTION_ALGORITHM[] aeAlgos = ASYMMETRIC_ENCRYPTION_ALGORITHM.values();
		
		//判断入参是否符合模式字符串
		boolean isOk1 = true;
		try { DIGITAL_SIGNATURE_ALGORITHM.valueOf(value.toUpperCase()); }catch(Exception e) { isOk1 = false; }
		boolean isOk2 = true;
		try { ASYMMETRIC_ENCRYPTION_ALGORITHM.valueOf(value.toUpperCase()); }catch(Exception e) { isOk2 = false; }
		
		// 都不合符，才抛异常
		if(!isOk1 && !isOk2) {
			List<String> names = new ArrayList<String>();
			for(int i=0;i<dsAlgos.length;i++) {
				names.add(dsAlgos[i].name());
			}
			for(int i=0;i<aeAlgos.length;i++) {
				names.add(aeAlgos[i].name());
			}
			String outputNames = String.join(", ", names);
			//
			String exec_info = String.format("parameter %s invalid. It should be one of them (%s).", name, outputNames);
			throw new ParameterException(exec_info);
		}
		
	}

}
