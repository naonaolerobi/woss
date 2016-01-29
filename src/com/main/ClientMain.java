package com.main;

import java.io.IOException;
import java.io.ObjectStreamClass;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Set;

import com.client.Client;
import com.client.Gather;
import com.common.Configure;

/**
 * AAA服务器端主程序
 * @author Smile 燕
 *
 */
public class ClientMain {
	
	public static void main(String[] args) {
		Configure configure = new Configure();
		

		
//		采集数据
		Gather gather = configure.getGather();
		Set bidrs= (Set)gather.gather();
		
//		发送数据
		Client client = configure.getClient();
		client.send(bidrs);
		
		
		
		
	}
}
