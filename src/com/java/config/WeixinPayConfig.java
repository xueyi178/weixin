package com.java.config;

/**
 *1. 微信支付配置文件
 * @author Administrator
 *
 */
public class WeixinPayConfig {

	private static final String appid=""; // 公众账号ID
	
	private static final String mch_id=""; // 商户号
	
	private static final String device_info="WEB"; // 设备号
	
	private static final String url="https://api.mch.weixin.qq.com/pay/unifiedorder"; // 支付请求地址
	
	//是一个servlet请求
	//通知地址
	private static final String notify_url="http://pay2.java1234.com/notifyUrl"; // 公众账号ID
	
	private static final String key="11111"; // 商户的kye【API密钥】

	public static String getAppid() {
		return appid;
	}

	public static String getMchId() {
		return mch_id;
	}

	public static String getDeviceInfo() {
		return device_info;
	}

	public static String getUrl() {
		return url;
	}

	public static String getNotifyUrl() {
		return notify_url;
	}

	public static String getKey() {
		return key;
	}
}
