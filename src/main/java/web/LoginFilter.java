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
 * ��¼���������������ж��û��Ƿ��¼
 * @author wangg
 *
 */
public class LoginFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		   //ƾɶ��ת�ͣ���ͼ
           HttpServletRequest request = (HttpServletRequest)req;
           HttpServletResponse response =(HttpServletResponse)res;
           //������������Ҫ��飬�������ų�
           String[] paths = new String[]{
        		   "/toLogin.do","/login.do","/createImg.do"
           };
           //�жϵ�ǰ·���Ƿ�Ϊ3��֮һ���������������������ִ�к�����ж�
           String sp = request.getServletPath();
           for(String p:paths){
        	   if(sp.equals(p)){
        		   chain.doFilter(request, response);
        	       return;
        	   }
           }
           
		   //��session�г��Ի�ȡadminCode
	       HttpSession session = request.getSession();
	       String adminCode = (String)session.getAttribute("adminCode");
		   //ͨ��adminCode�ж��û��Ƿ��¼
	       if(adminCode==null){
	    	   //û��¼���ض����¼ҳ��
                response.sendRedirect(request.getContextPath()+"/toLogin.do");
	       }else {
			  //�ѵ�¼���������
	    	   chain.doFilter(request, response);
		}
	       
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
