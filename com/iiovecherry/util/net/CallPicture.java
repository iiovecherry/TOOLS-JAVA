package com.iiovecherry.net.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 
 * @FUCTION:网络访问获取图片信息
 * @EditTime:2014-8-20 at 下午2:35:36
 * @author iiovecherry
 * @MARKUP:
 */
public class CallPicture {
	private PictureRrequest reRrequest;
	private String message;

	public CallPicture(PictureRrequest reRrequest) {
		super();
		this.reRrequest = reRrequest;
	}

	/**
	 * 访问网络，返回数据
	 * 
	 * @return
	 */
	public Bitmap getData() {
		Bitmap bitmap = null;
		if (!validateParam()) {
			return bitmap;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		InputStream is;
		options.inJustDecodeBounds = true;
		try {
			int timeoutConnection = 10000;
			int timeoutSocket = 13000;
			HttpGet httpRequest = new HttpGet(reRrequest.getUrl());
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpResponse response = httpclient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
			is = bufferedHttpEntity.getContent();
			BitmapFactory.decodeStream(is, null, options);
			options.inSampleSize = calculateSampleSize(options, reRrequest.getWidth(), reRrequest.getHeight());
			options.inJustDecodeBounds = false;
			if (is.markSupported()) {
				is.reset();
			}
			bitmap = BitmapFactory.decodeStream(is, null, options);
			is.close();
		} catch (MalformedURLException e) {
			message = Message.MSG_EXCEPTION_URL;
			e.printStackTrace();
		} catch (IOException e) {
			message = Message.MSG_EXCEPTION_IO;
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 对请求参数进行验证
	 */
	public boolean validateParam() {
		if (reRrequest == null) {
			message = Message.PARAM_NULL_BITREQUEST;
			return false;
		}
		if (reRrequest.getUrl() == null) {
			message = Message.PARAM_NULL_URL;
			return false;
		}
		return true;
	}

	/**
	 * 计算取样大小
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int sampleSize = 1;
		// 获取图片参数
		final int height = options.outHeight;
		final int width = options.outWidth;
		// 计算取样参数
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / sampleSize) > reqHeight && (halfWidth / sampleSize) > reqWidth) {
				sampleSize *= 2;
			}
		}
		return sampleSize;
	}

	public PictureRrequest getReRrequest() {
		return reRrequest;
	}

	public void setReRrequest(PictureRrequest reRrequest) {
		this.reRrequest = reRrequest;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
