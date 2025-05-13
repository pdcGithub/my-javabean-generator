/******************************************************************************************************

This file "MyStrUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mickarea.generator.opts.MyWriter;

/**
 * &gt;&gt;&nbsp;一个字符串工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月16日-2025年5月13日
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
	
	//消息类型，成功
	public static final String MSG_TYPE_SUCCESS = "success";
	//消息类型，错误
	public static final String MSG_TYPE_ERROR = "error";
	//消息类型，转换时造成的失败
	public static final String MSG_TYPE_FAULT = "fault";
	
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
	 * <p>消息格式如下：{"status":"%s", "oriMessage":"%s", "encodeMessage":"%s"}</p>
	 * @param message 要输出的消息
	 */
	public static void successOut(String message) {
		MyStrUtil.standardOutput(MSG_TYPE_SUCCESS, message);
	}
	
	/**
	 * 使用标准输出流，输出执行成功的消息（这里和 successOut 不同，这里可以选择输出的是 json格式 还是 原始格式）
	 * @param message 要输出的消息。
	 * @param isConsole 是否命令行终端。如果为 true ，则直接输出，如果是 false 用 json 方式输出。
	 */
	public static void successOut(String message, boolean isConsole) {
		if(isConsole) {
			System.out.print(message);
		}else {
			MyStrUtil.standardOutput(MSG_TYPE_SUCCESS, message);
		}
	}
	
	/**
	 * 使用标准输出流，输出执行失败的消息
	 * <p>消息格式如下：{"status":"%s", "oriMessage":"%s", "encodeMessage":"%s"}</p>
	 * @param message 要输出的消息
	 */
	public static void errorOut(String message) {
		MyStrUtil.standardOutput(MSG_TYPE_ERROR, message);
	}
	
	/**
	 * 使用标准输出流，输出执行失败的消息（这里和 errorOut 不同，这里可以选择输出的是 json格式 还是 原始格式）
	 * @param message 要输出的消息。
	 * @param isConsole 是否命令行终端。如果为 true ，则直接输出，如果是 false 用 json 方式输出。
	 */
	public static void errorOut(String message, boolean isConsole) {
		if(isConsole) {
			System.out.print(message);
		}else {
			MyStrUtil.standardOutput(MSG_TYPE_ERROR, message);
		}
	}
	
	/**
	 * <p>关于消息的标准输出，消息将会以json字符串的方式，直接输出到标准输出流；不是返回字符串</p>
	 * <p>因为 success 和 error 两种输出方式 基本一致。所以共用一个实现即可。</p>
	 * <p>消息格式如下：{"status":"%s", "oriMessage":"%s", "encodeMessage":"%s"}</p>
	 * @param type 消息的类型
	 * @param message 要显示的消息
	 */
	public static final void standardOutput(String type, String message) {
		//定义一个用于输出的 map 对象
		Map<String, String> outMap = new HashMap<String, String>();
		//设置消息状态
		outMap.put("status", type);
		//设置原始消息
		outMap.put("oriMessage", message);
		//设置转格式后的消息
		String tmpMsg ;
		try {
			//在转码时，空格会被转换为 + 号，所以要替换掉
			tmpMsg = URLEncoder.encode(message, "UTF-8").replaceAll("\\+", "%20");
		} catch (Exception e) {
			//设置消息异常时的默认信息
			tmpMsg = "encode error ["+e.getMessage()+"]";
			//将异常信息，写入 jar 执行的日志文件
			mylogger.error("消息转换编码异常", e);
		}
		outMap.put("encodeMessage", tmpMsg);
		//输出信息
		ObjectMapper om = new ObjectMapper();
		try {
			//正常输出
			System.out.println(om.writeValueAsString(outMap));
		} catch (Exception e2) {
			//将异常信息，写入 jar 执行的日志文件
			mylogger.error("消息转换编码异常", e2);
			//如果转 json 失败 ，我们还是要处理，并输出 对应的信息
			String template = "{\"status\":\"%s\", \"oriMessage\":\"%s\", \"encodeMessage\":\"%s\"}";
			System.out.println(String.format(template,
					MSG_TYPE_FAULT,
					"对象转换异常，无法转换输出信息",
					"%E5%AF%B9%E8%B1%A1%E8%BD%AC%E6%8D%A2%E5%BC%82%E5%B8%B8%EF%BC%8C%E6%97%A0%E6%B3%95%E8%BD%AC%E6%8D%A2%E8%BE%93%E5%87%BA%E4%BF%A1%E6%81%AF"));
		}
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
					if(!Pattern.matches("[a-zA-Z0-9]{1,6}", uArr[i])) {
						throw new NumberFormatException("数字("+uArr[i]+")超过能接受的数字范围 (0x0 - 0x10FFFF)，请检查。");
					}
					//改进数据识别，因为有些二进制数据(比如：\u20BB7)可能是 4 字节的。
					//为了能够直接识别 ，不能用 char 来转换因为它是2字节的。
					sb.append(new String(new int[] {Integer.parseInt(uArr[i], 16)}, 0, 1));
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
			//如果转换没有结果，则返回原值，并输出异常（这里不能直接用 isEmptyString ，因为可能是空白字符）
			if(tmp!=null && tmp.length()>0) {
				re = tmp;
			}else {
				mylogger.error(String.format("将内容 unicodeString=%s，转换为 普通 字符异常。",unicodeString));
			}
		}
		//返回结果
		return re;
	}
	
	/**
	 * 绘制一行内容
	 * @param spaceNum 行首的缩进数，每4个空格为1次。即当 spaceNum 为 1，会在行首插入4个空格
	 * @param content 行的内容
	 * @param lineSepNum 行的结尾换行处理，数值为换行次数。
	 * @return 一个构造好的字符串
	 */
	public static final String writeLine(int spaceNum, String content, int lineSepNum) {
		StringBuffer sb = new StringBuffer();
		//先拼接空格，每4个一次
		for(int i=0;i<spaceNum;i++) {
			sb.append(MyWriter.space4);
		}
		//拼接内容
		sb.append(content);
		//拼接换行符
		for(int j=0;j<lineSepNum;j++) {
			sb.append(MyWriter.lineSeperator);
		}
		//返回结果
		return sb.toString();
	}
	
	/**
	 * 绘制一行内容 writeLine 方法的简化版，行首不缩进，结尾有1个换行符
	 * @param content 行的内容
	 * @return 一个构造好的字符串
	 */
	public static final String writeLine(String content) {
		return writeLine(0, content, 1);
	}
	
	/**
	 * 绘制一行内容 writeLine 方法的简化版，行首自由缩进，结尾有1个换行符
	 * @param spaceNum 行首的缩进数，每4个空格为1次。即当 spaceNum 为 1，会在行首插入4个空格
	 * @param content 行的内容
	 * @return 一个构造好的字符串
	 */
	public static final String writeLine(int spaceNum, String content) {
		return writeLine(spaceNum, content, 1);
	}
	
	/**
	 * 绘制一行内容 writeLine 方法的简化版，行首不缩进，结尾换行符自由控制
	 * @param content 行的内容 
	 * @param lineSepNum 行的结尾换行处理，数值为换行次数。
	 * @return 一个构造好的字符串
	 */
	public static final String writeLine(String content, int lineSepNum) {
		return writeLine(0, content, lineSepNum);
	}
	
	/**
	 * 打印一个对象数组的值，以逗号分割
	 * @param objs 对象数组
	 * @return 一个以逗号分隔的对象信息
	 */
	public static final String showArrayValues(Object[] objs) {
		StringBuffer re = new StringBuffer();
		if(objs!=null && objs.length>0) {
			for(int i=0;i<objs.length;i++) {
				re.append(objs[i].toString());
				if(i<objs.length-1) {
					re.append(" , ");
				}
			}
		}
		return re.toString();
	}
	
	/*
	public static void main(String[] args) {
		//原始字符串
		System.out.println("===");
		System.out.print(writeLine(3, "xxx", 2));
		System.out.println("===");
	}
	*/
}
