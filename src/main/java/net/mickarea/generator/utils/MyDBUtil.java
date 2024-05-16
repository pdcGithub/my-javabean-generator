/******************************************************************************************************

This file "DBUtil.java" is part of project "generator" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2024 Michael Pang.

*******************************************************************************************************/
package net.mickarea.generator.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.mickarea.generator.models.CommandArguments;
import net.mickarea.generator.models.DatabasePoolManager;
import net.mickarea.generator.models.SqlResult;

/**
 * &gt;&gt;&nbsp;关于数据库操作的工具类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2024年5月15日
 */
public class DBUtil {

	/**
	 * &gt;&gt;&nbsp;数据库连接测试
	 * @param cArgs 命令行传来的参数
	 * @return SqlResult 执行结果对象
	 */
	public static SqlResult testDBConnection(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			//参数校验
			if(cArgs==null) throw new Exception("the argument from command-line is empty ");
			if(!cArgs.getDatabaseType().equalsIgnoreCase("mysql")) throw new Exception("database["+cArgs.getDatabaseType()+"] is not supported");
			//创建连接池对象
			mana = DatabasePoolManager.getInstance(cArgs);
			//获取连接
			conn = mana.getDataPoolByName(cArgs.getPoolName()).getConnection();
			//开始连接测试
			stmt = conn.prepareStatement("select 1 ");
			result.setOk(stmt.execute());
			
		}catch(Exception e) {
			result.setOk(false);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setMessage(String.format("DB Exception=> SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				result.setMessage(e.getMessage());
			}
		}finally {
			//资源释放
			if(rs!=null) { try { rs.close();} catch (Exception e) {} rs=null;}
			if(stmt!=null) { try { stmt.close();} catch (Exception e) {} stmt=null;}
			if(conn!=null) { try { conn.close();} catch (Exception e) {} conn=null;}
			//
			if(mana!=null){
				mana.destroyDataPools();
				mana=null;
			}
		}
		return result;
	}
	
	/**
	 * &gt;&gt;&nbsp;从数据库查询一些表或者视图信息，返回给调用者
	 * @param cArgs 命令行传来的参数
	 * @return SqlResult 执行结果对象
	 */
	public static SqlResult getDatabaseObjects(CommandArguments cArgs) {
		//响应结果对象
		SqlResult result = new SqlResult();
		//数据库管理对象
		DatabasePoolManager mana = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			//参数校验
			if(cArgs==null) throw new Exception("the argument from command-line is empty ");
			if(!cArgs.getDatabaseType().equalsIgnoreCase("mysql")) throw new Exception("database["+cArgs.getDatabaseType()+"] is not supported");
			//创建连接池对象
			mana = DatabasePoolManager.getInstance(cArgs);
			//获取连接
			conn = mana.getDataPoolByName(cArgs.getPoolName()).getConnection();
			//开始连接测试
			stmt = conn.prepareStatement("select table_name  from information_schema.tables where table_schema = ? order by table_name");
			stmt.setObject(1, cArgs.getSchema());
			//执行语句
			//调用SQL查询语句
			boolean hasResultSet = stmt.execute();
			if(hasResultSet) {
				rs = stmt.getResultSet();
				if(rs!=null) {
					List<String> tmpDatas = new ArrayList<String>();
					while(rs.next()) {
						tmpDatas.add(rs.getString(1));
					}
					if(tmpDatas.size()<=0) {
						result.setOk(false);
						result.setMessage("nothing is returned by database");
					}else {
						//将结果转换为数据字符串
						ObjectMapper mapper = new ObjectMapper();
						result.setOk(true);
						result.setMessage("");
						result.setData(mapper.writeValueAsString(tmpDatas));
					}
				}
			} else {
				result.setOk(false);
				result.setMessage("nothing is returned by database");
			}
		}catch(Exception e) {
			result.setOk(false);
			if(e instanceof SQLException) { // 数据库相关异常
				SQLException ex = (SQLException)e;
				result.setMessage(String.format("DB Exception=> SQLException(%s), SQLState(%s), VendorError(%s)", ex.getMessage(), ex.getSQLState(), ex.getErrorCode()));
			}else {
				result.setMessage(e.getMessage());
			}
		}finally {
			//资源释放
			if(rs!=null) { try { rs.close();} catch (Exception e) {} rs=null;}
			if(stmt!=null) { try { stmt.close();} catch (Exception e) {} stmt=null;}
			if(conn!=null) { try { conn.close();} catch (Exception e) {} conn=null;}
			//
			if(mana!=null){
				mana.destroyDataPools();
				mana=null;
			}
		}
		return result;
	}
	
}
