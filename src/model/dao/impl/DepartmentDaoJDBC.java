package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			
			String sql = "INSERT INTO department (Name) VALUES (?)";			
			
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);			

			st.setString(1, obj.getName());
					
			int affectedRows = st.executeUpdate();
			
			if( affectedRows > 0 ) {				
				ResultSet rs = st.getGeneratedKeys();
				obj.setId(rs.getInt(1));				
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Department findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
