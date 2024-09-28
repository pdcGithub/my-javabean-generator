/******************************************************************************************************

This file "App.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.entrance;

import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.opts.MainController;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 这是一个Java代码生成器的入口程序
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月8日-2024年9月28日
 */
public class App {

	/**
	 * 程序入口方法
	 * @param args
	 */
	public static void main(String[] args) {
		//根据不同的参数，执行不同的处理
		if(args!=null && args.length>0) {
			switch(args.length) {
			case 10:
				//如果参数等于10个，则创建连接池，测试数据库是否连通
				CommandArguments cArgs = new CommandArguments(args[0], args[1], args[2], args[3], args[4], args[5], 
						args[6], args[7], args[8], args[9]);
				//构建主控制器
				MainController mainCtrl = new MainController(cArgs);
				//数据库连接测试
				mainCtrl.testConnection();
				break;
			case 12:
				//如果参数等于12个，则获取数据库表、视图信息
				CommandArguments cArgs2 = new CommandArguments(args[0], args[1], args[2], args[3], args[4], args[5], 
						args[6], args[7], args[8], args[9], args[10], args[11]);
				//构建主控制器
				MainController mainCtrl2 = new MainController(cArgs2);
				//数据字典查询（获取当前数据库有什么表、有什么视图）
				mainCtrl2.searchDictInfo();
				break;
			case 16:
				//如果参数等于16个，则构建实体对象
				CommandArguments cArgs3 = new CommandArguments(args[0], args[1], args[2], args[3], args[4], args[5], 
						args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14], args[15]);
				//构建主控制器
				MainController mainCtrl3 = new MainController(cArgs3);
				//数据字典查询（获取当前数据库有什么表、有什么视图）
				mainCtrl3.genJavaEntityFile();
				break;
			default:
				//参数不齐全，则无法处理
				MyStrUtil.errorOut("jar 文件所获取的参数不全，请检查。");
			}
		}else {
			//如果没有入口参数，则返回一段欢迎信息
			MyStrUtil.successOut("欢迎使用 mickarea.net 出品! 这个 Java 程序包可以完美运行!");
		}
	}

}
