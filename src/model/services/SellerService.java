package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	//dependencia e o padrao factory
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		//retornando os dados do banco de dados		
		return dao.findAll();
	}
	
	public void saveOrUpdate(Seller obj) {
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	public void remove(Seller obj) {//removendo um departamento
		dao.deleteById(obj.getId());
	}
}
