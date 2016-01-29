package com.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.common.Backup;
import com.common.Configure;
import com.common.ConfigureAware;
import com.common.wossModel;
import com.model.BIDR;

/**
 * 入库模块
 * 	将接收到的完整数据入库
 * @author Smile 燕
 *
 */
public class DBStore implements wossModel,ConfigureAware{
	

	/**
	 * 将接收到的完整数据入库
	 * @param bidrs 待入库的完整数据
	 */
	private String driver;
	private String user;
	private String password;
	private String url;
	private Backup backup;
	private Object obj;
	private String serverFileName;
	private Logger logger;
	@Override
	public void setConfigure(Configure configure) {
		// TODO Auto-generated method stub
		backup=configure.getBackup();
		logger=configure.getWossLogger().getServerLogger();		
	}
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		driver=properties.getProperty("driverName");
		url=properties.getProperty("url");
		user=properties.getProperty("user");
		password=properties.getProperty("password");
		serverFileName=properties.getProperty("bakFileName");
		
	}
	public void saveToDB(Set<BIDR> bidrs){
		Set<BIDR> set = bidrs;
		Connection conn = null;
		try {
			Class.forName(driver);
			conn= DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
			//System.out.println(conn==null);
			try {
				obj=backup.load(serverFileName);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(!(obj==null)){
					Set<BIDR> set1=(Set) obj;
					logger.info("正在加载上一次存储失败的数据···");
					set.addAll(set1);
					logger.info("已加载");
				}else{}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("开始存储数据，请稍后···");
			for (BIDR bidr : set) {
				int day=bidr.getLogin_date().getDate();
				String sql = "insert into T_detail_"+day+" values(?,?,?,?,?,?)";
				//System.out.println(sql);
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, bidr.getAaa_login_name());
				ps.setString(2, bidr.getLogin_ip());
				ps.setTimestamp(3, bidr.getLogin_date());
				ps.setTimestamp(4, bidr.getLogout_date());
				ps.setString(5, bidr.getNas_ip());
				ps.setLong(6, bidr.getTime_duration());
				ps.executeQuery();
				ps.close();
				
			}
			logger.info("存储数据完毕");
			conn.commit();
			
			//String sql=;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			try {
				logger.info("存储数据异常，正在备份数据···");
				conn.rollback();
				backup.backup(serverFileName, set);
				logger.info("已备份");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
			e.printStackTrace();
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
}
