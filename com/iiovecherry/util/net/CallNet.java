package com.iiovecherry.net.util.net;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

/**
 * 
 * @FUCTION:网络访问工具类
 * @EditTime:2014-8-20 at 下午12:01:16
 * @author iiovecherry
 * @MARKUP:自动开启线程访问网络
 */
public class CallNet extends Handler implements Runnable {
	// 获取网络图片
	public static int MODEL_PICTRURE = 1;
	// 获取网络字符串
	public static int MODEL_STRING = 0;
	// 访问网络方式
	private int model;
	// webservice请求参数
	private WebserviceRequest webRequest;
	// 图片获取请求参数
	private PictureRrequest PicRequest;
	// 请求随叫数,最大值1024
	private int randomRequstCode;
	//用来缓存网络访问获取的数据
	private String message;
	private String dataStr;
	private Bitmap dataBitMap;
	/**
	 * 获取String回调接口
	 */
	private ICallBackString IBackStr;
	/**
	 * 获取网络图图片回调接口
	 */
	private ICallBackBitmap IBackBit;

	public CallNet(WebserviceRequest request) {
		super();
		this.webRequest = request;
		this.model = MODEL_STRING;
	}

	public CallNet(ICallBackString iBackStr, WebserviceRequest request) {
		super();
		this.IBackStr = iBackStr;
		this.webRequest = request;
		this.model = MODEL_STRING;
	}

	public CallNet(PictureRrequest request) {
		super();
		this.PicRequest = request;
		this.model = MODEL_PICTRURE;
	}

	public CallNet(ICallBackBitmap iBackBit, PictureRrequest request) {
		super();
		this.IBackBit = iBackBit;
		this.PicRequest = request;
		this.model = MODEL_PICTRURE;
	}

	/**
	 * 调用该方法启动网络访问
	 */
	public void getData() {
		this.randomRequstCode = (int) (Math.random() * 1024);
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void handleMessage(Message msg) {
		if (randomRequstCode == msg.what) {
			if (model == MODEL_STRING) {
				if(IBackStr!=null)
				IBackStr.netFinished(dataStr, message);
			} else if (model == MODEL_PICTRURE) {
				if(IBackBit!=null)
				IBackBit.netFinished(dataBitMap, message);
			}
		}
	}
	@Override
	public void run() {
		if (model == MODEL_STRING) {
			CallString option = new CallString(webRequest);
			dataStr = option.getData();
			message = option.getMessage();
		} else if (model == MODEL_PICTRURE) {
			CallPicture option = new CallPicture(PicRequest);
			dataBitMap = option.getData();
			message = option.getMessage();
		}
		this.sendEmptyMessage(randomRequstCode);
	}

	public ICallBackString getIBackStr() {
		return IBackStr;
	}

	public void setIBackStr(ICallBackString iBackStr) {
		IBackStr = iBackStr;
	}

	public ICallBackBitmap getIBackBit() {
		return IBackBit;
	}

	public void setIBackBit(ICallBackBitmap iBackBit) {
		IBackBit = iBackBit;
	}

	public WebserviceRequest getWebRequest() {
		return webRequest;
	}

	public void setWebRequest(WebserviceRequest webRequest) {
		this.webRequest = webRequest;
	}

	public PictureRrequest getPicRequest() {
		return PicRequest;
	}

	public void setPicRequest(PictureRrequest picRequest) {
		PicRequest = picRequest;
	}
	/**
	 * 返回当前请求访问网络获取的类型，是获取图片还是String数据
	 * @return
	 */
	public int getModel() {
		return model;
	}
	/**
	 * 获取网络访问的随机数，每次访问网络都会生成一个0-1023的随机随机数,一次辨别是否是一次网络访问请求
	 * @return
	 */
	public int getRandomRequstCode() {
		return randomRequstCode;
	}
}
