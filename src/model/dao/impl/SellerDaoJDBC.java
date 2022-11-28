package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(""
					+ "SELECT seller.* , department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");			
			st.setInt(1, id);
			rs= st.executeQuery();
			
			// Verifico se tem registro 
			// se não return null
			// se sim vou instanciar os objetos
			if(rs.next()) {
				
				// Crio um obj department
				Department dep = instantiateDepartment(rs);				
				// Crio um obj seller ja com obj department
				Seller seller = instantiateSeller(rs,dep);				
				return seller;
				
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	/**
	 * Método auxiliar findById
	 * @param ResultSet rs
	 * @param Department dep
	 * @return Seller obj
	 * @throws SQLException 
	 */
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("Id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));		
		seller.setDepartment(dep);
		return seller;
	}

	/**
	 * Método auxiliar findById
	 * @param ResultSet rs
	 * @return Department obj
	 * @throws SQLException 
	 */
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(""
					+ "SELECT seller.* , department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department ON seller.DepartmentId = department.Id "
					+ "WHERE seller.DepartmentId = ? "
					+ "ORDER BY seller.Name");			
			st.setInt(1, department.getId());
			rs= st.executeQuery();
			
			
			// Lista onde sera guardado o resultado da query
			List<Seller> list = new ArrayList<>();
			// Sera usado para não repetir o departamento.
			Map<Integer,Department> map = new HashMap<>();
			
			// Verifico se tem registro 
			// se não return null
			// se sim vou instanciar os objetos
			while(rs.next()) {
				
				// verifico se tenho o department dentro do map
				// se não crio um novo objeto department e add no map
				Department dep = map.get(rs.getInt("DepartmentId"));				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
						
				// Crio um obj seller ja com obj department
				Seller seller = instantiateSeller(rs,dep);				
				list.add(seller);				
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
		
	}

}
