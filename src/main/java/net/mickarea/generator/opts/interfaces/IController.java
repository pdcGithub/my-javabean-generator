/******************************************************************************************************

This file "IController.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts.interfaces;

import java.util.List;

import javax.sql.DataSource;

import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.SqlResult;

/**
 * 一个处理程序共同的接口类（用于规范处理程序）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月11日-2024年6月15日
 */
public interface IController {

	/**
	 * 数据库连接测试
	 * @param cArgs 命令行传来的参数
	 * @return 处理结果信息字符串
	 */
	public SqlResult testConnection(CommandArguments cArgs);
	
	/**
	 * 数据字典查询（查看有哪些表、视图）
	 * @param cArgs 命令行传来的参数
	 * @return 处理结果信息字符串
	 */
	public SqlResult searchDictInfo(CommandArguments cArgs);
	
	/**
	 * Java Bean 文件生成（表、视图的结构查询）
	 * @param cArgs 命令行传来的参数
	 * @return 处理结果信息字符串
	 */
	public SqlResult genJavaEntityFile(CommandArguments cArgs);
	
	/**
	 * 将数据库表或者视图，转换为实体对象 java 类文件（这个方法，一般由抽象类实现，因为代码是重复的）
	 * @param ds 数据库源
	 * @param schemaName 数据库实例名 或者 数据库模式名
	 * @param tableNames 表或者视图的字符串
	 * @param charSet 文件的字符集
	 * @param fileDir 文件所在的文件夹
	 * @return List&lt;GenResult&gt; 生成的Java 实体类的相关信息
	 * @throws Exception 一些异常
	 */
	public List<GenResult> dbObjectToJavaEntity(DataSource ds, String schemaName, String tableNames, String charSet, String fileDir)  throws Exception ;
	
	/**
	 * 将sql语句，转换为实体对象 java 类文件（这个方法，一般由抽象类实现，因为代码是重复的）
	 * @param ds 数据库源
	 * @param sql sql语句
	 * @param charSet 文件的字符集
	 * @param fileDir 文件所在的文件夹
	 * @return List&lt;GenResult&gt; 生成的Java 实体类的相关信息
	 * @throws Exception 一些异常
	 */
	public List<GenResult> dbSqlToJavaEntity(DataSource ds, String sql, String charSet, String fileDir) throws Exception ;
}
