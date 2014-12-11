package com.iiovecherry.net.util.net;

import java.util.HashMap;

/**
 * 	
 * 	@FUCTION:webservice请求获取网络String请求参数封装
 *	@EditTime:2014-8-20 at 下午2:15:34
 *  @author iiovecherry
 *	@MARKUP:
 */
public class WebserviceRequest {
	//网络请求的weBservice地址
	private String url;
	private String method;
	private String nameSpace;
	private HashMap<String, String> params;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getNameSpace() {
		return nameSpace;
	}
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	public HashMap<String, String> getParams() {
		return params;
	}
	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}	
}
