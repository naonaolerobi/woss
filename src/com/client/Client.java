package com.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.common.Backup;
import com.common.Configure;
import com.common.ConfigureAware;
import com.common.wossModel;
import com.model.BIDR;

/**
 * 
 * 网络模块之发送端
 * 	发送采集到的完整数据到中央处理器
 * @author Smile 燕
 *
 */
public class Client implements wossModel,ConfigureAware {
	/**
	 * 发送采集到的数据到中央处理器
	 * @param bidrs 采集到的数据
	 */
	private ObjectOutputStream oos;
	private String ip;
	private int port;
	private Backup backup;
	private String clientFileName;
	private Logger logger;
	@Override
	public void setConfigure(Configure configure) {
		// TODO Auto-generated method stub
		backup=configure.getBackup();
		logger=configure.getWossLogger().getClientLogger();
	}
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		ip=properties.getProperty("ip");
		port=Integer.parseInt(properties.getProperty("port"));
		clientFileName=properties.getProperty("bakFileName");
	}
	public void send(Set<BIDR> bidrs){
		try {
			Socket socket = new Socket(ip,port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			logger.info("正在传送数据···");
			oos.writeObject(bidrs);
			logger.info("传送数据结束");
		} catch (Exception e) {
			try {
				logger.info("数据传送失败，正在备份未能成功发送的数据···");
				backup.backup("BidrSet", bidrs);
				logger.info("已备份");
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				oos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		
	}
}
