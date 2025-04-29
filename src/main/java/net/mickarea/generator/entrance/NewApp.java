/******************************************************************************************************

This file "NewApp.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2022 - 2025 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.entrance;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import net.mickarea.generator.constants.MyConstants.RUNNING_MODE;
import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.NewCommToolArgs;
import net.mickarea.generator.opts.MainController;
import net.mickarea.generator.utils.MyStrUtil;
import net.mickarea.generator.validators.MyGlobalValidator;

/**
 * 这个是 V 1.0 版本的程序入口. 与 0.0 版本的区别是，它使用了标准化的命令行参数处理。
 * 每个参数都带有参数名，方便记忆，也方便书写。因为，有参数名，就不用按顺序写。
 * 由于这个 jar 包是用来给一个 GUI 工具调用的。所以它输出的信息，是一个 json 字符串。
 * <p>
 * <h3>输出的结果举例 1 </h3>
 * <code>
 * {"oriMessage":"The following option is required: [-m | --mode]",
 *  "encodeMessage":"The%20following%20option%20is%20required%3A%20%5B-m%20%7C%20--mode%5D",
 *  "status":"error"}
 * </code>
 * <h3>输出的结果举例 2</h3>
 * <code>
 * {"oriMessage":"xxx",
 *  "encodeMessage":"xxx",
 *  "status":"success"}
 * </code>
 * </p>
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2025年4月29日
 */
public class NewApp {

	/**
	 * 程序入口方法
	 * @param args
	 */
	public static void main(String[] args) {
		
		//创建一个用于装载参数的对象
		NewCommToolArgs toolArgs = new NewCommToolArgs();
		
		//创建命令行参数识别对象
		JCommander cmd = JCommander.newBuilder().addObject(toolArgs).build();
		
		//这里进行参数校验 和 转换（校验出错，则抛出异常）===== 单个参数校验
		try {
			cmd.parse(args);
		}catch(ParameterException e1) {
			MyStrUtil.errorOut(e1.getMessage());
			return ;
		}
		
		//如果是显示帮助信息，则打印（这里一般是命令行直接调用 -h 或者 --help 参数才会输出的。GUI工具不会触发这里）
		if(toolArgs.help) {
			//输出帮助信息
			cmd.usage();
			//结束执行
			return ;
		}
		
		//自定义全局参数校验 ===== 关联参数校验
		String execMsg = new MyGlobalValidator(toolArgs).valid();
		if(execMsg!=null && execMsg.length()>0) {
			//抛出异常
			MyStrUtil.errorOut(execMsg);
			return ;
		}
		
		//先赋值，这样在后面就不用写几遍了。
		CommandArguments cArgs = new CommandArguments(toolArgs);
		
		//正式执行（根据模式参数来判断要怎么执行），为了减少代码量，尽可能用原有代码。
		RUNNING_MODE mode = toolArgs.runningMode;
		switch(mode) {
		case JAR_TEST:
			// jar 包测试 模式
			MyStrUtil.successOut("欢迎使用 mickarea.net 出品! 这个 Java 程序包可以完美运行!");
			break;
		case DB_CONN_TEST:
			//测试数据库 是否可以正常连接
			new MainController(cArgs).testConnection();
			break;
		case DB_OBJ_SELECT:
			//这里进行数据库对象查询处理
			new MainController(cArgs).searchDictInfo();
			break;
		case JAVA_BEAN_GEN:
			// Java Bean 实体类生成模式
			new MainController(cArgs).genJavaEntityFile();
			break;
		case JAVA_FEATURE_GEN:
			// Java 功能生成 模式
			MyStrUtil.errorOut(String.format("the feature is developing.", mode));
			break;
		default:
			// 其它情况，暂时不处理
			MyStrUtil.errorOut(String.format("the mode [%s] is not supported.", mode));
			break;
		}
	}

}