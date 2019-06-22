package com.java.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;

import com.java.config.WeixinPayConfig;
import com.java.utils.Md5Util;
import com.java.utils.XmlUtil;
/**
 * 1.微信支付的异步回调
 * 项目名称：WeixinPay 
 * 类名称：PayNotifyUrlServlet
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午3:42:45
 */
public class PayNotifyUrlServlet extends HttpServlet{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -5657639524850583417L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//读取参数    
		InputStream inputStream ;    
		StringBuffer sb = new StringBuffer();    
		inputStream = req.getInputStream();    
		String s ;    
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));    
		while ((s = in.readLine()) != null){    
			sb.append(s);    
		}    
		in.close();    
		inputStream.close();  
		System.out.println("sb:"+sb.toString());

		//解析xml成map    
		Map<String, String> m = new HashMap<String, String>();    
		try {
			m = XmlUtil.doXMLParse(sb.toString());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    

		//过滤空 设置 TreeMap    
		SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();          
		Iterator<String> it = m.keySet().iterator();    
		while (it.hasNext()) {    
			String parameter = it.next();    
			String parameterValue = m.get(parameter);    

			String v = "";    
			if(null != parameterValue) {    
				v = parameterValue.trim();    
			} 
			packageParams.put(parameter, v);    
		}  

		// 微信支付的API密钥    
		String key = WeixinPayConfig.getKey();

		if(isTenpaySign("UTF-8", packageParams, key)){ // 验证通过
			if("SUCCESS".equals((String)packageParams.get("result_code"))){  
				System.out.println("验证通过");
			}else{
				System.out.println("支付失败");
			}
		}else{
			System.out.println("验证未通过");
		}
	}


	/**  
	 * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。  
	 * @return boolean  
	 */    
	public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {    
		StringBuffer sb = new StringBuffer();    
		Set es = packageParams.entrySet();    
		Iterator it = es.iterator();    
		while(it.hasNext()) {    
			Map.Entry entry = (Map.Entry)it.next();    
			String k = (String)entry.getKey();    
			String v = (String)entry.getValue();    
			if(!"sign".equals(k) && null != v && !"".equals(v)) {    
				sb.append(k + "=" + v + "&");    
			}    
		}    
		sb.append("key=" + API_KEY);    

		//算出摘要    
		String mysign = Md5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();    
		String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();    

		return tenpaySign.equals(mysign);    
	}  
}
