package com.java.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.java.config.WeixinPayConfig;
import com.java.utils.DateUtil;
import com.java.utils.HttpClientUtil;
import com.java.utils.StringUtil;
import com.java.utils.WeiXinMethod;
import com.java.utils.XmlUtil;

/**
 * 1.生成验证码的servlet
 * 项目名称：WeixinPay 
 * 类名称：LoadPayImageServlet
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午1:00:21
 */
public class LoadPayImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//根据订单号获取二维码信息
		//生成订单号
		String orderId = DateUtil.getCurrentDateStr();
		
		//创建一个map
		Map<String, Object> map = new HashMap<>();
		//公众账号ID
		map.put("appid", WeixinPayConfig.getAppid());
		//商户号
		map.put("mch_id", WeixinPayConfig.getMchId());
		//设备号
		map.put("device_info", WeixinPayConfig.getDeviceInfo());
		//异步通知地址
		map.put("notify_url", WeixinPayConfig.getNotifyUrl());
		//随机字符串
		map.put("nonce_str", StringUtil.getRandomString(30));
		//交易类型
		map.put("trade_type", "NATIVE -Native");
		//商户订单号
		map.put("out_trade_no", orderId);
		//商品描述
		map.put("body", "测试商品");
		//标记金额
		map.put("total_fee", 1);
		//终端IP
		//map.put("spbill_create_ip", WeiXinMethod.getRemortIP(request));
		map.put("spbill_create_ip", "127.0.0.1"); // 终端IP
		//签名
		map.put("sign", WeiXinMethod.getSign(map));
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		//发现xml消息;  WeixinPayConfig.getUrl(): 表示请求的URL
		InputStream in=HttpClientUtil.sendXMLDataByPost(WeixinPayConfig.getUrl(), xml).getEntity().getContent(); 
		//从返回的流里面获取到;  获取二维码地址
		String code_url = WeiXinMethod.getElementValue(in,"code_url");
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();  
	    Map hints = new HashMap();  
	    BitMatrix bitMatrix = null;  
	    try {  
	         bitMatrix = multiFormatWriter.encode(code_url, BarcodeFormat.QR_CODE, 250, 250,hints);  
	         BufferedImage image =WeiXinMethod.toBufferedImage(bitMatrix);  
	         //输出二维码图片流  
	         ImageIO.write(image, "png", response.getOutputStream());  
	     } catch (WriterException e1) {  
	         e1.printStackTrace();  
	     }  
	}
}
