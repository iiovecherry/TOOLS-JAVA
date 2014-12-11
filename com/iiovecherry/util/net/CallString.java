package com.iiovecherry.net.util.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 	
 * 	@FUCTION:网络访问获取String数据
 *	@EditTime:2014-8-20 at 下午2:34:35
 *  @author iiovecherry
 *	@MARKUP:
 */
public class CallString {
	private WebserviceRequest request;
	private String message;
	public CallString(WebserviceRequest request) {
		super();
		this.request = request;
	}
	/**
	 *  网络访问返回数据
	 * @return
	 */
	public String getData() {
		String dataStr=null;
		if(!validateParam()){
			return dataStr;
		}	
		SoapObject soap = new SoapObject(request.getNameSpace(), request.getMethod());
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = soap;
		envelope.dotNet = false;
		//该方法有待改进
		HashMap<String, String> params=request.getParams();
		if(params!=null){
			Set<String> iterator=params.keySet();
			for(String key:iterator){
				soap.addProperty(key, params.get(key));
			}
		}
		envelope.setOutputSoapObject(soap);
		HttpTransportSE ht = new HttpTransportSE(request.getUrl());
		try {
			ht.call(null, envelope);
			dataStr = envelope.getResponse().toString();
		} catch (UnknownHostException e) {
			message = Message.MSG_EXCEPTION_UNKNOWHOST;
			e.printStackTrace();
		} catch (IOException e) {
			message = Message.MSG_EXCEPTION_IO;
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			message = Message.MSG_EXCEPTION_PARSER;
			e.printStackTrace();
		}
		ht.debug = true;
		return dataStr;		
	}
	/**
	 * 验证请求参数
	 * @return
	 */
	public boolean validateParam(){
		if(request==null){
			message=Message.PARAM_NULL_STRREQUEST;
			return false;
		}
		if(request.getMethod()==null){
			message=Message.PARAM_NULL_METHOD;
			return false;
		}
		if(request.getNameSpace()==null){
			message=Message.PARMA_NULL_NAMASPACE;
			return false;
		}
		if(request.getUrl()==null){
			message=Message.PARAM_NULL_URL;
			return false;
		}
		return true;
	}
	public WebserviceRequest getRequest() {
		return request;
	}
	public void setRequest(WebserviceRequest request) {
		this.request = request;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
