package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {	
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: seller findById ===");		
		Seller seller = sellerDao.findById(7);		
		System.out.println(seller);
		
		System.out.println("\n=== TEST 2: seller findByDepartment ===");
		Department department = new Department(4,null);
		List<Seller> listFindByDepartment = sellerDao.findByDepartment(department);  
		listFindByDepartment.forEach(System.out::println);
		
		System.out.println("\n=== TEST 3: seller findAll ===");
		List<Seller> listFindAll = sellerDao.findAll();  
		listFindAll.forEach(System.out::println);
	}

}
