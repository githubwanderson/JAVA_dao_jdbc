package application;

import java.util.Date;
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
		
		System.out.println("\n=== TEST 4: seller insert ===");
		Seller newSeller = new Seller(null,"Joâo Mauro","jm@gmail.com", new Date(), 4505.60, department); 
		sellerDao.insert(newSeller);
		System.out.println(newSeller);
		
		System.out.println("\n=== TEST 5: seller update ===");
		seller = sellerDao.findById(7);
		department = new Department(1,null);
		seller.setBaseSalary(9500.50);
		seller.setDepartment(department);
		sellerDao.update(seller);
		System.out.println(seller);
		
		System.out.println("\n=== TEST 6: seller delete ===");
		sellerDao.deleteById(11);
		System.out.println("Deletado com sucesso!");
		
		
	}

}
