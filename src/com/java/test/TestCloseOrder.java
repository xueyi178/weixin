package com.java.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.java.config.WeixinPayConfig;
import com.java.utils.HttpClientUtil;
import com.java.utils.Md5Util;
import com.java.utils.StringUtil;
import com.java.utils.XmlUtil;

/**
 * 1.关闭订单接口
 * 项目名称：WeixinPay 
 * 类名称：TestCloseOrder
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午4:52:24
 */
public class TestCloseOrder {

	private static String url="https://api.mch.weixin.qq.com/pay/closeorder";
	
	public static void main(String[] args) throws UnsupportedOperationException, ClientProtocolException, IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("appid", WeixinPayConfig.getAppid()); // 公众账号ID
		map.put("mch_id", WeixinPayConfig.getAppid()); // 商户号
		map.put("out_trade_no", "20180404022005421"); // 商户订单号
		map.put("nonce_str", StringUtil.getRandomString(30)); // 随机字符串
		map.put("sign", getSign(map)); // 签名
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		InputStream in=HttpClientUtil.sendXMLDataByPost(url, xml).getEntity().getContent(); // 发现xml消息
		getElementValue(in);
	}
	
	/**
	 * 通过返回IO流获取支付地址
	 * @param in
	 * @return
	 */
	private static void getElementValue(InputStream in){
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
        }
	}
	
	/**
     * 微信支付签名算法sign
     */
    private static String getSign(Map<String,Object> map) {
        StringBuffer sb = new StringBuffer();
        String[] keyArr = (String[]) map.keySet().toArray(new String[map.keySet().size()]);//获取map中的key转为array
        Arrays.sort(keyArr);//对array排序
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
}
