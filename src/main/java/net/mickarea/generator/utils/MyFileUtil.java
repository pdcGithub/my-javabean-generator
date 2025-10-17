/******************************************************************************************************

This file "FileUtil.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.regex.Pattern;

import net.mickarea.generator.beans.TabOrViewTmpObj;
import net.mickarea.generator.constants.MyConstants.IMAGE_TYPE;
import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.ValidResult;
import net.mickarea.generator.opts.MyWriter;

/**
 * &gt;&gt;&nbsp;文件读写操作工具类（默认的文件读写字符集为 UTF-8）
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月16日-2025年10月17日
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
	
	/**
	 * 根据从数据库获取的元数据信息，构造一个 Java Bean 类 文件（由于表名、文件夹路径、字符集，这些参数在前面的程序中已经校验过，这里不再校验）
	 * @param tmpObjs 关于数据库对象的元数据信息
	 * @param tabName 数据库对象名（表名或者视图名，如果是sql语句映射，则为一个随机名）
	 * @param fileDir 文件生成后，保存的文件夹
	 * @param charSet 文件生成时，使用的字符集
	 * @param dbTakes 数据库处理耗时（毫秒）
	 * @return 一个生成结果对象，内包含一些信息
	 */
	public static GenResult genJavaBeanFileFromDictInfo(List<TabOrViewTmpObj> tmpObjs, String tabName, String fileDir, String charSet, long dbTakes) {
		//定义返回结果
		GenResult result = null;
		
		//判断是要生成实体(java bean)，还是要生成虚拟实体(virtual entity bean)
		boolean isVo = false;
		if(Pattern.matches("sql\\_\\d+", tabName)) {
			isVo = true;
		}
		
		//实体名称
		String entityName = MyStrUtil.genNameFromTable(tabName)+(isVo?"VO":"");
		//实体文件名成
		String entityFileName = entityName+".java";
		//实体文件路径
		String entityFilePath = fileDir + File.separator + entityFileName ;
		
		//开始构造文件
		if(tmpObjs==null || tmpObjs.size()<=0) {
			result = new GenResult(tabName, entityName, false, "cannot found any dic info of table", "", dbTakes);
			return result;
		}
		
		//构造内容
		StringBuffer sb = new StringBuffer();
		sb.append(MyWriter.writeFileHeader(entityName));
		sb.append(MyWriter.writePackageAndImport());
		sb.append(MyWriter.writeTableCode(entityName, (isVo?null:tabName), tmpObjs));
		
		//写文件
		ValidResult myRe = saveToLocalpath(sb.toString(), fileDir, entityFileName, charSet);
		if(myRe.getValid()) {
			result = new GenResult(tabName, entityName, true, "", entityFilePath, dbTakes);
		}else {
			//如果文件保存异常，这里会返回一些异常信息
			result = new GenResult(tabName, entityName, false, myRe.getMessage(), entityFilePath, dbTakes);
		}
		
		return result;
	}
	
	/**
	 * 获取文件路径字符串中的文件名后缀
	 * @param filename 文件路径字符串
	 * @return 如果找到后缀，则返回，否则，返回空字符串。这里取的是后缀，不带 点号 ，字符串默认为小写。
	 */
	public static final String getFileExtension(String filename) {
		String re = "";
		//如果字符串不为空，则处理
		if(!MyStrUtil.isEmptyString(filename)) {
			//找出最后一个点
			int lastIndex = filename.lastIndexOf(".");
			//有一种情况，就是 点是有的，但是没有后缀 。 Orz 。
			if(lastIndex>=0 && lastIndex<filename.length()-1) {
				// 这里取的是后缀，不带 点号 。
				re = filename.substring(lastIndex+1).toLowerCase();
			}
		}
		return re;
	}
	
	/**
	 * 获取文件对象中的文件名后缀
	 * @param f 文件对象
	 * @return 如果找到后缀，则返回，否则，返回空字符串。
	 */
	public static final String getFileExtension(File f) {
		String re = "";
		if(f!=null && f.exists() && f.isFile()) {
			re = getFileExtension(f.getName());
		}
		return re;
	}
	
	/**
	 * 判断文件路径所对应的文件类型，是否为符合要求的图片类型。
	 * @param filepath 文件路径
	 * @return 如果符合返回 true，否则返回 false
	 */
	public static final boolean isImageExtensionOk(String filepath) {
		boolean re = false;
		//先获取后缀，返回的结果可能为空字符串
		String extension = getFileExtension(filepath);
		//不为空，则开始判断类型是否可用
		if(!MyStrUtil.isEmptyString(extension)) {
			//看后缀是否符合要求
			try {
				IMAGE_TYPE.valueOf(extension.toUpperCase());
				re = true;
			}catch(Exception e) {
			}
		}
		//
		return re;
	}
	
	/**
	 * 判断文件对象的文件类型，是否为符合要求的图片类型。
	 * @param imageFile 文件对象
	 * @return 如果符合返回 true，否则返回 false
	 */
	public static final boolean isImageExtensionOk(File imageFile) {
		boolean re = false;
		//首先要判断文件对象是否可用
		if(imageFile!=null && imageFile.exists() && imageFile.isFile()) {
			//然后根据文件路径判断
			re = isImageExtensionOk(imageFile.getName());
		}
		//
		return re;
	}
	
	/**
	 * <p>将给定地址的文件，以字节数组的方式返回。（这里不抛异常，只返回 null 作为异常标识，异常信息会记录到日志文件）</p>
	 * <p>注意：如果文件很大，大于 500MB，建议换一个实现方式。因为，这里是一次性读入内存，会内存溢出。</p>
	 * @param filePath 文件地址
	 * @return 字节数组形式的文件对象。如果文件异常（比如：文件不存在、无法读取、流处理报错等），则返回 null
	 */
	public static final byte[] loadByteArrayFromFile(String filePath) {
		//
		byte[] re = null;
		//
		if(filePath==null) {
			MyStrUtil.mylogger.error("读取文件异常，文件路径为 null：");
			return re;
		}
		//创建文件对象，判断文件是否存在
		File targetFile = new File(filePath);
		if(!targetFile.exists()) {
			MyStrUtil.mylogger.error(String.format("读取文件异常，文件[%s]不存在：", filePath));
			return re;
		}
		//判断文件是否可读
		if(!targetFile.canRead()) {
			MyStrUtil.mylogger.error(String.format("读取文件异常，文件[%s]无法读取：", filePath));
			return re;
		}
		// 开始处理
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;
		try {
			//将文件识别为 文件流
			fis = new FileInputStream(filePath);
			//初始化 输出用的字节流
			baos = new ByteArrayOutputStream();
			//设置缓冲区
			int buffSize = 1024*8;
			int len = 0;
			byte[] buffer = new byte[buffSize];
			//开始读写
			while((len=fis.read(buffer))!=-1) {
				baos.write(buffer, 0 , len);
			}
			//结果转化
			re = baos.toByteArray();
		} catch (Exception e) {
			// 记录异常
			MyStrUtil.mylogger.error(String.format("读取文件[%s]为 ByteBuffer 时发生异常。", filePath), e);
		} finally {
			if(baos!=null) {
				try {baos.close(); } catch (Exception e1) { }
				baos=null;
			}
			if(fis!=null) {
				try {fis.close(); } catch (Exception e2) { }
				fis=null;
			}
		}
		
		//
		return re;
	}
	
	/**
	 * <p>将给定地址的文件，以 ByteBuffer 的方式返回。（这里不抛异常，只返回 null 作为异常标识，异常信息会记录到日志文件）</p>
	 * <p>注意：如果文件很大，大于 500MB，建议换一个实现方式。因为，这里是一次性读入内存，会内存溢出。</p>
	 * @param filePath 文件地址
	 * @return ByteBuffer 对象。如果文件异常（比如：文件不存在、无法读取、流处理报错等），则返回 null
	 */
	public static final ByteBuffer loadByteBufferFromFile(String filePath) {
		ByteBuffer re = null;
		byte[] byteArray = loadByteArrayFromFile(filePath);
		if(byteArray!=null) {
			//初始化一个同样大小的缓冲区
			ByteBuffer buffer = ByteBuffer.allocate(byteArray.length);
			// 放入缓冲区
			buffer.put(byteArray);
			// 充值缓冲区
			buffer.flip();
			//
			re = buffer;
		}
		return re;
	}
	
}
