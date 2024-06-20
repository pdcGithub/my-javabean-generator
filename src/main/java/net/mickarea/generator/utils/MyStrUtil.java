/******************************************************************************************************

This file "MyStrUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * &gt;&gt;&nbsp;一个字符串工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月16日-2024年6月15日
 */
public class MyStrUtil {

	//java 关键字；构造属性时不能和关键字同名
	private static final String[] javaKeyWords = {"abstract", "assert",     "boolean", "break",     "byte",      "case",       "catch",    "char",    "class", "continue", 
			                                      "default",  "do",         "double",  "else",      "enum",      "extends",    "final",    "finally", "float",  "for", 
			                                      "if",       "implements", "import",  "int",       "interface", "instanceof", "long",     "native",  "new",    "package", 
			                                      "private",  "protected",  "public",  "return",    "short",     "static",     "strictfp", "super",   "switch", "synchronized",
			                                      "this",     "throw",      "throws",  "transient", "try",       "void",       "volatile", "while",   "true",   "false",
			                                      "null",     "goto",       "const"};
	
	//定义一个日志处理类
	public static final Logger mylogger = LoggerFactory.getLogger("jar");
	
	//返回消息的前缀（成功）
	public static final String SUCCESS_PREFIX = "success:";
	
	//返回消息的前缀（失败）
	public static final String ERROR_PREFIX = "error:";
	
	/**
	 * 私有构造函数，防止被 new 创建对象
	 */
	private MyStrUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;判断字符串参数是否为空，当字符内容为null也判断为空。
	 * @param param 待判断的字符串参数
	 * @return 如果param为null，'null'，空白字符，空等则为true。
	 */
	public static boolean isEmptyString(String param) {
		boolean result = false;
		if(param==null || param.trim().equalsIgnoreCase("null") || param.trim().length()==0 || Pattern.matches("[\\s]+", param.trim())) {
			result = true;
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的字段名，生成一个符合驼峰写法的属性名
	 * @param columnName 字段名
	 * @return 属性名
	 */
	public static String genNameFromColumn(String columnName) {
		String result = "";
		//传入的字段名，不能为空；不能带有"点"符号
		if(!MyStrUtil.isEmptyString(columnName) && !Pattern.matches(".*[\\.].*", columnName)) {
			String input = columnName.toLowerCase().replaceAll("[\\s]+", "");
			result = MyStrUtil.makeFirstCharLowerCase(MyStrUtil.makeHumpString(input, "_"));
		}
		//如果拼接出来的结果，跟关键字冲突，则直接加 's'
		if(MyStrUtil.isJavaKeyWords(result)) {
			result += "s";
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据传入的表或者视图名，生成一个Java类名
	 * @param tableName 表名或者视图名
	 * @return Java类名
	 */
	public static String genNameFromTable(String tableName) {
		return makeFirstCharUpperCase(genNameFromColumn(tableName));
	}
	
	/**
	 * &gt;&gt;&nbsp;根据输入的英文字符串，返回一个驼峰表示的字符串
	 * @param input 输入的字符串
	 * @param splitString 字符串中的分割符号
	 * @return
	 */
	public static String makeHumpString(String input, String splitString) {
		String output = "";
		if(!isEmptyString(input) && !isEmptyString(splitString)) {
			//统一转小写字母，然后再处理
			String[] s = input.toLowerCase().split(splitString);
			for(int i=0;i<s.length;i++) {
				s[i] = makeFirstCharUpperCase(s[i]);
			}
			output = String.join("", s);
		}
		return output ;
	}
	
	/**
	 * &gt;&gt;&nbsp;根据输入的英文字符串，返回一个驼峰表示的字符串
	 * @param input 输入的字符串
	 * @return
	 */
	public static String makeHumpString(String input) {
		return makeHumpString(input, "_");
	}
	
	/**
	 * 使一个英文字符串的首字母变为大写的形式
	 * @param input 输入字符串
	 * @return 处理后的字符串
	 */
	public static String makeFirstCharUpperCase(String input) {
		String output = "";
		if(!isEmptyString(input)) {
			output = input.substring(0, 1).toUpperCase()+input.substring(1);
		}
		return output;
	}
	
	/**
	 * 使一个英文字符串的首字母变为小写的形式
	 * @param input 输入字符串
	 * @return 处理后的字符串
	 */
	public static String makeFirstCharLowerCase(String input) {
		String output = "";
		if(!isEmptyString(input)) {
			output = input.substring(0, 1).toLowerCase()+input.substring(1);
		}
		return output;
	}
	
	/**
	 * &gt;&gt;&nbsp;判断输入的字符串是否与java关键字同名
	 * @param input 待判断的字符串
	 * @return 如果与java关键字同名，则返回true；否则，返回false
	 */
	public static boolean isJavaKeyWords(String input) {
		boolean result = false;
		for(String s: javaKeyWords) {
			if(s.equals(input)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 使用标准输出流，输出执行成功的消息
	 * @param message 要输出的消息
	 */
	public static void successOut(String message) {
		System.out.println(SUCCESS_PREFIX+message);
	}
	
	/**
	 * 使用标准输出流，输出执行失败的消息
	 * @param message 要输出的消息
	 */
	public static void errorOut(String message) {
		System.out.println(ERROR_PREFIX+message);
	}
	
	/**
	 * 将 一些特殊的 java 类 替换为 java 标准的 其它类。
	 * @param oldTypeName 原有的java 类名
	 * @return java 标准的类名
	 */
	public static final String jdbcClassTypeTranslate(String oldTypeName) {
		String newTypeName = null;
		switch(oldTypeName) {
		case "oracle.sql.TIMESTAMP":
			newTypeName = "java.sql.Timestamp";
			break;
		case "oracle.sql.ROWID":
			newTypeName = "java.lang.String";
			break;
		case "oracle.jdbc.OracleClob":
			newTypeName = "java.lang.String";
			break;
		case "oracle.jdbc.OracleNClob":
			newTypeName = "java.lang.String";
			break;
		case "oracle.jdbc.OracleBlob":
			newTypeName = "byte[]";
			break;
		default:
			newTypeName = oldTypeName;
			break;
		}
		//替换掉 
		if(newTypeName!=null) {
			newTypeName = newTypeName.replaceFirst("java\\.lang\\.", "");
		}
		return newTypeName;
	}
}
