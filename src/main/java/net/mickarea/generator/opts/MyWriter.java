/******************************************************************************************************

This file "MyWriter.java" is part of project "entitytools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.opts;

import java.time.LocalDateTime;
import java.util.List;

import net.mickarea.generator.beans.TabOrViewTmpObj;
import net.mickarea.generator.utils.MyStrUtil;

/**
 * >> 一个私人的实体类字符输出工具
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年7月18日-2024年10月28日
 */
public final class MyWriter {
	
	/**
	 * 换行符
	 */
	public static final String lineSeperator = System.getProperty("line.separator");
	/**
	 * 四个空格
	 */
	public static final String space4 = "    ";
	
	/**
	 * 私有构造函数，防止被 new 创建对象
	 */
	private MyWriter() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 文件头信息输出
	 * @param entityName 要生成的实体类的类名
	 * @return Java Bean 类文件头信息（版权和协议声明信息）
	 */
	public static final String writeFileHeader(String entityName) {
		StringBuffer sb = new StringBuffer();
		sb.append("/******************************************************************************************************"+lineSeperator);
		sb.append(lineSeperator);
		sb.append("The file is generated automatically."+lineSeperator);
		sb.append("This Class File \""+entityName+".java\" is part of project <\"just write what you want\"> , which is belong to Michael Pang (It's Me)."+lineSeperator);
		sb.append("In my license, all codes can be shared free of charge. "+lineSeperator);
		sb.append("However, if it is used for commercial purposes, I need to be notified."+lineSeperator);
		sb.append("Here is my email \"pangdongcan@live.com\""+lineSeperator);
		sb.append(lineSeperator);
		sb.append("Copyright (c) "+LocalDateTime.now().getYear()+" Michael Pang."+lineSeperator);
		sb.append(lineSeperator);
		sb.append("*******************************************************************************************************/"+lineSeperator);
		return sb.toString();
	}
	
	/**
	 * 包信息和导入的类信息
	 * @return 包信息和导入的类信息
	 */
	public static final String writePackageAndImport() {
		StringBuffer sb = new StringBuffer();
		sb.append("package net.mickarea;"+lineSeperator);
		sb.append(lineSeperator);
		sb.append("import net.mickarea.tools.annotation.MyColumnReadOnly;"+lineSeperator);
		sb.append("import net.mickarea.tools.annotation.MyAutoIncrement;"+lineSeperator);
		sb.append("import net.mickarea.tools.annotation.MyColumn;"+lineSeperator);
		sb.append("import net.mickarea.tools.annotation.MyIdGroup;"+lineSeperator);
		sb.append("import net.mickarea.tools.annotation.MyTableOrView;"+lineSeperator);
		sb.append("import net.mickarea.tools.annotation.MyVirtualEntity;"+lineSeperator);
		sb.append("import net.mickarea.tools.utils.Stdout;"+lineSeperator);
		sb.append(lineSeperator);
		return sb.toString();
	}
	
	/**
	 * 绘制数据库表实体类信息
	 * @param entityName 要生成的实体类的类名
	 * @param tableName 实体类对应的数据库表、视图名称。如果是虚拟实体VO，则不需要传入
	 * @param tmpObj 待生成的实体类的结构信息
	 * @return 类代码字符串
	 */
	public static final String writeTableCode(String entityName, String tableName, List<TabOrViewTmpObj> tmpObj) {
		LocalDateTime time = LocalDateTime.now();
		String dateTime = time.getYear()+"年"+time.getMonth().getValue()+"月"+time.getDayOfMonth()+"日";
		StringBuffer sb = new StringBuffer();
		sb.append("/**"+lineSeperator);
		sb.append(" * 一个普通的 java bean 类"+lineSeperator);
		sb.append(" * @author Michael Pang (Dongcan Pang)"+lineSeperator);
		sb.append(" * @version 1.0"+lineSeperator);
		sb.append(" * @since "+dateTime+lineSeperator);
		sb.append(" */"+lineSeperator);
		if(tableName!=null) {
			sb.append("@MyTableOrView(name=\""+tableName+"\")"+lineSeperator);
		}else {
			//如果是SQL语句映射的内容，它就是虚拟实体类
			sb.append("@MyVirtualEntity"+lineSeperator);
		}
		sb.append("public class "+entityName+" {"+lineSeperator);
		
		//属性处理
		sb.append(writeTableCodePropertyInfo(tmpObj));
		
		//构造函数处理
		sb.append(writeTableCodeConstructorInfo(entityName, tmpObj));
		
		//setter and getter 处理
		sb.append(writeTableCodeSetterAndGetter(tmpObj));
		
		//toString 处理
		sb.append(writeTableCodeToString(entityName, tmpObj));
		
		sb.append("}"+lineSeperator);
		return sb.toString();
	}
	
