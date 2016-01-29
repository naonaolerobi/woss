package com.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

/**
 * 备份类
 * @author Smile 燕
 */
public class Backup implements wossModel,ConfigureAware{
	private String path;
	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		path=properties.getProperty("path");
	}
	
	/**
	 * 备份数据
	 * @param fileName 备份文件
	 * @param data  备份数据
	 * @throws Exception 
	 */
	public void backup(String fileName,Object data) throws Exception{
		File file =new File(path+fileName);
		if(!file.exists()){
			file.createNewFile();
		}else{
			file.delete();
			file.createNewFile();
		}
		ObjectOutputStream out=new ObjectOutputStream(
								new FileOutputStream(file));
		out.writeObject(data);
		out.flush();	
		out.close();
	}
	
	/**
	 * 加载备份
	 * @param fileName 备份文件
	 * @return  备份数据
	 * @throws Exception  
	 */
	public Object load(String fileName) throws Exception{
		Object object =null;
		File file=new File(path+fileName);
		if(file.exists()){
			ObjectInputStream oin=new ObjectInputStream(
										new FileInputStream(file));
			object = oin.readObject();
			oin.close();
		}
		return object;
	}
	
	/**
	 * 删除备份
	 * @param fileName
	 */
	public void deleteBackup(String fileName){
		File file=new File(path+fileName);
		if(file.exists()){
			file.delete();
		}
		
	}

	@Override
	public void setConfigure(Configure configure) {
		// TODO Auto-generated method stub
		
	}

}
