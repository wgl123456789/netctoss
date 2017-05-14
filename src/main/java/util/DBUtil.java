package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
/*
 * 1.DBUtil是DBTool的升级版
 * 2.采用连接池来管理连接
 */
public class DBUtil {
	//DBCP连接池提供的实现类
	private static BasicDataSource ds;
	
	static{
		Properties p = new Properties();
		try {
			//1.读取参数
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String user = p.getProperty("user");
			String pwd = p.getProperty("pwd");
			String initSize = p.getProperty("initSize");
			String maxSize = p.getProperty("maxSize");
			//2.创建连接池（1次）
			ds = new BasicDataSource();
			//3.向连接池设置参数
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(pwd);
			ds.setInitialSize(new Integer(initSize));
			ds.setMaxActive(new Integer(maxSize));
			
		} catch (IOException e) {
			/*
			 * 异常的处理原则
			 * 	1.记录日志(log4j)
			 */
			e.printStackTrace();
			//2.能解决就解决(看开发规范)
			//3.解决不了向上抛给调用者
			//具体抛出那种类型的异常看开发规范
			throw new RuntimeException("加载配置文件失败",e);
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	/*
	 * 1.目前我们使用的连接都是连接池创建的
	 * 2.连接池重写了连接对象内部的close()
	 * 3.目前close()内部的逻辑是归还：清除连接对象内部包含的所有数据，将连接对象状态设置为空闲态
	 */
	public static void close(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭连接失败",e);
			}
		}
	}
	
	public static void main(String[] ages) throws SQLException{
		Connection conn = DBUtil.getConnection();
		System.out.println(conn);
		conn.close();
	}

}
