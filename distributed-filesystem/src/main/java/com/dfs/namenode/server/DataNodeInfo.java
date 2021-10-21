package com.dfs.namenode.server;

/**
 * 用来描述datanode的信息
 * @author
 *
 */
public class DataNodeInfo {

	private String ip;
	private String hostname;
	
	public DataNodeInfo(String ip, String hostname) {
		this.ip = ip;
		this.hostname = hostname;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
}
