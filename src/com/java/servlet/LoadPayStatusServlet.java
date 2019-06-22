package com.java.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 1.serlvet获取支付状态
 * 项目名称：WeixinPay 
 * 类名称：LoadPayStatusServlet
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午4:04:25
 */
public class LoadPayStatusServlet extends HttpServlet{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -3635061463852497902L;
	
	private int i = 0;

	private int result = 0;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		i++;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(i == 10) {
			result = 1;
		}
		System.out.println("i="+ i);
		System.out.println("result="+ result);
		resp.setContentType("text/html;charset=utf-8");
		PrintWriter writer = resp.getWriter();
		writer.println(result);
		writer.flush();
		writer.close();
	}
}
