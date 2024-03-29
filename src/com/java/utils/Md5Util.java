package com.java.utils;

import java.security.MessageDigest;
/**
 * 1.md5的工具类 
 * 项目名称：WeixinPay 
 * 类名称：Md5Util
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午3:23:40
 */
public class Md5Util {

	/**
	 * 1.MD5加码 生成32位md5码
	 * @param str
	 * @return
	 */
    public static String string2MD5(String str){
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 2.摘要的计算
     * @param origin
     * @param charsetname
     * @return
     */
    public static String MD5Encode(String origin, String charsetname) {    
        String resultString = null;    
        try {    
            resultString = new String(origin);    
            MessageDigest md = MessageDigest.getInstance("MD5");    
            if (charsetname == null || "".equals(charsetname))    
                resultString = byteArrayToHexString(md.digest(resultString    
                        .getBytes()));    
            else    
                resultString = byteArrayToHexString(md.digest(resultString    
                        .getBytes(charsetname)));    
        } catch (Exception exception) {    
        }    
        return resultString;    
    }    
    
    private static String byteArrayToHexString(byte b[]) {    
        StringBuffer resultSb = new StringBuffer();    
        for (int i = 0; i < b.length; i++)    
            resultSb.append(byteToHexString(b[i]));    
        return resultSb.toString();    
    }    
    
    private static String byteToHexString(byte b) {    
        int n = b;    
        if (n < 0)    
            n += 256;    
        int d1 = n / 16;    
        int d2 = n % 16;    
        return hexDigits[d1] + hexDigits[d2];    
    }    
    
    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5","6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };    
}
