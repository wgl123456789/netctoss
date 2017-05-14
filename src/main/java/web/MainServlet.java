package web;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.prism.Image;

import dao.AdminDao;
import dao.CostDao;
import entity.Admin;
import entity.Cost;
import util.ImageUtil;

public class MainServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //获取访问路径
		String path = req.getServletPath();
		//根据规范（看图）处理此路径
		if("/findCost.do".equals(path)){
			//查询资费
			findCost(req,res);
			
		}else if ("/toAddCost.do".equals(path)) {
			//打开增加页面
			toAddCost(req,res);
		}else if ("/addCost.do".equals(path)) {
			 //增加资费
			addCost(req, res);
		}else if ("/toUpdateCost.do".equals(path)) {
			//打开修改界面
			toUpdateCost(req,res);
		}else if ("/updateCost.do".equals(path)) {
			//修改保存
			updateCost(req,res);
			
		}else if ("/toLogin.do".equals(path)) {
			//打开登陆页面
			toLogin(req,res);
		}else if ("/toIndex.do".equals(path)) {
			//打开主页
			toIndex(req,res);
		}else if ("/login.do".equals(path)) {
			//登录检查
			login(req,res);
		}else if ("/createImg.do".equals(path)) {
			//生成验证码
			createImg(req,res);
			
		}else {
			throw new RuntimeException("查无此页");
		}
	
	}
	
	protected void createImg(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		  //生成验证码及其图片
		Object[] objects = ImageUtil.createImage();
		//将验证码存入session
		HttpSession session = req.getSession();
		session.setAttribute("imgcode", objects[0]);
		//将图片输入给浏览器
		res.setContentType("image/png");
		OutputStream os = res.getOutputStream();
		BufferedImage img = (BufferedImage)objects[1];
		ImageIO.write(img, "png", os);
		os.close();
		
	}
	
	
	protected void toUpdateCost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		//根据id接收请求数据
		 String id = req.getParameter("costId");
		 //查询id的来源
		CostDao dao = new CostDao();
		Cost cost = dao.findById(new Integer(id));
               
		   req.setAttribute("cost", cost);
		   req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);		
		
	}
	
	protected void updateCost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
		   res.setCharacterEncoding("utf-8");
		   //接受表单信息
		    String costId = req.getParameter("costId");
			String name = req.getParameter("name");
			String baseDuration = req.getParameter("baseDuration");
			String baseCost = req.getParameter("baseCost");
			String unitCost = req.getParameter("unitCost");
			String descr = req.getParameter("descr");
			String costType = req.getParameter("costType");
			//保存这些数据
			Cost c = new Cost();
			c.setCostId(new Integer (costId));
			c.setName(name);
			c.setDescr(descr);
			c.setCostType(costType);
			if(baseDuration!=null &&!baseDuration.equals("")){
				c.setBaseDuration(new Integer(baseDuration));
			}
		   
			if(baseCost != null &&!baseCost.equals("")){
				c.setBaseCost(new Double(baseCost));
			}
            if(unitCost != null && unitCost.equals("")){
            	c.setUnitCost(new Double(unitCost));
            }
			new CostDao().sava(c);
			//重定向到查询资费
			//当前：/necctoss/updateCost.do
			//目标：/netctoss/findCost.do
			res.sendRedirect("findCost.do");
		 
		
		
		
	}
	
	protected void login(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		//接受表单数据
		String adminCode = req.getParameter("adminCode");
		String password = req.getParameter("password");
		String code = req.getParameter("code");
		HttpSession ses = req.getSession();
		String ss = (String) ses.getAttribute("imgcode");
		//检查账号密码
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		System.out.println(ss);
		System.out.println(code);
		if(a==null){
			//账号错误
			req.setAttribute("error", "账号错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			
		}else if (!a.getPassword().equals(password)) {
			//密码错误
			req.setAttribute("error", "密码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else if (!ss.equalsIgnoreCase(code)) {
			//验证码错误
			req.setAttribute("error", "验证码错误");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			
		}else {
			//检查通过
			//将账号存入cookie，给后续功能使用
			Cookie c = new Cookie("adminCode",adminCode);
			res.addCookie(c);
			
			//也可以将账号存入session
			ses.setAttribute("adminCode",adminCode);
			//重定向到主页
			res.sendRedirect("toIndex.do");
		}
		
	}
	
	protected void toLogin(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		
	}
	protected void toIndex(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		req.getRequestDispatcher("WEB-INF/main/index.jsp").forward(req, res);
		
	}
	
	protected void addCost(HttpServletRequest req, HttpServletResponse res) 
			                   throws ServletException, IOException{
		req.setCharacterEncoding("utf-8");
		//接收表单数据
		String name = req.getParameter("name");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		String costType = req.getParameter("costType");
		//保存这些数据
		Cost c = new Cost();
		c.setName(name);
		if(baseDuration!=null &&!baseDuration.equals("")){
			c.setBaseDuration(new Integer(baseDuration));
		}
	   
		if(baseCost != null &&!baseCost.equals("")){
			c.setBaseCost(new Double(baseCost));
		}

		c.setDescr(descr);
		c.setCostType(costType);
		new CostDao().sava(c);
		//重定向到查询资费
		//当前：/necctoss/addCost.do
		//目标：/netctoss/findCost.do
		res.sendRedirect("findCost.do");
	}
	
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res)
			                               throws ServletException, IOException{
		 //转发:/nectcoss/toAddCost.do
		 //当前:/netctoss/WEB-INF/cost/add.jsp
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
		
	}
	
	protected void findCost(HttpServletRequest req, HttpServletResponse res) 
			                                  throws ServletException, IOException{
		//查询所有的资费
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		//转发
		req.setAttribute("costs",list);
		//当前:/netctoss/findCost.do
		//目标:/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
		
		
		
		
		
	}
   
}
