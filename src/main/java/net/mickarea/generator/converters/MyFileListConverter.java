/******************************************************************************************************

This file "MyFileListConverter.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.converters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IStringConverter;

import net.mickarea.generator.utils.MyStrUtil;

/**
 * 这是一个文件列表对象的转换器。因为，默认的转换处理没有对 空白字符做处理。所以这里自己写一个
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年5月13日
 */
public class MyFileListConverter implements IStringConverter<List<File>> {

	@Override
	public List<File> convert(String value) {
		List<File> re = null;
		//如果不为空，则转换，否则直接返回 null
		if(!MyStrUtil.isEmptyString(value)) {
			//替换处理要进一步处理，因为逗号两边可能有空白字符
			String[] filePaths = value.split("\\s*[,，]\\s*");
			//创建列表对象
			re = new ArrayList<File>();
			//遍历
			for(String path: filePaths) {
				//路径的前后，都不能有空白字符
				re.add(new File(path.trim()));
			}
		}
		return re;
	}

}
