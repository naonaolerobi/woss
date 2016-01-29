package com.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.common.Backup;
import com.common.Configure;
import com.common.ConfigureAware;
import com.common.wossModel;
import com.model.BIDR;

/**
 * 采集模块
 * 	对原始数据处理，获得完整数据
 * @author Smile 燕
 *
 */
public class Gather implements wossModel,ConfigureAware{
	
	/**
	 * 
	 * 采集原始计费Log（radwtmp)文件，
	 * 	整理成BIDR类数据清单
	 * 
	 * @return  封装BIDR集合的对象
	 */

	private Map<String, BIDR> BidrMap;
	private Set<BIDR> BidrSet;
	private BufferedReader br;
	private FileReader fis;
	private String aaa_login_name;
	private String login_ip;
	private Timestamp login_date;
	private Timestamp logout_date;
	private long time_duration;
	private static int count;
	private static long str_count;
	private Backup backup ;
	private long mark;
	private String path;
	private String gatherFileName;
	private String countFileName;
	private Logger logger;
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		path=properties.getProperty("path");
		gatherFileName=properties.getProperty("gatherFileName");
		//System.out.println(gatherFileName);
		countFileName=properties.getProperty("countFileName");
	}
	@Override
	public void setConfigure(Configure configure) {
		// TODO Auto-generated method stub
		backup=configure.getBackup();
		logger=configure.getWossLogger().getClientLogger();
		
		
	}
	public Set<BIDR> gather() {
		try {
			fis = new FileReader(path);
//			fis = new FileReader("src/com/data/ssssas");
			br = new BufferedReader(fis);
			try {
				BidrSet= new HashSet();
				logger.info("加载不完整Map数据中···");
				BidrMap=(Map<String, BIDR>) backup.load(gatherFileName);
				//System.out.println(	logger.getName());
				logger.info("加载不完整Map数据结束");
				logger.info("加载计数器中···");
				Object obj= backup.load(countFileName);
				logger.info("加载计数器完毕");
				if(BidrMap==null){
					BidrMap = new HashMap();
				}
				if(!(obj==null)){
					//System.out.println("mark is not null");
					mark = (long) obj;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String line = null;
			String lineArray[] = null;
			logger.info("正在跳过上一次进度···");
			br.skip(mark);
			logger.info("已跳过");
			logger.info("开始采集数据，请等待···");
			while(br.read()>=0){
				count++;
				BIDR bidr = new BIDR();
				line = br.readLine();
				str_count+=line.length()+3;
				lineArray = line.split("[|]");
				if(lineArray[lineArray.length-3].equals("7")){
					//System.out.println("上线");
					aaa_login_name = lineArray[0];
					bidr.setAaa_login_name(aaa_login_name);
					login_ip = lineArray[4];
					bidr.setLogin_ip(login_ip);
					 
					 String s_date = lineArray[3];
					 long l_date =Long.parseLong(s_date);
					 login_date = new Timestamp(l_date);
					 bidr.setLogin_date(login_date);
					// System.out.println(login_date);
//					 System.out.println(login_ip);
//					 System.out.println(bidr==null);
					 
//					 System.out.println(BidrMap==null);
					 BidrMap.put(login_ip, bidr);
				 }else if(lineArray[lineArray.length-3].equals("8")){
			//		 System.out.println("下线");
					 String s_date = lineArray[lineArray.length-2];
					 long l_date =Long.parseLong(s_date);
					 logout_date = new Timestamp(l_date);
					 login_ip = lineArray[lineArray.length-1];
					 
					 BIDR b=BidrMap.get(login_ip);
					 if(b==null){
						 System.out.println("采集出错");
					 }
					 else{
						 
						 b.setLogout_date(logout_date);						 
						 login_date  = b.getLogin_date();
						 time_duration = logout_date.getTime()-login_date.getTime();
						 b.setTime_duration(time_duration);
						 BidrSet.add(b);
						// System.out.println("jinru");
						 BidrMap.remove(login_ip);
					 }
				 }

					 
				 }
			logger.info("采集结束");
			//进行map集合的备份
			try {
				logger.info("正在进行Map不完整数据的备份···");
				backup.backup(gatherFileName, BidrMap);
				logger.info("已备份");
				logger.info("正在对读取字符个数进行备份");
				//backup.backup("BidrSet", BidrSet);
				backup.backup(countFileName, str_count);
				logger.info("已备份");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//System.out.println(BidrSet==null);
		return BidrSet;
	}

	
//	public static void main(String[] args) {
//	
//		HashSet s=(HashSet) new Gather().gather();
//		for (Object object : s) {
//			BIDR sss = (BIDR) object;
//		System.out.println(sss);
//			
//		}
//		System.out.println(str_count);
//	}
}