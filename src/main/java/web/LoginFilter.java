package web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查过滤器，用来判断用户是否登录
 * @author wangg
 *
 */
public class LoginFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		   //凭啥能转型，看图
           HttpServletRequest request = (HttpServletRequest)req;
           HttpServletResponse response =(HttpServletResponse)res;
           //有三个请求不需要检查，将他们排除
           String[] paths = new String[]{
        		   "/toLogin.do","/login.do","/createImg.do"
           };
           //判断当前路径是否为3者之一，若是则请求继续，不再执行后面的判断
           String sp = request.getServletPath();
           for(String p:paths){
        	   if(sp.equals(p)){
        		   chain.doFilter(request, response);
        	       return;
        	   }
           }
           
		   //从session中尝试获取adminCode
	       HttpSession session = request.getSession();
	       String adminCode = (String)session.getAttribute("adminCode");
		   //通过adminCode判断用户是否登录
	       if(adminCode==null){
	    	   //没登录，重定向登录页面
                response.sendRedirect(request.getContextPath()+"/toLogin.do");
	       }else {
			  //已登录，请求继续
	    	   chain.doFilter(request, response);
		}
	       
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
