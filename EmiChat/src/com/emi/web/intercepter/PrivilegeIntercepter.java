package com.emi.web.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class PrivilegeIntercepter implements HandlerInterceptor {

	/**
	 * afterCompletion在DispatcherServlet完全处理完请求后被调用,可用于清理资源等 。afterCompletion()执行完成后开始渲染页面
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * postHandle在业务处理器处理请求执行完成后,生成视图之前执行;
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * preHandle在业务处理器处理请求之前被调用;
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(request.getSession().getAttribute("user") == null)
		{
			request.setAttribute("msg", "对不起，您还未登录，请先登录");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			//request.getRequestDispatcher("/indexUI").forward(request, response);
			/*request.getRequestDispatcher(path)的参数path既可以填入项目的文件路径（eg：/WEB-INF/jsp/alert.jsp）
			也可以填入浏览器的url地址（eg：/alertUI），但它们都是服务器端跳转。*/
			return false;
		}
		else
		{
			return true;
		}
	}

}
