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
        //��ȡ����·��
		String path = req.getServletPath();
		//���ݹ淶����ͼ�������·��
		if("/findCost.do".equals(path)){
			//��ѯ�ʷ�
			findCost(req,res);
			
		}else if ("/toAddCost.do".equals(path)) {
			//������ҳ��
			toAddCost(req,res);
		}else if ("/addCost.do".equals(path)) {
			 //�����ʷ�
			addCost(req, res);
		}else if ("/toUpdateCost.do".equals(path)) {
			//���޸Ľ���
			toUpdateCost(req,res);
		}else if ("/updateCost.do".equals(path)) {
			//�޸ı���
			updateCost(req,res);
			
		}else if ("/toLogin.do".equals(path)) {
			//�򿪵�½ҳ��
			toLogin(req,res);
		}else if ("/toIndex.do".equals(path)) {
			//����ҳ
			toIndex(req,res);
		}else if ("/login.do".equals(path)) {
			//��¼���
			login(req,res);
		}else if ("/createImg.do".equals(path)) {
			//������֤��
			createImg(req,res);
			
		}else {
			throw new RuntimeException("���޴�ҳ");
		}
	
	}
	
	protected void createImg(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		  //������֤�뼰��ͼƬ
		Object[] objects = ImageUtil.createImage();
		//����֤�����session
		HttpSession session = req.getSession();
		session.setAttribute("imgcode", objects[0]);
		//��ͼƬ����������
		res.setContentType("image/png");
		OutputStream os = res.getOutputStream();
		BufferedImage img = (BufferedImage)objects[1];
		ImageIO.write(img, "png", os);
		os.close();
		
	}
	
	
	protected void toUpdateCost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		//����id������������
		 String id = req.getParameter("costId");
		 //��ѯid����Դ
		CostDao dao = new CostDao();
		Cost cost = dao.findById(new Integer(id));
               
		   req.setAttribute("cost", cost);
		   req.getRequestDispatcher("WEB-INF/cost/update.jsp").forward(req, res);		
		
	}
	
	protected void updateCost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException{
		   res.setCharacterEncoding("utf-8");
		   //���ܱ���Ϣ
		    String costId = req.getParameter("costId");
			String name = req.getParameter("name");
			String baseDuration = req.getParameter("baseDuration");
			String baseCost = req.getParameter("baseCost");
			String unitCost = req.getParameter("unitCost");
			String descr = req.getParameter("descr");
			String costType = req.getParameter("costType");
			//������Щ����
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
			//�ض��򵽲�ѯ�ʷ�
			//��ǰ��/necctoss/updateCost.do
			//Ŀ�꣺/netctoss/findCost.do
			res.sendRedirect("findCost.do");
		 
		
		
		
	}
	
	protected void login(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException{
		//���ܱ�����
		String adminCode = req.getParameter("adminCode");
		String password = req.getParameter("password");
		String code = req.getParameter("code");
		HttpSession ses = req.getSession();
		String ss = (String) ses.getAttribute("imgcode");
		//����˺�����
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode(adminCode);
		System.out.println(ss);
		System.out.println(code);
		if(a==null){
			//�˺Ŵ���
			req.setAttribute("error", "�˺Ŵ���");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			
		}else if (!a.getPassword().equals(password)) {
			//�������
			req.setAttribute("error", "�������");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
		}else if (!ss.equalsIgnoreCase(code)) {
			//��֤�����
			req.setAttribute("error", "��֤�����");
			req.getRequestDispatcher("WEB-INF/main/login.jsp").forward(req, res);
			
		}else {
			//���ͨ��
			//���˺Ŵ���cookie������������ʹ��
			Cookie c = new Cookie("adminCode",adminCode);
			res.addCookie(c);
			
			//Ҳ���Խ��˺Ŵ���session
			ses.setAttribute("adminCode",adminCode);
			//�ض�����ҳ
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
		//���ձ�����
		String name = req.getParameter("name");
		String baseDuration = req.getParameter("baseDuration");
		String baseCost = req.getParameter("baseCost");
		String unitCost = req.getParameter("unitCost");
		String descr = req.getParameter("descr");
		String costType = req.getParameter("costType");
		//������Щ����
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
		//�ض��򵽲�ѯ�ʷ�
		//��ǰ��/necctoss/addCost.do
		//Ŀ�꣺/netctoss/findCost.do
		res.sendRedirect("findCost.do");
	}
	
	protected void toAddCost(HttpServletRequest req, HttpServletResponse res)
			                               throws ServletException, IOException{
		 //ת��:/nectcoss/toAddCost.do
		 //��ǰ:/netctoss/WEB-INF/cost/add.jsp
		req.getRequestDispatcher("WEB-INF/cost/add.jsp").forward(req, res);
		
	}
	
	protected void findCost(HttpServletRequest req, HttpServletResponse res) 
			                                  throws ServletException, IOException{
		//��ѯ���е��ʷ�
		CostDao dao = new CostDao();
		List<Cost> list = dao.findAll();
		//ת��
		req.setAttribute("costs",list);
		//��ǰ:/netctoss/findCost.do
		//Ŀ��:/netctoss/WEB-INF/cost/find.jsp
		req.getRequestDispatcher("WEB-INF/cost/find.jsp").forward(req, res);
		
		
		
		
		
	}
   
}
