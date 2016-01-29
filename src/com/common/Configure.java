package com.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.client.Client;
import com.client.Gather;
import com.server.DBStore;
import com.server.Server;

public class Configure {
	private Map<String, Object> wossModels = new HashMap<String, Object>();
	public Configure(){
		this("F:\\woss\\client\\etc\\config.xml");
	}
	public Configure(String configFileName){
		SAXReader saxReader = new SAXReader();
		try {
			Document doc = saxReader.read(new File(configFileName));
			Element rootElement = doc.getRootElement();
			List<Element> classEles = rootElement.elements();
			for (Element e1 : classEles) {
				String className = e1.attributeValue("class");
				Properties properties = new Properties();
				List<Element> proEles = e1.elements();
					for (Element e2 : proEles) {
						properties.put(e2.getName(), e2.getText());
					}
					wossModel wossModel = (wossModel)Class.forName(className).newInstance();
					wossModel.init(properties);
					wossModels.put(e1.getName(),wossModel);	
			}
			for (Object o : wossModels.values()) {
				if(o instanceof wossModel){
					((ConfigureAware)o).setConfigure(this);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件解析异常");
		}
		
	}
	
	public Client getClient(){
		return (Client) wossModels.get("client");
	}
	public Gather getGather(){
		return (Gather) wossModels.get("gather");
	}
	public Server getServer(){
		return (Server) wossModels.get("server");
	}
	public Backup getBackup(){
		return (Backup) wossModels.get("backup");
	}
	public DBStore getDBStore(){
		return (DBStore) wossModels.get("DBStore");
	}
	public WossLogger getWossLogger(){
		return (WossLogger) wossModels.get("logger");
	}

}
