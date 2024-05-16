/******************************************************************************************************

This file "FileUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import net.mickarea.generator.models.ValidResult;

/**
 * &gt;&gt;&nbsp;文件读写操作工具类（默认的文件读写字符集为 UTF-8）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月16日
 */
public final class MyFileUtil {
	
	/**
	 * &gt;&gt;&nbsp;默认的字符集，UTF-8
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 构造函数私有化，防止创建对象
	 */
	private MyFileUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @param isAppend 是否为追加模式。如果为true，内容将以追加形式写入
	 * @param charset 写入文件时的指定字符集
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static ValidResult saveToLocalpath(String text, String dir, String fileName, boolean isAppend, String charset) {
		
		ValidResult result = new ValidResult();
		
		//判断传入的参数是否有效
		if(MyStrUtil.isEmptyString(text) || MyStrUtil.isEmptyString(dir) || MyStrUtil.isEmptyString(fileName) || MyStrUtil.isEmptyString(charset)) {
			result.setMessage("save file error, params invalid (text="+text+"), (dir="+dir+"), (fileName="+fileName+"), (charset="+charset+")");
			result.setValid(false);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//文件夹处理，如果文件夹不存在，则创建
		File fileDir = new File(dir);
		try {
			if(!fileDir.exists()) {
				fileDir.mkdirs();
			}
		}catch(Exception e) {
			result.setMessage("mkdir error, "+e.getMessage()+", please check (dir="+fileDir.getAbsolutePath()+")");
			result.setValid(false);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//目标文件处理，如果文件不存在，则创建
		File fileTarget = new File(dir+File.separator+fileName);
		try {
			if(!fileTarget.exists()) {
				fileTarget.createNewFile();
			}
		}catch(Exception e) {
			result.setMessage("create file error, "+e.getMessage()+", please check (file="+fileTarget.getAbsolutePath()+")");
			result.setValid(false);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//判断文件是否可写
		boolean canWrite = true;
		try {
			canWrite = fileTarget.canWrite();
		}catch(Exception e) {
			canWrite = false;
			result.setMessage("check writable error (file="+fileTarget.getAbsolutePath()+"), "+e.getMessage());
			result.setValid(false);
			//直接返回，避免还继续执行。
			return result;
		}
		
		//开始目标文件写入
		if(canWrite) {
			BufferedWriter bw = null;
			OutputStreamWriter outw = null;
			FileOutputStream fout = null;
			try {
				fout = new FileOutputStream(fileTarget, isAppend);
				outw = new OutputStreamWriter(fout, charset);//这里指定了字符集
				bw = new BufferedWriter(outw);
				//写信息
				bw.write(text);
				bw.flush();
				//设置完成标志
				result.setMessage("");
				result.setValid(true);
				
			} catch (Exception e) {
				result.setMessage("failed to write to file=("+fileTarget.getAbsolutePath()+"), "+e.getMessage());
				result.setValid(false);
			} finally {
				//文件写入工具，资源回收
				if(bw!=null) { try {bw.close();} catch (Exception e1) {} bw = null; }
				if(outw!=null) { try {outw.close();} catch (Exception e2) {} outw = null; }
				if(fout!=null) { try {fout.close();} catch (Exception e3) {} fout = null; }
			}
		} else {
			result.setMessage("file=("+fileTarget.getAbsolutePath()+") could not be written.");
			result.setValid(false);
		}
		
		//最终返回结果
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件（可配置是否以追加形式写入）
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @param isAppend 是否为追加模式。如果为true，内容将以追加形式写入
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static ValidResult saveToLocalpath(String text, String dir, String fileName, boolean isAppend) {
		return saveToLocalpath(text, dir, fileName, isAppend, DEFAULT_CHARSET);
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static ValidResult saveToLocalpath(String text, String dir, String fileName) {
		return saveToLocalpath(text, dir, fileName, false, DEFAULT_CHARSET);
	}
	
	/**
	 * &gt;&gt;&nbsp;将字符串内容写入到指定路径的文件
	 * @param text 待写入内容
	 * @param dir 指定的文件夹
	 * @param fileName 指定的文件名
	 * @param charset 写入文件时的指定字符集
	 * @return 如果程序执行成功，则返回true；否则，返回false。（如果执行过程中有什么异常信息，会以标准方式输出）
	 */
	public static ValidResult saveToLocalpath(String text, String dir, String fileName, String charset) {
		return saveToLocalpath(text, dir, fileName, false, charset);
	}
	
}
