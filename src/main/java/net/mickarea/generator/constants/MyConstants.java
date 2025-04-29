/******************************************************************************************************

This file "MyConstants.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.constants;

/**
 * 这是一个常量类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月21日
 */
public class MyConstants {
	/**
	 * 这是运行模式枚举类
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年4月21日
	 */
	public static enum RUNNING_MODE {
		/**
		 * 运行模式：jar 包调用测试
		 */
		JAR_TEST, 
		/**
		 * 运行模式：数据库连接测试
		 */
		DB_CONN_TEST,
		/**
		 * 运行模式：数据库对象选择处理
		 */
		DB_OBJ_SELECT,
		/**
		 * 运行模式：Java Bean 生成处理
		 */
		JAVA_BEAN_GEN,
		/**
		 * 运行模式：Java 功能生成处理
		 */
		JAVA_FEATURE_GEN
	}
	
	/**
	 * 这是数据库类型枚举类
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年4月22日
	 */
	public static enum DATABASE_TYPE {
		/**
		 * 数据库类型：MySQL
		 */
		MYSQL,
		/**
		 * 数据库类型：Oracle
		 */
		ORACLE,
		/**
		 * 数据库类型：SqlServer
		 */
		SQLSERVER
	}
	
	/**
	 * 这是操作类型枚举类
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年4月22日
	 */
	public static enum ACTION_TYPE {
		/**
		 * 数据库对象
		 */
		OBJECT,
		/**
		 * 数据库sql语句
		 */
		SQL
	}
	
}
