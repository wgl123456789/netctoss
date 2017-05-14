package dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Cost;
import util.DBUtil;

public class CostDao implements Serializable{
    public List<Cost> findAll(){
    	Connection conn = null;
        try {
			conn = DBUtil.getConnection();
			String sql = "select * from cost_wgl order by cost_id";
			Statement smt = conn.createStatement();
		    ResultSet rs = smt.executeQuery(sql);
		    List<Cost> list = new ArrayList<Cost>();
	        while(rs.next()){
	        	Cost c = new Cost();
	        	c.setCostId(rs.getInt("cost_id"));
	        	c.setName(rs.getString("name"));
	        	c.setBaseDuration(rs.getInt("base_duration"));
	        	c.setBaseCost(rs.getDouble("base_cost"));
                c.setUnitCost(rs.getDouble("unit_cost"));
                c.setStatus(rs.getString("status"));
                c.setDescr(rs.getString("descr"));
                c.setCreatime(rs.getTimestamp("creatime"));
                c.setStartime(rs.getTimestamp("startime"));
                c.setCostType(rs.getString("cost_type"));
                list.add(c);
	        }
	        return list;
        } catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询资费失败",e);
		}finally{
			DBUtil.close(conn);
		}
    		 
       
    }
  
    public static void main(String[] args) {
		CostDao dao = new CostDao();
         Cost c = new Cost();
         c.setName("包月");
         c.setBaseDuration(660);
         c.setBaseCost(66.0);
         c.setUnitCost(0.6);
         c.setDescr("66元比较实惠");
         c.setCostType("2");
		  dao.sava(c);
	}
    private Cost createCost(ResultSet rs) 
    		throws SQLException {
    		Cost c = new Cost();
    		c.setCostId(rs.getInt("cost_id"));
    		c.setName(rs.getString("name"));
    		c.setBaseDuration(rs.getInt("base_duration"));
    		c.setBaseCost(rs.getDouble("base_cost"));
    		c.setUnitCost(rs.getDouble("unit_cost"));
    		c.setStatus(rs.getString("status"));
    		c.setDescr(rs.getString("descr"));
    		c.setCreatime(rs.getTimestamp("creatime"));
    		c.setStartime(rs.getTimestamp("startime"));
    		c.setCostType(rs.getString("cost_type"));
    		return c;
    	}
    
	public void sava(Cost c){
		Connection conn=null;
		try {
			conn = DBUtil.getConnection();
			String sql ="insert into cost_wgl values(cost_seq_wgl.nextval, "
					+ "?,?,?,?,1,?,sysdate,null,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			//jdbc的方法setInt()/setDouble(),
			//不允许传入null,但当前业务该字段
			//确实可能是null;并且数据库中该字段
			//也允许是null;
			//解决方法:将这样的数据当做对象处理
			
			ps.setString(1, c.getName());
			ps.setObject(2, c.getBaseDuration());
			ps.setObject(3, c.getBaseCost());
			ps.setObject(4, c.getUnitCost());
			ps.setString(5, c.getDescr());
			ps.setString(6, c.getCostType());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("增加资费失败",e);
		}finally{
			DBUtil.close(conn);
		}
		
	}
    
	public Cost findById(int id){
		Connection conn = null;
		try {
			conn = DBUtil.getConnection();
			String sql = "select * from cost_wgl where cost_id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			 ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				return createCost(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("查询id失败",e);
		}
		return null;
	}
	



	public void update(Cost cost){
		if(cost==null){
			Connection conn = null;
			try {
				conn = DBUtil.getConnection();
				String sql = "update cost_wgl set name=?, base_duration=?, base_cost=?,"
						+ "unit_cost=?, descr=?, cost_type=? "
						+ "where cost_id=?";
             PreparedStatement ps = conn.prepareStatement(sql);
              ps.setString(1, cost.getName());
 			  ps.setObject(2, cost.getBaseDuration());
 			  ps.setObject(3, cost.getBaseCost());
 			  ps.setObject(4, cost.getUnitCost());
 			  ps.setString(5, cost.getDescr());
 			  ps.setString(6, cost.getCostType());
 			  ps.setInt(7, cost.getCostId());
 			  ps.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("修改资费失败");
			}finally{
				DBUtil.close(conn);
			}
			
		}
	}
	
	
    
}
