package com.common;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class WossLogger implements wossModel,ConfigureAware{
	private String clientLoggerName ;
	private String serverLoggerName;
	private String logPropertiesLocation;

	@Override
	public void init(Properties properties) {
		// TODO Auto-generated method stub
		logPropertiesLocation = properties.getProperty("logPropertiesLocation");
		PropertyConfigurator.configure(logPropertiesLocation);
		clientLoggerName = properties.getProperty("clientLoggerName");
		serverLoggerName = properties.getProperty("serverLoggerName");
	}
	public Logger getClientLogger(){
		return Logger.getLogger(clientLoggerName);
	}
	public Logger getServerLogger(){
		return Logger.getLogger(serverLoggerName);
	}
	@Override
	public void setConfigure(Configure configure) {
		// TODO Auto-generated method stub
		
	}

}
