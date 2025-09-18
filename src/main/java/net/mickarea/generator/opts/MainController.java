/******************************************************************************************************

This file "MainController.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts;

import java.io.File;
import java.lang.reflect.Method;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.mickarea.generator.constants.MyConstants.EN_DE_TYPE;
import net.mickarea.generator.constants.MyConstants.INPUT_CONTENT_TYPE;
import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.GenResult;
import net.mickarea.generator.models.NewCommToolArgs;
import net.mickarea.generator.models.SqlResult;
import net.mickarea.generator.utils.MyFileUtil;
import net.mickarea.generator.utils.MyImageUtil;
import net.mickarea.generator.utils.MyRSAUtil;
import net.mickarea.generator.utils.MyStrUtil;
import net.mickarea.generator.utils.MySummaryUtil;

/**
 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年6月11日-2025年9月18日
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
	 * 新版本的命令行参数（为了避免不必要的麻烦，直接添加就行了，别改原来的处理）
	 */
	private NewCommToolArgs newArgs = null;

	/**
	 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
	 */
	public MainController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
	 * @param cArgs CommandArguments 命令行传来的参数
	 */
	public MainController(CommandArguments cArgs) {
		super();
		this.cArgs = cArgs;
	}
	
	/**
	 * 一个主控制器.当入口程序获取了适合的参数，则根据参数，动态调用对应的实现代码
	 * @param newArgs NewCommToolArgs 命令行传来的参数
	 */
	public MainController(NewCommToolArgs newArgs) {
		super();
		this.newArgs = newArgs;
		this.cArgs = new CommandArguments(newArgs);
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
	 * Java Action 类 和 Page 类的文件生成
	 */
	public void genActionAndPageClass() {
		//生成 Action 类
		MyFeatureActionWriter w1 = new MyFeatureActionWriter(this.newArgs);
		//生成 Page 类
		MyFeaturePageWriter w2 = new MyFeaturePageWriter(this.newArgs);
		//执行生成处理
		GenResult r1 = w1.genJavaClassFile();
		GenResult r2 = w2.genJavaClassFile();
		//处理结果组合成列表
		List<GenResult> rList = new ArrayList<GenResult>();
		rList.add(r1);
		rList.add(r2);
		//将数据转换为字符串
		ObjectMapper mapper = new ObjectMapper();
		String dataString = "[]";
		try {
			dataString = mapper.writeValueAsString(rList);
			//输出
			MyStrUtil.successOut(dataString, this.cArgs.isConsole());
		} catch (JsonProcessingException e) {
			//
			MyStrUtil.errorOut("JsonProcessingException "+e.getMessage(), this.cArgs.isConsole());
		}
	}
	
	/**
	 * 图片缩放处理（这里有2种不同的方式，一种是指定一堆图片文件；一种是指定一个文件夹，将它里面的图片全部转换）。
	 * 因为两种模式是互斥的，所以 当两个参数都指定，则只处理 指定的文件列表
	 * 当然，这里的文件夹参数，不进行递归处理。递归太容易出问题了。只扫描第一层子目录。
	 */
	public void scaleImages() {
		
		// 先判断当前是什么模式，用 列表 还是 用文件夹
		String imageMode = "dir";
		if(newArgs.imageFiles!=null && newArgs.imageFiles.size()>0) {
			imageMode = "files";
		}
		
		// 前面已经校验了，文件夹 和 文件 都是存在的。但是还是要做一些过滤。
		// 比如，如果是文件夹模式，它下面可能没有符合要求的文件，或者部分不符合，还是要过滤一次。
		// 另外，提供的文件列表可能会有重复的路径。因此，不能直接用参数提供的列表
		
		//定义一个处理list
		List<File> targets = null;
		
		// 按模式获取可用的图片列表
		switch(imageMode) {
		case "dir":
			//首先搜索子文件（如果没有子文件，返回一个长度为0的数组）
			File[] childrenFilesArray = newArgs.imageDir.listFiles();
			//转换为列表，方便用流处理
			List<File> childrenFiles = Arrays.asList(childrenFilesArray);
			//过滤重复 和 不需要的 文件
			targets = childrenFiles.stream().filter(MyFileUtil::isImageExtensionOk).distinct().collect(Collectors.toList());
			break;
		case "files":
			targets = newArgs.imageFiles.stream().filter(MyFileUtil::isImageExtensionOk).distinct().collect(Collectors.toList());
			break;
		default:
			targets = new ArrayList<File>();
			break;
		}
		
		//开始转换处理
		if(targets.size()>0) {
			//循环遍历，因为会出现报错的可能，还是用 for 循环靠谱
			List<String> errorFileList = new ArrayList<String>();
			for(File img: targets) {
				String oriPath = img.getAbsolutePath();
				String newPath = newArgs.entityDir + File.separator + img.getName();
				try {
					MyImageUtil.getScaledImage(oriPath, newPath, newArgs.imageWidth, newArgs.imageHeight);
				} catch (Exception e) {
					//增加日志输出
					MyStrUtil.mylogger.error(e.getMessage(), e);
					errorFileList.add(newPath);
				}
			}
			//输出汇总信息
			if(errorFileList.size()>0) {
				MyStrUtil.errorOut("error:"+errorFileList.toString(), this.cArgs.isConsole());
			}else {
				MyStrUtil.successOut("All images scaled successfully", this.cArgs.isConsole());
			}
		}else {
			// 没有可以缩放的文件
			MyStrUtil.errorOut("No one image is available for image zooming.", this.cArgs.isConsole());
		}
	}
	
	/**
	 * 这里执行数字签名的提取（在这之前，已经对参数进行了 独立校验 和 关联校验，可以直接使用了）
	 * 在数字签名处理上，只是对字符串、文件进行一个简单的签名算法处理，提取数字信息特征。处理时，我们根据参数，拼接一个函数名，然后反射调用
	 * <p>调用举例：--console -m DIGITAL_SIGNATURE -ic "aaa"          -ict "text" -algo "MD5"</p>
	 * <p>调用举例：--console -m DIGITAL_SIGNATURE -ic "D:\\test.txt" -ict "file" -algo "MD5"</p>
	 * <p>
	 * 正常的输出结果可能如下
	 * <code>{"oriMessage":"e22843e821a6285c6ba899353c15c6d0","encodeMessage":"e22843e821a6285c6ba899353c15c6d0","status":"success"}</code>
	 * </p>
	 * <p>
	 * 异常的输出结果可能如下
	 * <code>{"oriMessage":"The received file information is not a valid file.","encodeMessage":"The%20received%20file%20information%20is%20not%20a%20valid%20file.","status":"error"}</code>
	 * </p>
	 */
	public void extractDigitalSignature() {
		
		// 首先获取参数
		String inputContent = this.newArgs.inputContent;
		INPUT_CONTENT_TYPE inputContentType = this.newArgs.inputContentType;
		String algorithm = this.newArgs.algorithmName;
		
		// 这里开始构造函数名
		String methodName = "get"; // 方法前缀
		methodName += algorithm.toUpperCase().replace("-", "_"); // 算法名称，不能带横线
		methodName += "_Info"; // 匹配名称信息
		if(inputContentType.equals(INPUT_CONTENT_TYPE.FILE)) {
			// 如果是文件路径，还得再拼接一段
			methodName += "FromFile";
		}
		
		// 获取反射类
		Class<MySummaryUtil> utilCls = MySummaryUtil.class;
		try {
			// 获取反射的方法
			Method m1 = utilCls.getMethod(methodName, String.class);
			Object result = m1.invoke(null, inputContent);
			//
			if(result==null) throw new Exception("在进行提取数字签名特征信息时，发生异常，请检查日志");
			
			// 终端输出结果
			MyStrUtil.successOut((String)result, cArgs.isConsole());
			
		} catch (Exception e) {
			//打印异常信息到文件
			MyStrUtil.mylogger.error("[执行处理方法 "+methodName+" 异常]", e);
			//将异常信息处理一下输出到标准输出流
			if(e instanceof NoSuchMethodException) {
				String temp = "处理方法 %s 不存在。";
				MyStrUtil.errorOut(String.format(temp, methodName), cArgs.isConsole());
			}else {
				String temp = "发现了一些不寻常的异常，信息如下[%s]，具体请检查日志信息。";
				MyStrUtil.errorOut(String.format(temp, e.getMessage()), cArgs.isConsole());
			}
		}
		
	}
	
	/**
	 * 这里执行非对称加密和解密（在这之前，已经对参数进行了 独立校验 和 关联校验，可以直接使用了）。
	 * 在加密时，如果没有公钥，则可以根据密钥长度，重新生成新的公钥和私钥，然后再加密。
	 * 在解密时，必须提供私钥。
	 * <p>调用举例：1、加密: -m ASYMMETRIC_ENCRYPTION -ende encrypt -algo rsa -ic "aaa" -ict text -kl 1024</p>
	 * <p>调用举例：2、加密: -m ASYMMETRIC_ENCRYPTION -ende encrypt -algo rsa -ic "aaa" -ict text -pubkey 5555</p>
	 * <p>调用举例：3、解密: -m ASYMMETRIC_ENCRYPTION -ende decrypt -algo rsa -ic "aaa" -ict text -prikey 66666</p>
	 */
	public void doAsymmetricEncryptionOrDecryption() {
		
		// 首先获取参数
		EN_DE_TYPE actionType = this.newArgs.execType; // 处理方式：加密/解密
		String inputContent = this.newArgs.inputContent; // 要处理的内容
		//INPUT_CONTENT_TYPE inputContentType = this.newArgs.inputContentType; // 要处理的内容的类型: text, file
		String algorithm = this.newArgs.algorithmName; // 算法名称
		int keyLength = this.newArgs.keyLength;        // 密钥长度
		String publicKey = this.newArgs.publicKey;     // 公钥信息
		String privateKey = this.newArgs.privateKey;   // 私钥信息
		
		// 开始处理
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("publicKey", "");
		data.put("privateKey", "");
		data.put("resultString", "");
		
		// 这里暂时不分 类型 和 算法。因为暂时只支持 RSA 算法的 字符串处理
		try {
			switch(actionType) {
			case ENCRYPT:
				// 加密（如果没有公钥，需要先生成）
				if(MyStrUtil.isEmptyString(publicKey)) {
					KeyPair pair = MyRSAUtil.genKeys(keyLength, algorithm.toUpperCase());
					publicKey = MyRSAUtil.getPublicKey(pair);
					privateKey = MyRSAUtil.getPrivateKey(pair);
					// 设置
					data.put("publicKey", publicKey);
					data.put("privateKey", privateKey);
				}
				String tmp = MyRSAUtil.encode(inputContent, publicKey); // 公钥加密
				data.put("resultString", tmp);
				break;
			case DECRYPT:
				// 解密
				String tmp2 = MyRSAUtil.decode(inputContent, privateKey); //私钥解密
				data.put("resultString", tmp2);
				break;
			}
			// 这里进行赋值，并输出
			ObjectMapper om = new ObjectMapper();
			String re = om.writeValueAsString(data);
			MyStrUtil.successOut(re, cArgs.isConsole());
		} catch (Exception e) {
			//打印异常信息到文件
			MyStrUtil.mylogger.error("[执行处理方法 doAsymmetricEncryptionOrDecryption 异常]", e);
			//将异常信息处理一下输出到标准输出流
			String temp = "发现异常，信息如下[%s]，具体请检查日志信息。";
			MyStrUtil.errorOut(String.format(temp, e.getMessage()), cArgs.isConsole());
		}
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
				throw new Exception("接收到的参数内容为 null 值.");
			}
			//参数校验 （2025-04-30 这里不做校验了，因为 参数接收时，已经校验过了）
			//ValidResult validRe = cArgs.valid();
			//if(!validRe.getValid()) {
			//	throw new Exception(validRe.getMessage());
			//}
			
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
				MyStrUtil.successOut(sqlRe.getData(), cArgs.isConsole());
			}else {
				throw new Exception(sqlRe.getMessage());
			}
		} catch(Exception e) {
			//打印异常信息到文件
			MyStrUtil.mylogger.error("[执行处理方法 "+methodName+" 异常]", e);
			//将异常信息处理一下输出到标准输出流
			if(e instanceof ClassNotFoundException) {
				String temp = "%s, 暂时不支持该数据库 [%s] 的访问。";
				MyStrUtil.errorOut(String.format(temp, e.getMessage(), cArgs.getDatabaseType()), cArgs.isConsole());
			}else if(e instanceof NoSuchMethodException) {
				String temp = "处理方法 %s 不存在。";
				MyStrUtil.errorOut(String.format(temp, methodName), cArgs.isConsole());
			}else {
				String temp = "发现了一些不寻常的异常，信息如下[%s]，具体请检查日志信息。";
				MyStrUtil.errorOut(String.format(temp, e.getMessage()), cArgs.isConsole());
			}
		}
	}
	
}
