package com.java.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.java.config.WeixinPayConfig;
import com.java.utils.DateUtil;
import com.java.utils.HttpClientUtil;
import com.java.utils.Md5Util;
import com.java.utils.StringUtil;
import com.java.utils.XmlUtil;
/**
 * 1.H5的支付验证
 * 项目名称：WeixinPay 
 * 类名称：WebPayServlet
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午5:47:59
 */
public class WebPayServlet extends HttpServlet{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -8156014837823648521L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 生成订单号
		String orderNo=DateUtil.getCurrentDateStr(); 
		Map<String,Object> map=new HashMap<String,Object>();
		// 公众账号ID
		map.put("appid", WeixinPayConfig.getAppid()); 
		// 商户号
		map.put("mch_id", WeixinPayConfig.getMchId()); 
		// 设备号
		map.put("device_info", WeixinPayConfig.getDeviceInfo()); 
		// 异步通知地址
		map.put("notify_url", WeixinPayConfig.getNotifyUrl()); 
		 // 交易类型
		map.put("trade_type", "MWEB");
		// 商户订单号
		map.put("out_trade_no", orderNo); 
		 // 商品描述
		map.put("body", "测试商品");
		// 标价金额
		map.put("total_fee", 100); 
		// map.put("spbill_create_ip", getRemortIP(req)); // 终端IP
		map.put("spbill_create_ip", "127.0.0.1"); // 终端IP
		// 随机字符串
		map.put("nonce_str", StringUtil.getRandomString(30)); 
		// 签名
		map.put("sign", getSign(map)); 
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		// 发现xml消息
		InputStream in=HttpClientUtil.sendXMLDataByPost(WeixinPayConfig.getUrl(), xml).getEntity().getContent(); 
		// 支付跳转链接
		String mweb_url=getElementValue(in,"mweb_url"); 
		//支付完成后,进行页面跳转
		mweb_url+="&redirect_url="+URLEncoder.encode("http://pay2.java1234.com/", "GBK");
		System.out.println(mweb_url);
		resp.sendRedirect(mweb_url);
	}
	
	/**
     * 获取本机IP地址
     * @return IP
     */
	private static String getRemortIP(HttpServletRequest request) {  
        if (request.getHeader("x-forwarded-for") == null) {  
            return request.getRemoteAddr();  
        }  
        return request.getHeader("x-forwarded-for");  
    }
	
	/**
     * 微信支付签名算法sign
     */
    private String getSign(Map<String,Object> map) {
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
    
    /**
	 * 通过返回IO流获取支付地址
	 * @param in
	 * @return
	 */
	private String getElementValue(InputStream in,String key){
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
	
}
