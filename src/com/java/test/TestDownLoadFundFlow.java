package com.java.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.ClientProtocolException;

import com.java.config.WeixinPayConfig;
import com.java.utils.HMACSHA256Uitl;
import com.java.utils.HttpClientUtil;
import com.java.utils.StringUtil;
import com.java.utils.XmlUtil;

/**
 * 下载资金账单测试
 * @author Administrator
 *
 */
public class TestDownLoadFundFlow {

	private static String url="https://api.mch.weixin.qq.com/pay/downloadfundflow";
	
	public static void main(String[] args) throws UnsupportedOperationException, ClientProtocolException, IOException {
		Map<String,Object> map=new HashMap<String,Object>();
		// 公众账号ID
		map.put("appid", WeixinPayConfig.getAppid()); 
		// 商户号
		map.put("mch_id", WeixinPayConfig.getMchId()); 
		// 随机字符串
		map.put("nonce_str", StringUtil.getRandomString(30)); 
		// 资金账单日期
		map.put("bill_date", "20180419"); 
		 // 资金账户类型
		map.put("account_type", "Basic");
		 // 签名
		map.put("sign", getSign(map));
		String xml=XmlUtil.genXml(map); 
		System.out.println(xml);
		InputStream in = HttpClientUtil.sendXMLDataByHttpsPost(url, xml).getEntity().getContent(); // 发现xml消息
		StringBuffer out = new StringBuffer();
		byte [] b = new byte[4096];
		for(int n;(n=in.read(b))!=-1;){
			out.append(new String(b,0,n));
		}
		System.out.println(out.toString());
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
        // String sign = Md5Util.string2MD5(sb.toString());
        String sign=HMACSHA256Uitl.HMACSHA256(sb.toString().getBytes(), WeixinPayConfig.getKey().getBytes());
        return sign;
    }
}
