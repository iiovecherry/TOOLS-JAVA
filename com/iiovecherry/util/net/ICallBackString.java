package com.iiovecherry.net.util.net;
/**
 * 	
 * 	@FUCTION:网络访问返回String类型数据回调接口
 *	@EditTime:2014-8-20 at 上午11:55:03
 *  @author iiovecherry
 *	@MARKUP:
 */
public interface ICallBackString {
	/**
	 * 网络访问完成回调接口
	 * @param dataStr 网络获取的字符串数据
	 * @param message 网络异常的错误信息
	 */
	public void netFinished(String dataStr,String message);
}
