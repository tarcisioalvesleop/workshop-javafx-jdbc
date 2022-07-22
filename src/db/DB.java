package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {	
	//metodos para conectar e desconectar com o banco de dados	
	
	private static Connection conn = null; 
	//metodo para conectar o bd
	public static Connection getConnection() {
		if(conn == null) {
			try {
			Properties props = loadProperties();//pegando as configuraçoes do db.properties pelo load
			String url = props.getProperty("dburl");//pegando a url do db.properties dburl
			conn = DriverManager.getConnection(url, props);//conectando com o banco de dados
			}
			catch( SQLException e) {
				throw new DbException(e.getMessage());//exceção personalisada para não colocar muito try no programa
			}
		}		
		return conn;
	}
	//fechando uma conexão
	public static void closeConnection() {
		if(conn != null) {
			try {
				conn.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	//carregando os dados da conexao do bando
	private static Properties loadProperties() {
		try(FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);//guarda no propos os arquivos lido por load(fs) 
			return props;
		}catch(IOException e){
			throw new DbException(e.getMessage());//passa mensagem que veio da exceção DbException
		}
	}
	
	//fechando uma consulta no SQL
	public static void closeStatement(Statement st) {
		if(st != null) {			
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	//fechando o resultado da consulta no SQL
		public static void closeResultSet(ResultSet rs) {
			if(rs != null) {			
				try {
					rs.close();
				} catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
			}
		}
}
