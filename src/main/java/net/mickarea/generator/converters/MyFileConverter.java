/******************************************************************************************************

This file "FileConverter.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.converters;

import java.io.File;

import com.beust.jcommander.IStringConverter;

import net.mickarea.generator.utils.MyStrUtil;

/**
 * 这个是文件路径转成文件对象的一个转换器，虽然默认提供了一个 FileConverter，但是它实现太简单了，自己实现一个更好。
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年5月12日-2025年5月13日
 */
public class MyFileConverter implements IStringConverter<File> {

	@Override
	public File convert(String value) {
		// 如果 value 为 null。直接创建 File 对象会报错，所以要处理一下
		// 另外，文件路径前后的空白字符要去掉
		// 如果传入的字符是空，则返回 null 对象。方便命令行判断
		return MyStrUtil.isEmptyString(value)?null:new File(value.trim());
	}

}
