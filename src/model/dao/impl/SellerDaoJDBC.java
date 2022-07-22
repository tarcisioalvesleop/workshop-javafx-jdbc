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

public class SellerDaoJDBC implements SellerDao {
	
	//Dao tem uma dependencia com o banco
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES (?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS); // returnar o id numero do vendedor
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();//executando a query no banco
			
			if(rowsAffected > 0) {//verifica se a incrementou uma linha o banco de dados
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {//incrementando o id no obj
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
			}			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);			
		}		
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name= ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();//executando a query no banco
						
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
			st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) {
		//buscando um seller atraves do ID
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = Department.Id "
					+ "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			//passando a tabela do query resultante para objetos em java
			if(rs.next()) {
				//montando o objeto department com os dados do rs
				Department dep = instantiateDepartment(rs);
				//montando o objeto Seller com os dados do rs
				Seller obj = instantiateSeller(rs, dep);
				
				return obj;				
			}
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			//fechando os recursos rs e st
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			//a conexão manteve aberta pelo fato de outro ser executado em seguida e será fechada no program main
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {//propagando a exceção rs(foi tratada onde chamou a função)
		//montando o objeto Seller com os dados do rs
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {//propagando a exceção rs (foi tratada onde chamou)
		//montando o objeto Departmet com os dados do rs
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));//igual no banco de dados nome do departamento
		dep.setName(rs.getString("DepName"));
		
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = Department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//guarda todos os department instanciando 
			
			//passando a tabela do query resultante para objetos em java
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));//verificando se existe ID do department
				if(dep == null) {//Para não repetir os department
				//montando o objeto department com os dados do rs
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);//adiciona no map
				}
				//montando o objeto Seller com os dados do rs
				Seller obj = instantiateSeller(rs, dep);				
				list.add(obj);			
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			//fechando os recursos rs e st
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			//a conexão manteve aberta pelo fato de outro ser executado em seguida e será fechada no program main
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = Department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();//guarda todos os department instanciando 
			
			//passando a tabela do query resultante para objetos em java
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));//verificando se existe ID do department
				if(dep == null) {//Para não repetir os department
				//montando o objeto department com os dados do rs
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);//adiciona no map
				}
				//montando o objeto Seller com os dados do rs
				Seller obj = instantiateSeller(rs, dep);				
				list.add(obj);			
			}
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			//fechando os recursos rs e st
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			//a conexão manteve aberta pelo fato de outro ser executado em seguida e será fechada no program main
		}
	}
	
	
}
