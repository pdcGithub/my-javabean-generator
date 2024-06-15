/******************************************************************************************************

This file "MainController.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts;

import java.lang.reflect.Method;

import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.SqlResult;
import net.mickarea.generator.models.ValidResult;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月11日-2024年6月12日
 */
public class MainController {
	
	/**
	 * 针对不同 数据库处理的 控制器 所在的 包路径
	 */
	public static final String CONTROLLER_PACKAGE_PATH = "net.mickarea.generator.opts.controllers";

	/**
	 * 命令行传来的参数
	 */
	private CommandArguments cArgs = null;

	/**
	 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
	 */
	public MainController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
	 * @param cArgs 命令行传来的参数
	 */
	public MainController(CommandArguments cArgs) {
		super();
		this.cArgs = cArgs;
	}
	
	// getter and setter .
	
	public CommandArguments getcArgs() {
		return cArgs;
	}
	public void setcArgs(CommandArguments cArgs) {
		this.cArgs = cArgs;
	}
	
	/**
	 * 数据库连接测试
	 */
	public void testConnection() {
		//当前方法名
		String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		//开始处理
		this.normalExecMethod(this.cArgs, currentMethodName);
	}

	/**
	 * 数据字典查询（获取当前数据库有什么表、有什么视图）
	 */
	public void searchDictInfo() {
		//当前方法名
		String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		//开始处理
		this.normalExecMethod(this.cArgs, currentMethodName);
	}

	/**
	 * Java Bean 文件生成
	 */
	public void genJavaEntityFile() {
		//当前方法名
		String currentMethodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		//开始处理
		this.normalExecMethod(this.cArgs, currentMethodName);
	}
	
	/**
	 * 这是一个通用的调取函数，因为 数据库连接、获取数据字典 以及 生成 Java 文件 有类似的代码。所以单独抽取出来。
	 * @param cArgs 命令行传来的参数
	 * @param methodName 要执行的方法名称
	 */
	private void normalExecMethod(CommandArguments cArgs, String methodName) {
		//开始处理
		try {
			//如果参数尚未初始化，则提示
			if(cArgs==null) {
				throw new Exception("the args from command is null.");
			}
			//参数校验
			ValidResult validRe = cArgs.valid();
			if(!validRe.getValid()) {
				throw new Exception(validRe.getMessage());
			}
			//构造调用的类 和 函数名
			String tmpName = MyStrUtil.makeHumpString(cArgs.getDatabaseType());
			String className = String.format("%s.%s%s", CONTROLLER_PACKAGE_PATH, tmpName, "Controller");
			//构建出要执行的类
			Class<?> cls = Class.forName(className);
			//构建出要执行的方法
			Method method = cls.getMethod(methodName, CommandArguments.class);
			//反射调用方法
			Object clsObj = cls.newInstance();
			Object resultObj = method.invoke(clsObj, cArgs);
			//获取结果
			SqlResult sqlRe = (SqlResult)resultObj;
			if(sqlRe.getOk()) {
				MyStrUtil.successOut(sqlRe.getData());
			}else {
				throw new Exception(sqlRe.getMessage());
			}
		} catch(Exception e) {
			//打印异常信息到文件
			MyStrUtil.mylogger.error("[执行处理方法 "+methodName+" 异常]", e);
			//将异常信息处理一下输出到标准输出流
			if(e instanceof ClassNotFoundException) {
				MyStrUtil.errorOut("class "+e.getMessage()+" not found, database type ["+cArgs.getDatabaseType()+"] not supported.");
			}else if(e instanceof NoSuchMethodException) {
				MyStrUtil.errorOut("method "+methodName+" of Controller not found.");
			}else {
				MyStrUtil.errorOut("some error was found ["+e.getMessage()+"], check log file for more details.");
			}
		}
	}
	
}