	/**
	 * >> 属性处理
	 * @param tmpList 类结构信息
	 * @return 关于类内部属性的描述代码字符串
	 */
	public static final String writeTableCodePropertyInfo(List<TabOrViewTmpObj> tmpList) {
		StringBuffer sb = new StringBuffer();
		if(tmpList!=null && tmpList.size()>0) {
			sb.append(lineSeperator);
			for(TabOrViewTmpObj t: tmpList) {
				if(t.getExtra().toLowerCase().contains("auto")) {
					sb.append(space4+"@MyAutoIncrement"+lineSeperator);
				}
				if(t.getExtra().toLowerCase().contains("readonly")) {
					sb.append(space4+"@MyColumnReadOnly"+lineSeperator);
				}
				if(t.getColumnKey().toLowerCase().contains("pri")) {
					sb.append(space4+"@MyIdGroup"+lineSeperator);
				}
				//字段名（要小写）
				String colName = MyStrUtil.isEmptyString(t.getColumnName())?"":t.getColumnName().toLowerCase();
				//注释信息（去除空字符，将双引号替换为\"）
				String colComment = MyStrUtil.isEmptyString(t.getColumnComment())?"":t.getColumnComment().replaceAll("[\\s]+", "");
				colComment = colComment.replaceAll("\"", "\\\\\"");
				
				sb.append(space4+"@MyColumn(name=\""+colName+"\", displayName=\""+colComment+"\", extProperty=\"\")"+lineSeperator);
				sb.append(space4+"private "+t.getPropertyType()+" "+t.getPropertyName()+";"+lineSeperator);
				sb.append(lineSeperator);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 构造函数处理
	 * @param beanName 实体类名
	 * @param tmpList 类结构信息
	 * @return 所生成的构造函数代码字符串
	 */
	public static final String writeTableCodeConstructorInfo(String beanName, List<TabOrViewTmpObj> tmpList) {
		StringBuffer sb = new StringBuffer();
		sb.append(space4+"/**"+lineSeperator);
		sb.append(space4+" * 构造函数"+lineSeperator);
		sb.append(space4+" */"+lineSeperator);
		//无参构造函数
		sb.append(space4+"public "+beanName+"() {"+lineSeperator);
		sb.append(space4+space4+"// TODO Auto-generated constructor stub"+lineSeperator);
		sb.append(space4+"}"+lineSeperator);
		//全参构造函数
		sb.append(space4+"public "+beanName+"(");
		for(int i=0;i<tmpList.size();i++) {
			sb.append(tmpList.get(i).getPropertyType());
			sb.append(" ");
			sb.append(tmpList.get(i).getPropertyName());
			if(i<tmpList.size()-1) {
				sb.append(", ");
			}
		}
		sb.append(") {"+lineSeperator);
		for(TabOrViewTmpObj t: tmpList) {
			sb.append(space4+space4+("this."+t.getPropertyName()+"="+t.getPropertyName()+";")+lineSeperator);
		}
		sb.append(space4+"}"+lineSeperator);
		sb.append(lineSeperator);
		return sb.toString();
	}
	
	/**
	 * >> setter 和 getter 的处理
	 * @param tmpList 类结构信息
	 * @return setter 和 getter 方法的代码字符串
	 */
	public static final String writeTableCodeSetterAndGetter(List<TabOrViewTmpObj> tmpList) {
		StringBuffer sb = new StringBuffer();
		sb.append(space4+"// getter and setter"+lineSeperator);
		for(TabOrViewTmpObj t : tmpList) {
			//getter 方法
			sb.append(space4+"public "+t.getPropertyType()+" get"+MyStrUtil.makeFirstCharUpperCase(t.getPropertyName())+"() {"+lineSeperator);
			sb.append(space4+space4+"return "+t.getPropertyName()+";"+lineSeperator);
			sb.append(space4+"}"+lineSeperator);
			//setter 方法
			sb.append(space4+"public void"+" set"+MyStrUtil.makeFirstCharUpperCase(t.getPropertyName())+"("+t.getPropertyType()+" "+t.getPropertyName()+") {"+lineSeperator);
			sb.append(space4+space4+"this."+t.getPropertyName()+"="+t.getPropertyName()+";"+lineSeperator);
			sb.append(space4+"}"+lineSeperator);
		}
		sb.append(lineSeperator);
		return sb.toString();
	}
	
	/**
	 * toString 处理
	 * @param beanName 实体类名
	 * @param tmpList 类结构信息
	 * @return toString 方法的代码字符串
	 */
	public static final String writeTableCodeToString(String beanName, List<TabOrViewTmpObj> tmpList) {
		StringBuffer sb = new StringBuffer();
		sb.append(space4+"@Override"+lineSeperator);
		sb.append(space4+"public String toString() {"+lineSeperator);
		sb.append(space4+space4);
		sb.append("String s = \""+beanName+"{");
		for(int i=0;i<tmpList.size();i++) {
			sb.append(tmpList.get(i).getPropertyName()+":%s");
			if(i<tmpList.size()-1) {
				sb.append(", ");
			}
		}
		sb.append("}\";"+lineSeperator);
		sb.append(space4+space4);
		sb.append("return Stdout.fplToAnyWhere(s, ");
		for(int i=0;i<tmpList.size();i++) {
			sb.append("this."+tmpList.get(i).getPropertyName());
			if(i<tmpList.size()-1) {
				sb.append(", ");
			}
		}
		sb.append(");"+lineSeperator);
		sb.append(space4+"}"+lineSeperator);
		return sb.toString();
	}

}
