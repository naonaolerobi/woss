package com.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class BIDR implements Serializable{
	private String  aaa_login_name;
	private String login_ip;
	private Timestamp login_date;
	private Timestamp logout_date;
	private String nas_ip;
	private long time_duration;
	public String getAaa_login_name() {
		return aaa_login_name;
	}
	public void setAaa_login_name(String aaa_login_name) {
		this.aaa_login_name = aaa_login_name;
	}
	public String getLogin_ip() {
		return login_ip;
	}
	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}
	public Timestamp getLogin_date() {
		return login_date;
	}
	public void setLogin_date(Timestamp login_date) {
		this.login_date = login_date;
	}
	public Timestamp getLogout_date() {
		return logout_date;
	}
	public void setLogout_date(Timestamp logout_date) {
		this.logout_date = logout_date;
	}
	public String getNas_ip() {
		return nas_ip;
	}
	public void setNas_ip(String nas_ip) {
		this.nas_ip = nas_ip;
	}
	public long getTime_duration() {
		return time_duration;
	}
	public void setTime_duration(long time_duration) {
		this.time_duration = time_duration;
	}
	public String toString(){
		return aaa_login_name+" "+login_date+" "+logout_date+" "+login_ip+" "+time_duration;
	}
	
	 
	

}
