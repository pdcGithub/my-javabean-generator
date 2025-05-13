/******************************************************************************************************

This file "CommToolArgs.java" is part of project "cmdtester" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

import java.io.File;
import java.util.List;

import com.beust.jcommander.Parameter;

import net.mickarea.generator.constants.MyConstants.ACTION_TYPE;
import net.mickarea.generator.constants.MyConstants.DATABASE_TYPE;
import net.mickarea.generator.constants.MyConstants.RUNNING_MODE;
import net.mickarea.generator.converters.MyFileConverter;
import net.mickarea.generator.converters.MyFileListConverter;
import net.mickarea.generator.validators.ActionTypeValidator;
import net.mickarea.generator.validators.CharsetValidator;
import net.mickarea.generator.validators.ConnectionTimeoutValidator;
import net.mickarea.generator.validators.DatabaseTypeValidator;
import net.mickarea.generator.validators.JDBCDriverNameValidator;
import net.mickarea.generator.validators.JDBCUrlValidator;
import net.mickarea.generator.validators.RunningModelValidator;
import net.mickarea.generator.validators.SqlObjectsValidator;
import net.mickarea.generator.validators.StringIsNullValidator;
import net.mickarea.generator.validators.ThreadNumValidator;
import net.mickarea.generator.validators.WidthAndHeightValidator;

/**
 * 这是一个参数接收的类。它用于处理 pdc common tool 这个 GUI 工具的输入参数
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月22日-2025年5月13日
 */
public class NewCommToolArgs {

	/**
	 * 这个是运行模型参数，必填
	 */
	@Parameter(names = {"-m","--mode"}, 
			required = true, 
			validateWith = RunningModelValidator.class,
			description = "Running mode of the generator.")
	public RUNNING_MODE runningMode;
	
	/**
	 * 这是帮助参数
	 */
	@Parameter(names = {"-h", "--help"}, 
			description = "Show help informations.", help = true)
	public boolean help;
	
	/**
	 * 这个是用于区分你是命令行 还是 GUI 的一个运行参数。当为 true，输出将会更简短。因为 GUI 工具需要一个 json 信息。而命令行模式不需要
	 */
	@Parameter(names = {"--console"},
			description = "If you are running on console by yourself, please add this parameter. The output informations will be shorter.")
	public boolean console = false;
	
	// =========================== 上面是一般参数，下面是特殊参数
	
	/**
	 * 数据库类型，如果是数据库操作，则必填
	 */
	@Parameter(names = {"-db", "--dbtype"},
			validateWith = DatabaseTypeValidator.class,
			description = "Your database type. for example: mysql, oracle, sqlserver.")
	public DATABASE_TYPE databaseType;
	
	/**
	 * 配置名称，可填可不填
	 */
	@Parameter(names = {"-n", "--name"},
			validateWith = StringIsNullValidator.class,
			description = "The name of configuration.")
	public String poolName = "defaultName";
	
	/**
	 * JDBC 驱动类的名字
	 */
	@Parameter(names = {"-jdn", "--jdbc_driver_name"},
			validateWith = JDBCDriverNameValidator.class,
			description = "The JDBC driver class name. For example: 'com.mysql.cj.jdbc.Driver'.")
	public String jdbcDriverName;
	
	/**
	 * JDBC 连接的 URL
	 */
	@Parameter(names = {"-ju", "--jdbc_url"},
			validateWith = JDBCUrlValidator.class,
			description = "The JDBC connection URL. For example: 'jdbc:mysql://127.0.0.1:3306/myspace_db'")
	public String jdbcUrl;
	
	/**
	 * 连接数据库所用的用户
	 */
	@Parameter(names = {"-dun", "--db_user_name"},
			validateWith = StringIsNullValidator.class,
			description = "The name of user who connecting database.")
	public String dbUserName;
	
	/**
	 * 连接数据库所用的用户的密码
	 */
	@Parameter(names = {"-dup", "--db_user_passwd"},
			validateWith = StringIsNullValidator.class,
			description = "The password of user who connecting to database.")
	public String dbUserPasswd;
	
	/**
	 * 是否自动提交事务，默认 否
	 */
	@Parameter(names = {"-ac", "--auto_commit"},
			description = "Whether or not to commit automatically.")
	public boolean isAutoCommit = false;
	
