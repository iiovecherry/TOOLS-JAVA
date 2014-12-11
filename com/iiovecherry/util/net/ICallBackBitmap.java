package com.iiovecherry.net.util.net;

import android.graphics.Bitmap;

/**
 * 	
 * 	@FUCTION:网络访问图片回调接口
 *	@EditTime:2014-8-20 at 上午11:57:44
 *  @author iiovecherry
 *	@MARKUP:返回数据类型：Bitmap
 */
public interface ICallBackBitmap {
	/**
	 * 网络访问完成回调接口
	 * @param bitmap 网络放回图片信息
	 * @param message 网络异常返回的提示信息
	 */
	public void netFinished(Bitmap bitmap,String message);
}
