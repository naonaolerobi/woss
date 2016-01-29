package com.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import com.common.Configure;
import com.model.BIDR;
import com.server.DBStore;
import com.server.Server;

/**
 * 中央处理器端主程序
 * 	@author Smile 燕
 *
 */
public class ServerMain {
	
	public static void main(String[] args) {
		Configure configure = new Configure();
		
//		接收数据
		Server server = configure.getServer();
		Set<BIDR> set=server.receive();
		int count=0;
//		for (Object object : set) {
//			count++;
//			System.out.println(count);
//			BIDR sss = (BIDR) object;
//			System.out.println(sss);
//			
//	}
//		数据入库
		DBStore dbs = configure.getDBStore();
		dbs.saveToDB(set);
	}
}
