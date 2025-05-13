/******************************************************************************************************

This file "MyGlobalValidator.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.validators;

import java.io.File;

import net.mickarea.generator.constants.MyConstants.IMAGE_TYPE;
import net.mickarea.generator.constants.MyConstants.RUNNING_MODE;
import net.mickarea.generator.models.NewCommToolArgs;
import net.mickarea.generator.utils.MyFileUtil;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * 这里是命令行参数，接收完毕后的检验处理。有些参数是有相关性的，所以要分开处理。
 * 对于单个的参数校验，交给 JCommander 的 Validator 类就行了。因为 旧版的 JCommander 没有关联校验参数。所以要自己写关联校验
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月24日-2025年5月13日
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
		case IMAGE_SCALING:
			re = this.imageScalingValid();
			break;
		default:
			// 其它情况，暂时不处理
			re = String.format("global validator error, the mode [%s] is not supported.", mode);
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
	
	/**
	 * <p>这里是 图片缩放 功能的参数校验</p>
	 * <p>关于 图片缩放功能 有 5 个参数是必须校验的：图片文件列表，图片文件夹，图片生成后的路径，图片生成后的宽度、高度。</p>
	 * <p>其中，图片文件列表，图片文件夹 是互斥的。文件列表 和 文件夹 只需要提供一个。因为处理模式有2种，按列表或者按文件夹。
	 * 如果2个都提供，则按文件列表处理</p>
	 * @return 校验通过，返回空；校验失败，返回一段提示信息。
	 */
	private String imageScalingValid() {
		// 调用参数举例 1 ：
		// -m image_scaling -ifd "C:\\Users\\Michael\\Pictures\\testimgs" -d "C:\\Users\\Michael\\Pictures\\testimgs\\output"  -waz 1600 -haz 901
		// 调用参数举例 2 ：
		// -m image_scaling -ifs "C:\\Users\\Michael\\Pictures\\testimgs\\DSC00016.JPG, C:\\Users\\Michael\\Pictures\\testimgs\\DSC00019.JPG, " -d "C:\\Users\\Michael\\Pictures\\testimgs\\output"  -waz 1600 -haz 901
		String re = "";
		
		// 先判断当前是什么模式，用 列表 还是 用文件夹
		String imageMode = "dir";
		if(myArgs.imageFiles!=null && myArgs.imageFiles.size()>0) {
			imageMode = "files";
		}
		
		//按照模式校验参数
		switch(imageMode) {
		case "dir":
			//对于文件夹，要判断 是否存在 并且 是否为文件夹
			if(myArgs.imageDir==null || !myArgs.imageDir.exists() || !myArgs.imageDir.isDirectory()) {
				re = String.format("The path=[%s] of images is abnormal. It may not exist or not a folder.", myArgs.imageDir);
			}else if(!myArgs.imageDir.canRead()) {
				re = String.format("The path=[%s] can not read.", myArgs.imageDir);
			}
			break;
		case "files":
			//对于文件列表，需要判断文件是否有效。因为只处理几种图片文件
			if(myArgs.imageFiles==null || myArgs.imageFiles.size()<=0) {
				re = "The path of image files is empty, please check it.";
			}else {
				//这里校验是否文件是否存在，并且是否为图片
				for(File f: myArgs.imageFiles) {
					if(!f.exists() || !f.isFile()) {
						re = String.format("The file=[%s] is not exist or not an image file.", f.getAbsolutePath());
						break;
					}else if(!MyFileUtil.isImageExtensionOk(f)) {
						re = String.format(
								"File type=[%s] is incorrect, the file type can only be one of the following types (%s).", 
								MyFileUtil.getFileExtension(f),
								MyStrUtil.showArrayValues(IMAGE_TYPE.values()));
						break;
					}
				}
			}
			break;
		default:
			re = String.format("The image processing mode=[%s] is abnormal, please check it.", imageMode);
			break;
		}
		
		// 上阶段的校验结果
		if(!MyStrUtil.isEmptyString(re)) {
			return re;
		}
		
		// 校验剩下的东西，比如 宽度，高度
		if(myArgs.imageWidth<=0 || myArgs.imageHeight<=0) {
			re = String.format("The parameter scaled width or height is abnormal. Please check them. width=[%s], height=[%s]", 
					myArgs.imageWidth, 
					myArgs.imageHeight);
			return re;
		}
		
		//存放路径校验
		File dir = MyStrUtil.isEmptyString(this.myArgs.entityDir)?null:new File(this.myArgs.entityDir);
		if(dir==null || !dir.exists() || !dir.isDirectory()) {
			re = String.format("The path=[%s] for storing the generated images is abnormal. It may not exists or not a direcotry.",
					dir.getAbsolutePath());
			return re;
		}else if(!dir.canWrite()){
			re = String.format("you have not right to write this folder=[%s].", dir.getAbsolutePath());
			return re;
		}
		
		return re;
	}
	
}
