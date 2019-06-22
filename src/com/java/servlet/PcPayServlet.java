package com.java.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 1.servlet
 * 查询出商品信息的servlet
 * 项目名称：WeixinPay 
 * 类名称：PcPayServlet
 * 开发者：Lenovo
 * 开发时间：2019年6月22日下午12:54:38
 */
public class PcPayServlet extends HttpServlet{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 7854897091811579442L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//根据实际业务获取商品信息以及支付金额
		req.getRequestDispatcher("pay.jsp").forward(req, resp);
	}
}
