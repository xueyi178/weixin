package com.java.utils;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.zxing.common.BitMatrix;
import com.java.config.WeixinPayConfig;

public class WeiXinMethod {
	/**
	 * 获取本机IP地址
	 * @return IP
	 */
	public static String getRemortIP(HttpServletRequest request) {  
		if (request.getHeader("x-forwarded-for") == null) {  
			return request.getRemoteAddr();  
		}  
		return request.getHeader("x-forwarded-for");  
	}

	/**
	 * 微信支付签名算法sign
	 */
	public static String getSign(Map<String,Object> map) {
		StringBuffer sb = new StringBuffer();
		//获取map中的key转为array
		String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);
		Arrays.sort(keyArr);
		//对array排序
		for (int i = 0, size = keyArr.length; i < size; ++i) {
			if ("sign".equals(keyArr[i])) {
				continue;
			}
			sb.append(keyArr[i] + "=" + map.get(keyArr[i]) + "&");
		}
		sb.append("key=" + WeixinPayConfig.getKey());
		String sign = Md5Util.string2MD5(sb.toString());
		return sign;
	}

	/**
	 * 通过返回IO流获取支付地址
	 * @param in
	 * @return
	 */
	public static String getElementValue(InputStream in,String key){
		SAXReader reader = new SAXReader();
		Document document=null;
		try {
			document = reader.read(in);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		List<Element> childElements = root.elements();
		for (Element child : childElements) {
			System.out.println(child.getName()+":"+child.getStringValue());
			if(key.equals(child.getName())){
				return child.getStringValue();
			}
		}
		return null;
	}

	/** 
	 * 类型转换 
	 * @author chenp 
	 * @param matrix 
	 * @return 
	 */  
	public static BufferedImage toBufferedImage(BitMatrix matrix) {  
		int width = matrix.getWidth();  
		int height = matrix.getHeight();  
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  
		for (int x = 0; x < width; x++) {  
			for (int y = 0; y < height; y++) {  
				image.setRGB(x, y, matrix.get(x, y) == true ? 0xff000000 : 0xFFFFFFFF);  
			}  
		}  
		return image;  
	}
}
