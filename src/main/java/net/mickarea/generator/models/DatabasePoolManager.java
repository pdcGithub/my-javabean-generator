/******************************************************************************************************

This file "DBUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.models;

import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * &gt;&gt;&nbsp;数据库连接池管理类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日
 */
public class DatabasePoolManager {
	
	private final ConcurrentHashMap<String, HikariDataSource> dataPools;
	
	//这里用于确保管理类只有一个实例，否则会出现用 new 创建多个管理类的混乱局面
	private static DatabasePoolManager mana;
	public static synchronized DatabasePoolManager getInstance(CommandArguments cArgs) {
		if(mana==null) {
			mana = new DatabasePoolManager(cArgs);
		}
		return mana;
	}
	
	/**
	 * &gt;&gt;&nbsp;构造函数（主要负责 dataPools 数据库连接池创建）
	 */
	private DatabasePoolManager(CommandArguments cArgs) {
		
		//数据库连接池载体，可装载多个连接池，以实现多数据库访问。键值对为==》 连接池名称：连接池对象
		this.dataPools = new ConcurrentHashMap<String, HikariDataSource>();
				
		//设置配置信息
		HikariConfig dbConfig = new HikariConfig();
		dbConfig.setPoolName(cArgs.getPoolName());
		dbConfig.setDriverClassName(cArgs.getJdbcDriver());
		dbConfig.setJdbcUrl(cArgs.getJdbcUrl());
		dbConfig.setUsername(cArgs.getDbUser());
		dbConfig.setPassword(cArgs.getDbPasswd());
		dbConfig.setAutoCommit(new Boolean(cArgs.getIsAutoCommit()));
		dbConfig.setConnectionTimeout(new Long(cArgs.getConnTimeout()));
		dbConfig.setMinimumIdle(new Integer(cArgs.getMinThreadNum()));
		dbConfig.setMaximumPoolSize(new Integer(cArgs.getMaxThreadNum()));
		
		//创建连接池
		this.dataPools.put(dbConfig.getPoolName(), new HikariDataSource(dbConfig));
	}
	
	/**
	 * &gt;&gt;&nbsp;根据配置中定义的数据库连接池信息，返回一个数据库连接池对象。
	 * @param poolName 连接池名称，在配置文件中的 poolName 属性。
	 * @return 返回一个数据库连接池对象。
	 */
	public DataSource getDataPoolByName(String poolName){
		return (DataSource)this.dataPools.get(poolName);
	}
	
	/**
	 * &gt;&gt;&nbsp;<p>释放资源，关闭所管理的连接池。</p>
	 * <p>由于连接池有异常捕捉，这里就不需要异常捕捉处理了。它的关闭操作，由新建的线程完成</p>
	 */
	public void destroyDataPools() {
		this.dataPools.forEach((name, source)->{
			if(source!=null) {
				source.close();
			}
		});
	}
}