	/**
	 * 数据库连接超时阈值。单位是 毫秒
	 */
	@Parameter(names = {"-ct", "--connection_timeout"},
			validateWith = ConnectionTimeoutValidator.class,
			description = "The threshold of database connection. In milliseconds.")
	public int connectionTimeout = 5000;
	
	/**
	 * 数据库连接池最小的连接数
	 */
	@Parameter(names = {"-min", "--min_thread"},
			validateWith = ThreadNumValidator.class,
			description = "The minimum number of connections in a connection pool.")
	public int minThread = 10;
	
	/**
	 * 数据库连接池最大的连接数
	 */
	@Parameter(names = {"-max", "--max_thread"},
			validateWith = ThreadNumValidator.class,
			description = "The maximum number of connections in the connection pool.")
	public int maxThread = 40;
	
	/**
	 * 文件保存时的字符集
	 */
	@Parameter(names = {"-fc", "--file_charset"},
			validateWith = CharsetValidator.class,
			description = "The character set used when the file is saved.")
	public String fileCharset;
	
	/**
	 * 操作类型：数据库表、视图；Sql 语句
	 */
	@Parameter(names = {"-at", "--action_type"},
			validateWith = ActionTypeValidator.class,
			description = "The action type of generator.")
	public ACTION_TYPE actionType;
	
	/**
	 * 数据库模式名
	 */
	@Parameter(names = {"-sc", "--schema"},
			validateWith = StringIsNullValidator.class,
			description = "The name of the database schema.")
	public String schema;
	
	/**
	 * 数据库模式对应的用户名
	 */
	@Parameter(names = {"-scu", "--schema_user"},
			validateWith = StringIsNullValidator.class,
			description = "The username of the database schema.")
	public String schemaUser;
	
	/**
	 * 要处理的数据库对象名
	 */
	@Parameter(names = {"-so", "--sql_objects"},
			validateWith = SqlObjectsValidator.class,
			description = "The name of the database object to be generated.")
	public String sqlObjects;
	
	/**
	 * 要处理的数据库sql语句
	 */
	@Parameter(names = {"-st", "--sql_string"},
			validateWith = StringIsNullValidator.class,
			description = "The database SQL statement to be generated.")
	public String sqlString;
	
	/**
	 * 要生成的Action类名
	 */
	@Parameter(names = {"-acn","--action_class_name"},
			validateWith = StringIsNullValidator.class,
			description="The name of action Class which is being generated.")
	public String actionClassName;
	
	/**
	 * 要存放实体的文件夹路径
	 */
	@Parameter(names = {"-d", "--dir"},
			validateWith = StringIsNullValidator.class,
			description = "This is the directory where the entity is stored after it is generated.")
	public String entityDir;
	
	// ===========================================  下面是 图片处理的参数 ========================= 
	
	/**
	 * 要处理的图片文件列表（这是对文件路径进行 List 转换）
	 */
	@Parameter(names = {"-ifs", "--image_files"},
			listConverter = MyFileListConverter.class,
			description = "This is the full path of your image files. If there are multiple files, separate them with commas.")
	public List<File> imageFiles ;
	
	/**
	 * 要处理的图片所在文件夹。这个参数与 imageFiles 互斥。当错误地同时使用 imageFiles 和 imageDir 参数，默认只识别 imageFiles 参数。
	 * （这是对文件路径进行 File 对象转换）
	 */
	@Parameter(names = {"-ifd", "--image_files_dir"},
			converter = MyFileConverter.class,
			description = "This is the original folder path where the images are stored."
			)
	public File imageDir;
	
	/**
	 * 图片缩放处理后的宽度，单位 px
	 */
	@Parameter(names = {"-waz", "--width_after_zooming"},
			validateWith = WidthAndHeightValidator.class,
			description = "The width after image scaling, unit (pixels)"
			)
	public int imageWidth = -1;
	
	/**
	 * 图片缩放处理后的高度，单位 px
	 */
	@Parameter(names = {"-haz", "--height_after_zooming"},
			validateWith = WidthAndHeightValidator.class,
			description = "The height after image scaling, unit (pixels)"
			)
	public int imageHeight = -1;
}