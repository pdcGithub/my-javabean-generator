/******************************************************************************************************

This file "MyFeaturePageWriter.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts;

import java.io.File;
import java.time.LocalDateTime;

import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.NewCommToolArgs;
import net.mickarea.generator.models.ValidResult;
import net.mickarea.generator.utils.MyFileUtil;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 这是一个生成功能模板代码的处理程序。Page 类的生成
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年5月8日-2025年5月9日
 */
public class MyFeaturePageWriter {
	
	/**
	 * 要生成的 Action 类的名字。
	 */
	private String actionName = null;
	
	/**
	 * 文件保存时，使用的字符集名称
	 */
	private String charset = null;
	
	/**
	 * 文件保存时，存放的文件夹位置
	 */
	private String fileDirectory  = null;
	
	/**
	 * 生成的 Java 类名称
	 */
	private String className = null;
	
	/**
	 * 这个是构造函数，主要用于一些常用的参数赋值以及简单的转换（在构建之前，必须先校验。由于的入口程序已经校验，所以这里不校验）
	 * @param args 命令行接收到的参数对象
	 */
	public MyFeaturePageWriter(NewCommToolArgs args) {
		this.actionName = args.actionClassName;
		this.charset = args.fileCharset;
		this.fileDirectory = args.entityDir;
		this.className = this.actionName+"Page";
	}

	/**
	 * 这里是文件生成的处理。这里借用 GenResult 作为返回结果。它将指定 操作结果，文件路径等内容
	 * @return GenResult 结果对象
	 */
	public GenResult genJavaClassFile() {
		
		//定义一个字符串缓冲类
		StringBuffer sb = new StringBuffer();
		
		//完整的文件路径
		String filePath = this.fileDirectory + File.separator + this.className + ".java";
		
		//============================ 开始拼接 ==============================
		// 0. 文件注释
		sb.append(MyWriter.writeFileHeader(this.className));
		// 1. 包信息
		sb.append(MyStrUtil.writeLine("package net.mickarea;", 2));
		// 2. 引入的类信息
		sb.append(this.writeImportInfo());
		// 3. 类的内容
		sb.append(this.writeClassInfo());
		//====================================================================
		
		//保存文件
		ValidResult fileResult = MyFileUtil.saveToLocalpath(sb.toString(), this.fileDirectory, this.className+".java", false, this.charset);
		
		//定义返回结果
		GenResult re = new GenResult("", this.className, fileResult.getValid(), fileResult.getMessage(), filePath, 0);
		
		//返回结果
		return re;
	}
	
	/**
	 * 导入的类信息
	 * @return 导入的类信息
	 */
	private String writeImportInfo() {
		//
		StringBuffer sb = new StringBuffer();
		sb.append(MyStrUtil.writeLine("import javax.servlet.http.HttpServletRequest;"));
		sb.append(MyStrUtil.writeLine("import javax.servlet.http.HttpServletResponse;", 2));
		//
		sb.append(MyStrUtil.writeLine("import net.mickarea.constants.ControllerConstants;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.constants.web.annotation.MyWebParam;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.niceweb.pagetemps.MyHtml5PageV2;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.niceweb.utils.MyWebParamUtil;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.webtools.ui.models.Ht;", 2));
		//
		return sb.toString();
	}
	
	/**
	 * 类的内容
	 * @return 类的内容
	 */
	private String writeClassInfo() {
		//
		StringBuffer sb = new StringBuffer();
		
		LocalDateTime time = LocalDateTime.now();
		String dateTime = time.getYear()+"年"+time.getMonth().getValue()+"月"+time.getDayOfMonth()+"日";
		
		// 类注释
		sb.append(MyStrUtil.writeLine("/**"));
		sb.append(MyStrUtil.writeLine(" * <p>这个是 xxxxxx 信息展示。</p>"));
		sb.append(MyStrUtil.writeLine(" * @author Michael Pang (Dongcan Pang)"));
		sb.append(MyStrUtil.writeLine(" * @version 1.0"));
		sb.append(MyStrUtil.writeLine(" * @since "+dateTime));
		sb.append(MyStrUtil.writeLine(" */"));
		// 类名
		sb.append(MyStrUtil.writeLine("public class "+this.className+" extends MyHtml5PageV2 {", 2));
		// 属性
		sb.append(MyStrUtil.writeLine(1, "//这是一个样例参数，仅作参考"));
		sb.append(MyStrUtil.writeLine(1, "@MyWebParam"));
		sb.append(MyStrUtil.writeLine(1, "public String testParam1 = \"default\";", 2));
		// 构造方法
		sb.append(MyStrUtil.writeLine(1, "/**"));
		sb.append(MyStrUtil.writeLine(1, " * 构造函数，用于构建页面。功能比如：页面的参数初始化之类的。"));
		sb.append(MyStrUtil.writeLine(1, " * @param request http 请求对象"));
		sb.append(MyStrUtil.writeLine(1, " * @param response http 响应对象"));
		sb.append(MyStrUtil.writeLine(1, " * @param title 页面标题"));
		sb.append(MyStrUtil.writeLine(1, " * @throws Exception 如果初始化出错，则抛出异常。这些异常在 Action 类有捕捉"));
		sb.append(MyStrUtil.writeLine(1, " */"));
		sb.append(MyStrUtil.writeLine(1, "public "+this.className+"(HttpServletRequest request, HttpServletResponse response, String title) throws Exception {"));
		sb.append(MyStrUtil.writeLine(2, "//父类信息初始化"));
		sb.append(MyStrUtil.writeLine(2, "super(request, response, title, "+this.className+".class);"));
		sb.append(MyStrUtil.writeLine(2, "//子类初始化（属性自动装载）"));
		sb.append(MyStrUtil.writeLine(2, "MyWebParamUtil.paramsAutoLoaded(this, request);"));
		sb.append(MyStrUtil.writeLine(2, "//分页参数，需要放在属性初始化后"));
		sb.append(MyStrUtil.writeLine(2, "//this.pageInfo = new PageInfo(this.toPage, this.pagingNumber);"));
		sb.append(MyStrUtil.writeLine(1, "}", 2));
		// getSubbodyInfo 方法
		sb.append(MyStrUtil.writeLine(1, "@Override"));
		sb.append(MyStrUtil.writeLine(1, "public String getSubbodyInfo() {"));
		sb.append(MyStrUtil.writeLine(2, "//输出缓存"));
		sb.append(MyStrUtil.writeLine(2, "StringBuffer buffer = new StringBuffer();"));
		sb.append(MyStrUtil.writeLine(2, "return buffer.toString();"));
		sb.append(MyStrUtil.writeLine(1, "}", 2));
		//
		sb.append("}");
		//
		return sb.toString();
	}
}
