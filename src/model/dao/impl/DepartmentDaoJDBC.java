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

public class DepartmentDaoJDBC implements DepartmentDao{
	
	//Dao tem uma dependencia com o banco
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO Department "
					+ "(Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			//passando os valores para o st
			st.setString(1, obj.getName());			
			//executando a query
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();//salva o Id gerado no banco de dados
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);					
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado! Nenhuma linha foi inserida.");
			}
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);			
		}		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE Department "
					+ "SET Name = ? WHERE Id = ?");
			
			//passando os valores para o st
			st.setString(1, obj.getName());	
			st.setInt(2, obj.getId());
			
			//executando a query
			st.executeUpdate();
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);			
		}
		
	}

	@Override
	public void deleteById(Integer id) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM Department WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}catch(SQLException e) {
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
			st = conn.prepareStatement("SELECT Department.* FROM Department WHERE Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			//verificando se obteve resultado
			if(rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
				
				return dep;
			}
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
		
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT Department.* FROM Department ORDER BY Name");
			//executando a query
			rs = st.executeQuery();
			
			//criando uma lista de departamento
			List<Department> list = new ArrayList<>();
			
			while(rs.next()) {//passando a tabela do query resultante para objetos em java
				//montando o objeto Departmet com os dados do rs
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));//igual no banco de dados nome do departamento
				dep.setName(rs.getString("Name"));
				
				list.add(dep);
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
