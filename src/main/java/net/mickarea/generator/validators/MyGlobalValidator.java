/******************************************************************************************************

This file "MyGlobalValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import java.io.File;

import net.mickarea.generator.constants.MyConstants.RUNNING_MODE;
import net.mickarea.generator.models.NewCommToolArgs;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 这里是命令行参数，接收完毕后的检验处理。有些参数是有相关性的，所以要分开处理。
 * 对于单个的参数校验，交给 JCommander 的 Validator 类就行了。因为 旧版的 JCommander 没有关联校验参数。所以要自己写关联校验
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月24日-2025年4月25日
 */
public class MyGlobalValidator {

	/**
	 * 这是待处理的命令行参数对象
	 */
	private NewCommToolArgs myArgs;
	
	/**
	 * 这是构造函数，只是传递参数进来而已
	 * @param myArgs 参数是一个用于接收命令行参数的对象
	 */
	public MyGlobalValidator(NewCommToolArgs myArgs) {
		this.myArgs = myArgs;
	}
	
	/**
	 * 这是总的校验方法。直接调用就好。对于不同的情况的校验，交给子方法。
	 * 对于校验方式，要看对应的运行模式：JAR_TEST, DB_CONN_TEST, DB_OBJ_SELECT, JAVA_BEAN_GEN, JAVA_FEATURE_GEN
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	public String valid() {
		String re = null;
		RUNNING_MODE mode = this.myArgs.runningMode;
		switch(mode) {
		case JAR_TEST:
			// jar 包测试 模式
			re = this.jarTestValid();
			break;
		case DB_CONN_TEST:
			// 数据库 连接测试 模式
			re = this.dbConnTestValid();
			break;
		case DB_OBJ_SELECT:
			// 数据库 对象查询 模式
			re = this.dbObjSelectValid();
			break;
		case JAVA_BEAN_GEN:
			// Java Bean 实体类生成模式
			re = this.javaBeanGenValid();
			break;
		case JAVA_FEATURE_GEN:
			// Java 功能生成 模式
			re = this.javaFeatureGenValid();
			break;
		default:
			// 其它情况，暂时不处理
			re = String.format("the mode [%s] is not supported.", mode);
			break;
		}
		return re;
	}
	
	/**
	 * 这里是 Java 的 jar 包执行测试，它不需要其它参数，写不写都一样的。
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	private String jarTestValid() {
		// jar 执行测试，只需要直接调，这里直接返回空字符
		return "";
	}
	
	/**
	 * 这里是数据库连接测试。他需要一些参数。
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	private String dbConnTestValid() {
		//
		// ['databaseType', 'poolName', 'jdbcDriver', 'jdbcUrl', 'dbUser', 
        //  'dbPasswd', 'isAutoCommit', 'connTimeout', 'minThreadNum', 'maxThreadNum'];
		// 调用参数举例：
		// -m db_conn_test -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://127.0.0.1:3306/myspace_db" -dun spaceadmin 
		// -dup abcd1234 -ct 10000 -min 10 -max 20 -ac
		//
		// 对于数据库连接测试。需要上面这些参数（isAutoCommit 是布尔类型，不用校验）
		String re = "";
		
		// 先做参数非空校验，因为除了布尔类型，其它都有可能为空。
		if(this.myArgs.databaseType==null || 
				MyStrUtil.isEmptyString(this.myArgs.poolName) || MyStrUtil.isEmptyString(this.myArgs.jdbcDriverName) ||
				MyStrUtil.isEmptyString(this.myArgs.jdbcUrl) || MyStrUtil.isEmptyString(this.myArgs.dbUserName) || 
				MyStrUtil.isEmptyString(this.myArgs.dbUserPasswd) || 
				this.myArgs.connectionTimeout<=0 || this.myArgs.minThread<=0 || this.myArgs.maxThread<=0) {
			//
			re = String.format("some parameters invalid. we need these args={"
					+ "%s=%s, %s=%s, %s=%s, %s=%s, %s=%s,"
					+ "%s=%s, %s=%s, %s=%s, %s=%s, %s=%s}", 
					"databaseType",this.myArgs.databaseType, "poolName",this.myArgs.poolName, 
					"jdbcDriverName",this.myArgs.jdbcDriverName, "jdbcUrl",this.myArgs.jdbcUrl, 
					"dbUserName",this.myArgs.dbUserName, "dbUserPasswd",this.myArgs.dbUserPasswd, 
					"isAutoCommit",this.myArgs.isAutoCommit, "connectionTimeout",this.myArgs.connectionTimeout, 
					"minThread",this.myArgs.minThread, "maxThread",this.myArgs.maxThread);
			return re;
		}
		
		//校验线程的大小关系
		if(this.myArgs.minThread > this.myArgs.maxThread) {
			re = String.format("The number of threads is abnormal. min=%s, max=%s", this.myArgs.minThread, this.myArgs.maxThread);
			return re;
		}
		
		return re;
	}
	
	/**
	 * 这里是数据库库表和视图对象查询。他需要一些参数。
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	private String dbObjSelectValid() {
		// ['databaseType', 'poolName', 'jdbcDriver', 'jdbcUrl', 'dbUser', 
        //  'dbPasswd', 'isAutoCommit', 'connTimeout', 'minThreadNum', 'maxThreadNum',
        //  'schema', 'schemaUser']
		// 调用参数举例：
		// -m db_obj_select -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://127.0.0.1:3306/myspace_db" -dun spaceadmin 
		// -dup abcd1234 -ct 10000 -min 10 -max 20 -ac -sc schema -scu schemauser
		String re = "";
		
		// 由于 要连接数据库查询数据库对象，需要先连接数据库。所以，校验上有些是重复的。
		re = this.dbConnTestValid();
		if(!MyStrUtil.isEmptyString(re)) {
			return re;
		}
		
		// 校验模式名和模式用户，不为空即可
		if(MyStrUtil.isEmptyString(this.myArgs.schema) || MyStrUtil.isEmptyString(this.myArgs.schemaUser)) {
			re = String.format(
					"schema name or schema user name is empty. please check. args={schema=%s, schemaUser=%s}", 
					this.myArgs.schema, this.myArgs.schemaUser);
			return re;
		}
		
		//
		return re;
	}
	
	/**
	 * 这里是 Java 实体类生成的处理。他需要一些参数。
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	private String javaBeanGenValid() {
		// ['databaseType', 'poolName', 'jdbcDriver', 'jdbcUrl', 'dbUser', 
        //  'dbPasswd', 'isAutoCommit', 'connTimeout', 'minThreadNum', 'maxThreadNum',
        //  'schema', 'schemaUser', 'charset', 'actionType', 'sqlObjects', 'sqlStr', 'entityDir']
		// 调用参数举例：
		// -m java_bean_gen -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://127.0.0.1:3306/myspace_db" 
		// -dun spaceadmin -dup abcd1234 -ct 10000 -min 10 -max 20 -ac -sc schema -scu schemauser -fc utf-8 -at object -so a,b -d "C:\\"
		// 调用参数举例：
		// -m java_bean_gen -db mysql -jdn com.mysql.cj.jdbc.Driver -ju "jdbc:mysql://127.0.0.1:3306/myspace_db" 
		// -dun spaceadmin -dup abcd1234 -ct 10000 -min 10 -max 20 -ac -sc schema -scu schemauser -fc utf-8 -at sql -st "select 1 from dual" -d "C:\\"
		String re = "";
		// 因为校验上有类似的地方，所以调用即可
		re = this.dbObjSelectValid();
		if(!MyStrUtil.isEmptyString(re)) {
			return re;
		}
		// 剩下的 'charset', 'actionType', 'sqlObjects', 'sqlStr', 'entityDir' 开始校验
		//校验 文件字符集 参数
		if(MyStrUtil.isEmptyString(this.myArgs.fileCharset)) {
			re = String.format("the character set is empty. please check it.");
			return re;
		}
		//校验 操作类型 参数
		if(this.myArgs.actionType==null) {
			re = String.format("the action type is empty.");
			return re;
		}
		//校验 与操作类型 相关的参数
		switch(this.myArgs.actionType) {
		case OBJECT:
			// 对于处理方式是 对象，则校验对象信息
			if(MyStrUtil.isEmptyString(this.myArgs.sqlObjects)) {
				re = "the names of database object is empty.";
			}
			break;
		case SQL:
			// 对于处理方式是 sql 语句，则校验 sql 语句
			if(MyStrUtil.isEmptyString(this.myArgs.sqlString)) {
				re = "the sql string is empty.";
			}
			break;
		default:
			re = "the action type [%s] is invalid.";
			break;
		}
		if(!MyStrUtil.isEmptyString(re)) {
			return re;
		}
		//校验 实体存放的目录是否 为空
		if(MyStrUtil.isEmptyString(this.myArgs.entityDir)) {
			re = "the value of entityDir is empty.";
			return re;
		}
		//校验 实体存放的目录是否存在
		File dir = new File(this.myArgs.entityDir);
		if(!dir.exists() || !dir.isDirectory()) {
			re = "the path of entity direcotry is not exists or is not a direcotry.";
			return re;
		}
		
		return re;
	}
	
	/**
	 * 这里是 Java 功能生成用的参数校验
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	private String javaFeatureGenValid() {
		// ['actionClassName', 'charset', 'entityDir']
		// 调用参数举例：
		// -m java_feature_gen -acn Tester01 -fc utf-8 -d "C:\\"
		String re = "";
		
		// 功能生成，只需要 actionClassName
		if(MyStrUtil.isEmptyString(this.myArgs.actionClassName)) {
			re = "the value of java action name is empty.";
			return re;
		}
		
		//校验 字符集
		if(MyStrUtil.isEmptyString(this.myArgs.fileCharset)) {
			re = String.format("the character set is empty. please check it.");
			return re;
		}
		
		//校验 实体存放的目录是否 为空
		if(MyStrUtil.isEmptyString(this.myArgs.entityDir)) {
			re = "the value of entityDir is empty.";
			return re;
		}
		//校验 实体存放的目录是否存在
		File dir = new File(this.myArgs.entityDir);
		if(!dir.exists() || !dir.isDirectory()) {
			re = "the path of entity direcotry is not exists or is not a direcotry.";
			return re;
		}
		
		return re;
	}
	
}
