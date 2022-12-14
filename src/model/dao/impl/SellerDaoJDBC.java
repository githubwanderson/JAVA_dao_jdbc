package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		
		PreparedStatement st = null;
		
		try {
			
			String sql =
					"INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ "VALUES"
					+ "(?,?,?,?,?)";
			
			st = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			// Instanciar uma data do SQL
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());	
			
			int rowsAffected = st.executeUpdate();
			
			// Se houver linhas afetadas então o registro foi inserido com sucesso
			if(rowsAffected > 0) {
				// Pego a tabela de retorno que contem os ids inseridos
				// Como sera inserido apenas um, entao pego a linha 1
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				else {
					throw new DbException("Erro: Nenhuma linha foi alterada.");
				}
				DB.closeResultSet(rs);					
			}
		}
		catch(SQLException e) {
			throw new DbException("Error: " + e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	/**
	 * Referencia do metodo insert
	 */
	@Override
	public void update(Seller obj) {
		
		PreparedStatement st = null;
		
		try {
			
			String sql =
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?";
			
			st = conn.prepareStatement(sql);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();			
		}
		catch(SQLException e) {
			throw new DbException("Error: " + e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1, id);
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected == 0) throw new DbException("Id não encontrado.");
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}		
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

	/**
	 * Lista todos os vendedores "Sellers"
	 * Copia do metodo findByDepartment porem sem clausula where
	 */
	@Override
	public List<Seller> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(""
					+ "SELECT seller.* , department.Name as DepName "
					+ "FROM seller "
					+ "INNER JOIN department ON seller.DepartmentId = department.Id "
					+ "ORDER BY seller.Name");		
			
			rs= st.executeQuery();			
			
			List<Seller> list = new ArrayList<>();
			
			Map<Integer,Department> map = new HashMap<>();

			while(rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));				
				if(dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}				
						
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
