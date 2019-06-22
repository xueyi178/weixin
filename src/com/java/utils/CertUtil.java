package com.java.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;

import com.java.config.WeixinPayConfig;

/**
 * 1.加载证书的类
 * 项目名称：WeixinPay 
 * 类名称：CertUtil
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午4:54:44
 */
public class CertUtil {
	/**
     * 加载证书
     */
    public static SSLConnectionSocketFactory initCert() throws Exception {
        FileInputStream instream = null;
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        instream = new FileInputStream(new File("c:/apiclient_cert.p12"));
        keyStore.load(instream, WeixinPayConfig.getMchId().toCharArray());

        if (null != instream) {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore,WeixinPayConfig.getMchId().toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

        return sslsf;
    }
}
