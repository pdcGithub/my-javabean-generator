/******************************************************************************************************

This file "MySummaryUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 这是一个简易的数字签名处理工具。它由 pdc-common-tools 移植过来。至于为什么要移植，因为输出处理上，有些不同
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年9月15日
 */
public final class MySummaryUtil {
	
	/**
	 * 私有构造函数
	 */
	private MySummaryUtil() {
		// TODO Auto-generated constructor stub
	}
	
	//摘要算法的名称
	public static final String MD5 = "MD5";
	public static final String SHA_1 = "SHA-1";
	public static final String SHA_224 = "SHA-224";
	public static final String SHA_256 = "SHA-256";
	public static final String SHA_384 = "SHA-384";
	public static final String SHA_512 = "SHA-512";
	
	/**
	 * 按照 MD5 算法，获取摘要信息
	 * @param preStr 待处理的字符串
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getMD5Info(String preStr) {
		return work(preStr, MD5);
	}
	/**
	 * 按照 MD5 算法，获取摘要信息
	 * @param filePath 待处理的文件路径
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getMD5InfoFromFile(String filePath) {
		return workForFile(filePath, MD5);
	}
	
	/**
	 * 按照 SHA_1 算法，获取摘要信息
	 * @param preStr 待处理的字符串
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_1_Info(String preStr) {
		return work(preStr, SHA_1);
	}
	/**
	 * 按照 SHA_1 算法，获取摘要信息
	 * @param filePath 待处理的文件路径
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_1_InfoFromFile(String filePath) {
		return workForFile(filePath, SHA_1);
	}
	
	/**
	 * 按照 SHA_224 算法，获取摘要信息
	 * @param preStr 待处理的字符串
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_224_Info(String preStr) {
		return work(preStr, SHA_224);
	}
	/**
	 * 按照 SHA_224 算法，获取摘要信息
	 * @param filePath 待处理的文件路径
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_224_InfoFromFile(String filePath) {
		return workForFile(filePath, SHA_224);
	}
	
	/**
	 * 按照 SHA_256 算法，获取摘要信息
	 * @param preStr 待处理的字符串
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_256_Info(String preStr) {
		return work(preStr, SHA_256);
	}
	/**
	 * 按照 SHA_256 算法，获取摘要信息
	 * @param filePath 待处理的文件路径
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_256_InfoFromFile(String filePath) {
		return workForFile(filePath, SHA_256);
	}
	
	/**
	 * 按照 SHA_384 算法，获取摘要信息
	 * @param preStr 待处理的字符串
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_384_Info(String preStr) {
		return work(preStr, SHA_384);
	}
	/**
	 * 按照 SHA_384 算法，获取摘要信息
	 * @param filePath 待处理的文件路径
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_384_InfoFromFile(String filePath) {
		return workForFile(filePath, SHA_384);
	}
	
	/**
	 * 按照 SHA_512 算法，获取摘要信息
	 * @param preStr 待处理的字符串
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_512_Info(String preStr) {
		return work(preStr, SHA_512);
	}
	/**
	 * 按照 SHA_512 算法，获取摘要信息
	 * @param filePath 待处理的文件路径
	 * @return 摘要信息。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	public static String getSHA_512_InfoFromFile(String filePath) {
		return workForFile(filePath, SHA_512);
	}
	
	/**
	 * 执行字符串的数字摘要提取工作，提取成功返回一个字符串信息（16进制的字符串信息）
	 * <p>字符串在处理时，按照 UTF-8解码</p>
	 * @param preStr 待处理的字符串
	 * @param algorithm 数字摘要的算法名
	 * @return 一个字符串信息（16进制的字符串信息）。当发生异常，会返回一个 null 值，然后记录异常信息到 日志文件 中。
	 */
	private static String work(String preStr, String algorithm) {
		String re = null;
		//待处理的字符串和算法名都不为空的时候才处理
		if(!MyStrUtil.isEmptyString(preStr) && !MyStrUtil.isEmptyString(algorithm)) {
			try {
				//按照算法名，获取处理实例
				MessageDigest digest = MessageDigest.getInstance(algorithm);
				//更新摘要信息，并提取
				digest.update(preStr.getBytes(StandardCharsets.UTF_8));
				byte[] byteRe = digest.digest();
				//将获取的摘要字节数组转化为16进制的字符串信息
				re = byteToHexString(byteRe);
			} catch (NoSuchAlgorithmException e) {
				// 记录到日志
				MyStrUtil.mylogger.error("数字摘要提取处理，传入的算法名错误，算法名："+algorithm+"，字符串："+preStr, e);
			} catch (Exception e1) {
				// 记录到日志
				MyStrUtil.mylogger.error("数字摘要提取处理，发生其它异常："+e1.getMessage()+"，请检查日志", e1);
			}
		}else {
			MyStrUtil.mylogger.error("执行发生异常，接收到的 待处理字符串 或者 加密算法信息 为空。");
		}
		return re;
	}
	
	/**
	 * 执行文件的数字摘要提取工作，提取成功返回一个字符串信息（16进制的字符串信息）
	 * @param filePath 文件路径
	 * @param algorithm 加密算法名称
	 * @return 一个字符串信息（16进制的字符串信息）当然，如果执行时判断条件没触发，会返回一个 null 值。
	 * @throws Exception 如果有异常，会记录到日志文件，并且抛出
	 */
	private static String workForFile(String filePath, String algorithm) {
		String re = null;
		
		//文件路径不能为空；算法名不能为空
		if(MyStrUtil.isEmptyString(filePath) || MyStrUtil.isEmptyString(algorithm)) {
			MyStrUtil.mylogger.error("执行发生异常，接收到的 待文件路径 或者 加密算法信息 为空。");
			return re;
		}
		
		try {
			//按照算法名，获取处理实例
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			// 更新摘要信息，并提取
			// 将文件以 字节 的方式处理
			ByteBuffer buffer = MyFileUtil.loadByteBufferFromFile(filePath);
			if(buffer!=null) {
				digest.update(buffer);
			}else {
				throw new Exception("文件["+filePath+"] 转为字节数据异常，获取到的结果为："+buffer);
			}
			//
			byte[] byteRe = digest.digest();
			//将获取的摘要字节数组转化为16进制的字符串信息
			re = byteToHexString(byteRe);
		} catch (NoSuchAlgorithmException e) {
			MyStrUtil.mylogger.error("数字摘要提取处理，传入的算法名错误，算法名："+algorithm+"，文件路径："+filePath, e);
		} catch (Exception e1) {
			MyStrUtil.mylogger.error("数字摘要提取处理，发生其它异常："+e1.getMessage(), e1);
		}
		
		return re;
	}
	
	/**
	 * 将字节数组转换成16进制的字符串
	 * @param bytes 待转换的字节数组
	 * @return 16进制的字符串
	 */
	private static String byteToHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		if(bytes!=null) {
			for(byte b : bytes) {
				//java.lang.Integer.toHexString() 方法的参数是int(32位)类型，
				//如果输入一个byte(8位)类型的数字，这个方法会把这个数字的高24为也看作有效位，就会出现错误
				//如果使用& 0XFF操作，可以把高24位置0以避免这样错误
				String tmp = Integer.toHexString(b & 0xFF);
				if(tmp.length()==1) {
					tmp = "0"+tmp;
				}
				buffer.append(tmp);
			}
		}
		return buffer.toString();
	}

}