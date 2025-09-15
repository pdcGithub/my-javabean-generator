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
 * @since 2025年4月21日-2025年9月15日
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
		JAVA_FEATURE_GEN,
		/**
		 * 运行模式：图片缩放处理
		 */
		IMAGE_SCALING,
		/**
		 * 运行模式：数字签名
		 */
		DIGITAL_SIGNATURE,
		/**
		 * 运行模式：非对称加密
		 */
		ASYMMETRIC_ENCRYPTION
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
	
	/**
	 * 这是可以接受处理的图片文件类型（不是所有的图片类型都能处理）
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年5月12日
	 */
	public static enum IMAGE_TYPE {
		JPG,
		JPEG,
		PNG
	}
	
	/**
	 * 这是数字签名算法的名称，目前只支持这些算法
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年9月15日
	 */
	public static enum DIGITAL_SIGNATURE_ALGORITHM {
		MD5,
		SHA_1,
		SHA_224,
		SHA_256,
		SHA_384,
		SHA_512
	}
	
	/**
	 * 这是非对称加密算法的名称，目前只支持这些算法
	 * @author Michael Pang (Dongcan Pang)
	 * @version 1.0
	 * @since 2025年9月15日
	 */
	public static enum ASYMMETRIC_ENCRYPTION_ALGORITHM {
		RSA
	}
	
}
