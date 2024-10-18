/******************************************************************************************************

This file "MSSQLTestApp.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.dbtest;

import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.DatabasePoolManager;
import net.mickarea.generator.models.SimpleDBData;
import net.mickarea.generator.utils.MyDBUtil;

/**
 * 关于 Microsoft SQL Server 的链接测试
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年10月18日
 */
public class MSSQLTestApp {

	/**
	 * 测试的主方法，主要用于程序代码的测试。因为是测试程序，它抛出所有异常，方便调试
	 * @param args 命令行入参，一般不需要处理
	 */
	public static void main(String[] args) throws Exception {
		
		CommandArguments cParams = new CommandArguments();
		//连接池的名称，随便写一个即可
		cParams.setPoolName("MSSQLLocal001");
		//JDBC 驱动名
		cParams.setJdbcDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		//JDBC 连接字符串
		//由于连接默认是使用 TLS 加密的，所以要配上参数 encrypt 和 trustServerCertificate
		//如果不需要 TLS 加密，直接 配置 encrypt=false 即可
		cParams.setJdbcUrl("jdbc:sqlserver://localhost:1433;DatabaseName=pdc_test;encrypt=true;trustServerCertificate=true");
		//数据库用户（一般使用普通用户，别用管理员用户）
		cParams.setDbUser("pdc");
		//数据库用户的密码
		cParams.setDbPasswd("1234567890-=");
		//数据库处理是否 自动提交事务
		cParams.setIsAutoCommit("false");
		//数据库链接超时阈值（毫秒）
		cParams.setConnTimeout("5000");
		//连接池，最小连接数（线程数）
		cParams.setMinThreadNum("10");
		//连接池，最大连接数（线程数）
		cParams.setMaxThreadNum("50");
		
		//开始连接数据库
		DatabasePoolManager dbMana = DatabasePoolManager.getInstance(cParams);
		//执行数据库查询
		SimpleDBData sdb = MyDBUtil.mySqlQuery(dbMana.getDataPoolByName(cParams.getPoolName()), "select 1 ", null);
		//根据状态处理
		if(sdb.getResponseStatus().equalsIgnoreCase(SimpleDBData.ERR)) {
			//数据库处理发生异常，获取 sdb 的返回值，并抛出
			throw new Exception(sdb.getResponseInfo());
		}else if(sdb.getData()!=null && sdb.getData().length>0) {
			System.out.println("数据库连接成功.");
			//打印sdb
			System.out.println(sdb);
		}else {
			//一些未知的异常
			throw new Exception("在连接数据库时，发生了一些异常.");
		}
		
	}

}
