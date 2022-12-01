package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {

		PreparedStatement st = null;
		
		try {
			
			String sql = "INSERT INTO Department (Name) VALUES (?)";			
			
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);			

			st.setString(1, obj.getName());
					
			int affectedRows = st.executeUpdate();
			
			if( affectedRows > 0 ) {				
				ResultSet rs = st.getGeneratedKeys();
				// Se a proxima linha não for nulo
				if(rs.next()) {
					obj.setId(rs.getInt(1));	
				}			
				DB.closeResultSet(rs);
			}
			else {				
				throw new DbException("Erro: Nenhuma linha foi inserida.");				
			}			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
		
		PreparedStatement st = null;
		
		try {
			
			String sql = "UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?";
			
			st = conn.prepareStatement(sql);
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
		
	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;
		
		try {
			
			String sql = "DELETE FROM department "
					+ "WHERE Id = ?";
			
			st = conn.prepareStatement(sql);
			
			st.setInt(1, id);
			
			int rowAffected = st.executeUpdate();
			
			if(rowAffected==0) {
				throw new DbException("Id não encontrado. Registro não excluido.");
			}			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
		
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null; 
		
		try {
			
			String sql = "SELECT * FROM department "
					+ "WHERE Id = ?";
			
			st = conn.prepareStatement(sql);
			
			st.setInt(1, id);	
					
			rs = st.executeQuery();
						
			if(rs.next()) {				
				Department dept = new Department();				
				dept.setId( rs.getInt("Id") );
				dept.setName( rs.getString("Name") );				
				return dept;				
			}	
			
			return null;
		}
		catch(SQLException e) {
			throw new DbException("Update error: " + e.getMessage() );
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);			
		}
		
	}

	@Override
	public List<Department> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			String sql = "SELECT * FROM department";
			
			st = conn.prepareStatement(sql);
			
			st.executeQuery();
			
			rs = st.getResultSet();
			
			List<Department> dpto = new ArrayList<>();
			
			while(rs.next()) {	
				Department newDpto = new Department();
				newDpto.setId( rs.getInt("Id"));
				newDpto.setName( rs.getString("Name") );				
				dpto.add(newDpto);					
			}			
			return dpto;			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
	}
	
}




















