package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import com.common.Configure;
import com.common.ConfigureAware;
import com.common.wossModel;
import com.model.BIDR;

/**
 * 网络模块之接收端
 * 	接收AAA服务器采集到的完整数据
 * @author Smile 燕
 *
 */
public class Server implements wossModel,ConfigureAware{
	/**
	 * 接收AAA服务器采集到的完整数据
	 * @return 接收到的完整数据
	 */
	
	private ObjectInputStream ois;
	private Set<BIDR> set;
	private int port;
	private Logger logger;
	@Override
	public void setConfigure(Configure configure) {
		// TODO Auto-generated method stub
		logger=configure.getWossLogger().getServerLogger();
	}
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		port = Integer.parseInt(properties.getProperty("port"));
	}
	public Set<BIDR> receive(){
		try {
			ServerSocket server = new ServerSocket(port);
			Socket socket=server.accept();
			ois = new ObjectInputStream(socket.getInputStream());
			logger.info("正在接受数据···");
			set = (Set)ois.readObject();
			logger.info("接受数据完毕");
			
			ois.close();
			socket.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return set;
	}
}
