package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
/*
 * 1.DBUtil��DBTool��������
 * 2.�������ӳ�����������
 */
public class DBUtil {
	//DBCP���ӳ��ṩ��ʵ����
	private static BasicDataSource ds;
	
	static{
		Properties p = new Properties();
		try {
			//1.��ȡ����
			p.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));
			String driver = p.getProperty("driver");
			String url = p.getProperty("url");
			String user = p.getProperty("user");
			String pwd = p.getProperty("pwd");
			String initSize = p.getProperty("initSize");
			String maxSize = p.getProperty("maxSize");
			//2.�������ӳأ�1�Σ�
			ds = new BasicDataSource();
			//3.�����ӳ����ò���
			ds.setDriverClassName(driver);
			ds.setUrl(url);
			ds.setUsername(user);
			ds.setPassword(pwd);
			ds.setInitialSize(new Integer(initSize));
			ds.setMaxActive(new Integer(maxSize));
			
		} catch (IOException e) {
			/*
			 * �쳣�Ĵ���ԭ��
			 * 	1.��¼��־(log4j)
			 */
			e.printStackTrace();
			//2.�ܽ���ͽ��(�������淶)
			//3.������������׸�������
			//�����׳��������͵��쳣�������淶
			throw new RuntimeException("���������ļ�ʧ��",e);
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return ds.getConnection();
	}
	
	/*
	 * 1.Ŀǰ����ʹ�õ����Ӷ������ӳش�����
	 * 2.���ӳ���д�����Ӷ����ڲ���close()
	 * 3.Ŀǰclose()�ڲ����߼��ǹ黹��������Ӷ����ڲ��������������ݣ������Ӷ���״̬����Ϊ����̬
	 */
	public static void close(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�����ʧ��",e);
			}
		}
	}
	
	public static void main(String[] ages) throws SQLException{
		Connection conn = DBUtil.getConnection();
		System.out.println(conn);
		conn.close();
	}

}
