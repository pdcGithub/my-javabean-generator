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
 * @since 2024年5月16日-2024年9月12日
 */
public final class MyStrUtil {

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
	
	/**
	 * <p>将普通的字符串，转换为 Unicoe 字符串。</p>
	 * <p>比如 "ceshi，我是𠮷" 转换为 \u0063\u0065\u0073\u0068\u0069\uFF0C\u6211\u662F\uD842\uDFB7</p>
	 * <p>如果字符的码点大于 0xFFFF ，则会转为 两个。比如：𠮷 转换为 \uD842\uDFB7</p>
	 * @param normalString 待转换的普通字符串
	 * @return 一段 Unicode 格式的字符串。
	 */
	public static final String parseStrToUnicode(String normalString) {
		//默认直接赋值，如果校验不通过，则直接返回原值。
		String re = normalString;
		//开始校验（字符串不为 null 即可）
		if(normalString!=null && normalString.length()>0) {
			//开始处理
			StringBuffer sb = new StringBuffer();
			//字符串 拆解为 字符数组
			char[] arr = normalString.toCharArray();
			//循环遍历，字符 转换为 16进制表示
			for(int i=0;i<arr.length;i++) {
				String tmpStr = Integer.toHexString(arr[i]);
				switch(tmpStr.length()) {
				case 1:
					tmpStr = "000"+tmpStr;
					break;
				case 2:
					tmpStr = "00"+tmpStr;
					break;
				case 3:
					tmpStr = "0"+tmpStr;
					break;
				}
				sb.append("\\u"+tmpStr.toUpperCase());
			}
			//结果重新赋值
			String tmp = sb.toString();
			//如果转换没有结果，则返回原值，并输出异常
			if(tmp.length()>0) {
				re = tmp;
			}else {
				mylogger.error(String.format("将内容 normalString=%s，转换为 Unicode 字符异常。",normalString));
			}
		}
		//返回结果
		return re;
	}
	
	/**
	 * <p>将Unicoe 字符串，转换为 普通的字符串。</p>
	 * <p>比如 \u0063\u0065\u0073\u0068\u0069\uFF0C\u6211\u662F\uD842\uDFB7 转换为  "ceshi，我是𠮷"</p>
	 * @param unicodeString
	 * @return 一段 普通的字符串。
	 */
	public static final String parseUnicodeToStr(String unicodeString) {
		//默认直接赋值，如果校验不通过，则直接返回原值。
		String re = unicodeString;
		//开始校验（不为空，且 需要有 \\\\u 这个字符串）
		if(!isEmptyString(unicodeString) && unicodeString.contains("u")) {
			//按字符串，拆分。拆分后，可能有空的内容在数组，需要跳过处理
			String[] uArr = unicodeString.split("\\\\u");
			//
			StringBuffer sb = new StringBuffer();
			//
			for(int i=0;i<uArr.length;i++) {
				//如果为空，则跳过
				if(isEmptyString(uArr[i])) {
					continue;
				}
				//将字符串转为 数字，然后转 字符。
				char charInt = 0;
				try {
					if(!Pattern.matches("[a-zA-Z0-9]{4}", uArr[i])) {
						throw new NumberFormatException("接受的数字超过 0xFFFF，请检查。");
					}
					charInt = (char)Integer.parseInt(uArr[i], 16);
					sb.append(charInt);
				} catch (NumberFormatException e) {
					mylogger.error(String.format("将 16进制字符串=%s，转换为 数字异常。%s", uArr[i], e.getMessage()));
					break;
				} catch (Exception e2) {
					mylogger.error(String.format("将 字符=%s 信息，构造字符串异常。 %s", charInt, e2.getMessage()));
					break;
				}
			}
			//结果重新赋值
			String tmp = sb.toString();
			//如果转换没有结果，则返回原值，并输出异常
			if(!isEmptyString(tmp)) {
				re = tmp;
			}else {
				mylogger.error(String.format("将内容 unicodeString=%s，转换为 普通 字符异常。",unicodeString));
			}
		}
		//返回结果
		return re;
	}
	
	/*
	public static void main(String[] args) {
		
		//原始字符串
		String a1 = "ceshi，我是𠮷";
		String a2 = "aaaaaaaa";
		String a3 = "       				";
		String a4 = "     \n       \t      \r       ";
		String a5 = null;
		String a6 = "";
		System.out.println("value='"+parseStrToUnicode(a1)+"'");
		System.out.println("value='"+parseStrToUnicode(a2)+"'");
		System.out.println("value='"+parseStrToUnicode(a3)+"'");
		System.out.println("value='"+parseStrToUnicode(a4)+"'");
		System.out.println("value='"+parseStrToUnicode(a5)+"'");
		System.out.println("value='"+parseStrToUnicode(a6)+"'");
		
		//unicode
		String u1 = "";
		String u2 = null;
		String u3 = "   ";
		String u4 = "		   \t   \r   \n";
		String u5 = "\\u0063\\u0065\\u0073\\u0068\\u0069\\uFF0C\\u6211\\u662F\\uD842\\uDFB7";
		String u6 = "\\u20BB7";
		String u7 = "\\uSSSS";
		System.out.println("unicode='"+parseUnicodeToStr(u1)+"'");
		System.out.println("unicode='"+parseUnicodeToStr(u2)+"'");
		System.out.println("unicode='"+parseUnicodeToStr(u3)+"'");
		System.out.println("unicode='"+parseUnicodeToStr(u4)+"'");
		System.out.println("unicode='"+parseUnicodeToStr(u5)+"'");
		System.out.println("unicode='"+parseUnicodeToStr(u6)+"'");
		System.out.println("unicode='"+parseUnicodeToStr(u7)+"'");
	}
	*/
}
