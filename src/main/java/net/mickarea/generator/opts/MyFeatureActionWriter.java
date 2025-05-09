/******************************************************************************************************

This file "MyFeatureActionWriter.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
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
 * 这是一个生成功能模板代码的处理程序。Action 类 的生成
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年5月7日-2025年5月9日
 */
public class MyFeatureActionWriter {

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
	 * 生成的 Java 页面类名称
	 */
	private String pageClassName = null;
	
	/**
	 * 这个是构造函数，主要用于一些常用的参数赋值以及简单的转换（在构建之前，必须先校验。由于的入口程序已经校验，所以这里不校验）
	 * @param args 命令行接收到的参数对象
	 */
	public MyFeatureActionWriter(NewCommToolArgs args) {
		this.actionName = args.actionClassName;
		this.charset = args.fileCharset;
		this.fileDirectory = args.entityDir;
		this.className = this.actionName+"Action";
		this.pageClassName = this.actionName+"Page";
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
		sb.append(MyStrUtil.writeLine("import javax.servlet.http.HttpServletResponse;", 2));
		//
		sb.append(MyStrUtil.writeLine("import net.mickarea.constants.ControllerConstants;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.webtools.ui.models.BusinessAction;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.constants.web.annotation.MyWebParam;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.constants.web.objs.ResultForJson;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.niceweb.beans.tables.BiUsers;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.niceweb.utils.MyWebAppUtil;"));
		sb.append(MyStrUtil.writeLine("import net.mickarea.niceweb.utils.MyWebParamUtil;", 2));
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
		sb.append(MyStrUtil.writeLine(" * <p>可能的访问路径 xxxxx</p>"));
		sb.append(MyStrUtil.writeLine(" * @author Michael Pang (Dongcan Pang)"));
		sb.append(MyStrUtil.writeLine(" * @version 1.0"));
		sb.append(MyStrUtil.writeLine(" * @since "+dateTime));
		sb.append(MyStrUtil.writeLine(" */"));
		
		// 类名
		sb.append(MyStrUtil.writeLine("public class "+this.className+" extends BusinessAction {", 2));
		// 属性
		sb.append(MyStrUtil.writeLine(1, "//这是一个样例参数，仅作参考"));
		sb.append(MyStrUtil.writeLine(1, "@MyWebParam"));
		sb.append(MyStrUtil.writeLine(1, "public String testParam1 = \"default\";", 2));
		// output 方法
		sb.append(MyStrUtil.writeLine(1, "/**"));
		sb.append(MyStrUtil.writeLine(1, " * output 函数，是默认的对外输出函数。构造好的html内容，通过这个函数输出。"));
		sb.append(MyStrUtil.writeLine(1, " * <p>当然也可以用其它函数名，只是需要在 url 上添加参数。比如：https://localhost/zzz/xxxx/yyyy.shtml?func=output222</p>"));
		sb.append(MyStrUtil.writeLine(1, " * <p>这样执行时，框架会自动匹配当前Action的 output222 方法</p>"));
		sb.append(MyStrUtil.writeLine(1, " * @return 返回的字符串，一般是 html 内容（这个是页面生成方法，负责整个主题页面构建。至于其它的内容，由js负责）"));
		sb.append(MyStrUtil.writeLine(1, " */"));
		sb.append(MyStrUtil.writeLine(1, "public String output() throws Exception {"));
		sb.append(MyStrUtil.writeLine(2, "try {"));
		sb.append(MyStrUtil.writeLine(3, "BiUsers currentUser = MyWebAppUtil.getLoginUser(getRequest());"));
		sb.append(MyStrUtil.writeLine(3, "String re = \"\";"));
		sb.append(MyStrUtil.writeLine(3, "if(currentUser!=null) {"));
		sb.append(MyStrUtil.writeLine(4, this.pageClassName+" page = new "+this.pageClassName+"(this.getRequest(), this.getResponse(), ControllerConstants.WEBSITE_NAME);"));
		sb.append(MyStrUtil.writeLine(4, "re = page.toHtmlString();"));
		sb.append(MyStrUtil.writeLine(3, "} else {"));
		sb.append(MyStrUtil.writeLine(4, "this.getRequest().setAttribute(\"errorInfo\", \"异常：没有权限访问这个内容\");"));
		sb.append(MyStrUtil.writeLine(4, "this.getRequest().setAttribute(\"errorException\", new Exception(\"异常：没有权限访问这个内容\"));"));
		sb.append(MyStrUtil.writeLine(4, "this.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN, \"异常：没有权限访问这个内容\");"));
		sb.append(MyStrUtil.writeLine(3, "}"));
		sb.append(MyStrUtil.writeLine(3, "return re;"));
		sb.append(MyStrUtil.writeLine(2, "}catch(Exception e) {"));
		sb.append(MyStrUtil.writeLine(3, "throw e; //这里不需要处理，只需要打印是什么错误即可，框架有异常捕捉页面。"));
		sb.append(MyStrUtil.writeLine(2, "}"));
		sb.append(MyStrUtil.writeLine(1, "}", 2));
		// 一个 json 方法
		sb.append(MyStrUtil.writeLine(1, "/**"));
		sb.append(MyStrUtil.writeLine(1, " * 这是一个样例方法。它是 js 服务请求响应用的。如不用可删除。"));
		sb.append(MyStrUtil.writeLine(1, " * @return json 格式的信息字符串"));
		sb.append(MyStrUtil.writeLine(1, " */"));
		sb.append(MyStrUtil.writeLine(1, "public String demoFunc() throws Exception {"));
		sb.append(MyStrUtil.writeLine(2, "//定义一个 json 结果"));
		sb.append(MyStrUtil.writeLine(2, "ResultForJson jsonRe = new ResultForJson();"));
		sb.append(MyStrUtil.writeLine(2, "//自动装载"));
		sb.append(MyStrUtil.writeLine(2, "MyWebParamUtil.paramsAutoLoaded(this, getRequest());"));
		sb.append(MyStrUtil.writeLine(2, "//返回 json 结果"));
		sb.append(MyStrUtil.writeLine(2, "return jsonRe.toString();"));
		sb.append(MyStrUtil.writeLine(1, "}", 2));
		
		sb.append("}");
		//
		return sb.toString();
	}
}
