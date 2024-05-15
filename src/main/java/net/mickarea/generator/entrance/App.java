/******************************************************************************************************

This file "App.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.entrance;

import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.SqlResult;
import net.mickarea.generator.models.ValidResult;
import net.mickarea.generator.utils.DBUtil;

/**
 * &gt;&gt;&nbsp;这是一个Java代码生成器的入口程序
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月8日-2024年5月15日
 */
public class App {

	/**
	 * &gt;&gt;&nbsp;入口方法
	 * @param args
	 */
	public static void main(String[] args) {
		
		//根据不同的参数，执行不同的处理
		if(args!=null && args.length>0) {
			try {
				switch(args.length) {
				case 10:
					//如果参数等于10个，则创建连接池，测试数据库是否连通
					CommandArguments cArgs = new CommandArguments(args[0], args[1], args[2], args[3], args[4], args[5], 
							args[6], args[7], args[8], args[9]);
					//参数校验
					ValidResult vResult = cArgs.valid();
					if(!vResult.getValid()) {
						throw new Exception(vResult.getMessage());
					}
					//数据库连接测试
					SqlResult sResult = DBUtil.testDBConnection(cArgs);
					if(sResult.getOk()) {
						System.out.println("success:database connnection is ok.");
					}else {
						throw new Exception(sResult.getMessage());
					}
					break;
				case 12:
					//如果参数等于12个，则获取数据库表、视图信息
					CommandArguments cArgs2 = new CommandArguments(args[0], args[1], args[2], args[3], args[4], args[5], 
							args[6], args[7], args[8], args[9], args[10], args[11]);
					//参数校验
					ValidResult vResult2 = cArgs2.valid();
					if(!vResult2.getValid()) {
						throw new Exception(vResult2.getMessage());
					}
					//数据库库表获取处理
					SqlResult sResult2 = DBUtil.getDatabaseObjects(cArgs2);
					if(sResult2.getOk()) {
						System.out.println("success:"+sResult2.getData());
					}else {
						throw new Exception(sResult2.getMessage());
					}
					break;
				case 15:
					//如果参数等于15个，则构建实体对象
					CommandArguments cArgs3 = new CommandArguments(args[0], args[1], args[2], args[3], args[4], args[5], 
							args[6], args[7], args[8], args[9], args[10], args[11], args[12], args[13], args[14]);
					//参数校验
					ValidResult vResult3 = cArgs3.valid();
					if(!vResult3.getValid()) {
						throw new Exception(vResult3.getMessage());
					}
					//构建实体对象
					
					break;
				default:
					//参数不齐全，则无法处理
					throw new Exception("jar file need more parameters.");
				}
			} catch (Exception e) {
				System.out.println("error:"+e.getMessage());
			}
		}else {
			//如果没有入口参数，则返回一段欢迎信息
			System.out.println("success:Welcome! The Jar works perfectly!");
		}
		
	}

}
